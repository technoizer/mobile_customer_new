package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.util.Locale;

import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Request;

public class UnpaidActivity extends AppCompatActivity {

    private String TOKEN;
    private Auth auth;
    private Request item;
    TextView namapenyediajasa, tipejasa, jamservis, lokasiservis, perkiraanharga, hargatotal, deskripsi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        item = (Request) getIntent().getSerializableExtra("Request");

        namapenyediajasa = (TextView)findViewById(R.id.namapenyediajasa);
        tipejasa = (TextView)findViewById(R.id.jenisservis);
        jamservis = (TextView)findViewById(R.id.jamservis);
        lokasiservis = (TextView)findViewById(R.id.lokasiservis);
        hargatotal = (TextView)findViewById(R.id.hargatotal);
        perkiraanharga = (TextView)findViewById(R.id.perkiraanharga);
        deskripsi = (TextView)findViewById(R.id.catatanpenyediajasa);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(item.getHargatotal().longValue());
        String temp2 = formatter.format(item.getHargaperkiraan().longValue());

        namapenyediajasa.setText(item.getNamapenyediajasa());
        tipejasa.setText(item.getTipejasa());
        jamservis.setText(item.getTanggalmulai() +" at " + item.getJamservis());
        lokasiservis.setText("Lokasi : " + item.getLokasi());
        hargatotal.setText("Harga Total \t\t\t\t: Rp. " + temp + ",-");
        perkiraanharga.setText("Harga Perkiraan \t: Rp. " + temp2 + ",-");
        deskripsi.setText(item.getCatatanpenyediajasa());
    }

    private class AsyncTaskOTP extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private String status;
        private String ticket;

        public AsyncTaskOTP(){
            dialog = new ProgressDialog(UnpaidActivity.this);
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
            String url = "http://128.199.115.34:6557/ipg/ticket?amount="+item.getHargatotal()+"&tracenumber=123456&returnurl=http://servisin.au-syd.mybluemix.net/api/customer/request/bayar/"+item.getTransaksi_id()+"/"+TOKEN;
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

    public void payRequest(View view) {
        new AsyncTaskOTP().execute("hehe");
    }

}