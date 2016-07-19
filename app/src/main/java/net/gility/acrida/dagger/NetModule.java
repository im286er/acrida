package net.gility.acrida.dagger;

import net.gility.acrida.BuildConfig;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.config.AppConfig;
import net.gility.acrida.utils.AndroidUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;

/**
 * @author Alimy
 * Created by Michael Li on 7/20/16.
 */

@Module(
        includes = {
                AppModule.class
        }
)
public class NetModule {
    static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);
    public final static String HOST = "www.oschina.net";

    @Provides @Singleton
    public OkHttpClient provideOkHttpClient(ApplicationLoader application) {
        return createOkHttpClient(application).build();
    }

    static OkHttpClient.Builder createOkHttpClient(ApplicationLoader app) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "AcridaCache");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!AndroidUtils.isNetworkConnected(app.getApplicationContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (AndroidUtils.isNetworkConnected(app.getApplicationContext())) {
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=0")
                        .build();
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        };

        Interceptor requestInterceptor = chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader("Accept-Language", Locale.getDefault().toString());
            requestBuilder.addHeader("Host", HOST);
            requestBuilder.addHeader("Connection", "Keep-Alive");
            requestBuilder.addHeader("User-Agent", getUserAgent(app));
            return chain.proceed(requestBuilder.build());
        };

        CookieJar memoryCookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        builder.cache(cache)
                .addInterceptor(requestInterceptor)
                .addInterceptor(cacheInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        // cookieJar
        builder.cookieJar(memoryCookieJar);

        return builder;
    }

    /**
     * 获得请求的服务端数据的userAgent
     *
     * @param applicationLoader
     * @return
     */
    public static String getUserAgent(ApplicationLoader applicationLoader) {
        StringBuilder ua = new StringBuilder("OSChina.NET");
        ua.append('/' + applicationLoader.getPackageInfo().versionName + '_'
                + applicationLoader.getPackageInfo().versionCode);// app版本信息
        ua.append("/Android");// 手机系统平台
        ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
        ua.append("/" + android.os.Build.MODEL); // 手机型号
        ua.append("/" + AppConfig.getInstance().getAppId());// 客户端唯一标识
        return ua.toString();
    }

}
