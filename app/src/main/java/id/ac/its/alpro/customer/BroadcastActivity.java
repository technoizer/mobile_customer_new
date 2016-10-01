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

public class BroadcastActivity extends AppCompatActivity {
    private String TOKEN;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();
        String url = getResources().getString(R.string.url) + "api/mobile/customer/request/broadcast/" + TOKEN;
        WebView myWebView = (WebView) findViewById(R.id.web1);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(url);
        Log.d("URL",url);

    }

}
