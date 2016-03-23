package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import id.ac.its.alpro.customer.adaptor.PickProviderAdaptor;
import id.ac.its.alpro.customer.adaptor.RequestBelumBayarListAdaptor;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Provider;
import id.ac.its.alpro.customer.component.Request;

public class PickProviderActivity extends AppCompatActivity {

    private String TOKEN;
    private Auth auth;
    private ListView listView;
    private TextView empty;
    private Request item;
    private ArrayList<Provider> providers = new ArrayList<>();
    private String id_penyedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_provider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        item = (Request) getIntent().getSerializableExtra("Request");

        TextView jenis_servis = (TextView)findViewById(R.id.jenis_servis);
        TextView tanggal_servis = (TextView)findViewById(R.id.tanggal_servis);
        TextView lokasi_servis = (TextView)findViewById(R.id.lokasi_servis);

        jenis_servis.setText(item.getTipejasa());
        tanggal_servis.setText(item.getTanggalrequest());
        lokasi_servis.setText("Lokasi : " + item.getLokasi());

        listView = (ListView) findViewById(R.id.listView);
        refreshContent();

    }

    public void seeDetailProvider(View view) {
        Provider provider = (Provider) view.getTag();
        Intent i = new Intent(getApplicationContext(), ProviderProfileActivity.class);
        i.putExtra("ID_PENYEDIA",provider.getPenyediajasa_id());
        i.putExtra("Auth", auth);
        startActivity(i);
    }

    public void callProvider(View view) {
        Provider tmp = (Provider) view.getTag();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tmp.getNohp()));
        startActivity(intent);
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(PickProviderActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            PickProviderAdaptor adaptor = new PickProviderAdaptor(PickProviderActivity.this,R.layout.item_pick_provider, providers);
            listView.setAdapter(adaptor);
            dialog.dismiss();
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
            String url;
            if (item.getTipetransaksi_id() == 1){
                url = "http://servisin.au-syd.mybluemix.net/api/customer/request/direct/lihat/"+item.getTransaksi_id();
            }
            else{
                url = "http://servisin.au-syd.mybluemix.net/api/customer/request/broadcast/lihat/"+item.getTransaksi_id();
            }

            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                Respon respon = baru.fromJson(reader, Respon.class);
                providers = respon.getProviders();
                Log.d("HAHAHA", providers.toString());

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }


    public void refreshContent() {
        providers.clear();
        new AsyncTaskList().execute("hehe");
    }

    public class Respon{
        ArrayList<Provider> providers = new ArrayList<>();
        int transaksi_id;

        public Respon(ArrayList<Provider> providers, int transaksi_id) {
            this.providers = providers;
            this.transaksi_id = transaksi_id;
        }

        public ArrayList<Provider> getProviders() {
            return providers;
        }

        public void setProviders(ArrayList<Provider> providers) {
            this.providers = providers;
        }

        public int getTransaksi_id() {
            return transaksi_id;
        }

        public void setTransaksi_id(int transaksi_id) {
            this.transaksi_id = transaksi_id;
        }

        @Override
        public String toString() {
            return providers.toString();
        }
    }

    private class AsyncTaskPick extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskPick(){
            dialog = new ProgressDialog(PickProviderActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            Toast.makeText(getApplicationContext(),"Penyedia jasa berhasil dipilh.",Toast.LENGTH_LONG).show();
            dialog.dismiss();
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
            HttpClient httpclient = new DefaultHttpClient();
            String url;
            if (item.getTipetransaksi_id() == 1){
                url = "http://servisin.au-syd.mybluemix.net/api/customer/request/direct/pilih/"+item.getTransaksi_id()+"/"+id_penyedia+"/"+TOKEN;
            }
            else{
                url = "http://servisin.au-syd.mybluemix.net/api/customer/request/broadcast/pilih/"+item.getTransaksi_id()+"/"+id_penyedia+"/"+TOKEN;
            }

            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
//                Gson baru = new Gson();
//                Respon respon = baru.fromJson(reader, Respon.class);
//                providers = respon.getProviders();
//                Log.d("HAHAHA", providers.toString());

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }
    public void pickProvider(View view) {
        Provider provider = (Provider) view.getTag();
        id_penyedia = provider.getPenyediajasa_id();
        new AsyncTaskPick().execute("hehe");
    }

}
