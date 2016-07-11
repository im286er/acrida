package net.gility.acrida.ui.fragment.team;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.NotebookData;
import net.gility.acrida.content.NotebookDataList;
import net.gility.acrida.model.SimpleBackPage;
import net.gility.acrida.content.User;
import net.gility.acrida.network.OSChinaApi;
import net.gility.acrida.storage.NoteDatabase;
import net.gility.acrida.ui.adapter.team.NotebookAdapter;
import net.gility.acrida.ui.fragment.BaseFragment;
import net.gility.acrida.ui.widget.KJDragGridView;
import net.gility.acrida.ui.widget.KJDragGridView.OnDeleteListener;
import net.gility.acrida.ui.widget.KJDragGridView.OnMoveListener;
import net.gility.acrida.ui.widget.StateView;
import net.gility.acrida.utils.KJAnimations;
import net.gility.acrida.utils.SynchronizeController;
import net.gility.acrida.utils.SynchronizeController.SynchronizeListener;
import net.gility.acrida.utils.UIHelper;
import net.gility.acrida.utils.XmlUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 便签列表界面
 * 
 * @author kymjs (https://github.com/kymjs)
 */
public class NoteBookFragment extends BaseFragment implements
        OnItemClickListener, OnRefreshListener {

    @BindView(R.id.frag_note_list)
    KJDragGridView mGrid;
    @BindView(R.id.frag_note_trash)
    ImageView mImgTrash;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.error_layout)
    StateView mStateView;

    private NoteDatabase noteDb;
    private SynchronizeController controller;
    private List<NotebookData> datas;
    private NotebookAdapter adapter;
    private User user;
    private Activity aty;

    /**
     * 用来做更进一步人性化的防手抖策略时使用<br>
     * 比如由于手抖动上下拉菜单时拉动一部分，但又没有达到可刷新的时候，暂停一段时间，这个时候用户的逻辑应该是想移动item的。<br>
     * （这手抽的也太厉害了吧，这里为了效率就算了，没必要那么复杂）<br>
     * 其实应该还有一种根据setOnFocusChangeListener来改写的方法，我没有尝试。
     */
    // private static final Handler mHandler = new Handler() {
    // @Override
    // public void handleMessage(android.os.Message msg) {
    // mList.setDragEnable(true);
    // };
    // };

    /********************************* function method ******************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_note, container,
                false);
        aty = getActivity();
        ButterKnife.bind(this, rootView);
        initData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refurbish();
        setListCanPull();
    }

    @Override
    public void onDestroy() {
        noteDb.destroy();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notebook_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            Bundle bundle = new Bundle();
            bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                    NoteEditFragment.NOTEBOOK_FRAGMENT);
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.NOTE_EDIT,
                    bundle);
            break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Bundle bundle = new Bundle();
        bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                NoteEditFragment.NOTEBOOK_ITEM);
        bundle.putSerializable(NoteEditFragment.NOTE_KEY, datas.get(position));
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.NOTE_EDIT, bundle);
    }

    /***************************** initialization method ***************************/

    @Override
    public void initData() {
        user = ApplicationLoader.getInstance().getLoginUser();
        controller = new SynchronizeController();
        noteDb = new NoteDatabase(aty);
        datas = noteDb.query();// 查询操作，忽略耗时
        if (datas != null) {
            adapter = new NotebookAdapter(aty, datas);
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void initView(View view) {
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(this);
        mGrid.setTrashView(mImgTrash);
        mGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGrid.setOnDeleteListener(new OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                delete(position);
            }
        });
        mGrid.setOnMoveListener(new OnMoveListener() {
            @Override
            public void startMove() {
                mSwipeRefreshLayout.setEnabled(false);
                mImgTrash.startAnimation(KJAnimations.getTranslateAnimation(0,
                        0, mImgTrash.getTop(), 0, 500));
                mImgTrash.setVisibility(View.VISIBLE);
            }

            @Override
            public void finishMove() {
                setListCanPull();
                mImgTrash.setVisibility(View.INVISIBLE);
                mImgTrash.startAnimation(KJAnimations.getTranslateAnimation(0,
                        0, 0, mImgTrash.getTop(), 500));
                if (adapter.getDataChange()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            noteDb.reset(adapter.getDatas());
                        }
                    }).start();
                }
            }

            @Override
            public void cancleMove() {}
        });
        mSwipeRefreshLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mState = STATE_PRESSNONE;
                    mGrid.setDragEnable(false);
                    // 如果你愿意还可以进一步人性化处理，请看mHandler注释
                    // mHandler.sendMessageDelayed(Message.obtain(), 400);
                } else {
                    mGrid.setDragEnable(true);
                }
                return false;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mStateView.setStateType(StateView.TYPE_NO_DATA);
        if (!datas.isEmpty()) {
            mStateView.setVisibility(View.GONE);
        }
    }

    /********************************* GridView method ******************************/

    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }
        // 设置顶部正在刷新
        mGrid.setSelection(0);
        setSwipeRefreshLoadingState();

        refurbish();
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState() {
        mState = STATE_REFRESH;
        mGrid.setDragEnable(false);
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
        mState = STATE_NOMORE;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
        mGrid.setDragEnable(true);
    }

    /**
     * 未登陆用户不能下拉同步，只能使用本地存储
     */
    private void setListCanPull() {
        if (!ApplicationLoader.getInstance().isLogin()) {
            mSwipeRefreshLayout.setEnabled(false);
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * 更新空视图时的tip显示
     */
    private void updateEmptyView() {
        if (datas != null && !datas.isEmpty()) {
            mStateView.setVisibility(View.GONE);
        } else {
            mStateView.setVisibility(View.VISIBLE);
            mStateView.setStateType(StateView.TYPE_NO_DATA);
            mStateView.setNoDataContent("暂无便签，请添加或下拉同步");
        }
    }

    /********************************* synchronize method ******************************/

    /**
     * 使用自带缓存功能的网络请求，防止多次刷新造成的流量损耗以及服务器压力
     */
    private void refurbish() {
        datas = noteDb.query();
        if (datas != null) {
            if (adapter != null) {
                adapter.refurbishData(datas);
            } else {
                adapter = new NotebookAdapter(aty, datas);
                mGrid.setAdapter(adapter);
            }
        }
        if (user.getId() == 0) { // 未登录时不请求网络
            return;
        }
        controller.doSynchronize(aty, new SynchronizeListener() {
            @Override
            public void onSuccess(byte[] arg2) {
                NotebookDataList dataList = XmlUtils.toBean(
                        NotebookDataList.class, arg2);
                if (dataList != null && dataList.getList() != null) {
                    noteDb.reset(dataList.getList());
                    datas = noteDb.query();
                    adapter.refurbishData(datas);
                    updateEmptyView();
                }
                setSwipeRefreshLoadedState();
            }

            @Override
            public void onFailure() {
                ApplicationLoader.showToast("网络不好，请稍后执行同步");
                setSwipeRefreshLoadedState();
            }
        });
        updateEmptyView();
    }

    /**
     * 删除数据
     * 
     * @param data
     */
    private void delete(int index) {
        final int noteId = datas.get(index).getId();
        // 只有是登录用户才执行网络请求
        if (user.getId() > 0) {
            OSChinaApi.deleteNoteBook(noteId, user.getId(),
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                byte[] arg2) {}

                        @Override
                        public void onFailure(int arg0, Header[] arg1,
                                byte[] arg2, Throwable arg3) {
                            ApplicationLoader.showToast("网络异常,云端文件暂未删除");
                        }
                    });
        }

        noteDb.delete(noteId);
        datas.remove(index);
        if (datas != null && adapter != null) {
            adapter.refurbishData(datas);
            mGrid.setAdapter(adapter);
        }

        updateEmptyView();
    }
}