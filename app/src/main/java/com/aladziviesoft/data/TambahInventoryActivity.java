package com.aladziviesoft.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aladziviesoft.data.adapter.DBCRUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahInventoryActivity extends AppCompatActivity {

    @BindView(R.id.namabarang)
    EditText namabarang;
    @BindView(R.id.hargabarang)
    EditText hargabarang;
    @BindView(R.id.banyakbarang)
    EditText banyakbarang;
    @BindView(R.id.simpaninven)
    Button simpaninven;
    DBCRUD koneksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_inventory);
        ButterKnife.bind(this);

        koneksi = new DBCRUD(this);
        koneksi.open();
    }


    public void onClicked(View view) {
        String namab = namabarang.getText().toString();
        String hargab = hargabarang.getText().toString();
        String banyakb = banyakbarang.getText().toString();
        if (namab.isEmpty() || hargab.isEmpty()) {
            Toast.makeText(TambahInventoryActivity.this, "Masukkan Nama Barang dan harga", Toast.LENGTH_SHORT).show();
        } else {
            long id = koneksi.insertinventory(namab, hargab, banyakb);
            if (id <= 0) {
                Toast.makeText(TambahInventoryActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                namabarang.setText("");
                hargabarang.setText("");
                banyakbarang.setText("");
            } else {
                Toast.makeText(TambahInventoryActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                namabarang.setText("");
                hargabarang.setText("");
                banyakbarang.setText("");
            }
        }
        finish();
    }
}
