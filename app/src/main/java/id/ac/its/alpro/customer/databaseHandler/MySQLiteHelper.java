package id.ac.its.alpro.customer.databaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import id.ac.its.alpro.customer.component.Auth;


/**
 * Created by ALPRO on 24/12/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AuthTable";
    // Books table name
    private static final String TABLE_AUTH = "auth";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_STATUS = "status";


    private static final String[] COLUMNS = {KEY_ID,KEY_USERNAME,KEY_PASSWORD,KEY_TOKEN,KEY_STATUS};


    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_AUTH_TABLE = "CREATE TABLE auth ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "token TEXT, " +
                "status TEXT)";
        db.execSQL(CREATE_AUTH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS auth");
        this.onCreate(db);
    }

    public void insert(Auth auth){

        Log.d("insert", auth.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, auth.getUsername());
        values.put(KEY_PASSWORD, auth.getPassword());
        values.put(KEY_TOKEN, auth.getToken());
        values.put(KEY_STATUS, auth.getStatus());
        db.insert(TABLE_AUTH, null, values);
        db.close();
    }

    public Auth get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Auth auth = new Auth();
        Cursor cursor =
                db.query(TABLE_AUTH, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();

        try {
            auth.setId(Integer.parseInt(cursor.getString(0)));
            auth.setUsername(cursor.getString(1));
            auth.setPassword(cursor.getString(2));
            auth.setToken(cursor.getString(3));
            auth.setStatus(cursor.getString(4));

            Log.d("get(" + id + ")", auth.toString());
        }
        catch (Exception e){

        }

        return auth;
    }

    public List<Auth> getAll() {
        List<Auth> auths = new LinkedList<Auth>();
        String query = "SELECT  * FROM " + TABLE_AUTH;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Auth auth = null;
        if (cursor.moveToFirst()) {
            do {
                auth = new Auth();
                auth.setId(Integer.parseInt(cursor.getString(0)));
                auth.setUsername(cursor.getString(1));
                auth.setPassword(cursor.getString(2));
                auth.setToken(cursor.getString(3));
                auth.setStatus(cursor.getString(4));
                auths.add(auth);
            } while (cursor.moveToNext());
        }

        Log.d("getAll()", auths.toString());
        return auths;
    }

    public int update(Auth auth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, auth.getUsername());
        values.put(KEY_PASSWORD, auth.getPassword());
        values.put(KEY_TOKEN, auth.getToken());
        values.put(KEY_STATUS, auth.getStatus());

        int i = db.update(TABLE_AUTH, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(auth.getId()) }); //selection args
        db.close();

        return i;
    }

    public void delete(Auth auth) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUTH, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(auth.getId())}); //selections args
        db.close();
        Log.d("delete", auth.toString());

    }
}
