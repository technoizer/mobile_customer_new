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

import id.ac.its.alpro.customer.component.Auth;

public class ProviderProfileActivity extends AppCompatActivity {
    private String TOKEN;
    private Auth auth;
    private String id_penyedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = (Auth) getIntent().getSerializableExtra("Auth");
        id_penyedia = getIntent().getStringExtra("ID_PENYEDIA");
        TOKEN = auth.getToken();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        String url = "http://ridhoperdana.net/servisin/htdocs/public/api/admin/provider/profil/"+id_penyedia+"/"+TOKEN;
        WebView myWebView = (WebView) findViewById(R.id.web1);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl(url);
        Log.d("URL", url);
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
