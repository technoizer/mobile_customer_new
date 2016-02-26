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
import id.ac.its.alpro.customer.component.Transaksi;

/**
 * Created by Luffi on 27/02/2016.
 */
public class EcashListAdaptor extends ArrayAdapter<Transaksi> {

    private List<Transaksi> items;
    private int layoutResourceId;
    private Context context;

    public EcashListAdaptor(Context context, int layoutResourceId, List<Transaksi> items){
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
        holder.nama = (TextView)row.findViewById(R.id.nama);
        holder.jenis_transaksi = (TextView)row.findViewById(R.id.jenis_transaksi);
        holder.tanggal = (TextView)row.findViewById(R.id.tanggal);
        holder.amount = (TextView)row.findViewById(R.id.amount);
        setupItem(holder);
        return row;
    }

    private void setupItem(NewRequestHolder holder){
        holder.nama.setText(holder.item.getName());
        holder.jenis_transaksi.setText(holder.item.getTransferType());
        holder.tanggal.setText(holder.item.getTransactionDate());
        holder.amount.setText(holder.item.getAmount());
    }

    public static class NewRequestHolder{
        Transaksi item;
        TextView jenis_transaksi;
        TextView nama;
        TextView tanggal;
        TextView amount;
    }
}
