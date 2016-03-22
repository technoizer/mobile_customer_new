package id.ac.its.alpro.customer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

        /*Method Footer*/
        ImageView btn;
        LinearLayout order, history, ecash, setting;
        order = (LinearLayout) findViewById(R.id.orderBtn);
        history = (LinearLayout) findViewById(R.id.historyBtn);
        ecash = (LinearLayout) findViewById(R.id.ecashBtn);
        setting = (LinearLayout) findViewById(R.id.settingBtn);
        btn = (ImageView) findViewById(R.id.orderImg);
        btn.setColorFilter(Color.argb(255,244,67,54));

//        order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),NewRequestActivity.class);
//                i.putExtra("Auth", auth);
//                startActivity(i);
//            }
//        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),History.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        ecash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),EcashWalletActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RequestBelumBayarActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });


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
