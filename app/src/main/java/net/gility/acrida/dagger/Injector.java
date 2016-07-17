package net.gility.acrida.dagger;

import net.gility.acrida.android.ApplicationLoader;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

public class Injector {

    static class SingletonInstance {
        static final AppComponent component = DaggerAppComponent.builder()
                .appModule(new AppModule(ApplicationLoader.instance))
                .build();
    }

    static public AppComponent obtain() {
        return SingletonInstance.component;
    }
}
