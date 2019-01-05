package com.aladziviesoft.data.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aladziviesoft.data.R;
import com.aladziviesoft.data.UpdateInventoryActivity;
import com.aladziviesoft.data.model.ListInventoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListInventoryAdapter extends RecyclerView.Adapter<ListInventoryAdapter.Holder> {
    DBAdapter dbcenter;
    private List<ListInventoryModel> arraylist;
    private Context context;

    public ListInventoryAdapter(List<ListInventoryModel> arraylist, Context context) {
        this.arraylist = arraylist;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_list_inventory, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        holder.Namabarang.setText(arraylist.get(i).getNamaBarang());
        holder.Hargabarang.setText(arraylist.get(i).getHargaBarang());
        holder.Banyakbarang.setText(arraylist.get(i).getBanyakBarang());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] item = {"Update", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pilih Aksi");
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on item[which]
                        Intent intent;
                        switch (which) {
                            case 0:
                                item[0] = "Update";
                                intent = new Intent(context, UpdateInventoryActivity.class);
                                intent.putExtra("id_inventory", arraylist.get(i).getIdBarang());
                                intent.putExtra("nama_barang", arraylist.get(i).getNamaBarang());
                                intent.putExtra("qty", arraylist.get(i).getBanyakBarang());
                                intent.putExtra("harga", arraylist.get(i).getHargaBarang());
                                context.startActivity(intent);
                                break;

                            case 1:
                                item[1] = "Delete";
                                dbcenter = new DBAdapter(context);
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from inventory where id_inven = '" + arraylist.get(i).getIdBarang() + "'");
                                break;

                        }
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.Namabarang)
        TextView Namabarang;
        @BindView(R.id.Hargabarang)
        TextView Hargabarang;
        @BindView(R.id.Banyakbarang)
        TextView Banyakbarang;
        @BindView(R.id.cardview)
        CardView cardview;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
