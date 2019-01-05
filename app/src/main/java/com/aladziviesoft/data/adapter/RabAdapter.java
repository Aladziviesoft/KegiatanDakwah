package com.aladziviesoft.data.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aladziviesoft.data.R;
import com.aladziviesoft.data.model.RabModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RabAdapter extends RecyclerView.Adapter<RabAdapter.RabHolder> {

    private Context context;
    private ArrayList<RabModel> arrayList;

    public RabAdapter(Context context, ArrayList<RabModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RabHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_list_rab, viewGroup, false);
        return new RabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RabHolder rabHolder, int i) {
        rabHolder.namarab.setText(arrayList.get(i).getNamaRab());
        rabHolder.jumlahuang.setText(arrayList.get(i).getJumlahUang());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RabHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.namarab)
        TextView namarab;
        @BindView(R.id.jumlahuang)
        TextView jumlahuang;

        public RabHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
