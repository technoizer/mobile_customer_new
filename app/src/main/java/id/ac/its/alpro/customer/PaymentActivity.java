package id.ac.its.alpro.customer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.ac.its.alpro.customer.R;
import id.ac.its.alpro.customer.component.Auth;

public class PaymentActivity extends AppCompatActivity {
    private String TOKEN;
    private Auth auth;
    private String ticket;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        ticket = (String) getIntent().getStringExtra("Ticket");

        TOKEN = auth.getToken();
        String url = "http://128.199.115.34:8080/ecommgateway/payment.html?id="+ticket;
        WebView myWebView = (WebView) findViewById(R.id.web1);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(url);
        Log.d("URL", url);
    }

    @Override
    protected void onStop() {
        super.onStop();


    }


}
