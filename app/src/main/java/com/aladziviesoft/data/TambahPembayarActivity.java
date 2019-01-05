package com.aladziviesoft.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aladziviesoft.data.utils.AndLog;
import com.aladziviesoft.data.utils.GlobalToast;
import com.aladziviesoft.data.utils.OwnProgressDialog;
import com.aladziviesoft.data.utils.SessionManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aladziviesoft.data.utils.AppConf.URL_ADD_TAAWUN;

public class TambahPembayarActivity extends AppCompatActivity {

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
    @BindView(R.id.simpan)
    Button simpan;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    SessionManager sessionManager;
    OwnProgressDialog pDialog;
    private RequestQueue requestQueue;
    String idkeg, status;
    ConnectivityManager conMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pembayar);
        ButterKnife.bind(this);
        cekInternet();

        sessionManager = new SessionManager(TambahPembayarActivity.this);
        pDialog = new OwnProgressDialog(TambahPembayarActivity.this);
        requestQueue = Volley.newRequestQueue(TambahPembayarActivity.this);

        idkeg = getIntent().getStringExtra("id_kegiatan");

    }

    public void onRadioButtonClicked() {
        // Is the button now checked?
        int id = rgrub.getCheckedRadioButtonId();
        switch (id) {
            case R.id.lunas:
                status = "1";
                break;
            case R.id.tidaklunas:
                status = "0";
                break;
        }
    }

    private void SimpanData() {
        onRadioButtonClicked();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_ADD_TAAWUN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("ress", response);

                GlobalToast.ShowToast(TambahPembayarActivity.this, "Kegiatan berhasil ditambahkan");
                pDialog.dismiss();
                finish();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Ada Kesalahan", Toast.LENGTH_LONG).show();
                AndLog.ShowLog("errss", String.valueOf(error));
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                String token, apikey, refresh_code;
                token = sessionManager.getToken();
                apikey = sessionManager.getApikey();
                refresh_code = sessionManager.getRefreshcode();

                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("token", AESCrypt.decrypt("tok", token));
                    params.put("apikey", AESCrypt.decrypt("api", apikey));
                    params.put("refresh_code", AESCrypt.decrypt("ref", refresh_code));
                    params.put("id_users", AESCrypt.decrypt("id_user", sessionManager.getIduser()));
                    params.put("id_kegiatan", idkeg);
                    params.put("nama_penyetor", nama.getText().toString());
                    params.put("jumlah_uang", uang.getText().toString());
                    params.put("status", status);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                AndLog.ShowLog("params", String.valueOf(params));
                return params;
            }

        };
        requestQueue.add(strReq);

    }


    public void cekInternet() {

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.simpan)
    public void onViewClicked() {
        if (nama.getText().toString().trim().length() > 0 && uang.getText().toString().trim().length() > 0) {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                SimpanData();
            } else {
                Snackbar snackbar = Snackbar
                        .make(linearlayout, "No Internet Connection, Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                pDialog.dismiss();
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(linearlayout, "Kolom tidak boleh kosong, silahkan di isi", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}


