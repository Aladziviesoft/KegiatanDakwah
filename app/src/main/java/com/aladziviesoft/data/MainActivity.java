package com.aladziviesoft.data;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladziviesoft.data.adapter.DashAdapter;
import com.aladziviesoft.data.model.DashModel;
import com.aladziviesoft.data.model.SaldoModel;
import com.aladziviesoft.data.utils.AndLog;
import com.aladziviesoft.data.utils.DecimalsFormat;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aladziviesoft.data.utils.AppConf.URL_LIHAT_SALDO;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rec_menu)
    RecyclerView recMenu;
    @BindView(R.id.txNamaUser)
    TextView txNamaUser;
    @BindView(R.id.txSaldo)
    TextView txSaldo;
    @BindView(R.id.imageView)
    ImageView imageView;
    SessionManager sessionManager;
    DashAdapter adapter;
    String result = "result";
    String messages;
    OwnProgressDialog pDialog;
    ConnectivityManager conMgr;
    StringRequest stringRequest;
    private ArrayList<DashModel> arrayList = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(MainActivity.this);
        pDialog = new OwnProgressDialog(MainActivity.this);
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        recMenu.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3, LinearLayoutManager.VERTICAL, false);
        recMenu.setLayoutManager(layoutManager);

        DashAdapter adapter = new DashAdapter(MainActivity.this, arrayList);
        recMenu.setAdapter(adapter);

        txNamaUser.setText(sessionManager.getNama());

        LihatSaldo();

        setMenu();

    }


    private void LihatSaldo() {
        pDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, URL_LIHAT_SALDO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("detail", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("result");
                    if (result.equals("expired")) {

                        sessionManager.logoutUser();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);

                    }
                    else {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int a = 0; a < jsonArray.length(); a++) {
                            JSONObject json = jsonArray.getJSONObject(a);
                            SaldoModel saldoModel = new SaldoModel();
                            saldoModel.setId_saldo(json.getString("id_saldo"));
                            txSaldo.setText("Rp. " + DecimalsFormat.priceWithoutDecimal(json.getString("saldo")));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
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

    private void setMenu() {
        arrayList.add(new DashModel(R.drawable.kegiatan, "Kegiatan" + "\n" + "Dakwah"));
        arrayList.add(new DashModel(R.drawable.saldo, "Kas" + "\n" + "Bendahara"));
        arrayList.add(new DashModel(R.drawable.inventory, "Inventory" + "\n"));
        arrayList.add(new DashModel(R.drawable.edituser, "Edit User"));
        arrayList.add(new DashModel(R.drawable.about2, "About"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title dialog
            alertDialogBuilder.setTitle("Ingin Keluar dari aplikasi?");

            // set pesan dari dialog
            alertDialogBuilder
                    .setMessage("Klik Ya untuk keluar!")
                    .setIcon(R.drawable.ic_logout)
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sessionManager.logoutUser();

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // jika tombol ini diklik, akan menutup dialog
                            // dan tidak terjadi apa2
                            dialog.cancel();
                        }
                    });

            // membuat alert dialog dari builder
            AlertDialog alertDialog = alertDialogBuilder.create();

            // menampilkan alert dialog
            alertDialog.show();
        }

        if (id == R.id.nav_refresh) {
            LihatSaldo();
        }

        return super.onOptionsItemSelected(item);
    }

}
