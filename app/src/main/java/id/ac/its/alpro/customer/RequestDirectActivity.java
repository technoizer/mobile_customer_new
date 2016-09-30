package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import id.ac.its.alpro.customer.component.Auth;

public class RequestDirectActivity extends AppCompatActivity {
    ArrayList<String> tipeJasa = new ArrayList<>();
    ArrayList<String> keyTipe = new ArrayList<>();

    ArrayList<String> penyediaJasa = new ArrayList<>();
    ArrayList<String> keyPenyedia = new ArrayList<>();

    private String TOKEN;
    private Auth auth;
    Spinner serviceType, providerName;
    EditText catatanKerusakan, Lokasi;
    String catatan, lokasi, tipe, penyedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_direct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();
        serviceType = (Spinner) findViewById(R.id.serviceType);
        providerName = (Spinner) findViewById(R.id.providerName);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        catatanKerusakan = (EditText) findViewById(R.id.catatanKerusakan);
        Lokasi = (EditText) findViewById(R.id.lokasi);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catatan = catatanKerusakan.getText().toString().trim();
                lokasi = Lokasi.getText().toString().trim();
                tipe = keyTipe.get(serviceType.getSelectedItemPosition());
                penyedia = keyPenyedia.get(providerName.getSelectedItemPosition());
                new AsyncTaskPost().execute("hehe");
            }
        });

        serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                penyediaJasa.clear();
                keyPenyedia.clear();
                tipe = keyTipe.get(serviceType.getSelectedItemPosition());
                refreshPenyedia();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*Method Footer*/
        ImageView btn;
        LinearLayout order, history, ecash, setting;
        order = (LinearLayout) findViewById(R.id.orderBtn);
        history = (LinearLayout) findViewById(R.id.historyBtn);
        ecash = (LinearLayout) findViewById(R.id.ecashBtn);
        setting = (LinearLayout) findViewById(R.id.settingBtn);
        btn = (ImageView) findViewById(R.id.orderImg);
        btn.setColorFilter(Color.argb(255, 244, 67, 54));

//        order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),NewRequestActivity.class);
//                i.putExtra("Auth", auth);
//                startActivity(i);
//            }
//        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),History.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        ecash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),EcashWalletActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SettingActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });


        refreshContent();
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(RequestDirectActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, tipeJasa);
            serviceType.setAdapter(adapter);
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait a Moment...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void postData() {
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://ridhoperdana.net/servisin/htdocs/public/api/customer/request/listjenis/0";
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Restipe[] request = baru.fromJson(reader, Restipe[].class);
//                Log.d("Hehe", request.toString());
                for (int i = 0; i < request.length; i++){
                    tipeJasa.add(request[i].getNama());
                    keyTipe.add(request[i].getTipejasa_id());
                }
//                Log.d("finish",finishedReq.toString());
//                Log.d("ongoing", onGoingReq.toString());

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshContent() {
        tipeJasa.clear();
        keyTipe.clear();
        new AsyncTaskList().execute();
    }

    public class Restipe{
        String tipejasa_id, nama, id;

        public Restipe(String tipejasa_id, String nama, String id) {
            this.tipejasa_id = tipejasa_id;
            this.nama = nama;
            this.id = id;
        }

        public String getTipejasa_id() {
            return tipejasa_id;
        }

        public void setTipejasa_id(String tipejasa_id) {
            this.tipejasa_id = tipejasa_id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private class AsyncTaskPost extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        int status;
        public AsyncTaskPost(){
            dialog = new ProgressDialog(RequestDirectActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            if(status == 200){
                Toast.makeText(getApplicationContext(), "Request Berhasil Dibuat", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(),"Request Gagal Dibuat!!",Toast.LENGTH_SHORT).show();

            finish();
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait a Moment...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void postData() {
            ArrayList<NameValuePair> postParameters;
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://ridhoperdana.net/servisin/htdocs/public/api/customer/request/direct/"+TOKEN;
            HttpPost httpPost = new HttpPost(url);
            Log.d("URL",url);
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("catatancustomer", catatan));
            postParameters.add(new BasicNameValuePair("tipejasa_id", tipe));
            postParameters.add(new BasicNameValuePair("penyediajasa_id", penyedia));
            postParameters.add(new BasicNameValuePair("lokasi", lokasi));
            postParameters.add(new BasicNameValuePair("lat", 0+""));
            postParameters.add(new BasicNameValuePair("lng", 0+""));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
                HttpResponse response = httpclient.execute(httpPost);
                status = response.getStatusLine().getStatusCode();

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    private class AsyncTaskPenyedia extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskPenyedia(){
            dialog = new ProgressDialog(RequestDirectActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, penyediaJasa);
            providerName.setAdapter(adapter);
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait a Moment...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void postData() {
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://ridhoperdana.net/servisin/htdocs/public/api/customer/request/jenisservis/penyedia/"+tipe;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Restipe[] request = baru.fromJson(reader, Restipe[].class);
//                Log.d("Hehe", request.toString());
                for (int i = 0; i < request.length; i++){
                    penyediaJasa.add(request[i].getNama());
                    keyPenyedia.add(request[i].getId());
                }
//                Log.d("finish",finishedReq.toString());
//                Log.d("ongoing", onGoingReq.toString());

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshPenyedia(){
        new AsyncTaskPenyedia().execute("hehe");
    }
}
