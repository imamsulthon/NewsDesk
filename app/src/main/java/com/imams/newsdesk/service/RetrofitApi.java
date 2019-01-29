package com.imams.newsdesk.service;

import com.imams.newsdesk.model.response.ArticleResponse;
import com.imams.newsdesk.model.response.SourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {

    String BASE_URL = "https://newsapi.org/v2/";

    @GET("sources/")
    Call<SourceResponse> getAllNewsSources(
            @Query("apiKey") String apiKey);

    @GET("top-headlines/")
    Call<ArticleResponse> getTopHeadlines(
            @Query("sources") String id,
            @Query("apiKey") String apiKey);

    @GET("everything/")
    Call<ArticleResponse> getArticles(
            @Query("sources") String sources,
            @Query("q") String query,
            @Query("page") int page,
            @Query("apiKey") String apiKey);


}
