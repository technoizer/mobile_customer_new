package id.ac.its.alpro.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.ac.its.alpro.customer.component.Auth;

public class NewRequestActivity extends AppCompatActivity {

    private String TOKEN;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();
    }

    public void broadcastRequest(View view) {
        Intent i = new Intent(getApplicationContext(), BroadcastActivity.class);
        i.putExtra("Auth", auth);
        startActivity(i);
    }

    public void directRequest(View view){
        Intent i = new Intent(getApplicationContext(), DirectRequestActivity.class);
        i.putExtra("Auth", auth);
        startActivity(i);
    }
}
