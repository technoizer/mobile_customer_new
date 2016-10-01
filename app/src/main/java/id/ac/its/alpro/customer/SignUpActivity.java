package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Entity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import id.ac.its.alpro.customer.BriResponse.RegistrasiTBankResult;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.databaseHandler.MySQLiteHelper;

public class SignUpActivity extends AppCompatActivity {

    EditText nama, email, nohp, password, password_c,no_ktp,tempat_lahir,tanggal_lahir,alamat,kota, pekerjaan;
    TextView signin;
    TextInputLayout nama_l, email_l, nohp_l, password_l, password_c_l;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        signup = (Button)findViewById(R.id.signupBtn);
        signin = (TextView)findViewById(R.id.signin);
        nama = (EditText)findViewById(R.id.nama_signup);
        nohp = (EditText)findViewById(R.id.phone_number);
        password_c = (EditText)findViewById(R.id.password_confirm);
        email = (EditText)findViewById(R.id.email_signup);
        password = (EditText)findViewById(R.id.password_signup);
        no_ktp = (EditText)findViewById(R.id.no_ktp);
        tempat_lahir = (EditText)findViewById(R.id.tempat_lahir);
        tanggal_lahir = (EditText)findViewById(R.id.tanggal_lahir);
        alamat = (EditText)findViewById(R.id.alamat);
        kota = (EditText)findViewById(R.id.kota);
        pekerjaan = (EditText)findViewById(R.id.pekerjaan);

        email_l = (TextInputLayout)findViewById(R.id.layout_email_signup);
        nama_l = (TextInputLayout)findViewById(R.id.layout_nama);
        nohp_l = (TextInputLayout)findViewById(R.id.layout_phone_number);
        password_l = (TextInputLayout)findViewById(R.id.layout_password_signup);
        password_c_l = (TextInputLayout)findViewById(R.id.layout_password_confirm);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validateNama() && validatePhone() && validatePassword() && validatePasswordC() && validatePasswordMatch()) {
                    new MyAsyncTask().execute("hehe");
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private int status = 0;
        private Res result;

        public MyAsyncTask(){
            dialog = new ProgressDialog(SignUpActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            RegistrasiTBank();
            return null;
        }

        protected void onPostExecute(Double res) {
            dialog.dismiss();
//            if (status == 200 && result.getStatus().equals("success")){
//                Toast.makeText(getApplicationContext(), "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//            }
//            else
//                Toast.makeText(getApplicationContext(), "Oops.., Terdapat Kesalahan, Coba Lagi!", Toast.LENGTH_SHORT).show();
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
            String url = getResources().getString(R.string.url) + "api/customer/register";
            HttpPost httpPost = new HttpPost(url);

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", email.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("nohp", nohp.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("nama", nama.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("password", password.getText().toString().toString()));

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
                    Log.d("Hehe", result.getStatus());
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void RegistrasiTBank() {

            ArrayList<NameValuePair> postParameters;
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://ridhoperdana.net/servisin/htdocs/public/api/bri/registrasitbank";
            HttpPost httpPost = new HttpPost(url);

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("nohandphone", nohp.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("nama", nama.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("noktp", no_ktp.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("tempatLahir", tempat_lahir.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("tanggalLahir", tanggal_lahir.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("alamat", alamat.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("kota", kota.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("email", email.getText().toString().trim()));
            postParameters.add(new BasicNameValuePair("pekerjaan", pekerjaan.getText().toString().trim()));

            Log.d("URL", url);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
                HttpResponse response = httpclient.execute(httpPost);
                status = response.getStatusLine().getStatusCode();

                Gson gson = new GsonBuilder().create();

                Log.d("RegistrasiTBankResult", EntityUtils.toString(response.getEntity()).toString());
                RegistrasiTBankResult registResult = gson.fromJson(EntityUtils.toString(response.getEntity()), RegistrasiTBankResult.class);

                Log.d("hasilSemua",registResult.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
            }
        }
    }

    public class Res{
        String status;

        public Res(String status, String token) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

    private boolean validateNama(){
        if (nama.getText().toString().trim().isEmpty()) {
            nama_l.setError("Masukkan Nama Anda!");
            return false;
        } else {
            nama_l.setErrorEnabled(false);
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

    private boolean validatePasswordC(){
        if (password_c.getText().toString().trim().isEmpty()) {
            password_c_l.setError("Masukkan Password Konfirmasi Anda!");
            return false;
        } else {
            password_c_l.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone(){
        if (nohp.getText().toString().trim().isEmpty()) {
            nohp_l.setError("Masukkan Password Anda!");
            return false;
        } else {
            nohp_l.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePasswordMatch(){
        if (!password.getText().toString().trim().equals(password_c.getText().toString().trim())) {
            password_c_l.setError("Password tidak sama!");
            return false;
        } else {
            password_c_l.setErrorEnabled(false);
        }
        return true;
    }
}
