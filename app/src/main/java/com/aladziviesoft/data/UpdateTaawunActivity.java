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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aladziviesoft.data.utils.AppConf.URL_ADD_TAAWUN;
import static com.aladziviesoft.data.utils.AppConf.URL_DETAIL_TAAWUN;
import static com.aladziviesoft.data.utils.AppConf.URL_EDIT_TAAWUN;

public class UpdateTaawunActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.txNamaPenyetor)
    EditText txNamaPenyetor;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.txJumlahUang)
    EditText txJumlahUang;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.uplunas)
    RadioButton uplunas;
    @BindView(R.id.uptidaklunas)
    RadioButton uptidaklunas;
    @BindView(R.id.uprgrub)
    RadioGroup uprgrub;
    @BindView(R.id.upsimpan)
    Button upsimpan;
    String id, nama, status;
    SessionManager sessionManager;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    OwnProgressDialog loading;
    ConnectivityManager conMgr;
    OwnProgressDialog pDialog;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taawun_update);
        ButterKnife.bind(this);
        cekInternet();

        requestQueue = Volley.newRequestQueue(UpdateTaawunActivity.this);
        pDialog = new OwnProgressDialog(UpdateTaawunActivity.this);
        sessionManager = new SessionManager(UpdateTaawunActivity.this);

        id = getIntent().getStringExtra("id_pembayaran");

        DetailDataTaawun();
    }

    public void onRadioButtonClicked() {
        // Is the button now checked?
        int id = uprgrub.getCheckedRadioButtonId();
        switch (id) {
            case R.id.uplunas:
                status = "1";
                AndLog.ShowLog("tesid",status);
                break;
            case R.id.uptidaklunas:
                status = "0";
                AndLog.ShowLog("tesid",status);
                break;
        }
    }

    private void UpdateDataTaawun() {
        onRadioButtonClicked();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_EDIT_TAAWUN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("ress", response);

                GlobalToast.ShowToast(UpdateTaawunActivity.this, "Kegiatan berhasil ditambahkan");
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
                    params.put("id_kegiatan", sessionManager.getIdKegiatan());
                    params.put("id_pembayaran", id);
                    params.put("nama_penyetor", txNamaPenyetor.getText().toString());
                    params.put("jumlah_uang", txJumlahUang.getText().toString());
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

    private void DetailDataTaawun() {
        stringRequest = new StringRequest(Request.Method.POST, URL_DETAIL_TAAWUN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("detail", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        txNamaPenyetor.setText(json.getString("nama_penyetor"));
                        txJumlahUang.setText(json.getString("jumlah_uang"));
                        if (json.getString("status").equals("1")) {
                            uplunas.setChecked(true);
                            uptidaklunas.setChecked(false);
                        } else {
                            uptidaklunas.setChecked(true);
                            uplunas.setChecked(false);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                String token, apikey, refresh_code;
                token = sessionManager.getToken();
                apikey = sessionManager.getApikey();
                refresh_code = sessionManager.getRefreshcode();

                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("id_pembayaran", id);
                    params.put("token", AESCrypt.decrypt("tok", token));
                    params.put("apikey", AESCrypt.decrypt("api", apikey));
                    params.put("refresh_code", AESCrypt.decrypt("ref", refresh_code));

                    AndLog.ShowLog("params", String.valueOf(params));

                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    @OnClick(R.id.upsimpan)
    public void onViewClicked() {
        if (txNamaPenyetor.getText().toString().trim().length() > 0 && txJumlahUang.getText().toString().trim().length() > 0) {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                UpdateDataTaawun();
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


