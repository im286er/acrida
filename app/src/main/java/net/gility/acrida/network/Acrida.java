package net.gility.acrida.network;

import android.support.annotation.NonNull;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.config.AppConfig;
import net.gility.acrida.content.Blog;
import net.gility.acrida.content.LoginUserBean;
import net.gility.acrida.content.News;
import net.gility.acrida.content.NewsList;
import net.gility.acrida.content.Post;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.content.TweetLike;
import net.gility.acrida.content.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * powered network request
 *
 * @author Alimy
 * @version 1.0, Created by Alimy on 10/23/15.
 */

public class Acrida {
    private final static int PAGE_SIZE = 20;

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @param callback
     */
    public static void login(String username, String password,
                             Callback<LoginUserBean> callback) {
        Client.instance.login(username, password, 1)
                .enqueue(callback);
    }

    /**
     * 获取新闻列表
     *
     * @param catalog  类别 （1，2，3）
     * @param page     第几页
     * @param callback
     */
    public static void getNewsList(int catalog, int page, Callback<List<News>> callback) {
        if (NewsList.CATALOG_WEEK == catalog) {
            Client.instance.getNewsList(catalog, page, PAGE_SIZE, "week")
                    .enqueue(callback);
        } else if (NewsList.CATALOG_MONTH == catalog) {
            Client.instance.getNewsList(catalog, page, PAGE_SIZE, "month")
                    .enqueue(callback);
        }
    }

    public static void getBlogList(String type, int page, Callback<List<Blog>> callback) {
        Client.instance.getBlogList(type, page, PAGE_SIZE)
                .enqueue(callback);
    }

    public static void getPostList(int catalog, int page, Callback<List<Post>> callback) {
        Client.instance.getPostList(catalog, page, PAGE_SIZE)
                .enqueue(callback);
    }

    public static void getPostListByTag(String tag, int page, Callback<List<Post>> callback) {
        Client.instance.getPostListByTag(tag, page, PAGE_SIZE)
                .enqueue(callback);
    }

    public static void getTweetList(int uid, int page, Callback<List<Tweet>> callback) {
        Client.instance.getTweetList(uid, page, PAGE_SIZE)
                .enqueue(callback);
    }

    public static void getTweetTopicList(String title, int page, Callback<List<Tweet>> callback) {
        Client.instance.getTweetTopicList(title, page, PAGE_SIZE)
                .enqueue(callback);
    }

    public static void getTweetLikeList(Callback<List<TweetLike>> callback) {
        Client.instance.getTweetLikeList().enqueue(callback);
    }


    public static void getTweetLikeList(int tweetId, int page, Callback<List<User>> callback) {
        Client.instance.getTweetLikeList(tweetId, page, PAGE_SIZE)
                .enqueue(callback);
    }

    public static void pubLikeTweet(int tweetId, int authorId, Callback<Void> callback) {
        Client.instance.pubLikeTweet(tweetId, ApplicationLoader.instance.getLoginUid(), authorId)
                .enqueue(callback);
    }

    public static void pubUnLikeTweet(int tweetId, int authorId, Callback<Void> callback) {
        Client.instance.pubUnLikeTweet(tweetId, ApplicationLoader.instance.getLoginUid(), authorId)
                .enqueue(callback);
    }

    public static class RetrofitHolder {
        public final static Retrofit instance = build();

        private static Retrofit build() {
            // custom OkHttpClient to add preferred header for every request
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    return chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .addHeader("User-Agent", getUserAgent())
                                    .addHeader("Connection", "Keep-Alive")
                                    .addHeader("Accept-Language", Locale.getDefault().toString())
                                    .build()
                    );
                }
            });

            // initial OkHttpClient's CookieHandler
            // CookieStore cookieStore = CookieStoreFactory.create(CookieStoreFactory.TYPE_PREFERENCE_BASED);
            // httpClient.setCookieHandler(new CookieManager(cookieStore, null));

            return new Retrofit.Builder()
                    .baseUrl("http://www.oschina.net")
                    .client(httpClient)
                    .build();
        }

        /**
         * 获得请求的服务端数据的userAgent
         *
         * @return User Agent
         */
        @NonNull
        private static String getUserAgent() {
            return new StringBuilder("OSChina.NET/")
                    .append(ApplicationLoader.instance.getPackageInfo().versionName)
                    .append('_')
                    .append(ApplicationLoader.instance.getPackageInfo().versionCode)// app版本信息
                    .append("/Android/")// 手机系统平台
                    .append(android.os.Build.VERSION.RELEASE)// 手机系统版本
                    .append("/")
                    .append(android.os.Build.MODEL) // 手机型号
                    .append("/")
                    .append(AppConfig.getInstance().getAppId())// 客户端唯一标识
                    .toString();
        }
    }

    private static class Client {
        final static OSChinaCallService instance = RetrofitHolder.instance.create(OSChinaCallService.class);
    }
}
