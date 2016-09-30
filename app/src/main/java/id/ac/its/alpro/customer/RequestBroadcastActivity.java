package id.ac.its.alpro.customer;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.component.Request;

public class RequestBroadcastActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ArrayList<String> tipeJasa = new ArrayList<>();
    ArrayList<String> keyTipe = new ArrayList<>();
    private String TOKEN;
    private Auth auth;
    Spinner serviceType;
    EditText catatanKerusakan, Lokasi;
    String catatan, lokasi, tipe;

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mCurrentLocation;
    private MapFragment mMapFragment;
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 1;

    private static int permission;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_broadcast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        verifyStoragePermissions(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();
        serviceType = (Spinner) findViewById(R.id.serviceType);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        catatanKerusakan = (EditText) findViewById(R.id.catatanKerusakan);
        Lokasi = (EditText) findViewById(R.id.lokasi);

        mMapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMap);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = MapFragment.newInstance();
            fragmentTransaction.replace(R.id.fragmentMap, mMapFragment).commit();
        }

        if (mMapFragment != null) {
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    GoogleMap mMap = googleMap;


                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mCurrentLocation = LocationServices
                            .FusedLocationApi
                            .getLastLocation(mGoogleApiClient);

                    initCamera(mMap, mCurrentLocation);
                }
            });
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catatan = catatanKerusakan.getText().toString().trim();
                lokasi = Lokasi.getText().toString().trim();
                tipe = keyTipe.get(serviceType.getSelectedItemPosition());
                new AsyncTaskPost().execute("hehe");
            }
        });


        /*Method Footer*/
        ImageView btn;
        LinearLayout order, history, ecash, setting;
        order = (LinearLayout) findViewById(R.id.orderBtn);
        history = (LinearLayout) findViewById(R.id.historyBtn);
        ecash = (LinearLayout) findViewById(R.id.ecashBtn);
        setting = (LinearLayout) findViewById(R.id.settingBtn);
        btn = (ImageView) findViewById(R.id.orderImg);
        btn.setColorFilter(Color.argb(255, 244, 67, 54));

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
                Intent i = new Intent(getApplicationContext(), History.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        ecash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EcashWalletActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                i.putExtra("Auth", auth);
                startActivity(i);
            }
        });

        refreshContent();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void initCamera(GoogleMap gmaps, android.location.Location location) {
        LatLng pos = new LatLng(-7.279379, 112.796987);

        CameraPosition position = CameraPosition.builder()
                .target(pos)
                .zoom(13f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        gmaps.addMarker(new MarkerOptions().position(pos));

        gmaps.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        gmaps.setMapType(MAP_TYPES[curMapTypeIndex]);
        gmaps.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gmaps.setMyLocationEnabled(true);
        gmaps.getUiSettings().setZoomControlsEnabled(true);
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(RequestBroadcastActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, tipeJasa);
            serviceType.setAdapter(adapter);
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
            String url = "http://ridhoperdana.net/servisin/htdocs/public/api/customer/request/listjenis/0";
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();
                Restipe[] request = baru.fromJson(reader, Restipe[].class);
//                Log.d("Hehe", request.toString());
                for (int i = 0; i < request.length; i++){
                    tipeJasa.add(request[i].getNama());
                    keyTipe.add(request[i].getTipejasa_id());
                }
//                Log.d("finish",finishedReq.toString());
//                Log.d("ongoing", onGoingReq.toString());

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshContent() {
        tipeJasa.clear();
        keyTipe.clear();
        new AsyncTaskList().execute();
    }

    public class Restipe{
        String tipejasa_id, nama;

        public Restipe(String tipejasa_id, String nama) {
            this.tipejasa_id = tipejasa_id;
            this.nama = nama;
        }

        public String getTipejasa_id() {
            return tipejasa_id;
        }

        public void setTipejasa_id(String tipejasa_id) {
            this.tipejasa_id = tipejasa_id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private class AsyncTaskPost extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        int status;
        public AsyncTaskPost(){
            dialog = new ProgressDialog(RequestBroadcastActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            if(status == 200){
                Toast.makeText(getApplicationContext(),"Request Berhasil Dibuat",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(),"Request Gagal Dibuat!!",Toast.LENGTH_SHORT).show();

            finish();
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
            ArrayList<NameValuePair> postParameters;
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://ridhoperdana.net/servisin/htdocs/public/api/customer/request/broadcast/"+TOKEN;
            HttpPost httpPost = new HttpPost(url);
            Log.d("URL",url);
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("catatancustomer", catatan));
            postParameters.add(new BasicNameValuePair("tipejasa_id", tipe));
            postParameters.add(new BasicNameValuePair("lokasi", lokasi));
            postParameters.add(new BasicNameValuePair("lat", 0+""));
            postParameters.add(new BasicNameValuePair("lng", 0+""));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
                HttpResponse response = httpclient.execute(httpPost);
                status = response.getStatusLine().getStatusCode();

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }
}
