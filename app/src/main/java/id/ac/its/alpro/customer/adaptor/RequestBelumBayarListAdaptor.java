package id.ac.its.alpro.customer.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.customer.R;
import id.ac.its.alpro.customer.component.Request;

/**
 * Created by Luffi on 26/02/2016.
 */
public class RequestBelumBayarListAdaptor extends ArrayAdapter<Request> {

    private List<Request> items;
    private int layoutResourceId;
    private Context context;

    public RequestBelumBayarListAdaptor(Context context, int layoutResourceId, List<Request> items){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewRequestHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId,parent,false);
        holder = new NewRequestHolder();
        holder.item = items.get(position);
        holder.nama_penyedia_jasa = (TextView)row.findViewById(R.id.nama_penyedia);
        holder.jenis_servis = (TextView)row.findViewById(R.id.jenis_service);
        holder.tanggal_servis = (TextView)row.findViewById(R.id.tanggal_request);
        holder.tanggal_selesai = (TextView)row.findViewById(R.id.tanggal_selesai);
        holder.harga_total = (TextView)row.findViewById(R.id.harga);
        holder.detail = (ImageButton)row.findViewById(R.id.detailButton);
        holder.call = (ImageButton)row.findViewById(R.id.callButton);
        holder.pay = (ImageButton)row.findViewById(R.id.payButton);
        setupItem(holder);
        return row;
    }

    private void setupItem(NewRequestHolder holder){
        holder.nama_penyedia_jasa.setText(holder.item.getNamapenyediajasa());
        holder.jenis_servis.setText(holder.item.getTipejasa());
        holder.tanggal_servis.setText("Tanggal Request : " + holder.item.getTanggalrequest());
        holder.tanggal_selesai.setText("Tanggal Selesai : " + holder.item.getTanggalselesai());

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(holder.item.getHargatotal().longValue());

        holder.harga_total.setText("Rp. " + temp + ",-");
        holder.call.setTag(holder.item);
        holder.detail.setTag(holder.item);
        holder.pay.setTag(holder.item);
    }

    public static class NewRequestHolder{
        Request item;
        TextView nama_penyedia_jasa;
        TextView jenis_servis;
        TextView tanggal_servis;
        TextView tanggal_selesai;
        TextView harga_total;
        ImageButton detail,call,pay;
    }
}
