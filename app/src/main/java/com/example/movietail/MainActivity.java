package com.example.movietail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView poster;
    EditText movie;
    LinearLayout tobeshowed;
    RelativeLayout search;
    String moviename;
    boolean clicked = true;
    ConstraintLayout layout;
    View loading;
    FloatingActionButton share;
    TextView message;
    String API = "b7de8f4e";//<----Enter here your API key from http://www.omdbapi.com/
    TextView title,released,genre,runtime,director,actors,country,awards,plot,rate,language,show_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie = (EditText)findViewById(R.id.movie);
        search = (RelativeLayout) findViewById(R.id.search);
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
        show_more = findViewById(R.id.show_more);
        tobeshowed = findViewById(R.id.tobeshowed);

        message=findViewById(R.id.message);
        layout=findViewById(R.id.container);
        loading=findViewById(R.id.include);
        share=findViewById(R.id.sharenote);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movie.getText().toString().isEmpty()) {
                    moviename = movie.getText().toString();
                    loading.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    share.setVisibility(View.VISIBLE);
                    new movieTask().execute();
                }
                else{
                    Toast.makeText(MainActivity.this, "Type something to search", Toast.LENGTH_LONG).show();
                }
            }

        });

        movie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH) {
                    if(!movie.getText().toString().isEmpty()) {
                        moviename=movie.getText().toString();
                        loading.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.VISIBLE);
                        message.setVisibility(View.GONE);
                        share.setVisibility(View.VISIBLE);
                        new movieTask().execute();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Type something to search", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });


    }



    class movieTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            show_more.setVisibility(View.INVISIBLE);
            tobeshowed.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... args) {
            return HttpRequest.excuteGet("https://www.omdbapi.com/?apikey="+API+"&t="+moviename);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                loading.setVisibility(View.GONE);
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



                show_more.setVisibility(View.VISIBLE);
                show_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clicked) {
                            tobeshowed.setVisibility(View.VISIBLE);
                            show_more.setText(R.string.show_less);
                            clicked =false;
                        }
                        else {
                            tobeshowed.setVisibility(View.GONE);
                            show_more.setText(R.string.show_more);
                            clicked =true;
                        }
                    }
                });

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

    public void ShareNote(View view){
        Intent share=new Intent(Intent.ACTION_SEND);
        String message;
        message="Movie Title :\n"+title.getText().toString()+"\n"+"Released date :\n"+released.getText().toString()
                +"\n"+"Review :\n"+rate.getText().toString()+"\n"+"Language :\n"+language.getText().toString()
                +"\n"+"Genre :\n"+genre.getText().toString()+"\n"+"Run time :\n"+runtime.getText().toString()
                +"\n"+"Actors :\n"+actors.getText().toString()+"\n"+"Director :\n"+director.getText().toString()
        ;
        share.putExtra(Intent.EXTRA_TEXT,message);
        share.setType("text/plain");
        startActivity(share);
    }

}
