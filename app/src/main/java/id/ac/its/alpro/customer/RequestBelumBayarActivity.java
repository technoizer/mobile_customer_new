package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.customer.adaptor.EcashListAdaptor;
import id.ac.its.alpro.customer.adaptor.RequestBelumBayarListAdaptor;
import id.ac.its.alpro.customer.adaptor.RequestBerlangsungListAdaptor;
import id.ac.its.alpro.customer.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Mandiri;
import id.ac.its.alpro.customer.component.Request;
import id.ac.its.alpro.customer.databaseHandler.MandiriECashDb;
import id.ac.its.alpro.customer.databaseHandler.PaymentActivity;

public class RequestBelumBayarActivity extends AppCompatActivity {
    private static List<Request> requestBelumBayar = new ArrayList<>();
    private static String TOKEN;
    private ListView listView;
    private TextView empty;
    String nohp, pass, token_m;
    Request obj;
    Spinner spinner;
    EditText review;
    Dialog myDialog;
    Auth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_belum_bayar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = (Auth) getIntent().getSerializableExtra("Auth");
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
            new AsyncTaskLogout(this, RequestBelumBayarActivity.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(RequestBelumBayarActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            RequestBelumBayarListAdaptor adaptor = new RequestBelumBayarListAdaptor(RequestBelumBayarActivity.this,R.layout.item_request_belum_bayar, requestBelumBayar);
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
            String url = "http://servisin.au-syd.mybluemix.net/api/customer/request/unpaid/"+TOKEN;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                Request request[] = baru.fromJson(reader, Request[].class);
                for (int i = 0; i < request.length; i++){
                    requestBelumBayar.add(request[i]);
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }


    public void refreshContent() {
        requestBelumBayar.clear();
        new AsyncTaskList().execute("hehe");
    }

    public void seeDetail(View view){
        Request tmp = (Request) view.getTag();
        final Dialog myDialog = new Dialog(this);
        myDialog.setTitle("Detail Servis");
        myDialog.setContentView(R.layout.dialog_detail_belum_bayar);
        TextView nama_penyedia_jasa = (TextView) myDialog.findViewById(R.id.nama_penyedia);
        TextView lokasi = (TextView) myDialog.findViewById(R.id.lokasi_servis);
        TextView jenis_servis = (TextView) myDialog.findViewById(R.id.jenis_service);
        TextView jam_request = (TextView)myDialog.findViewById(R.id.jam_request);
        TextView jam_selesai = (TextView)myDialog.findViewById(R.id.jam_selesai);
        TextView totalHarga = (TextView)myDialog.findViewById(R.id.harga);
        TextView catatanCustomer = (TextView)myDialog.findViewById(R.id.catatan_servis);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(tmp.getHargatotal().longValue());

        nama_penyedia_jasa.setText(tmp.getNamapenyediajasa());
        lokasi.setText(tmp.getLokasi());
        jenis_servis.setText(tmp.getTipejasa());
        jam_request.setText("Tanggal Request : " + tmp.getTanggalrequest());
        jam_selesai.setText("Tanggal Selesai : " + tmp.getTanggalselesai());
        totalHarga.setText("Total Cost : Rp. " + temp + ",-");
        catatanCustomer.setText(tmp.getCatatanpenyediajasa());

        myDialog.show();
    }

    public void callPenyedia(View view){
        Request tmp = (Request) view.getTag();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tmp.getNohp()));
        startActivity(intent);
    }

    public void payRequest(View view) {
        obj = (Request) view.getTag();

        Request tmp = (Request) view.getTag();
        myDialog = new Dialog(this);
        myDialog.setTitle("Review Servis");
        myDialog.setContentView(R.layout.dialog_isi_review);

        review = (EditText)myDialog.findViewById(R.id.review);
        final Button ambil = (Button) myDialog.findViewById(R.id.dialog_ambil);
        Button batal = (Button) myDialog.findViewById(R.id.dialog_batal);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MandiriECashDb db = new MandiriECashDb(getApplicationContext());
//                Mandiri temp = db.get(1);
//                if (temp.getPhone() != null) {
//                    nohp = temp.getPhone();
//                    pass = temp.getPassword();
//                    new AsyncTaskBayar().execute("hehe");
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Oops.. Terjadi Kesalahan, Lengkapi data E-Cash Mandiri pada menu setting halaman E-cash !", Toast.LENGTH_LONG).show();
//                }

                new AsyncTaskOTP().execute("hehe");
                new AsyncTaskBayar().execute("hehe");

            }
        });

        myDialog.show();



    }

    private class AsyncTaskBayar extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private String status;

        public AsyncTaskBayar(){
            dialog = new ProgressDialog(RequestBelumBayarActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
//            if (status.equals("LOGIN_FAILED"))
//                Toast.makeText(getApplicationContext(),"Oops.. Terjadi Kesalahan, Verivikasi kembali akun E-Cash Anda",Toast.LENGTH_LONG).show();
//            else if (status.equals("PROCESSED")) {
//                Toast.makeText(getApplicationContext(), "Pembayaran Berhasil Dilakukan.", Toast.LENGTH_LONG).show();
//            }
//            else
//                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
            dialog.dismiss();
            myDialog.dismiss();
            refreshContent();
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
            String url = "https://api.apim.ibmcloud.com/ex-icha-fmeirisidibmcom-ecash-be/sb/emoney/v1/loginMember?msisdn="+nohp+"&credentials="+pass+"&uid=asd123456";
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                //HttpResponse response = httpclient.execute(httpGet);
                //Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                //Gson baru = new Gson();
                //Res res= baru.fromJson(reader, Res.class);
                //status = res.getStatus();
                //token_m = res.getToken();
                //Log.d("HASIL", res.toString());
                //if (status.equals("VALID")) {
//                    httpclient = new DefaultHttpClient();
//                    url = "https://api.apim.ibmcloud.com/ex-icha-fmeirisidibmcom-ecash-be/sb/emoney/v1/transferMember?amount=" + obj.getHargatotal() + "&to=083830475754&token=" + token_m + "&description=" + URLEncoder.encode(obj.getTipejasa(), "UTF-8") + "&credentials=123456&from=" + nohp;
//                    httpGet = new HttpGet(url);
//                    Log.d("URL", url);
//                    response = httpclient.execute(httpGet);
//                    reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
//                    baru = new Gson();
//                    res = baru.fromJson(reader, Res.class);
//                    status = res.getStatus();
//                    Log.d("HASIL", res.toString());

                    httpclient = new DefaultHttpClient();
                    url = "http://servisin.au-syd.mybluemix.net/api/customer/request/bayar/"+obj.getTransaksi_id();
                    httpGet = new HttpGet(url);
                    HttpResponse response = httpclient.execute(httpGet);

                //}
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    private class AsyncTaskOTP extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private String status;
        private String ticket;

        public AsyncTaskOTP(){
            dialog = new ProgressDialog(RequestBelumBayarActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {

            if(status.equals("PROCESSED")){
                Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
                i.putExtra("Ticket",ticket);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
            dialog.dismiss();
            myDialog.dismiss();
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
            String url = "http://128.199.115.34:6557/ipg/ticket?amount="+obj.getHargatotal()+"&tracenumber=123456&returnurl=http://google.com";
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Res res= baru.fromJson(reader, Res.class);
                status = res.getStatus();
                ticket = res.getTicketID();
                Log.d("STATUS", ticket);

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public class Res{
        String msisdn, status, token, ticketID;

        public Res(String msisdn, String status, String token, String ticketID) {
            this.msisdn = msisdn;
            this.status = status;
            this.token = token;
            this.ticketID = ticketID;
        }

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
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

        public String getTicketID() {
            return ticketID;
        }

        public void setTicketID(String ticketID) {
            this.ticketID = ticketID;
        }

        public String toString(){
            return getToken() + " " + getStatus();
        }
    }
}
