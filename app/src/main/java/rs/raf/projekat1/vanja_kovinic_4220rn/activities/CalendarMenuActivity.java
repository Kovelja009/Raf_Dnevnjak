package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import timber.log.Timber;

public class CalendarMenuActivity extends AppCompatActivity {
    public static final String EMAIL_STRING = "email";
    public static final String USERNAME_STRING = "username";
    public static final String PASSWORD_STRING = "password";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_menu);
        initView();
    }

    private void initView(){
        textView = findViewById(R.id.pocetak);
        Intent intent = getIntent();
        if(intent != null){
            String name = intent.getStringExtra(EMAIL_STRING);
            textView.setText(name);
        }
    }

}
