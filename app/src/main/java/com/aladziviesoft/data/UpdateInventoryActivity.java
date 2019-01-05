package com.aladziviesoft.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateInventoryActivity extends AppCompatActivity {

    @BindView(R.id.upnamabarang)
    EditText upnamabarang;
    @BindView(R.id.uphargabarang)
    EditText uphargabarang;
    @BindView(R.id.upbanyakbarang)
    EditText upbanyakbarang;
    @BindView(R.id.updateinven)
    Button updateinven;
    String id, nama, harga, qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);
        ButterKnife.bind(this);

        id = getIntent().getStringExtra("id_inventory");
        nama = getIntent().getStringExtra("nama_barang");
        harga = getIntent().getStringExtra("harga");
        qty = getIntent().getStringExtra("qty");

        upnamabarang.setText(nama);
        upbanyakbarang.setText(qty);
        uphargabarang.setText(harga);

    }

    @OnClick(R.id.updateinven)
    public void onViewClicked() {
    }
}
