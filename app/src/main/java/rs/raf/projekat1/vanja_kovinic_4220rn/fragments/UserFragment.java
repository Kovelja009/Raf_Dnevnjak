package rs.raf.projekat1.vanja_kovinic_4220rn.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.BottomNavigationActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.MainActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;

public class UserFragment extends Fragment {

    private ImageView userImage;
    private TextView username;
    private TextView email;
    /////////////////////////////
//    First
    private Button changePasswrordBtn;
    private Button logoutBtn;
    ///////////////////////////
//    Second
    private TextInputLayout passwordLayout;
    private TextView passwordError;
    private TextInputLayout passwordConfirmLayout;
    private TextView passwordConfirmError;

    private Button changePasswordBtn;
    private Button cancelBtn;

    private CalendarDBHelper dbHelper;



    /////////////////////
    public UserFragment() {
        super(R.layout.fragment_user);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatabase();
        init();
    }

    private void initDatabase(){
        dbHelper = CalendarDBHelper.instanceOfDatabase(null);
    }

    private void init() {
        infoView();
        initViews();
        initListeners();
        showFirst();
    }


    private void initViews() {
        changePasswrordBtn = getActivity().findViewById(R.id.change_password_btn);
        logoutBtn = getActivity().findViewById(R.id.logout_btn);
        //////////////////////////
        passwordLayout = getActivity().findViewById(R.id.change_password_input);
        passwordError = getActivity().findViewById(R.id.change_password_missing);
        passwordError.setVisibility(View.INVISIBLE);

        passwordConfirmLayout = getActivity().findViewById(R.id.change_password_repeat_input);
        passwordConfirmError = getActivity().findViewById(R.id.change_password_repeat_missing);
        passwordConfirmError.setVisibility(View.INVISIBLE);

        changePasswordBtn = getActivity().findViewById(R.id.confirm_change_btn);
        cancelBtn = getActivity().findViewById(R.id.cancel_change_btn);
    }
    public void showFirst(){
        changePasswrordBtn.setVisibility(View.VISIBLE);
        logoutBtn.setVisibility(View.VISIBLE);
        //////////////////////////
        passwordLayout.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);

        passwordConfirmLayout.setVisibility(View.GONE);
        passwordConfirmError.setVisibility(View.GONE);

        changePasswordBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
    }

    public void showSecond(){
        changePasswrordBtn.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);
        //////////////////////////
        passwordLayout.setVisibility(View.VISIBLE);
        passwordLayout.setPasswordVisibilityToggleEnabled(true);
        passwordError.setVisibility(View.INVISIBLE);

        passwordConfirmLayout.setVisibility(View.VISIBLE);
        passwordLayout.setPasswordVisibilityToggleEnabled(true);
        passwordConfirmError.setVisibility(View.INVISIBLE);

        changePasswordBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
    }

    private void initListeners() {
        changePasswrordBtn.setOnClickListener(v -> {
            showSecond();
        });

        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            resetSharedPref();
            getActivity().finish();
            getActivity().startActivity(intent);

            Toast.makeText(getActivity().getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
        });

        changePasswordBtn.setOnClickListener(v -> {
            String password = passwordLayout.getEditText().getText().toString();
            String passwordConfirm = passwordConfirmLayout.getEditText().getText().toString();
            if(password.isEmpty())
                passwordError.setVisibility(View.VISIBLE);
            else
                passwordError.setVisibility(View.INVISIBLE);

            if(passwordConfirm.isEmpty())
                passwordConfirmError.setVisibility(View.VISIBLE);
            else
                passwordConfirmError.setVisibility(View.INVISIBLE);

            if(!password.isEmpty() && !passwordConfirm.isEmpty()) {
                if (!password.equals(passwordConfirm)){
                    Toast.makeText(getActivity().getApplicationContext(), "passwords doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(MainActivity.passwordValidation(password)){
                    passwordLayout.getEditText().setText("");
                    passwordConfirmLayout.getEditText().setText("");
                    setSharedPref(password);
                    showFirst();
                    saveToDatabase(password);
                    Toast.makeText(getActivity().getApplicationContext(), "password changed", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity().getApplicationContext(), "password is not valid", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(v -> {
            passwordLayout.getEditText().setText("");
            passwordConfirmLayout.getEditText().setText("");
            showFirst();
        });
    }

    private void saveToDatabase(String password){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        String email = sharedPreferences.getString(MainActivity.PREF_EMAIL, "");
        String url = sharedPreferences.getString(MainActivity.DEFAULT_IMAGE_URL, "");
        dbHelper.updateUserInDB(email, username, password, url);
    }

    private void setSharedPref(String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(MainActivity.PREF_PASSWORD, password)
                .apply();
    }

    private void resetSharedPref(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(MainActivity.PREF_EMAIL, "")
                .putString(MainActivity.PREF_USERNAME, "")
                .putString(MainActivity.PREF_PASSWORD, "")
                .apply();
    }

//////////////////////////////////////////////////////////
// Static part
    private void infoView(){
        userImage = getActivity().findViewById(R.id.userPictureIV);
        Glide
                .with(getContext())
                .load(MainActivity.DEFAULT_IMAGE_URL)
                .circleCrop()
                .into(userImage);

        username = getActivity().findViewById(R.id.usernameTV);
        email = getActivity().findViewById(R.id.emailTV);
        loadDataSharedPref();
    }

    private void loadDataSharedPref() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String usernameString = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        String emailString = sharedPreferences.getString(MainActivity.PREF_EMAIL, "");
        username.setText(usernameString);
        email.setText(emailString);
    }
}
