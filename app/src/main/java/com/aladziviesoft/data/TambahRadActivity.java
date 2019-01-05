package com.aladziviesoft.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aladziviesoft.data.utils.AndLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TambahRadActivity extends AppCompatActivity {

    @BindView(R.id.etNamaRencana)
    EditText etNamaRencana;
    @BindView(R.id.etNominal)
    EditText etNominal;
    @BindView(R.id.btAdd)
    Button btAdd;
    @BindView(R.id.parent_linear_layout)
    LinearLayout parentLinearLayout;
    String v1, v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_rad);
        ButterKnife.bind(this);
    }

    public void onAddField(View v) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.field, null);
        EditText etnamarencana = (EditText) addView.findViewById(R.id.etNamaRencana);
        EditText etnominal = (EditText) addView.findViewById(R.id.etNominal);

        Button buttonRemove = (Button) addView.findViewById(R.id.btDelete);
        final View.OnClickListener thisListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) addView.getParent()).removeView(addView);
                etNamaRencana.requestFocus();
            }
        };

        buttonRemove.setOnClickListener(thisListener);
        parentLinearLayout.addView(addView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simpan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_simpan) {
//            GlobalToast.ShowToast(TambahRadActivity.this, "Yop");
            String data = etNamaRencana.getText().toString().trim();
            AndLog.ShowLog("dsaa", data);

        }
        return true;

    }

    @OnClick({R.id.btAdd})
    public void onViewClicked(View view) {
        onAddField(view);


    }
}
