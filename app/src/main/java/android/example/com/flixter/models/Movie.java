package android.example.com.flixter.models;

import android.example.com.flixter.BuildConfig;
import android.os.Build;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.facebook.stetho.json.annotation.JsonProperty;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;

@Parcel
public class Movie {
    String posterPath, title, overview, backdropPath, trailerPath;
    Double voteAverage;
    Integer id;
    public Movie() {

    }
    public Movie(JSONObject jsonObject) throws Exception {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        findTrailerPath();
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws Exception{
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i <movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() { return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);}
    public Double getVoteAverage() {
        return voteAverage;
    }
    public int getId() {return id;}

    private void findTrailerPath() {
        AsyncHttpClient client = new AsyncHttpClient();
        String VIDEO_URL = String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=%s", id, BuildConfig.MOVIE_API_KEY);
        client.get(VIDEO_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject trailer = results.getJSONObject(0);
                    trailerPath = trailer.getString("key");
                    Log.i("Movie", "Results: " + results.toString());
                } catch (JSONException e) {
                    Log.e("Movie", "JSON error", e);

                } catch (Exception e) {
                    Log.e("Movie", "Movie error", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Movie", "Fail API");
            }
        });
    }

    public String getTrailerPath() {
        return trailerPath;
    }
}
