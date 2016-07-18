package net.gility.acrida.dagger;

import net.gility.acrida.network.OSChinaService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

@Module(
        includes = {
                AppModule.class
        }
)
public class ApiModule {
    @Provides @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://www.oschina.net")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides @Singleton
    static OSChinaService provideOSChinaService(Retrofit retrofit) {
        return retrofit.create(OSChinaService.class);
    }
}
