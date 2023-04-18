package rs.raf.projekat1.vanja_kovinic_4220rn.db;

import android.provider.BaseColumns;

public class CalendarContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CalendarContract() {}

    /* Inner class that defines the table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";

        public static final String COLUMN_NAME_URL = "url";
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String COLUMN_NAME_START_TIME = "start_date";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_USERNAME = "user";
    }
}
