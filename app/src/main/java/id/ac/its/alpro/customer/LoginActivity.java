package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.databaseHandler.MySQLiteHelper;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText email;
    TextView signup;
    EditText password;
    TextInputLayout email_l, password_l;
    MySQLiteHelper db;
    int REQUEST_EXIT = 0, REQUEST_OK = 1, REQUEST_NONE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = new MySQLiteHelper(getApplicationContext());
        signup = (TextView)findViewById(R.id.signupBtn);
        login = (Button)findViewById(R.id.login);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        email_l = (TextInputLayout)findViewById(R.id.layout_email);
        password_l = (TextInputLayout)findViewById(R.id.layout_password);

        Auth auth = db.get(1);
        if(auth.getId() != null && auth.getToken() != null && auth.getStatus() != null && auth.getStatus().equals("success")){
            Intent i = new Intent(this,NewRequestActivity.class);
            i.putExtra("Auth", auth);
            startActivity(i);
        }
        db.close();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validatePassword()) {
                    new MyAsyncTask().execute("hehe");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private int status = 0;
        private Res result;

        public MyAsyncTask(){
            dialog = new ProgressDialog(LoginActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double res) {
            dialog.dismiss();
            if (status == 200 && result.getStatus().equals("success")){
                db = new MySQLiteHelper(getApplicationContext());
                Auth tmp = db.get(1);
                Auth auth = new Auth(1,email.getText().toString().trim(),password.getText().toString().trim(), result.getToken(),result.status);
                if (tmp.getId() != null && tmp.getStatus().equals("expired")){
                    db.update(auth);
                }
                else{
                    db.insert(auth);
                }

                Intent i = new Intent(getApplicationContext(),NewRequestActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
                db.close();
            }
            else
                Toast.makeText(getApplicationContext(), "Oops.., Something went wrong, Try Again Later :)", Toast.LENGTH_SHORT).show();
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait a Moment...");
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void postData() {

            ArrayList<NameValuePair> postParameters;
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://servisin.au-syd.mybluemix.net/api/admin";
            HttpPost httpPost = new HttpPost(url);

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", email.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("password", password.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("sebagai", "2"));

            Log.d("URL", url);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
                HttpResponse response = httpclient.execute(httpPost);
                status = response.getStatusLine().getStatusCode();
                if (status == 200){
                    Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                    //Log.d("TES", getStringFromInputStream(reader));
                    Gson baru = new Gson();

                    result = baru.fromJson(reader, Res.class);
                    Log.d("Hehe", result.getStatus() + " " + result.getToken());
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public class Res{
        String status, token;

        public Res(String status, String token) {
            this.status = status;
            this.token = token;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private boolean validateEmail(){
        if (email.getText().toString().trim().isEmpty()) {
            email_l.setError("Masukkan Email Anda!");
            return false;
        } else {
            email_l.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword(){
        if (password.getText().toString().trim().isEmpty()) {
            password_l.setError("Masukkan Password Anda!");
            return false;
        } else {
            password_l.setErrorEnabled(false);
        }
        return true;
    }
}
