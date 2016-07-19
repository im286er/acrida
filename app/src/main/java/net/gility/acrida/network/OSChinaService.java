package net.gility.acrida.network;

import net.gility.acrida.content.BlogList;
import net.gility.acrida.content.NewsList;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * OSChina Request Api
 *
 * @author Alimy
 * @version 0.9 Created Michael Li on 07/19/16.
 */
public interface OSChinaService {

    @GET("action/api/news_list")
    Observable<Result<NewsList>> getNewsList(
            @Query("catalog") int catalog,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("show") String show);

    @GET("action/api/blog_list")
    Observable<Result<BlogList>> getBlogList(
            @Query("type") String type,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize);

}
