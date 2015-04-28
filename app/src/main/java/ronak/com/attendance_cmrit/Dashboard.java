package ronak.com.attendance_cmrit;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by ronak on 18-04-2015.
 */
public class Dashboard extends ActionBarActivity {
    private static String SHARED_FILE="keys";
    private static String USERNAME="username";
    private static String PASSWORD="password";
    private static String KEY="key";
    private static String ID="id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Attendance attendance = (Attendance) getIntent().getSerializableExtra("attendance");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(attendance.getName());
        int size=attendance.getPercentage().size();
        float per=0;
        if(size!=0)
            per = attendance.getPercentage().get(size-1);
        int id;
        if(per>=85)
            id = R.color.green;
        else if (per>=65)
            id = R.color.yellow;
        else
            id = R.color.red;
        actionBar.setBackgroundDrawable(getResources().getDrawable(id));
        ListView listView = (ListView) findViewById(R.id.list_attendance);
        Adapter adapter = new Adapter(this,0, attendance.getSubjects(), attendance.getTotal(), attendance.getPresent(), attendance.getPercentage());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.logout)
        {
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_FILE,MODE_PRIVATE).edit();
            editor.putString(ID,null);
            editor.apply();
            startActivity(new Intent(this, Login.class));
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
            finish();
        }
        return true;
    }
}

