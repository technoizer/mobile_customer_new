package id.ac.its.alpro.customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import id.ac.its.alpro.customer.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.customer.component.Auth;

public class SettingActivity extends AppCompatActivity {
    private String TOKEN;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        RelativeLayout logoutArea = (RelativeLayout) findViewById(R.id.logoutArea);
        logoutArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskLogout(SettingActivity.this, SettingActivity.this,TOKEN).execute("hehe");
            }
        });

        /*Method Footer*/
        ImageView btn;
        LinearLayout order, history, ecash, setting;
        order = (LinearLayout) findViewById(R.id.orderBtn);
        history = (LinearLayout) findViewById(R.id.historyBtn);
        ecash = (LinearLayout) findViewById(R.id.ecashBtn);
        btn = (ImageView) findViewById(R.id.settingImg);
        btn.setColorFilter(Color.argb(255, 244, 67, 54));

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NewRequestActivity.class);
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

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), History.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });
    }

}
