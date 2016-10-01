package id.ac.its.alpro.customer.asynctask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import id.ac.its.alpro.customer.LoginActivity;
import id.ac.its.alpro.customer.MainMenuActivity;
import id.ac.its.alpro.customer.R;
import id.ac.its.alpro.customer.component.Auth;
import id.ac.its.alpro.customer.databaseHandler.MySQLiteHelper;


/**
 * Created by ALPRO on 22/02/2016.
 */
public class AsyncTaskLogout extends AsyncTask<String, Integer, Double> {
    private ProgressDialog dialog;
    private String TOKEN;
    private Context context;
    private Activity mainActivity;


    public AsyncTaskLogout(Activity mainActivity, Context context, String TOKEN){
        super();
        dialog = new ProgressDialog(context);
        this.TOKEN = TOKEN;
        this.context = context;
        this.mainActivity = mainActivity;
    }
    @Override
    protected Double doInBackground(String... params) {
        // TODO Auto-generated method stub
        postData();
        return null;
    }

    protected void onPostExecute(Double result) {
        //Toast.makeText(getApplicationContext(), "command sent",Toast.LENGTH_LONG).show();
        dialog.dismiss();
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Logging Out...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void postData() {
        //Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        String url = mainActivity.getResources().getString(R.string.url) + "api/admin/logout/"+TOKEN;
        HttpGet httpGet = new HttpGet(url);
        Log.d("URL", url);

        try {
            HttpResponse response = httpclient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200){
                MySQLiteHelper db = new MySQLiteHelper(context);
                Auth tmp = db.get(1);
                tmp.setStatus("expired");
                db.update(tmp);
                db.close();
            }

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        finally {
        }
    }
}
