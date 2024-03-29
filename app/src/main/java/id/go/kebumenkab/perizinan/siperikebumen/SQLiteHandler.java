package id.go.kebumenkab.perizinan.siperikebumen;

/**
 * Created by Faldy on 26/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_UID = "uid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JABATAN = "jabatan";
    private static final String KEY_GOLONGAN = "golongan";
    private static final String KEY_NO_TELP = "no_telp";
    private static final String KEY_NO_HP = "no_hp";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_URL_API = "url_api";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_STATUS = "status";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMA + " TEXT,"
                + KEY_GOLONGAN + " TEXT," + KEY_NO_TELP + " TEXT," + KEY_NO_HP + " TEXT,"
                + KEY_ALAMAT + " TEXT," + KEY_UID + " TEXT," + KEY_JABATAN + " TEXT,"
                + KEY_USERNAME + " TEXT," + KEY_URL_API + " TEXT," + KEY_USER_TYPE + " TEXT," + KEY_STATUS + " INTEGER" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String nama, String uid, String jabatan, String golongan, String no_telp, String no_hp, String alamat, String username, String url_api, String user_type, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, nama);
        values.put(KEY_UID, uid);
        values.put(KEY_JABATAN, jabatan);
        values.put(KEY_GOLONGAN, golongan);
        values.put(KEY_NO_TELP, no_telp);
        values.put(KEY_NO_HP, no_hp);
        values.put(KEY_ALAMAT, alamat);
        values.put(KEY_USERNAME, username);
        values.put(KEY_URL_API, url_api);
        values.put(KEY_USER_TYPE, user_type);
        values.put(KEY_STATUS, status);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("nama", cursor.getString(1));
            user.put("golongan", cursor.getString(2));
            user.put("no_telp", cursor.getString(3));
            user.put("no_hp", cursor.getString(4));
            user.put("alamat", cursor.getString(5));
            user.put("uid", cursor.getString(6));
            user.put("jabatan", cursor.getString(7));
            user.put("username", cursor.getString(8));
            user.put("url_api", cursor.getString(9));
            user.put("user_type", cursor.getString(10));
            user.put("status", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
