package com.example.elnemr.final_movie_app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elnemr on 5/5/16.
 */
public class Detail_Fragment extends Fragment {
    public static boolean LAND = true;
    ImageView detailImage, trailerImage;
    TextView year, trailertext;
    TextView vote_average;
    TextView overview;
    TextView title;
    ListView listView, review;
    Pojo pojo;
    CheckBox favourite;
    String movieId;
    ProjectDBHelper dbHelper;

    public Detail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_layout, container, false);


        detailImage = (ImageView) view.findViewById(R.id.detail_movie_image);
        trailerImage = (ImageView) view.findViewById(R.id.trailerImage);
        year = (TextView) view.findViewById(R.id.deatil_movie_year);
        vote_average = (TextView) view.findViewById(R.id.detail_movie_vote_Average);
        overview = (TextView) view.findViewById(R.id.detail_movie_overview);
        title = (TextView) view.findViewById(R.id.detail_movie_title);
        trailertext = (TextView) view.findViewById(R.id.trailerText);
        listView = (ListView) view.findViewById(R.id.trailer);
        review = (ListView) view.findViewById(R.id.review);
        favourite = (CheckBox) view.findViewById(R.id.detail_movie_favourite);
        dbHelper = new ProjectDBHelper(getActivity().getApplicationContext());


        Bundle bundle = this.getArguments();
        pojo = (Pojo) bundle.getSerializable("z");
//        pojo = (Pojo) bundle.getStringArrayList("on");
        Picasso.with(getActivity().getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500/" + pojo.getImageurl())
                .into(detailImage);
        title.setText(pojo.getTitle());
        year.setText(pojo.getYear());
        vote_average.setText(pojo.getVote_average());
        overview.setText(pojo.getOverview());
        // Inflate the layout for this fragment
        movieId = pojo.getId();
        System.out.println(pojo.toString());
        new getTrailer().execute("http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=013fdbe9d59157bfc099e169c0ab7a83");

        favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                dbHelper.addInformation(pojo, sqLiteDatabase);
                Toast.makeText(getActivity().getApplicationContext(), "Done Save ", Toast.LENGTH_LONG).show();
                sqLiteDatabase.close();
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class getTrailer extends AsyncTask<String, Void, List<Pojo>> {
        List<Pojo> trailerList = new ArrayList<Pojo>();

        private static final String TAG_ARRAY = "results";
        private static final String TRAILER = "key";
        private static final String TRAILER_NAME = "name";


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Pojo> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();


                connection.connect();
                Log.e("  connectionconnect()  ", " connectionconnect();");
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Log.e("  bufferedReader()  ", " bufferedReader)");
                StringBuffer buffer = new StringBuffer();

                String Line = "";
                while ((Line = bufferedReader.readLine()) != null) {
                    buffer.append(Line);
                }
                String finalJSOn = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJSOn);
                JSONArray parentArray = parentObject.getJSONArray(TAG_ARRAY);

                trailerList = new ArrayList<>();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Pojo pojo = new Pojo();


                    pojo.setTrailerText(finalObject.getString(TRAILER_NAME));
                    pojo.setTrailer("https://www.youtube.com/watch?v=" + finalObject.getString(TRAILER));


                    trailerList.add(pojo);

                }
                return trailerList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<Pojo> trailerList) {
//          System.out.println("Size tlist " + trailerList.size());
//          System.out.println("Size tlist " + trailerList.get(0).getTrailerText());
//          System.out.println("Size tlist " + trailerList.get(1).getTrailerText());
//          System.out.println("Size tlisti " + movieId);

            if (trailerList != null) {

                Trailer trailer = new Trailer(getActivity().getApplicationContext(), trailerList);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(trailerList.get(position).getTrailer()));
                        startActivity(intent);


                    }
                });

                listView.setAdapter(trailer);
                trailer.notifyDataSetChanged();
                Log.e("getReviews", "befor");
                new getReviews().execute("http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=013fdbe9d59157bfc099e169c0ab7a83");
                Log.e("getReviews", "after");


            }


        }
    }

    public class getReviews extends AsyncTask<String, Void, List<Pojo>> {
        List<Pojo> reviewList = new ArrayList<Pojo>();

        private static final String TAG_ARRAY = "results";
        private static final String AUTHOR = "author";
        private static final String CONTENT = "content";


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Pojo> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();


                connection.connect();
                Log.e("  connectionconnect()  ", " connectionconnect();");
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Log.e("  bufferedReader()  ", " bufferedReader)");
                StringBuffer buffer = new StringBuffer();

                String Line = "";
                while ((Line = bufferedReader.readLine()) != null) {
                    buffer.append(Line);
                }
                String finalJSOn = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJSOn);
                JSONArray parentArray = parentObject.getJSONArray(TAG_ARRAY);

                reviewList = new ArrayList<>();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Pojo pojo = new Pojo();


                    pojo.setContent(finalObject.getString(CONTENT));
                    pojo.setAuthor(finalObject.getString(AUTHOR));


                    reviewList.add(pojo);

                }
                return reviewList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<Pojo> reviewList) {
            if (reviewList != null) {

                ReviewAdapter adapter = new ReviewAdapter(getActivity().getApplicationContext(), reviewList);
                review.setAdapter(adapter);

                    /*ArrayAdapter adapter = new ArrayAdapter(
                            getActivity().getApplicationContext(), R.layout.review,content
                    );

                    review.setAdapter(adapter);*/

            }


        }


    }


}
