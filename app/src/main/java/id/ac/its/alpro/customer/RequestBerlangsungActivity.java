package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.customer.adaptor.RequestBerlangsungListAdaptor;
import id.ac.its.alpro.customer.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Request;

public class RequestBerlangsungActivity extends AppCompatActivity {
    private static List<Request> requestsBerlangsung = new ArrayList<>();
    private static String TOKEN;
    private ListView listView;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_berlangsung);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Auth auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        listView = (ListView) findViewById(R.id.fragmenList);
        empty = (TextView)findViewById(R.id.empty);

        refreshContent();

        ImageButton tmp = (ImageButton) findViewById(R.id.refreshBtn);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            new AsyncTaskLogout(this, RequestBerlangsungActivity.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }

        return super.onOptionsItemSelected(item);
    }

    public void callPenyedia(View view){
        Request tmp = (Request) view.getTag();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tmp.getNohp()));
        startActivity(intent);
    }

    public void seeDetail(View view){
        Request tmp = (Request) view.getTag();
        final Dialog myDialog = new Dialog(this);
        myDialog.setTitle("Detail Servis");
        myDialog.setContentView(R.layout.dialog_detail_request_berlangsung);
        TextView nama_penyedia_jasa = (TextView) myDialog.findViewById(R.id.nama_penyedia_jasa);
        TextView lokasi = (TextView) myDialog.findViewById(R.id.lokasi_servis);
        TextView jenis_servis = (TextView) myDialog.findViewById(R.id.jenis_service);
        TextView jam_request = (TextView)myDialog.findViewById(R.id.jam_request);
        TextView perkiraanHarga = (TextView)myDialog.findViewById(R.id.harga_perkiraan);
        TextView catatanCustomer = (TextView)myDialog.findViewById(R.id.catatan_customer);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(tmp.getHargaperkiraan().longValue());

        nama_penyedia_jasa.setText(tmp.getNamapenyediajasa());
        lokasi.setText(tmp.getLokasi());
        jenis_servis.setText(tmp.getTipejasa());
        jam_request.setText(tmp.getTanggalrequest() + " at " + tmp.getJamservis());
        perkiraanHarga.setText("Estimation Cost : Rp. " + temp + ",-");
        catatanCustomer.setText(tmp.getCatatancustomer());

        myDialog.show();
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(RequestBerlangsungActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            RequestBerlangsungListAdaptor adaptor = new RequestBerlangsungListAdaptor(RequestBerlangsungActivity.this,R.layout.item_request_berlangsung,requestsBerlangsung);
            listView.setAdapter(adaptor);
            listView.setEmptyView(empty);
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
            String url = "http://servisin.au-syd.mybluemix.net/api/customer/request/ongoing/"+TOKEN;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                Request request[] = baru.fromJson(reader, Request[].class);
                for (int i = 0; i < request.length; i++){
                    requestsBerlangsung.add(request[i]);
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshContent() {
        requestsBerlangsung.clear();
        new AsyncTaskList().execute("hehe");
    }

}
