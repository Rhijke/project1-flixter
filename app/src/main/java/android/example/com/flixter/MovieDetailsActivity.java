package android.example.com.flixter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.example.com.flixter.models.Movie;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.parceler.Parcels;
import com.bumptech.glide.Glide;
public class MovieDetailsActivity extends AppCompatActivity{
    // the movie to display
    Movie movie;
    // the view objects
    TextView tvTitle, tvOverview;
    ImageView tvPoster;
    RatingBar rbVoteAverage;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // set variables
        context = this;
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvPoster = (ImageView) findViewById(R.id.tvPoster);
        // unwrap the movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // set the title and overview and poster
        try {
            getSupportActionBar().setTitle(movie.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch(Exception e) {
            Log.d("MovieDetails", e.getMessage());
        }

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        String imageURL;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL=movie.getBackdropPath();
        } else {
            imageURL=movie.getPosterPath();
        }
        Glide.with(context).load(imageURL).into(tvPoster);

        // vote average is 0..10 convert to 0..5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage/2.0f : voteAverage);

        tvPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to display MovieDetailsActivity
                Intent intent = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                // Pass the movie as an extra serialized via Parcels.wrap()
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Show the activity
                startActivity(intent);
            }
        });

    }

    // handle arrow click here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
