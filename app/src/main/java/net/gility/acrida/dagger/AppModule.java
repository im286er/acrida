package net.gility.acrida.dagger;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;

import net.gility.acrida.android.ApplicationLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

@Module
public class AppModule {
    private ApplicationLoader application;

    public AppModule(ApplicationLoader application) {
        this.application = application;
    }

    @Provides @Singleton
    ApplicationLoader provideApplication() {
        return application;
    }

    @Provides
    ClipboardManager provideClipboardManager(Application application) {
        return (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
    }
}
