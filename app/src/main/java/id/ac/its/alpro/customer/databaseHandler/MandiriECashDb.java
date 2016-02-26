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
import id.ac.its.alpro.customer.component.Mandiri;

/**
 * Created by Luffi on 27/02/2016.
 */
public class MandiriECashDb extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MandiriTable";
    // Books table name
    private static final String TABLE_MANDIRI = "mandiri";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";


    private static final String[] COLUMNS = {KEY_ID,KEY_PHONE,KEY_PASSWORD};


    public MandiriECashDb(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_AUTH_TABLE = "CREATE TABLE mandiri ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "phone TEXT, " +
                "password TEXT)";
        db.execSQL(CREATE_AUTH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mandiri");
        this.onCreate(db);
    }

    public void insert(Mandiri mandiri){

        Log.d("insert", mandiri.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, mandiri.getPhone());
        values.put(KEY_PASSWORD, mandiri.getPassword());
        db.insert(TABLE_MANDIRI, null, values);
        db.close();
    }

    public Mandiri get(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Mandiri mandiri = new Mandiri();
        Cursor cursor =
                db.query(TABLE_MANDIRI, // a. table
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
            mandiri.setId(Integer.parseInt(cursor.getString(0)));
            mandiri.setPhone(cursor.getString(1));
            mandiri.setPassword(cursor.getString(2));

            Log.d("get(" + id + ")", mandiri.toString());
        }
        catch (Exception e){

        }

        return mandiri;
    }

    public List<Mandiri> getAll() {
        List<Mandiri> mandiris = new LinkedList<Mandiri>();
        String query = "SELECT  * FROM " + TABLE_MANDIRI;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Mandiri mandiri = null;
        if (cursor.moveToFirst()) {
            do {
                mandiri = new Mandiri();
                mandiri.setId(Integer.parseInt(cursor.getString(0)));
                mandiri.setPhone(cursor.getString(1));
                mandiri.setPassword(cursor.getString(2));
                mandiris.add(mandiri);
            } while (cursor.moveToNext());
        }

        Log.d("getAll()", mandiris.toString());
        return mandiris;
    }

    public int update(Mandiri mandiri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, mandiri.getPhone());
        values.put(KEY_PASSWORD, mandiri.getPassword());

        int i = db.update(TABLE_MANDIRI, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(mandiri.getId()) }); //selection args
        db.close();

        return i;
    }

    public void delete(Mandiri mandiri) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MANDIRI, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(mandiri.getId())}); //selections args
        db.close();
        Log.d("delete", mandiri.toString());

    }
}
