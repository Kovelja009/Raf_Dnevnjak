package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.fragments.CalendarFragment;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.User;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.SplashViewModel;

public class MainActivity extends AppCompatActivity {
    private SplashViewModel splashViewModel;

    public static final String PREF_USERNAME = "username";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_EMAIL = "email";

    public static final String DEFAULT_IMAGE_URL = "https://preview.redd.it/h3qww4vmh3191.jpg?width=960&crop=smart&auto=webp&v=enabled&s=9b5ce1a7e50e30a2bc2c2873e895dcdae2626289";

    private TextInputEditText emailInput;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private Button loginBtn;

    private TextView emailMissing;
    private TextView usernameMissing;
    private TextView passwordMissing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;
        });


        check_login();

        setContentView(R.layout.activity_main);
        init();
    }

    private void check_login(){
            CalendarDBHelper dbHelper = CalendarDBHelper.instanceOfDatabase(getApplicationContext());
            if(dbHelper.getUsersFromDB().isEmpty()){
                dbHelper.addUserToDatabase("admin@raf.rs", "admin", "Admin1", DEFAULT_IMAGE_URL);
            }

        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        if (sharedPref.contains(PREF_USERNAME) && sharedPref.contains(PREF_PASSWORD) && sharedPref.contains(PREF_EMAIL)){
            String email = sharedPref.getString(PREF_EMAIL, "");
            String username = sharedPref.getString(PREF_USERNAME, "");
            String password = sharedPref.getString(PREF_PASSWORD, "");

            if(email.isEmpty() || username.isEmpty() || password.isEmpty())
                return;


            User user = dbHelper.getUserFromDB(email, username, password);
            if(user == null)
                return;

            Intent intent = new Intent(this, BottomNavigationActivity.class);
            intent.putExtra(BottomNavigationActivity.EMAIL_STRING, email.toString());
            intent.putExtra(BottomNavigationActivity.PASSWORD_STRING, password.toString());
            intent.putExtra(BottomNavigationActivity.USERNAME_STRING, username.toString());
            finish();
            startActivity(intent);
        }
    }

    private void init() {
        initView();
        initListeners();
    }

    private void initView() {
        emailInput = findViewById(R.id.emailInput1);
        usernameInput = findViewById(R.id.usernameInput1);
        passwordInput = findViewById(R.id.passwordInput1);
        loginBtn = findViewById(R.id.LoginBtn);

        emailMissing = findViewById(R.id.emailMissing);
        usernameMissing = findViewById(R.id.usernameMissing);
        passwordMissing = findViewById(R.id.passwordMissing);
        emailMissing.setVisibility(TextView.INVISIBLE);
        usernameMissing.setVisibility(TextView.INVISIBLE);
        passwordMissing.setVisibility(TextView.INVISIBLE);
    }

    private boolean emailValidation(String email){
        if(email.isEmpty()){
            emailMissing.setVisibility(TextView.VISIBLE);
            return false;
        }
        emailMissing.setVisibility(TextView.INVISIBLE);
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }


    public static boolean has_special_characters(String password){
        String regex = "^[~#^|$%&*!]*$";

        Pattern pattern = Pattern.compile(regex);

        for(char c : password.toCharArray()){
            if(pattern.matcher(String.valueOf(c)).matches()){
                return true;
            }
        }

        return false;
    }

    public static boolean passwordValidation(String password){
        if(password.isEmpty()){
            return false;
        }

        String regex = "^(?=.*[0-9])(?=.*[A-Z]).{5,20}$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(password).matches() && !has_special_characters(password);

    }
    private void initListeners() {
        loginBtn.setOnClickListener(v -> {
            Editable email = emailInput.getText();
            Editable username = usernameInput.getText();
            Editable password = passwordInput.getText();

            if(email == null || email.toString().equals(""))
                emailMissing.setVisibility(TextView.VISIBLE);
            else emailMissing.setVisibility(TextView.INVISIBLE);

            if(username == null || username.toString().equals(""))
                usernameMissing.setVisibility(TextView.VISIBLE);
            else usernameMissing.setVisibility(TextView.INVISIBLE);

            if(password == null || password.toString().equals("")){
                passwordMissing.setVisibility(TextView.VISIBLE);
                return;
            }else passwordMissing.setVisibility(TextView.INVISIBLE);

            if(!emailValidation(email.toString())){
                Toast toast = Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if(!passwordValidation(password.toString())){
                Toast toast = Toast.makeText(getApplicationContext(), "invalid password", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            CalendarDBHelper dbHelper = CalendarDBHelper.instanceOfDatabase(getApplicationContext());
            User user = dbHelper.getUserFromDB(email.toString(), username.toString(), password.toString());
            if(user == null){
                Toast.makeText(getApplicationContext(), "invalid credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, BottomNavigationActivity.class);
            intent.putExtra(BottomNavigationActivity.EMAIL_STRING, email.toString());
            intent.putExtra(BottomNavigationActivity.PASSWORD_STRING, password.toString());
            intent.putExtra(BottomNavigationActivity.USERNAME_STRING, username.toString());
            // Ukoliko bismo ovde pozvali finish() activity bi bio uklonjen sa activity backstack-a
            // pre nego sto bi na njega bio push-ovan SecondActivity

            shared_pref(email.toString(), username.toString(), password.toString());

            finish();
            startActivity(intent);

        });
    }

    private void shared_pref(String email, String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(PREF_EMAIL, email)
                .putString(PREF_USERNAME, username)
                .putString(PREF_PASSWORD, password)
                .apply();

    }
}