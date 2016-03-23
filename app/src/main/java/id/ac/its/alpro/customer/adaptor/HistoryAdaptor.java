package id.ac.its.alpro.customer.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import id.ac.its.alpro.customer.R;
import id.ac.its.alpro.customer.component.Request;
import id.ac.its.alpro.customer.component.Transaksi;

/**
 * Created by ALPRO on 22/03/2016.
 */
public class HistoryAdaptor extends ArrayAdapter<Request> {
    private List<Request> items;
    private int layoutResourceId;
    private Context context;
    private int section;

    public HistoryAdaptor(Context context, int layoutResourceId, List<Request> items, int section){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
        this.section = section;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HistoryHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId,parent,false);
        holder = new HistoryHolder();
        holder.item = items.get(position);
        holder.statusServis = (TextView)row.findViewById(R.id.statusServis);
        holder.jenisServis = (TextView)row.findViewById(R.id.jenisServis);
        holder.tanggalServis = (TextView)row.findViewById(R.id.tanggalServis);
        holder.lokasiServis = (TextView)row.findViewById(R.id.lokasiServis);
        holder.mainHistory = (RelativeLayout)row.findViewById(R.id.mainHistory);
        holder.mainHistory.setTag(holder.item);
        setupItem(holder);
        return row;
    }

    private void setupItem(HistoryHolder holder){
        String status = "";
        if(section == 1)
        {
            if (holder.item.getTanggalmulai() == null){
                status = "Belum Diambil";
            }
            else if(holder.item.getTanggalselesai() == null){
                status = "Sedang Dikerjakan";
            }
            else if(holder.item.getTanggalbayar() == null){
                status = "Belum Dibayar";
            }
        }
        else {
            if (holder.item.getFlagtransaksi() == 0){
                status = "Selesai";
            }
            else{
                status = "Dibatalkan";
            }
        }

        holder.statusServis.setText(status);
        holder.jenisServis.setText(holder.item.getTipejasa());
        holder.tanggalServis.setText(holder.item.getTanggalrequest());
        holder.lokasiServis.setText("Lokasi : " + holder.item.getLokasi());

    }

    public static class HistoryHolder {
        Request item;
        TextView statusServis;
        TextView tanggalServis;
        TextView jenisServis;
        TextView lokasiServis;
        RelativeLayout mainHistory;
        ImageView gambarStatus;
    }
}
