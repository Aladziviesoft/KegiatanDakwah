package com.aladziviesoft.data.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "data_pembayaran";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAMA = "nama_pembayar";
    public static final String COLUMN_UANG = "jumlah_uang";
    public static final String COLUMN_TANGGAL = "tanggal_bayar";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ID_KEG = "id_kegiatan";

    public static final String TABLE_NAME2 = "data_kegiatan";
    public static final String ID_KEGIATAN = "id";
    public static final String NAMA_KEGIATAN = "nama_kegiatan";
    public static final String JUMLAH_UANG = "jumlah_uang_kegiatan";

    public static final String TABLE_NAME3 = "inventory";
    public static final String ID_INVEN = "id_inven";
    public static final String NAMA_BARANG = "nama_barang";
    public static final String HARGA_BARANG = "harga_barang";
    public static final String BANYAK_BARANG = "banyak_barang";


    private static final String db_name = "data.db";
    private static final int db_version = 1;

    private static final String db_create = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAMA + " varchar(50) not null, "
            + COLUMN_UANG + " varchar(50) not null, "
            + COLUMN_TANGGAL + " timestamp not null, "
            + COLUMN_STATUS + " varchar(50) not null,"
            + COLUMN_ID_KEG +" integer );";

    private static final String db_buat = "create table "
            + TABLE_NAME2 + "("
            + ID_KEGIATAN + " integer primary key autoincrement, "
            + NAMA_KEGIATAN + " varchar(50) not null, "
            + JUMLAH_UANG + " varchar(50) not null);";

    private static final String db_buat2 = "create table "
            + TABLE_NAME3 + "("
            + ID_INVEN + " integer primary key autoincrement, "
            + NAMA_BARANG + " varchar(50) not null, "
            + HARGA_BARANG + " varchar(50) not null, "
            + BANYAK_BARANG + " varchar(50) not null);";

    public DBAdapter(Context context) {
        super(context, db_name, null, db_version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create);
        db.execSQL(db_buat);
        db.execSQL(db_buat2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBAdapter.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);

    }

}
