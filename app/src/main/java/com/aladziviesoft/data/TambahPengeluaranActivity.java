package com.aladziviesoft.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahPengeluaranActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.nama)
    EditText nama;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.uang)
    EditText uang;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.lunas)
    RadioButton lunas;
    @BindView(R.id.tidaklunas)
    RadioButton tidaklunas;
    @BindView(R.id.rgrub)
    RadioGroup rgrub;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @BindView(R.id.btSimpan)
    Button btSimpan;
    @BindView(R.id.btUpdate)
    Button btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btSimpan, R.id.btUpdate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btSimpan:
                break;
            case R.id.btUpdate:
                break;
        }
    }
}
