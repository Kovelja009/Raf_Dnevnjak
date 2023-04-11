package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.SplashViewModel;

public class MainActivity extends AppCompatActivity {
    private SplashViewModel splashViewModel;

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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;
        });
        setContentView(R.layout.activity_main);
        init();
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


    private boolean has_special_characters(String password){
        String regex = "^[~#^|$%&*!]*$";

        Pattern pattern = Pattern.compile(regex);

        for(char c : password.toCharArray()){
            if(pattern.matcher(String.valueOf(c)).matches()){
                return true;
            }
        }

        return false;
    }

    private boolean passwordValidation(String password){
        if(password.isEmpty()){
            usernameMissing.setVisibility(TextView.VISIBLE);
            return false;
        }
        usernameMissing.setVisibility(TextView.INVISIBLE);

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

//            String text = email.toString() + " " + username.toString() + " " + password.toString();
//            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//            toast.show();

            Intent intent = new Intent(this, CalendarMenuActivity.class);
            intent.putExtra(CalendarMenuActivity.EMAIL_STRING, email.toString());
            intent.putExtra(CalendarMenuActivity.PASSWORD_STRING, password.toString());
            intent.putExtra(CalendarMenuActivity.USERNAME_STRING, username.toString());
            // Ukoliko bismo ovde pozvali finish() activity bi bio uklonjen sa activity backstack-a
            // pre nego sto bi na njega bio push-ovan SecondActivity
            finish();
            startActivity(intent);


//            if (email.equals("admin") && username.equals("admin") && password.equals("admin")) {
//                Intent intent = new Intent(this, HomeActivity.class);
//                startActivity(intent);
//            }
        });
    }
}