package ronak.com.attendance_cmrit;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;


public class Login extends ActionBarActivity {

    private static String SHARED_FILE="keys";
    private static String USERNAME="username";
    private static String PASSWORD="password";
    private static String KEY="key";
    private static String ID="id";

    EditText username, password;
    Button submit_b;
    Fetch fetch;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Attendance");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        password = (EditText) findViewById(R.id.password);
        submit_b = (Button) findViewById(R.id.submit);
        fetch = new Fetch(this);
        preferences = getSharedPreferences(SHARED_FILE,MODE_PRIVATE);
        if(preferences.getString(ID,null)!=null)
        {
            fetch.download();
        }

    }

    public void login(View view)
    {
        String regex = "(\\d{10})|([1-4][Cc][Rr]\\d{2}\\w{2}\\d{3})";
        if(username.getText().toString().trim().matches(regex))
        {
            String username_s = username.getText().toString();
            String password_s = password.getText().toString();
            fetch.set_values(username_s,password_s);
            fetch.is_valid();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid Username", Toast.LENGTH_LONG).show();
        }

    }





}
