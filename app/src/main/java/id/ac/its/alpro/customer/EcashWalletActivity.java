package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.customer.adaptor.EcashListAdaptor;
import id.ac.its.alpro.customer.adaptor.RequestBelumBayarListAdaptor;
import id.ac.its.alpro.customer.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Mandiri;
import id.ac.its.alpro.customer.component.Request;
import id.ac.its.alpro.customer.component.Transaksi;
import id.ac.its.alpro.customer.databaseHandler.MandiriECashDb;

public class EcashWalletActivity extends AppCompatActivity {
    private String TOKEN;
    private Auth auth;
    private Mandiri tmp;
    EditText phone;
    EditText password;
    TextInputLayout phone_l, password_l;
    TextView balance;
    private ListView listView;
    private TextView empty;
    String nohp, pass, token_m;
    List<Transaksi> transaksis = new ArrayList<>();
    History history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecash_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        balance = (TextView)findViewById(R.id.balance);
        listView = (ListView) findViewById(R.id.fragmenList);
        empty = (TextView)findViewById(R.id.empty);

        MandiriECashDb db = new MandiriECashDb(getApplicationContext());
        tmp = db.get(1);
        if (tmp.getPhone() != null) {
            nohp = tmp.getPhone();
            pass = tmp.getPassword();
            refreshContent();
        }
        else {
            Toast.makeText(this,"Lengkapi data E-Cash Mandiri pada menu setting !",Toast.LENGTH_LONG).show();
        }

        ImageButton tmp = (ImageButton) findViewById(R.id.refreshBtn);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshContent();
            }
        });


        /*Method Footer*/
        ImageView btn;
        LinearLayout order, history, ecash, setting;
        order = (LinearLayout) findViewById(R.id.orderBtn);
        history = (LinearLayout) findViewById(R.id.historyBtn);
        ecash = (LinearLayout) findViewById(R.id.ecashBtn);
        setting = (LinearLayout) findViewById(R.id.settingBtn);
        btn = (ImageView) findViewById(R.id.ecashImg);
        btn.setColorFilter(Color.argb(255, 244, 67, 54));

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NewRequestActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),HistoryRequestActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

//        ecash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),EcashWalletActivity.class);
//                i.putExtra("Auth", auth);
//                startActivity(i);
//            }
//        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RequestBelumBayarActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mandiri, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            new AsyncTaskLogout(this, EcashWalletActivity.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }
        if (id == R.id.setting){
            final Dialog myDialog = new Dialog(this);
            myDialog.setTitle("Mandiri");
            myDialog.setContentView(R.layout.dialog_mandiri_setting);
            phone = (EditText)myDialog.findViewById(R.id.phone_number);
            password = (EditText)myDialog.findViewById(R.id.password);

            phone_l = (TextInputLayout)myDialog.findViewById(R.id.layout_phone_number);
            password_l = (TextInputLayout)myDialog.findViewById(R.id.layout_password);

            Button saveBtn = (Button)myDialog.findViewById(R.id.saveBtn);
            final MandiriECashDb db = new MandiriECashDb(getApplicationContext());
            tmp = db.get(1);
            if (tmp.getPhone() != null){
                phone.setText(tmp.getPhone());
                password.setText(tmp.getPassword());
            }

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validatePhone() && validatePassword()) {
                        Mandiri temp = new Mandiri(phone.getText().toString().trim(), password.getText().toString().trim(), 1);
                        if (tmp.getPhone() != null){
                            db.update(temp);

                        }
                        else{
                            db.insert(temp);
                        }
                        nohp = temp.getPhone();
                        pass = temp.getPassword();
                        myDialog.dismiss();
                    }
                }
            });
            myDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean validatePhone(){
        if (phone.getText().toString().trim().isEmpty()) {
            phone_l.setError("Masukkan Nomor Hp Anda!");
            return false;
        } else {
            phone_l.setErrorEnabled(false);
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

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private String status;

        public AsyncTaskList(){
            dialog = new ProgressDialog(EcashWalletActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            if (status.equals("SUCCESS")){
                EcashListAdaptor adaptor = new EcashListAdaptor(EcashWalletActivity.this,R.layout.item_ecash_wallet, history.getAccountHistoryDetails());
                listView.setAdapter(adaptor);
                listView.setEmptyView(empty);
                balance.setText(history.getAccountBalance());
            }
            else
                Toast.makeText(getApplicationContext(),"Oops.. Terjadi Kesalahan, Verivikasi kembali akun E-Cash Anda",Toast.LENGTH_LONG).show();
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
            String url = "https://api.apim.ibmcloud.com/ex-icha-fmeirisidibmcom-ecash-be/sb/emoney/v1/loginMember?msisdn="+nohp+"&credentials="+pass+"&uid=asd123456";
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Res res= baru.fromJson(reader, Res.class);
                status = res.getStatus();
                token_m = res.getToken();
                Log.d("HASIL", res.toString());

                if (status.equals("VALID")) {
                    httpclient = new DefaultHttpClient();
                    url = "https://api.apim.ibmcloud.com/ex-icha-fmeirisidibmcom-ecash-be/sb/emoney/v1/accountHistory?msisdn=" + nohp + "&onpage=0&pagesize=100&token=" + token_m;
                    httpGet = new HttpGet(url);
                    Log.d("URL", url);

                    response = httpclient.execute(httpGet);
                    reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                    baru = new Gson();
                    history = baru.fromJson(reader, History.class);
                    status = history.getStatus();
                    transaksis = history.getAccountHistoryDetails();
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public class Res{
        String msisdn, status, token;

        public Res(String msisdn, String status, String token) {
            this.msisdn = msisdn;
            this.status = status;
            this.token = token;
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

        public String toString(){
            return getToken() + " " + getStatus();
        }
    }

    public class History {
        String accountBalance, status, creditLimit;
        List<Transaksi> accountHistoryDetails;

        public History() {
        }

        public History(String accountBalance, String status, String creditLimit, List<Transaksi> accountHistoryDetails) {
            this.accountBalance = accountBalance;
            this.status = status;
            this.creditLimit = creditLimit;
            this.accountHistoryDetails = accountHistoryDetails;
        }

        public String getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(String accountBalance) {
            this.accountBalance = accountBalance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(String creditLimit) {
            this.creditLimit = creditLimit;
        }

        public List<Transaksi> getAccountHistoryDetails() {
            return accountHistoryDetails;
        }

        public void setAccountHistoryDetails(List<Transaksi> accountHistoryDetails) {
            this.accountHistoryDetails = accountHistoryDetails;
        }
    }

    public void refreshContent() {
        transaksis.clear();
        new AsyncTaskList().execute("hehe");
    }
}
