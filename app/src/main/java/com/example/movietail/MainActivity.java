package com.example.movietail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.bumptech.glide.Glide;
import com.example.movietail.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String moviename;
    boolean clicked = true;
    String API = "cd39e789";//<----Enter here your API key from http://www.omdbapi.com/

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        activityMainBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected(MainActivity.this)) {
                    moviename = activityMainBinding.movie.getText().toString();
                    new movieTask().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //    CHECK NETWORK CONNECTED OR NOT
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    class movieTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activityMainBinding.showMore.setVisibility(View.INVISIBLE);
            activityMainBinding.tobeshowed.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... args) {
            return HttpRequest.excuteGet("https://www.omdbapi.com/?apikey=" + API + "&t=" + moviename);
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                //JSON OBJECT :
                JSONObject jsonObj = new JSONObject(result);
                //   JSONObject rate = jsonObj.getJSONArray("Ratings").getJSONObject(1);

                //GET DATA FROM API :
                String poster_image = jsonObj.getString("Poster");
                String title_data = jsonObj.getString("Title");
                String released_data = jsonObj.getString("Released");
                String genre_data = jsonObj.getString("Genre");
                String runtime_data = jsonObj.getString("Runtime");
                String country_data = jsonObj.getString("Country");
                String language_data = jsonObj.getString("Language");
                String director_data = jsonObj.getString("Director");
                String actors_data = jsonObj.getString("Actors");
                String awards_data = jsonObj.getString("Awards");
                String rate_data = jsonObj.getString("imdbRating");
                String plot_data = jsonObj.getString("Plot");

                activityMainBinding.movieDetail.setVisibility(View.VISIBLE);
                activityMainBinding.showMore.setVisibility(View.VISIBLE);
                activityMainBinding.showMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clicked) {
                            activityMainBinding.tobeshowed.setVisibility(View.VISIBLE);
                            clicked = false;
                        } else {
                            activityMainBinding.tobeshowed.setVisibility(View.GONE);
                            clicked = true;
                        }
                    }
                });

                //SET DATA IN ACTIVITY :
                Glide.with(getApplicationContext()).load(poster_image).into(activityMainBinding.poster);
                activityMainBinding.title.setText(title_data);
                activityMainBinding.released.setText(released_data);
                activityMainBinding.genre.setText(genre_data);
                activityMainBinding.runtime.setText(runtime_data);
                activityMainBinding.director.setText(director_data);
                activityMainBinding.actors.setText(actors_data);
                activityMainBinding.country.setText(country_data);
                activityMainBinding.awards.setText(awards_data);
                activityMainBinding.rate.setText(rate_data);
                activityMainBinding.plot.setText(plot_data);
                activityMainBinding.language.setText(language_data);

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Enter valid movie name", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
