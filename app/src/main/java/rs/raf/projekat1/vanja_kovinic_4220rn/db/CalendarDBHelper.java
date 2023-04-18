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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;
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

        final String SQL_CREATE_TASKLIST_TABLE = "CREATE TABLE " +

                CalendarContract.TaskEntry.TABLE_NAME + " (" +
                CalendarContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CalendarContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT, " +
                CalendarContract.TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                CalendarContract.TaskEntry.COLUMN_NAME_START_TIME + " TEXT, " +
                CalendarContract.TaskEntry.COLUMN_NAME_END_TIME + " TEXT, " +
                CalendarContract.TaskEntry.COLUMN_NAME_PRIORITY + " TEXT, " +
                CalendarContract.TaskEntry.COLUMN_NAME_USERNAME + " TEXT "

                +

                ");";

        db.execSQL(SQL_CREATE_USERLIST_TABLE);
        db.execSQL(SQL_CREATE_TASKLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + CalendarContract.UserEntry.TABLE_NAME);
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

    public void addTaskToDatabase(String title, String description, String startTime, String endTime, String priority, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_TITLE, title);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_START_TIME, startTime);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_END_TIME, endTime);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_PRIORITY, priority);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_USERNAME, username);

        db.insert(CalendarContract.TaskEntry.TABLE_NAME, null, cv);
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

    public void updateTaskInDB(String title, String description, String startTime, String endTime, String priority, String username, String oldStartTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_TITLE, title);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_START_TIME, startTime);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_END_TIME, endTime);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_PRIORITY, priority);
        cv.put(CalendarContract.TaskEntry.COLUMN_NAME_USERNAME, username);

        String where = CalendarContract.TaskEntry.COLUMN_NAME_START_TIME + " = ? AND " + CalendarContract.TaskEntry.COLUMN_NAME_USERNAME + " = ?";
        db.update(CalendarContract.TaskEntry.TABLE_NAME, cv, where, new String[]{oldStartTime, username});
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

    public Task getTaskFromDB(String username, String startDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = CalendarContract.TaskEntry.COLUMN_NAME_START_TIME + " = ? AND " + CalendarContract.TaskEntry.COLUMN_NAME_USERNAME + " = ?";
        try (Cursor result = db.query(CalendarContract.TaskEntry.TABLE_NAME, null, where, new String[]{startDate, username}, null, null, null)) {
            if (result == null) {
                return null;
            }
            result.moveToFirst();
            String titleFromDB = result.getString(1);
            String descriptionFromDB = result.getString(2);
            String startTimeFromDB = result.getString(3);
            String endTimeFromDB = result.getString(4);
            int priorityFromDB = Integer.parseInt(result.getString(5));
            return new Task(priorityFromDB, titleFromDB, descriptionFromDB, Task.convertTimeFromDBFormat(startTimeFromDB), Task.convertTimeFromDBFormat(endTimeFromDB));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void deleteTaskFromDB(String username, String startDate) {
          SQLiteDatabase db = this.getWritableDatabase();
          String where = CalendarContract.TaskEntry.COLUMN_NAME_START_TIME + " = ? AND " + CalendarContract.TaskEntry.COLUMN_NAME_USERNAME + " = ?";
          db.delete(CalendarContract.TaskEntry.TABLE_NAME, where, new String[]{startDate, username});
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

    public List<Task> getTasksFromDB(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = CalendarContract.TaskEntry.COLUMN_NAME_USERNAME + " = ?";
        try (Cursor result = db.query(CalendarContract.TaskEntry.TABLE_NAME, null, where, new String[]{username}, null, null, null)){
            List<Task> tasks = new ArrayList<>();
            while(result.moveToNext()){
                String titleFromDB = result.getString(1);
                String descriptionFromDB = result.getString(2);
                String startTimeFromDB = result.getString(3);
                String endTimeFromDB = result.getString(4);
                int priorityFromDB = Integer.parseInt(result.getString(5));
                tasks.add(new Task(priorityFromDB, titleFromDB, descriptionFromDB, Task.convertTimeFromDBFormat(startTimeFromDB), Task.convertTimeFromDBFormat(endTimeFromDB)));
            }
            return tasks;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Task> getTasksByDayFromDB(String username, LocalDate day){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = CalendarContract.TaskEntry.COLUMN_NAME_USERNAME + " = ?";
        try (Cursor result = db.query(CalendarContract.TaskEntry.TABLE_NAME, null, where, new String[]{username}, null, null, null)){
            List<Task> tasks = new ArrayList<>();
            while(result.moveToNext()){
                String titleFromDB = result.getString(1);
                String descriptionFromDB = result.getString(2);
                String startTimeFromDB = result.getString(3);
                String endTimeFromDB = result.getString(4);
                int priorityFromDB = Integer.parseInt(result.getString(5));
                LocalDateTime startTime = Task.convertTimeFromDBFormat(startTimeFromDB);
                LocalDateTime endTime = Task.convertTimeFromDBFormat(endTimeFromDB);

                if(startTime.toLocalDate().equals(day) || endTime.toLocalDate().equals(day))
                    tasks.add(new Task(priorityFromDB, titleFromDB, descriptionFromDB, startTime, endTime));
            }

            return tasks.stream().sorted(Comparator.comparing(Task::getStartTime)).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
