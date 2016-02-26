package id.ac.its.alpro.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import id.ac.its.alpro.customer.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.customer.component.Auth;

public class MainMenuActivity extends AppCompatActivity {

    private String TOKEN;
    private Auth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();
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
            new AsyncTaskLogout(this, MainMenuActivity.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestBaru(View view) {
//        Intent i = new Intent(getApplicationContext(),NewRequestActivity.class);
//        i.putExtra("Auth", auth);
//        startActivity(i);
    }

    public void requestDiterima(View view) {
//        Intent i = new Intent(getApplicationContext(),RequestDiterimaActivity.class);
//        i.putExtra("Auth", auth);
//        startActivity(i);
    }

    public void cekPembayaran(View view) {
//        Intent i = new Intent(getApplicationContext(),HistoryRequestActivity.class);
//        i.putExtra("Auth", auth);
//        startActivity(i);
    }

    public void ongoingRequest(View view) {
        Intent i = new Intent(getApplicationContext(),RequestBerlangsungActivity.class);
        i.putExtra("Auth", auth);
        startActivity(i);
    }

    public void unpaidRequest(View view) {
        Intent i = new Intent(getApplicationContext(),RequestBelumBayarActivity.class);
        i.putExtra("Auth", auth);
        startActivity(i);
    }

    public void historyRequest(View view) {
        Intent i = new Intent(getApplicationContext(),HistoryRequestActivity.class);
        i.putExtra("Auth", auth);
        startActivity(i);
    }
}
