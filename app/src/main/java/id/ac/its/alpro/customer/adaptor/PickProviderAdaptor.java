package id.ac.its.alpro.customer.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.customer.R;
import id.ac.its.alpro.customer.component.Provider;

/**
 * Created by ALPRO on 23/03/2016.
 */
public class PickProviderAdaptor extends ArrayAdapter<Provider> {
    private List<Provider> items;
    private int layoutResourceId;
    private Context context;
    private int mode;

    public PickProviderAdaptor(Context context, int layoutResourceId, List<Provider> items){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PickProviderHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId,parent,false);
        holder = new PickProviderHolder();
        holder.item = items.get(position);
        holder.namapenyediajasa = (TextView)row.findViewById(R.id.namapenyediajasa);
        holder.namaperusahaan = (TextView)row.findViewById(R.id.namaperusahaan);
        holder.jamservis = (TextView)row.findViewById(R.id.jamservis);
        holder.hargaperkiraan = (TextView)row.findViewById(R.id.hargaperkiraan);
        holder.rating = (TextView)row.findViewById(R.id.ratingProvider);
        holder.detail = (ImageView)row.findViewById(R.id.detail);
        holder.pick = (ImageView)row.findViewById(R.id.pick);
        holder.call = (ImageView)row.findViewById(R.id.call);
        setupItem(holder);
        return row;
    }

    private void setupItem(PickProviderHolder holder){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(Double.parseDouble(holder.item.getHargaperkiraan()));

        holder.namapenyediajasa.setText(holder.item.getNamapenyediajasa());
        holder.namaperusahaan.setText("Instansi : " + holder.item.getNamaperusahaan());
        holder.jamservis.setText("Jam Servis : " + holder.item.getJamservis());
        holder.hargaperkiraan.setText("Harga perkiraan : Rp. " + temp + ",-");
        holder.rating.setText("Rating: " +holder.item.getRating()+"/5");
        holder.detail.setTag(holder.item);
        holder.pick.setTag(holder.item);
        holder.call.setTag(holder.item);
    }

    public static class PickProviderHolder {
        Provider item;
        TextView namapenyediajasa;
        TextView namaperusahaan;
        TextView jamservis;
        TextView hargaperkiraan;
        TextView rating;
        ImageView detail,pick,call;
    }
}
