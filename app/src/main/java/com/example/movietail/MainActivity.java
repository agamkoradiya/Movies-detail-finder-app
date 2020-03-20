package com.example.movietail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView poster;
    EditText movie;
    ImageView search;
    String moviename;
    String API = "";//<----Enter here your API key from http://www.omdbapi.com/
    TextView title,released,genre,runtime,director,actors,country,awards,plot,rate,language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie = (EditText)findViewById(R.id.movie);
        search = (ImageView) findViewById(R.id.button);
        poster = (ImageView) findViewById(R.id.poster);
        title = (TextView)findViewById(R.id.title);
        released = (TextView)findViewById(R.id.released);
        genre = (TextView)findViewById(R.id.genre);
        runtime = (TextView)findViewById(R.id.runtime);
        director = (TextView)findViewById(R.id.director);
        actors = (TextView)findViewById(R.id.actors);
        country = (TextView)findViewById(R.id.country);
        awards = (TextView)findViewById(R.id.awards);
        rate = (TextView)findViewById(R.id.rate);
        plot = (TextView)findViewById(R.id.plot);
        language = (TextView)findViewById(R.id.language);




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviename=movie.getText().toString();
                new movieTask().execute();
            }
        });
    }

    class movieTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://www.omdbapi.com/?apikey="+API+"&t="+moviename);
            return response;
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



                //SET DATA IN ACTIVITY :
                Glide.with(getApplicationContext()).load(poster_image).into(poster);
                title.setText(title_data);
                released.setText(released_data);
                genre.setText(genre_data);
                runtime.setText(runtime_data);
                director.setText(director_data);
                actors.setText(actors_data);
                country.setText(country_data);
                awards.setText(awards_data);
                rate.setText(rate_data);
                plot.setText(plot_data);
                language.setText(language_data);



            } catch (Exception e) {

                Toast.makeText(MainActivity.this, "Enter valid movie name", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
