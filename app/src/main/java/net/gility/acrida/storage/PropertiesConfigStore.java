package net.gility.acrida.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Config store based on Properties
 * this is not thread safe
 *
 * @author Alimy
 * @version Created by Alimy on 10/30/15.
 */
public class PropertiesConfigStore implements ConfigStore {
    private Properties properties;
    private File configStoreFile;

    public PropertiesConfigStore(File configStoreFile) {
        this.configStoreFile = configStoreFile;
        this.properties = new Properties();

        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(configStoreFile);
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            // some log here
        } catch (IOException e) {
            // some log here
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // some log here
                }
            }
        }
    }

    @Override
    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    @Override
    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
        update();
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        properties.putAll(properties);
        update();
    }

    @Override
    public void removeProperty(String name) {
        properties.remove(name);
        update();
    }

    @Override
    public void removeProperties(String... names) {
        for (String name : names)
            properties.remove(name);
        update();
    }

    private void update() {
        // treat persistent config to file
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream configOutputStream = null;
                    if (null != configStoreFile) {
                        configOutputStream = new FileOutputStream(configStoreFile);
                    }
                    properties.store(configOutputStream, null);
                    configOutputStream.flush();

                } catch (Exception e) {
                    // some log here
                }
            }
        }).start();
    }
}
