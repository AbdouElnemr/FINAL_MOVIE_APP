package com.example.elnemr.final_movie_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

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
public class Home extends Fragment {

    //    ProjectDBHelper dbHelper;
    private static final String TAG = Home.class.getSimpleName();
    GridView gridView;
    //     TextView thetitle, thevoteAverage, theOverview, theYear;
//    ImageView imageView;
//     Pojo pojo;
    JSONClass jsonClass = new JSONClass();
    List<Pojo> pojoList;
    private MyAdapter movieAdapter;
    static String STATE = "popular?";

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);

        setHasOptionsMenu(true);
        pojoList = new ArrayList<>(0);

        movieAdapter = new MyAdapter(getActivity(), pojoList);
        gridView.setAdapter(movieAdapter);

//        thetitle = (TextView) view.findViewById(R.id.detail_movie_title);
//        thevoteAverage = (TextView) view.findViewById(R.id.detail_movie_vote_Average);
//        theOverview = (TextView) view.findViewById(R.id.detail_movie_overview);
//        theYear = (TextView) view.findViewById(R.id.deatil_movie_year);
        executeTask();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state", STATE);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            STATE = savedInstanceState.getString("state");
        }
    }
    public void executeTask() {
        JSONClass task = new JSONClass();
        task.execute(STATE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            STATE = "popular?";
            executeTask();
            return true;
        } else if (id == R.id.top_rated) {
            STATE = "top_rated?";
            executeTask();
            return true;

        } else if (id == R.id.favourite) {
            ProjectDBHelper dbHelper = new ProjectDBHelper(getActivity().getApplicationContext());
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            System.out.println("Before read");
//            pojoList.clear();
//            pojoList.addAll();
            movieAdapter.addAll(dbHelper.getInformation(sqLiteDatabase));
            if (pojoList.isEmpty()) {
                Toast.makeText(getContext(), "No favorites", Toast.LENGTH_SHORT).show();
            }
            sqLiteDatabase.close();
            dbHelper.close();
//            Cursor cursor = (Cursor) pojoList;
//            if (cursor.moveToFirst()) {
//                do {
//
//                    thetitle.setText(Integer.parseInt(cursor.getString(0)));
//                    theOverview.setText(cursor.getString(1));
//                    thevoteAverage.setText(cursor.getString(2));
//                    theYear.setText(cursor.getString(3));
//
//                } while (cursor.moveToNext());

//            }


        }
        return super.onOptionsItemSelected(item);
    }

    public boolean testConnection() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isConnected();

    }

    public class JSONClass extends AsyncTask<String, Void, List<Pojo>> {
        private static final String TAG_ARRAY = "results";
        private static final String TAG_POSTER_PATH = "poster_path";
        private static final String TAG_OVERVIEW = "overview";
        private static final String TAG_ORIGINAL_TITLE = "original_title";
        private static final String TAG_VOTE_AVERAGE = "vote_average";
        private static final String RELEASE_DATE = "release_date";
        private static final String MoVIE_ID = "id";
        List<Pojo> pojoList = new ArrayList<Pojo>();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Pojo> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            String FORECAST_BASE_URL =
                    "https://api.themoviedb.org/3/movie/" + params[0];
            try {
                final String API_KEY = "api_key";
//                if (testConnection()) {
                Log.e("do in background", " before");
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, "013fdbe9d59157bfc099e169c0ab7a83").build();
                URL url = new URL(builtUri.toString());
                connection = (HttpURLConnection) url.openConnection();
                Log.e("do in background", " after");

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

                pojoList = new ArrayList<>();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Pojo pojo = new Pojo();

                    pojo.setImageurl(finalObject.getString(TAG_POSTER_PATH));
                    pojo.setOverview(finalObject.getString(TAG_OVERVIEW));
                    pojo.setId(finalObject.getString(MoVIE_ID));
                    pojo.setTitle(finalObject.getString(TAG_ORIGINAL_TITLE));
                    pojo.setVote_average(finalObject.getString(TAG_VOTE_AVERAGE));
                    pojo.setYear(finalObject.getString(RELEASE_DATE));

                    pojoList.add(pojo);
                }
                return pojoList;
//                } else {
                /*    Log.e("No Connection", "No Connection ");
                    dbHelper = new ProjectDBHelper(getActivity().getApplicationContext());
                    sqLiteDatabase = dbHelper.getReadableDatabase();
                    pojoList= dbHelper.getInformation(sqLiteDatabase);*/

//                    Toast.makeText(getActivity().getApplication()," No Connectionn ", Toast.LENGTH_LONG).show();

//                    return null;
//                }


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
        protected void onPostExecute(List<Pojo> aVoid) {

//          System.out.println("Size List Home "+ aVoid.size());

//        System.out.println("Size List Home "+ aVoid.get(0).getImageurl());
//          System.out.println("Size List Home "+ aVoid.get(0).getTitle());


            if (aVoid != null) {

                System.out.println("Size List Home " + aVoid.size());
//                pojoList.clear();
//                pojoList.addAll(aVoid);
                movieAdapter.addAll(aVoid);
                MainActivity.pojo = aVoid;

            } else {
//                Toast.makeText(getContext(), "Error!!", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
