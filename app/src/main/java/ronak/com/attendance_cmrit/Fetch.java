package ronak.com.attendance_cmrit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ronak on 18-04-2015.
 */
public class Fetch {

    private static String SHARED_FILE="keys";
    private static String USERNAME="username";
    private static String PASSWORD="password";
    private static String KEY="key";
    private static String ID="id";
    Attendance attendance;
    String username, password;
    Context context;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    public Fetch(Context context) {
        this.context = context;
        setup_dialog();
        editor = context.getSharedPreferences(SHARED_FILE,Context.MODE_PRIVATE).edit();
        preferences = context.getSharedPreferences(SHARED_FILE,Context.MODE_PRIVATE);
        attendance = new Attendance();
    }

    void setup_dialog()
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Connecting to the Server");
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait");
    }

    void set_values(String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
    }

    Boolean is_valid() {

        try {
            String encoded_username = URLEncoder.encode(username, "UTF-8");
            String encoded_password = URLEncoder.encode(password, "UTF-8");
            Log.e("ENCODED", encoded_username + " " + encoded_password);
            String url = "http://203.201.63.40/educ8_git/inc/ajax/process_login.inc.php?username=" + encoded_username + "&password=" + encoded_password;
            new login().execute(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    void download()
    {
        new Download().execute("Exceute");
    }



    public class login extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String key = "PHPSESSID";
                Connection.Response response = Jsoup.connect(params[0]).timeout(20000).method(Connection.Method.POST).execute();
                if(response.cookies().get(key)==null)
                    return false;
                //HashMap<String,String> map = new HashMap<String, String>();
                //map.put(key,id);
                editor.putString(USERNAME, username);
                editor.commit();
                editor.putString(PASSWORD, password);
                editor.commit();
                editor.putString(KEY, key);
                editor.commit();
                editor.putString(ID, response.cookies().get(key));
                editor.commit();
                //Document document = Jsoup.connect("http://203.201.63.40/educ8_git/dashboard_student.php").timeout(5000).cookies(map).get();
                //Log.e("NAme",document.select("span").text());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean)
            {
                Toast.makeText(context,"Username/Password is incorrect", Toast.LENGTH_LONG).show();
                editor.putString(USERNAME, null);
                editor.commit();
                editor.putString(PASSWORD, null);
                editor.commit();
                editor.putString(KEY, null);
                editor.commit();
                editor.putString(ID, null);
                editor.commit();
            }
            else
                new Download().execute("Execute");
            progressDialog.dismiss();
        }
    }

    public class Download extends AsyncTask<String,Integer,Boolean>
    {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String key= preferences.getString(KEY,null);
            String id = preferences.getString(ID,null);
            if(key==null || id==null)
                return false;
            HashMap<String,String> map = new HashMap<>();
            map.put(key,id);
            try {
                Document document = Jsoup.connect("http://203.201.63.40/educ8_git/dashboard_student.php").timeout(20000).cookies(map).get();
                if(document.select("span").get(0).text().contains("Enter"))
                    return false;
                attendance.setName(document.select("span").get(0).text());
                Elements elements = document.getElementsByTag("tr");
                Element element;
                LinkedList<String> subjects = new LinkedList<>();
                LinkedList<Float> total = new LinkedList<>();
                LinkedList<Float> present = new LinkedList<>();
                LinkedList<Float> percentage = new LinkedList<>();
                float t,pr,pe;
                t= 0;
                pr =0;
                pe = 0;
                float temp1;
                for(int i=1; i<elements.size(); ++i)
                {
                    element = elements.get(i);
                    subjects.add(element.select("td").get(0).text());
                    temp1 = Float.parseFloat(element.select("td").get(1).text().trim());
                    t += temp1;
                    total.add(temp1);
                    temp1 =  Float.parseFloat(element.select("td").get(2).text().trim());
                    pr += temp1;
                    present.add(temp1);
                    temp1 = Float.parseFloat(element.select("td").get(3).text().trim());
                    pe += temp1;
                    percentage.add(temp1);
                }
                subjects.add("Overall");
                total.add(t);
                present.add(pr);
                percentage.add((pe/(elements.size()-1)));
                attendance.setPercentage(percentage);
                attendance.setTotal(total);
                attendance.setPresent(present);
                attendance.setSubjects(subjects);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                //Toast.makeText(context,"Logged In"+ attendance.getPercentage().size(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, Dashboard.class);
                intent.putExtra("attendance", attendance);
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.finish();
            }
            else
            {
                try {
                    String encoded_password = URLEncoder.encode(preferences.getString(USERNAME, "ramukaka"), "UTF-8");
                    String encoded_username = URLEncoder.encode(preferences.getString(PASSWORD, "cmrit"), "UTF-8");
                    String url = "http://203.201.63.40/educ8_git/inc/ajax/process_login.inc.php?username=" + encoded_username + "&password=" + encoded_password;
                    new login().execute(url);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            progressDialog.dismiss();
        }
    }


}