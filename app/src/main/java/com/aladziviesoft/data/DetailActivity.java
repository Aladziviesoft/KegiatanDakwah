package com.aladziviesoft.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aladziviesoft.data.adapter.DBAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    protected Cursor cursor;
    Button tempkembali;
    DBAdapter koneksi;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tnama)
    TextView tnama;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tuang)
    TextView tuang;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.ttanggal)
    TextView ttanggal;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.tlunas)
    TextView tlunas;
    @BindView(R.id.kembali)
    Button kembali;
    @BindView(R.id.textView9)
    TextView textView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        koneksi = new DBAdapter(this);

        SQLiteDatabase db = koneksi.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM data_pembayaran WHERE nama_pembayar = '" +
                getIntent().getStringExtra("nama_pembayar") + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            textView9.setText(cursor.getString(0));
            tnama.setText(cursor.getString(1));
            tuang.setText(cursor.getString(2));
            ttanggal.setText(cursor.getString(3));
            tlunas.setText(cursor.getString(4));
        }
        kembali.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }


}


