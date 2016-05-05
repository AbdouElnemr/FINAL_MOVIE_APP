package com.example.elnemr.final_movie_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elnemr on 5/5/16.
 */
public class ProjectDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "studentsDB.DB";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_MOVIEW_TABLE = "CREATE TABLE "
            + MovieTable.StudentInfo.TABLE_NAME
            + "("
            + MovieTable.StudentInfo.ID + " INTEGER PRIMARY KEY,"
            + MovieTable.StudentInfo.YEAR + " TEXT,"
            + MovieTable.StudentInfo.FAVOURITE + " TEXT ,"
            + MovieTable.StudentInfo.VOTEAVERAGE + " TEXT,"
            + MovieTable.StudentInfo.TITLE + " TEXT,"
            + MovieTable.StudentInfo.URL + " TEXT,"
            + MovieTable.StudentInfo.OVERVIEW + " TEXT"
            + ")";
    SQLiteDatabase sqLiteDatabase;

    public ProjectDBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
        Log.e("DataBase Operation", "Database Opned ");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIEW_TABLE);
        Log.e("DataBase Operation", "Database Opned ");

    }

    public void addInformation(Pojo pojo, SQLiteDatabase db) {


        System.out.println(pojo.toString());
        ContentValues values = new ContentValues();

        values.put(MovieTable.StudentInfo.ID, String.valueOf(pojo.getId()));
        values.put(MovieTable.StudentInfo.YEAR, String.valueOf(pojo.getYear()));
        values.put(MovieTable.StudentInfo.URL, String.valueOf(pojo.getImageurl()));
        values.put(MovieTable.StudentInfo.VOTEAVERAGE, String.valueOf(pojo.getVote_average()));
        values.put(MovieTable.StudentInfo.OVERVIEW, String.valueOf(pojo.getOverview()));
        values.put(MovieTable.StudentInfo.TITLE, String.valueOf(pojo.getTitle()));
        // insert row
        long tag_id = db.insert(MovieTable.StudentInfo.TABLE_NAME, null, values);
        System.out.println("Insert " + tag_id);

    }

    public Cursor selectfav(SQLiteDatabase db, String wherecol) {

        Cursor cursor = null;
        String sql = "SELECT * FROM " + MovieTable.StudentInfo.TABLE_NAME
                + " WHERE " + wherecol + "=" + "f";
        System.out.println("SQL DONE");
        cursor = db.rawQuery(sql, null);
        System.out.println("SQL DONE1");
        return cursor;
    }

    public long insertRow(SQLiteDatabase db, String test) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieTable.StudentInfo.FAVOURITE, test);
        System.out.println("Insert Data" + test);
        // insert row
        long tag_id = db.insert(MovieTable.StudentInfo.TABLE_NAME, null, values);
        System.out.println("Insert Done");

        return tag_id;
    }

    public List<Pojo> getInformation(SQLiteDatabase db) {

        List<Pojo> movieList = new ArrayList<Pojo>();
        String selectQuery = "SELECT  * FROM " + MovieTable.StudentInfo.TABLE_NAME;

//        Log.e(LOG, selectQuery);

        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Pojo t = new Pojo();
                t.setOverview(c.getString((c.getColumnIndex(MovieTable.StudentInfo.OVERVIEW))));
                t.setTitle(c.getString(c.getColumnIndex(MovieTable.StudentInfo.TITLE)));
                t.setImageurl(c.getString(c.getColumnIndex(MovieTable.StudentInfo.URL)));
                t.setVote_average(c.getString((c.getColumnIndex(MovieTable.StudentInfo.VOTEAVERAGE))));
                t.setYear(c.getString(c.getColumnIndex(MovieTable.StudentInfo.YEAR)));

                // adding to tags list
                movieList.add(t);
            } while (c.moveToNext());
        }
        System.out.println("Size List Database " + movieList.size());
        return movieList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_MOVIEW_TABLE);
    }
}
