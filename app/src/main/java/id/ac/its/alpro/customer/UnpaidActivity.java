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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Provider;
import id.ac.its.alpro.customer.component.Request;

public class UnpaidActivity extends AppCompatActivity {
    private String ReviewVal;
    private Float RatingVal;
    private Dialog myDialog;
    EditText Review;
    RatingBar Rating;
    private String TOKEN, Mode;
    private Auth auth;
    private Request item;
    LinearLayout pay;
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
        Mode = getIntent().getStringExtra("Mode");
        namapenyediajasa = (TextView)findViewById(R.id.namapenyediajasa);
        tipejasa = (TextView)findViewById(R.id.jenisservis);
        jamservis = (TextView)findViewById(R.id.jamservis);
        lokasiservis = (TextView)findViewById(R.id.lokasiservis);
        hargatotal = (TextView)findViewById(R.id.hargatotal);
        perkiraanharga = (TextView)findViewById(R.id.perkiraanharga);
        deskripsi = (TextView)findViewById(R.id.catatanpenyediajasa);
        pay = (LinearLayout)findViewById(R.id.wrapperPay);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);

        String temp ="";
        String temp2="";
        if(!Mode.equals("Sedang Dikerjakan")){
            temp = formatter.format(item.getHargatotal().longValue());
        }

        temp2 = formatter.format(item.getHargaperkiraan().longValue());
        namapenyediajasa.setText(item.getNamapenyediajasa());
        tipejasa.setText(item.getTipejasa());
        jamservis.setText(item.getTanggalmulai() +" at " + item.getJamservis());
        lokasiservis.setText("Lokasi : " + item.getLokasi());
        perkiraanharga.setText("Harga Perkiraan \t: Rp. " + temp2 + ",-");
        hargatotal.setText("Harga Total \t\t\t\t: Rp. " + temp + ",-");
        deskripsi.setText(item.getCatatanpenyediajasa());

        if(Mode.equals("Sedang Dikerjakan")){
            pay.setVisibility(View.GONE);
            hargatotal.setText("Harga Total \t\t\t\t: - ");
            deskripsi.setText("-");
        }
    }

    public void callPenyedia(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getNohp()));
        startActivity(intent);
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
            String url = "http://128.199.115.34:6557/ipg/ticket?amount="+item.getHargatotal()+"&tracenumber=123456"+item.getTransaksi_id()+"&returnurl=http://servisin.au-syd.mybluemix.net/api/customer/request/bayar/"+item.getTransaksi_id()+"/"+TOKEN+"/"+ReviewVal+"/"+RatingVal;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Res res= baru.fromJson(reader, Res.class);
                status = res.getStatus();
                ticket = res.getTicketID();
//                Log.d("STATUS", ticket);

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

        Request tmp = (Request) view.getTag();
        myDialog = new Dialog(this);
        myDialog.setTitle("Review Penyedia Jasa");
        myDialog.setContentView(R.layout.dialog_review);
        myDialog.setCancelable(false);
        Review = (EditText) myDialog.findViewById(R.id.review);
        Rating = (RatingBar) myDialog.findViewById(R.id.ratingBar);
        Button cancel = (Button) myDialog.findViewById(R.id.cancel);
        Button pay = (Button) myDialog.findViewById(R.id.pay);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReviewVal = URLEncoder.encode(Review.getText().toString().trim(), "UTF-8");
                    RatingVal = Rating.getRating();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new AsyncTaskOTP().execute("hehe");

            }
        });

        myDialog.show();
    }
}
