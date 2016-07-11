package net.gility.acrida.network;

import net.gility.acrida.content.Blog;
import net.gility.acrida.content.LoginUserBean;
import net.gility.acrida.content.News;
import net.gility.acrida.content.Post;
import net.gility.acrida.content.Tweet;
import net.gility.acrida.content.TweetLike;
import net.gility.acrida.content.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * OSChina Request Api
 *
 * @author Alimy
 * @version 0.9 Created on 10/28/15.
 */
public interface OSChinaService {

    @POST("action/api/login_validate")
    Call<LoginUserBean> login(
            @Field("username") String userName,
            @Field("pwd") String password,
            @Field("keep_login") int keepLogin);

    @GET("action/api/news_list")
    Call<List<News>> getNewsList(
            @Query("catalog") int catalog,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("show") String show);

    @GET("action/api/blog_list")
    Call<List<Blog>> getBlogList(
            @Query("type") String type,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @GET("action/api/post_list")
    Call<List<Post>> getPostList(
            @Query("catalog") int catalog,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @GET("action/api/post_list")
    Call<List<Post>> getPostListByTag(
            @Query("tag") String tag,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @GET("action/api/tweet_list")
    Call<List<Tweet>> getTweetList(
            @Query("uid") int uid,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @GET("action/api/tweet_topic_list")
    Call<List<Tweet>> getTweetTopicList(
            @Query("title") String title,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @GET("action/api/action/api/my_tweet_like_list")
    @Headers("Connection: Keep-Alive")
    Call<List<TweetLike>> getTweetLikeList();

    @GET("action/api/action/api/tweet_like_list")
    Call<List<User>> getTweetLikeList(
            @Query("tweetid") int tweetId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

    @POST("action/api/action/api/tweet_like")
    Call<Void> pubLikeTweet(
            @Field("tweetid") int tweetId,
            @Field("uid") int uid,
            @Field("ownerOfTweet") int authorId);

    @POST("action/api/tweet_unlike")
    Call<Void> pubUnLikeTweet(
            @Field("tweetid") int tweetId,
            @Field("uid") int uid,
            @Field("ownerOfTweet") int authorIdint);
}
