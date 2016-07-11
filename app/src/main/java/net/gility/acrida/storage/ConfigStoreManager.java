package net.gility.acrida.storage;

import android.content.Context;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.config.AppConfig;
import net.gility.acrida.utils.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * Config Store manager
 *
 * @author Alimy
 * @version Created by Alimy on 10/30/15.
 */
public class ConfigStoreManager {

    public static String getProperty(String name) {
        return ConfigStoreHolder.instance.getProperty(name);
    }

    public static String getProperty(String name, String defaultValue) {
        return ConfigStoreHolder.instance.getProperty(name, defaultValue);
    }

    public static int getInt(String name) {
        return StringUtils.toInt(ConfigStoreHolder.instance.getProperty(name), 0);
    }

    public static boolean getBoolean(String name) {
        return StringUtils.toBool(ConfigStoreHolder.instance.getProperty(name));
    }

    public static void setProperty(String name, String value) {
        ConfigStoreHolder.instance.setProperty(name, value);
    }

    public static void setProperties(Map<String, String> properties) {
        ConfigStoreHolder.instance.setProperties(properties);
    }

    public static void removeProperty(String name) {
        ConfigStoreHolder.instance.removeProperty(name);
    }

    public static void removeProperties(String... names) {
        ConfigStoreHolder.instance.removeProperties(names);
    }

    private static class ConfigStoreHolder {
        final static ConfigStore instance = build();

        private static ConfigStore build() {
            // 把config建在(自定义)app_config的目录下
            File dirConf = ApplicationLoader.instance.getDir(AppConfig.CONFIG_FILE_DIR,
                    Context.MODE_PRIVATE);
            File configFile = new File(dirConf, AppConfig.CONFIG_FILE_NAME);

            return new PropertiesConfigStore(configFile);
        }
    }
}
