package net.gility.acrida.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import net.gility.acrida.BuildConfig;
import net.gility.acrida.config.AppConfig;
import net.gility.acrida.content.User;
import net.gility.acrida.dagger.Injector;
import net.gility.acrida.model.AppException;
import net.gility.acrida.model.Constants;
import net.gility.acrida.network.ApiHttpClient;
import net.gility.acrida.network.OSChinaService;
import net.gility.acrida.storage.DataCleanManager;
import net.gility.acrida.utils.CyptoUtils;
import net.gility.acrida.utils.MethodsCompat;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.TLog;
import net.gility.acrida.utils.UIHelper;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;

import javax.inject.Inject;

import static net.gility.acrida.config.AppConfig.KEY_FRITST_START;
import static net.gility.acrida.config.AppConfig.KEY_LOAD_IMAGE;
import static net.gility.acrida.config.AppConfig.KEY_NIGHT_MODE_SWITCH;
import static net.gility.acrida.config.AppConfig.KEY_TWEET_DRAFT;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 * @version 1.0
 * @created 2014-04-22
 */
public class ApplicationLoader extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小

    public static ApplicationLoader instance;

    private int loginUid;

    private boolean login;

    @Inject ActivityHierarchyServer mActivityHierarchyServer;
    @Inject OSChinaService mOsChinaService;
    @Inject SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.initStatic(this);
        Injector.obtain().inject(this);

        init();
        initLogin();
    }

    private void init() {
        instance = this;
        checkFirstStart();

        registerActivityLifecycleCallbacks(mActivityHierarchyServer);
        Thread.setDefaultUncaughtExceptionHandler(AppException
                .getAppExceptionHandler(this));
        UIHelper.sendBroadcastForNotice(this);

        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器
        KJLoger.openDebutLog(true);
        TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
        BitmapConfig.CACHEPATH = "OSChina/imagecache";
    }

    private void checkFirstStart() {
        if (mPreferences.getBoolean(KEY_FRITST_START, false)) {
            DataCleanManager.cleanInternalCache(ApplicationLoader.getInstance());
            mPreferences.edit().putBoolean(KEY_FRITST_START, false).apply();
        }
    }

    private void initLogin() {
        User user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = user.getId();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 获得当前app运行的AppContext
     * 
     * @return
     */
    public static ApplicationLoader getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getInstance().set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getInstance().get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getInstance().set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     * 
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getInstance().get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getInstance().remove(key);
    }

    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     * 
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.account", user.getAccount());
                setProperty("user.pwd",
                        CyptoUtils.encode("oschinaApp", user.getPwd()));
                setProperty("user.location", user.getLocation());
                setProperty("user.followers",
                        String.valueOf(user.getFollowers()));
                setProperty("user.fans", String.valueOf(user.getFans()));
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.favoritecount",
                        String.valueOf(user.getFavoritecount()));
                setProperty("user.gender", String.valueOf(user.getGender()));
                setProperty("user.isRememberMe",
                        String.valueOf(user.isRememberMe()));// 是否记住我的信息
            }
        });
    }

    /**
     * 更新用户信息
     * 
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.followers",
                        String.valueOf(user.getFollowers()));
                setProperty("user.fans", String.valueOf(user.getFans()));
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.favoritecount",
                        String.valueOf(user.getFavoritecount()));
                setProperty("user.gender", String.valueOf(user.getGender()));
            }
        });
    }

    /**
     * 获得登录用户的信息
     * 
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setPortrait(getProperty("user.face"));
        user.setAccount(getProperty("user.account"));
        user.setLocation(getProperty("user.location"));
        user.setFollowers(StringUtils.toInt(getProperty("user.followers"), 0));
        user.setFans(StringUtils.toInt(getProperty("user.fans"), 0));
        user.setScore(StringUtils.toInt(getProperty("user.score"), 0));
        user.setFavoritecount(StringUtils.toInt(
                getProperty("user.favoritecount"), 0));
        user.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        user.setGender(getProperty("user.gender"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        new KJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
        set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     * 
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(KEY_NIGHT_MODE_SWITCH, false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(KEY_NIGHT_MODE_SWITCH, on);
    }
}
