package com.aladziviesoft.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladziviesoft.data.AboutActivity;
import com.aladziviesoft.data.EditUserActivity;
import com.aladziviesoft.data.KegiatanActivity;
import com.aladziviesoft.data.InventoryActivity;
import com.aladziviesoft.data.R;
import com.aladziviesoft.data.SaldoActivity;
import com.aladziviesoft.data.model.DashModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashAdapter extends RecyclerView.Adapter<DashAdapter.DashHolder> {


    private Context context;
    private ArrayList<DashModel> models;

    public DashAdapter(Context context, ArrayList<DashModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public DashHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_list_menu, viewGroup, false);
        return new DashHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashHolder dashHolder, final int i) {

        dashHolder.img.setImageResource(models.get(i).getGambar());
        dashHolder.nama.setText(models.get(i).getNama());
        dashHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (i == 0) {
                    intent = new Intent(context, KegiatanActivity.class);
                    context.startActivity(intent);
                }

                if (i == 1) {
                    intent = new Intent(context, SaldoActivity.class);
                    context.startActivity(intent);
                }

                if (i == 2) {
                    intent = new Intent(context, InventoryActivity.class);
                    context.startActivity(intent);
                }

                if (i == 3) {
                    intent = new Intent(context, EditUserActivity.class);
                    context.startActivity(intent);
                }

                if (i == 4) {
                    intent = new Intent(context, AboutActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class DashHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.nama)
        TextView nama;
        @BindView(R.id.cardview)
        CardView cardview;

        public DashHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
