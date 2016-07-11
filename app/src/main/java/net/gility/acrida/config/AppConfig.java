package net.gility.acrida.config;

import android.content.Context;
import android.os.Environment;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午5:29:00
 * 
 */
public class AppConfig {

    private final static String APP_CONFIG = "config";
    public final static String CONFIG_FILE_DIR = "config";
    public final static String CONFIG_FILE_NAME = "config";

    public final static String CONF_COOKIE = "cookie";

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";

    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    public static final String KEY_NOTIFICATION_ACCEPT = "KEY_NOTIFICATION_ACCEPT";
    public static final String KEY_NOTIFICATION_SOUND = "KEY_NOTIFICATION_SOUND";
    public static final String KEY_NOTIFICATION_VIBRATION = "KEY_NOTIFICATION_VIBRATION";
    public static final String KEY_NOTIFICATION_DISABLE_WHEN_EXIT = "KEY_NOTIFICATION_DISABLE_WHEN_EXIT";
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";
    public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    public static final String KEY_TWEET_DRAFT = "KEY_TWEET_DRAFT";
    public static final String KEY_NOTE_DRAFT = "KEY_NOTE_DRAFT";

    public static final String KEY_FRITST_START = "KEY_FRIST_START";

    public static final String KEY_NIGHT_MODE_SWITCH="night_mode_switch";

    public static final String APP_QQ_KEY = "100942993";

    private AppConfig() {
        // empty
    }

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "OSChina"
            + File.separator + "osc_img" + File.separator;

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "OSChina"
            + File.separator + "download" + File.separator;

    // get a singleton instance
    public static AppConfig getInstance() {
        return AppConfigHolder.instance;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = get(CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            set(CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = ApplicationLoader.instance.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = ApplicationLoader.instance.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }

    static class AppConfigHolder {
       final static AppConfig instance = new AppConfig();
    }
}
