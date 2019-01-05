package com.aladziviesoft.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aladziviesoft.data.adapter.ListInventoryAdapter;
import com.aladziviesoft.data.model.ListInventoryModel;
import com.aladziviesoft.data.utils.AndLog;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aladziviesoft.data.utils.AppConf.URL_LIST_INVENTORY;
import static com.aladziviesoft.data.utils.AppConf.URL_LIST_KEGIATAN;

public class InventoryActivity extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    ListInventoryAdapter adapter;
    List<ListInventoryModel> arraylist = new ArrayList<>();
    @BindView(R.id.rec_inven)
    RecyclerView recInven;
    @BindView(R.id.Swipe)
    SwipeRefreshLayout Swipe;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    OwnProgressDialog loading;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(InventoryActivity.this);
        requestQueue = Volley.newRequestQueue(InventoryActivity.this);
        loading = new OwnProgressDialog(InventoryActivity.this);
        recInven.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(InventoryActivity.this, 1,
                GridLayoutManager.VERTICAL, false);
        recInven.setLayoutManager(layoutManager);

        Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arraylist.clear();
                ListDataInventory();
            }
        });

        ListDataInventory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_exit) {
            finish();
        }
        return true;

    }

    private void ListDataInventory() {

        stringRequest = new StringRequest(Request.Method.POST, URL_LIST_INVENTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndLog.ShowLog("ds", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        ListInventoryModel modelMenu = new ListInventoryModel();
                        modelMenu.setIdBarang(json.getString("id_inventory"));
                        modelMenu.setNamaBarang(json.getString("nama_barang"));
                        modelMenu.setHargaBarang(json.getString("harga"));
                        modelMenu.setBanyakBarang(json.getString("qty"));
                        arraylist.add(modelMenu);
                    }
                    ListInventoryAdapter adapter = new ListInventoryAdapter(arraylist, InventoryActivity.this);
                    recInven.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();
                if (Swipe != null) {
                    Swipe.setRefreshing(false);
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
                    params.put("token", AESCrypt.decrypt("tok", token));
                    params.put("apikey", AESCrypt.decrypt("api", apikey));
                    params.put("refresh_code", AESCrypt.decrypt("ref", refresh_code));

                    Log.d("params", String.valueOf(params));

                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                return params;
            }

        }

        ;

        requestQueue.add(stringRequest);
    }


    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        Intent a = new Intent(InventoryActivity.this, TambahInventoryActivity.class);
        startActivity(a);
    }
}
