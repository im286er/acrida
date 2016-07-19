package net.gility.acrida.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Entity;
import net.gility.acrida.content.ListEntity;
import net.gility.acrida.content.ResultBean;
import net.gility.acrida.storage.CacheManager;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.TDevice;
import net.gility.acrida.utils.ThemeSwitchUtils;
import net.gility.acrida.utils.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@SuppressLint("NewApi")
public abstract class InjectListFragment<T extends Entity, D extends ListEntity<T>> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener,
        OnScrollListener {

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    @BindView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.listview)
    protected ListView mListView;
    @BindView(R.id.error_layout)
    protected StateView mErrorLayout;

    protected ListBaseAdapter<T> mAdapter;
    protected int mStoreEmptyState = -1;
    protected int mCurrentPage = 0;
    protected int mCatalog = 1;
    // 错误信息
    protected net.gility.acrida.content.Result mResult;

    private AsyncTask<String, Void, ListEntity<T>> mCacheTask;
    private ParserTask mParserTask;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG, 0);
        }
    }

    @Override
    public void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setStateType(StateView.TYPE_NETWORK_LOADING);
                requestData(true);
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setStateType(StateView.TYPE_NETWORK_LOADING);
                mState = STATE_NONE;
                requestData(false);
            } else {
                mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
            }

        }
        if (mStoreEmptyState != -1) {
            mErrorLayout.setStateType(mStoreEmptyState);
        }
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getStateType();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadCacheTask();
        cancelParserTask();
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

    protected abstract ListBaseAdapter<T> getListAdapter();

    // 下拉刷新数据
    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }
        // 设置顶部正在刷新
        mListView.setSelection(0);
        setSwipeRefreshLoadingState();
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);
    }

    protected boolean requestDataIfViewCreated() {
        return true;
    }

    protected String getCacheKeyPrefix() {
        return null;
    }

    protected abstract Observable<Result<D>> obtainData();

    protected ListEntity<T> parseList(InputStream is) throws Exception {
        return null;
    }

    protected ListEntity<T> readList(Serializable object) {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    // 是否需要自动刷新
    protected boolean needAutoRefresh() {
        return true;
    }

    /***
     * 获取列表数据
     *
     * @param refresh
     * @return void
     * @author 火蚁 2015-2-9 下午3:16:12
     */
    protected void requestData(boolean refresh) {
        String key = getCacheKey();
        if (isReadCacheData(refresh)) {
            readCacheData(key);
        } else {
            // 取新的数据
            // sendRequestData();
            fetchDataFromNetwork();
        }
    }

    private Observable.Operator<D, Result<D>> mHandleErrorOperator = child ->
            new Subscriber<Result<D>>() {
                @Override
                public void onCompleted() {
                    child.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    child.onError(e);
                }

                @Override
                public void onNext(Result<D> dResult) {
                    D data = dResult.response().body();
                    if (data != null) child.onNext(data);
                    child.onError(new HttpException(dResult.response()));
                }
            };

    private void fetchDataFromNetwork() {
        addSubscription(
                obtainData()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .lift(mHandleErrorOperator)
                        .doOnError(throwable -> {
                            if (throwable instanceof HttpException)
                                onHttpException((HttpException) throwable);
                        })
                        .doOnNext(data -> {
                            saveCacheData(getCacheKey(), data);
                            if (mCurrentPage == 0 && needAutoRefresh()) {
                                ApplicationLoader.putToLastRefreshTime(getCacheKey(),
                                        StringUtils.getCurTimeStr());
                            }
                            if (isAdded()) {
                                if (mState == STATE_REFRESH) {
                                    onRefreshNetworkSuccess();
                                }
                            }
                        })
                        .subscribe(mObserver)
        );
    }

    private Observer<D> mObserver = new Observer<D>() {

        @Override
        public void onCompleted() {
            executeOnLoadFinish();
        }

        @Override
        public void onError(Throwable e) {
            if (isAdded()) {
                readCacheData(getCacheKey());
            }
        }

        @Override
        public void onNext(D d) {
            executeOnLoadDataSuccess(d.getList());
        }
    };

    private void onHttpException(HttpException httpException) {
        InputStream is = httpException.response().raw().body().byteStream();
        try {
            ResultBean resultBean = XmlUtils.toBean(ResultBean.class, is);
            if (resultBean != null) {
                mResult = resultBean.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 判断是否需要读取缓存的数据
     *
     * @param refresh
     * @return
     * @author 火蚁 2015-2-10 下午2:41:02
     */
    protected boolean isReadCacheData(boolean refresh) {
        String key = getCacheKey();
        if (!TDevice.hasInternet()) {
            return true;
        }
        // 第一页若不是主动刷新，缓存存在，优先取缓存的
        if (CacheManager.isExistDataCache(getActivity(), key) && !refresh
                && mCurrentPage == 0) {
            return true;
        }
        // 其他页数的，缓存存在以及还没有失效，优先取缓存的
        if (CacheManager.isExistDataCache(getActivity(), key)
                && !CacheManager.isCacheDataFailure(getActivity(), key)
                && mCurrentPage != 0) {
            return true;
        }

        return false;
    }

    // 是否到时间去刷新数据了
    private boolean onTimeRefresh() {
        String lastRefreshTime = ApplicationLoader.getLastRefreshTime(getCacheKey());
        String currTime = StringUtils.getCurTimeStr();
        long diff = StringUtils.calDateDifferent(lastRefreshTime, currTime);
        return needAutoRefresh() && diff > getAutoRefreshTime();
    }

    /***
     * 自动刷新的时间
     * <p>
     * 默认：自动刷新的时间为半天时间
     *
     * @return
     * @author 火蚁 2015-2-9 下午5:55:11
     */
    protected long getAutoRefreshTime() {
        return 12 * 60 * 60;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onTimeRefresh()) {
            onRefresh();
        }
    }

    protected void sendRequestData() {
    }

    private void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new ReadCacheTask(getActivity()).execute(cacheKey);
    }

    private void saveCacheData(String cacheKey, Serializable data) {
        new SaveCacheTask(getActivity(), data, cacheKey).execute();

    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class ReadCacheTask extends AsyncTask<String, Void, ListEntity<T>> {
        private final WeakReference<Context> mContextReference;

        private ReadCacheTask(Context context) {
            mContextReference = new WeakReference<>(context);
        }

        @Override
        protected ListEntity<T> doInBackground(String... params) {
            Serializable object = CacheManager.readObject(mContextReference.get(), params[0]);
            if (object == null) {
                return null;
            } else {
                return readList(object);
            }
        }

        @Override
        protected void onPostExecute(ListEntity<T> list) {
            super.onPostExecute(list);
            if (list != null) {
                executeOnLoadDataSuccess(list.getList());
            } else {
                executeOnLoadDataError(null);
            }
            executeOnLoadFinish();
        }
    }

    protected static class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContextReference;
        private final Serializable mObject;
        private final String mKey;

        public SaveCacheTask(Context context, Serializable object, String key) {
            mContextReference = new WeakReference<>(context);
            mObject = object;
            mKey = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContextReference.get(), mObject, mKey);
            return null;
        }
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            if (mCurrentPage == 0 && needAutoRefresh()) {
                ApplicationLoader.putToLastRefreshTime(getCacheKey(),
                        StringUtils.getCurTimeStr());
            }
            if (isAdded()) {
                if (mState == STATE_REFRESH) {
                    onRefreshNetworkSuccess();
                }
                executeParserTask(responseBytes);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            if (isAdded()) {
                readCacheData(getCacheKey());
            }
        }
    };

    protected void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }

        if (mResult != null && !mResult.OK()) {
            ApplicationLoader.showToast(mResult.getErrorMessage());
            // 注销登陆，密码已经修改，cookie，失效了
            ApplicationLoader.getInstance().Logout();
        }

        mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
        if (mCurrentPage == 0) {
            mAdapter.clear();
        }

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(mAdapter.getData(), data.get(i))) {
                data.remove(i);
                i--;
            }
        }
        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if ((mAdapter.getCount() + data.size()) == 0) {
            adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0
                || (data.size() < getPageSize() && mCurrentPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        mAdapter.setState(adapterState);
        mAdapter.addData(data);
        // 判断等于是因为最后有一项是listview的状态
        if (mAdapter.getCount() == 1) {

            if (needShowEmptyNoData()) {
                mErrorLayout.setStateType(StateView.TYPE_NO_DATA);
            } else {
                mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 是否需要隐藏listview，显示无数据状态
     *
     * @author 火蚁 2015-1-27 下午6:18:59
     */
    protected boolean needShowEmptyNoData() {
        return true;
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId() == data.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getPageSize() {
        return ApplicationLoader.PAGE_SIZE;
    }

    protected void onRefreshNetworkSuccess() {
    }

    protected void executeOnLoadDataError(String error) {
        if (mCurrentPage == 0
                && !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
            mErrorLayout.setStateType(StateView.TYPE_NETWORK_ERROR);
        } else {
            mErrorLayout.setStateType(StateView.TYPE_HIDE_LAYOUT);
            mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    private void executeParserTask(byte[] data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    class ParserTask extends AsyncTask<Void, Void, String> {

        private final byte[] reponseData;
        private boolean parserError;
        private List<T> list;

        public ParserTask(byte[] data) {
            this.reponseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ListEntity<T> data = parseList(new ByteArrayInputStream(reponseData));
                saveCacheData(getCacheKey(), data);
                list = data.getList();
                if (list == null) {
                    ResultBean resultBean = XmlUtils.toBean(ResultBean.class, reponseData);
                    if (resultBean != null) {
                        mResult = resultBean.getResult();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (parserError) {
                readCacheData(getCacheKey());
            } else {
                executeOnLoadDataSuccess(list);
                executeOnLoadFinish();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }
        // 判断是否滚动到底部
        boolean scrollEnd = false;
        try {
            if (view.getPositionForView(mAdapter.getFooterView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        } catch (Exception e) {
            scrollEnd = false;
        }

        if (mState == STATE_NONE && scrollEnd) {
            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestData(false);
                mAdapter.setFooterViewLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        // if (mState == STATE_NOMORE || mState == STATE_LOADMORE
        // || mState == STATE_REFRESH) {
        // return;
        // }
        // if (mAdapter != null
        // && mAdapter.getDataSize() > 0
        // && mListView.getLastVisiblePosition() == (mListView.getCount() - 1))
        // {
        // if (mState == STATE_NONE
        // && mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
        // mState = STATE_LOADMORE;
        // mCurrentPage++;
        // requestData(true);
        // }
        // }
    }

    /**
     * 保存已读的文章列表
     *
     * @param view
     * @param prefFileName
     * @param key
     */
    protected void saveToReadedList(final View view, final String prefFileName,
                                    final String key) {
        // 放入已读列表
        ApplicationLoader.putReadedPostList(prefFileName, key, "true");
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setTextColor(ApplicationLoader.getInstance().getResources().getColor(ThemeSwitchUtils.getTitleReadedColor()));
        }
    }

    private void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }
}
