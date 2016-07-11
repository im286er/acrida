package net.gility.acrida.storage;

import java.util.Map;

/**
 * Created by winsx on 10/30/15.
 */
public interface ConfigStore {
    String getProperty(String name);
    String getProperty(String name, String defaultValue);
    void setProperty(String name, String value);
    void setProperties(Map<String, String> properties);
    void removeProperty(String name);
    void removeProperties(String... names);
}
