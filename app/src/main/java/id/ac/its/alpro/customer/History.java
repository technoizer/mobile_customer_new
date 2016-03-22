package id.ac.its.alpro.customer;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.ArrayList;
import java.util.List;

import id.ac.its.alpro.customer.adaptor.HistoryAdaptor;
import id.ac.its.alpro.customer.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Request;

public class History extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static List<Request> onGoingReq = new ArrayList<>();
    private static List<Request> finishedReq = new ArrayList<>();
    private TabLayout tabs;
    String urlAmbil;
    private ViewPager mViewPager;
    private String TOKEN;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        tabs = (TabLayout) findViewById(R.id.tabs);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        refreshContent();

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
        btn = (ImageView) findViewById(R.id.historyImg);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            new AsyncTaskLogout(this, History.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_history, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.fragmenList);
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            if (section == 1){
                HistoryAdaptor adaptor = new HistoryAdaptor(getContext(),R.layout.item_history,onGoingReq, section);
                listView.setAdapter(adaptor);
                listView.setEmptyView(rootView.findViewById(R.id.empty));
            }
            else{
                HistoryAdaptor adaptor = new HistoryAdaptor(getContext(),R.layout.item_history,finishedReq, section);
                listView.setAdapter(adaptor);
                listView.setEmptyView(rootView.findViewById(R.id.empty));
            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "In Progress";
                case 1:
                    return "Finished";
            }
            return null;
        }
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(History.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabs.setupWithViewPager(mViewPager);
            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
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
            String url = "http://servisin.au-syd.mybluemix.net/api/customer/transaksi/"+TOKEN;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Request[] request = baru.fromJson(reader, Request[].class);
//                Log.d("Hehe", request.toString());
                for (int i = 0; i < request.length; i++){
                    if(request[i].getTanggalbayar() == null){
                        onGoingReq.add(request[i]);
                    }
                    else
                        finishedReq.add(request[i]);
                }
                Log.d("finish",finishedReq.toString());
                Log.d("ongoing", onGoingReq.toString());



            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshContent() {
        finishedReq.clear();
        onGoingReq.clear();
        new AsyncTaskList().execute("hehe");
    }

    public void historyListener(View view) {
        Request request = (Request) view.getTag();
        if (request.getHargaperkiraan() == null){
            Toast.makeText(getApplicationContext(),"Belum Diambil",Toast.LENGTH_LONG).show();
        }
        else if(request.getHargatotal() == null){
            Toast.makeText(getApplicationContext(),"Sedang Dikerjakan",Toast.LENGTH_LONG).show();
        }
        else if(request.getTanggalbayar() == null){
            Toast.makeText(getApplicationContext(),"Belum Dibayar",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Selesai",Toast.LENGTH_LONG).show();
        }
    }
}
