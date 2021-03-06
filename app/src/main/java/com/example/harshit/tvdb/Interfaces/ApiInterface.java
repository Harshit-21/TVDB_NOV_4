package com.example.harshit.tvdb.Interfaces;

import com.example.harshit.tvdb.Pojo.Bean_Alternatives;
import com.example.harshit.tvdb.Pojo.Bean_CastnCrewResponse;
import com.example.harshit.tvdb.Pojo.Bean_GenreResponse;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_MovieImages;
import com.example.harshit.tvdb.Pojo.Bean_MovieResponse;
import com.example.harshit.tvdb.Pojo.Bean_ReleaseDateResponse;
import com.example.harshit.tvdb.Pojo.Bean_TranslationsResponse;
import com.example.harshit.tvdb.Pojo.Bean_TvDetails;
import com.example.harshit.tvdb.Pojo.Bean_TvResponse;
import com.example.harshit.tvdb.Pojo.Bean_TvSeasonResponse;
import com.example.harshit.tvdb.Pojo.Bean_YoutubeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Harshit on 9/16/2017.
 */

public interface ApiInterface {
    //************************this is for getting the genre of the movie********************************
    @GET("genre/movie/list")
    Call<Bean_GenreResponse> getGenreList(@Query("api_key") String apiKey);

    //************************this is for getting the list of movies according to the genre********************************
    @GET("genre/{genre_id}/movies")
    Call<Bean_MovieResponse> getMovieAccToGenre(@Path("genre_id") int genre_id, @Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page, @Query("include_adult") boolean is_adult, @Query("sort_by") String order);

    //************************this is for getting to get movie details ********************************
    @GET("movie/{movie_id}")
    Call<Bean_MovieDetails> getMovieDetails(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);

    //            ******************this method is used to get alternative titles of the movie n all stuff********
    @GET("movie/{movie_id}/alternative_titles")
    Call<Bean_Alternatives> getAlternativeTitles(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    //            ********************get credit of the movie ********************************
    @GET("movie/{movie_id}/credits")
    Call<Bean_CastnCrewResponse> getCredits(@Path("movie_id") String movie_id, @Query("api_key") String apiKey);

    //*************************to get images of the movie ********************
    @GET("movie/{movie_id}/images")
    Call<Bean_MovieImages> getMovieImages(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    //            *********************to get the release date of the movie in diffrent country--------
    @GET("movie/{movie_id}/release_dates")
    Call<Bean_ReleaseDateResponse> getReleaseDates(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    //            *******************to get the videos link to play on youtube**************
    @GET("movie/{movie_id}/videos")
    Call<Bean_YoutubeResponse> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);


//    **********to get the transalations means to get the iso n all stuff********************

    @GET("movie/{movie_id}/translations")
    Call<Bean_TranslationsResponse> getDiffrentRegionsInfoOfMovie(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);


    //          --------------------to get the recommended movies************************
    @GET("movie/{movie_id}/recommendations")
    Call<Bean_MovieResponse> getRecommendedMovies(@Path("movie_id") String movie_id, @Query("api_key") String apiKey, @Query("language") String language);
    //            **********************to get the now_playing movies***************

    @GET("movie/now_playing")
    Call<Bean_MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);

    //            **********************to get the popular movies***************

    @GET("movie/popular")
    Call<Bean_MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);

    //            **********************to get the top_rated movies***************

    @GET("movie/top_rated")
    Call<Bean_MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);

    //            **********************to get the upcoming movies***************

    @GET("movie/upcoming")
    Call<Bean_MovieResponse> getupComingMovies(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);




//    ****************************these are the metides for the tv series********************


    @GET("tv/top_rated")
    Call<Bean_TvResponse> getTopRatedTv(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("tv/popular")
    Call<Bean_TvResponse> getPopularTv(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);


    @GET("tv/on_the_air")
    Call<Bean_TvResponse> getOnTheAirTv(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);


    @GET("tv/airing_today")
    Call<Bean_TvResponse> getAiringToday(@Query("api_key") String apiKey, @Query("language") String language,@Query("page") int page);

    // this is to get the tv videos
    @GET("tv/{tv_id}/videos")
    Call<Bean_YoutubeResponse> getTvVideos(@Path("tv_id") int tv_id, @Query("api_key") String apiKey, @Query("language") String language);




//https://api.themoviedb.org/3/tv/{tv_id}?api_key=<<api_key>>&language=en-US

    @GET("tv/{tv_id}")
    Call<Bean_TvDetails> getTvDetails(@Path("tv_id") int tv_id, @Query("api_key") String apiKey, @Query("language") String language);

// to get the recommended movies
//    https://api.themoviedb.org/3/tv/{tv_id}/recommendations?api_key=<<api_key>>&language=en-US&page=1
    @GET("tv/{tv_id}/recommendations")
    Call<Bean_TvResponse> getRecommendedTv(@Path("tv_id") int tv_id, @Query("api_key") String apiKey, @Query("language") String language);

    // this is for the cast n crew for the tv
//https://api.themoviedb.org/3/tv/{tv_id}/credits?api_key=<<api_key>>&language=en-US

    @GET("tv/{tv_id}/credits")
    Call<Bean_CastnCrewResponse> getCreditsTV(@Path("tv_id") int tv_id, @Query("api_key") String apiKey, @Query("language") String language);

    // this is used to get the season and episode details

    //https://api.themoviedb.org/3/tv/{tv_id}/season/{season_number}?api_key=<<api_key>>&language=en-US

    @GET("tv/{tv_id}/season/{season_number}")
    Call<Bean_TvSeasonResponse> getTvSeasonEpisodes(@Path("tv_id") int tv_id, @Path("season_number") int season_no, @Query("api_key") String apiKey, @Query("language") String language);








}
