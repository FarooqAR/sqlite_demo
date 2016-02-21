package com.example.floatingdemo.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by 192.168.3.31 on 2/21/2016.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="StudentDb";
    public static final String TABLE_NAME = "students";
    public static final String NAME = "name";
    public static final String UNI= "university";
    public static final String AGE= "age";
    public static final int VERSION= 1;
    private static SqliteHelper helper;
    public static SqliteHelper getHelper(Context context){
        if(helper == null){
            helper = new SqliteHelper(context,DB_NAME,null,VERSION);
        }
        return helper;
    }

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id integer primary key," + NAME + " text," + UNI + " text," + AGE + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createStudent(String name, String uni, String age, final CompletionListener listener){
        new AsyncTask<String, Void, Long>() {
            @Override
            protected Long doInBackground(String... params) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(NAME,params[0]);
                values.put(UNI,params[1]);
                values.put(AGE, params[2]);
                return db.insert(TABLE_NAME,null,values);
            }

            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                listener.onInserted(result);
            }
        }.execute(name, uni, age);
    }
    public void getAllStudents(final CompletionListener listener){
        new AsyncTask<Void,Void,ArrayList<Student>>(){

            @Override
            protected ArrayList<Student> doInBackground(Void... params) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
                ArrayList<Student> students = new ArrayList<Student>();
                while(result.moveToNext()){
                    String name = result.getString(result.getColumnIndex(NAME));
                    String uni = result.getString(result.getColumnIndex(UNI));
                    String age = result.getString(result.getColumnIndex(AGE));
                    Student student =new Student();
                    student.setAge(age);
                    student.setName(name);
                    student.setUni(uni);
                    students.add(student);
                }
                result.close();
                return students;
            }

            @Override
            protected void onPostExecute(ArrayList<Student> students) {
                super.onPostExecute(students);
                listener.onRetrieved(students);
            }
        }.execute();
    }
    public interface CompletionListener{
        void onInserted(Long result);
        void onRetrieved(ArrayList<Student> students);
    }
}
