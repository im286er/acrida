package net.gility.acrida.dagger;

import net.gility.acrida.android.ApplicationLoader;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

public class Injector {
    static ApplicationLoader application;

    private static class SingletonInstance {
        static final AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    public static void initStatic(ApplicationLoader app) {
        application = app;
    }

    static public AppComponent obtain() {
        return SingletonInstance.appComponent;
    }
}
