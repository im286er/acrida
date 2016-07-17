package net.gility.acrida.dagger;

import android.app.Application;

import com.hwangjr.rxbus.Bus;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import net.gility.acrida.android.ActivityHierarchyServer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

@Module
public class UiModule {

    @Provides @Singleton
    Bus ProvideBus() {
        return new Bus();
    }

    @Provides @Singleton
    ActivityHierarchyServer provideActivityHierarchyServer() {
        return ActivityHierarchyServer.NONE;
    }

    @Provides @Singleton
    Picasso providePicasso(Application app, OkHttpClient client) {
        return new Picasso.Builder(app)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }
}
