package android.example.com.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.com.flixter.adapters.MovieAdapter;
import android.example.com.flixter.models.Movie;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.MOVIE_API_KEY;
    public static final String TAG = "MainActivity";
    List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        // Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Success API");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    Log.i(TAG, "Movies: " + movies.size());
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "JSON error", e);

                } catch (Exception e) {
                    Log.e(TAG, "Movie error", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Fail API");
            }
        });
    }
}
