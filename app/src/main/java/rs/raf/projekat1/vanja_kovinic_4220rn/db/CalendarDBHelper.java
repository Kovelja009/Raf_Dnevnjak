package rs.raf.projekat1.vanja_kovinic_4220rn.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.raf.projekat1.vanja_kovinic_4220rn.model.User;


public class CalendarDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="calendar.db";
    public static final int DATABASE_VERSION = 1;

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    private static CalendarDBHelper instance;

    private CalendarDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static CalendarDBHelper instanceOfDatabase(Context context) {
        if (instance == null) {
            instance = new CalendarDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERLIST_TABLE = "CREATE TABLE " +

                CalendarContract.UserEntry.TABLE_NAME + " (" +
                CalendarContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CalendarContract.UserEntry.COLUMN_NAME_EMAIL + " TEXT, " +
                CalendarContract.UserEntry.COLUMN_NAME_USERNAME + " TEXT, " +
                CalendarContract.UserEntry.COLUMN_NAME_PASSWORD + " TEXT, " +
                CalendarContract.UserEntry.COLUMN_NAME_URL + " TEXT "

                +

                ");";

        db.execSQL(SQL_CREATE_USERLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + CalendarContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addUserToDatabase(String email, String username, String password, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_EMAIL, email);
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_USERNAME, username);
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_PASSWORD, password);
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_URL, url);

        db.insert(CalendarContract.UserEntry.TABLE_NAME, null, cv);
    }

    public void updateUserInDB(String email, String username, String password, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_EMAIL, email);
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_USERNAME, username);
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_PASSWORD, password);
        cv.put(CalendarContract.UserEntry.COLUMN_NAME_URL, url);

        db.update(CalendarContract.UserEntry.TABLE_NAME, cv, CalendarContract.UserEntry.COLUMN_NAME_EMAIL + " = ?", new String[]{email});
    }

    public User getUserFromDB(String email, String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = CalendarContract.UserEntry.COLUMN_NAME_EMAIL + " = ? AND " + CalendarContract.UserEntry.COLUMN_NAME_USERNAME + " = ? AND " + CalendarContract.UserEntry.COLUMN_NAME_PASSWORD + " = ?";
        try (Cursor result = db.query(CalendarContract.UserEntry.TABLE_NAME, null, where, new String[]{email, username, password}, null, null, null)){
            if(result == null){
                return null;
            }
            result.moveToFirst();
            String emailFromDB = result.getString(1);
            String usernameFromDB = result.getString(2);
            String passwordFromDB = result.getString(3);
            String urlFromDB = result.getString(4);
            return new User(emailFromDB, usernameFromDB, passwordFromDB, urlFromDB);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getUsersFromDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor result = db.query(CalendarContract.UserEntry.TABLE_NAME, null, null, null, null, null, null)){
            List<User> users = new ArrayList<>();
            while(result.moveToNext()){
                String emailFromDB = result.getString(1);
                String usernameFromDB = result.getString(2);
                String passwordFromDB = result.getString(3);
                String urlFromDB = result.getString(4);
                users.add(new User(emailFromDB, usernameFromDB, passwordFromDB, urlFromDB));
            }
            return users;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getStringFromDate(Date date){
        if(date == null){
            return null;
        }
        return dateFormat.format(date);
    }

    private Date getDateFromString(String date){
        try {

            return dateFormat.parse(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
