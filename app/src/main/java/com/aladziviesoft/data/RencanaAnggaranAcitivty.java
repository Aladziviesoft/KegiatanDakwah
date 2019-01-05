package com.aladziviesoft.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.aladziviesoft.data.adapter.RabAdapter;
import com.aladziviesoft.data.model.RabModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RencanaAnggaranAcitivty extends AppCompatActivity {


    RabAdapter adapter;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.etNamaKEgiatan)
    TextView etNamaKEgiatan;
    @BindView(R.id.txJumlahUang)
    TextView txJumlahUang;
    @BindView(R.id.switch1)
    Switch switch1;
    @BindView(R.id.rec_list_rad)
    RecyclerView recListRad;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    private ArrayList<RabModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rencana_anggaran);
        ButterKnife.bind(this);
        recListRad.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(RencanaAnggaranAcitivty.this, 1, LinearLayoutManager.VERTICAL, false);
        recListRad.setLayoutManager(layoutManager);


    }


    @OnClick({R.id.imgBack, R.id.floatingActionButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                break;
            case R.id.floatingActionButton:
                Intent intent = new Intent(RencanaAnggaranAcitivty.this, TambahRadActivity.class);
                startActivity(intent);
                break;
        }
    }
}
