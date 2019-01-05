package com.aladziviesoft.data.utils;


public class AppConf {
//    public static final String BASE_URL = "http://iqbal.gapoktanmajumapan.com/";
public static final String BASE_URL = "http://localhost/";
    private static final String BASE_INDEX = BASE_URL + "api_majlis_taklim_mobile/index.php/";
    public static final String URL_LIHAT_SALDO = BASE_INDEX + "kegiatan_c/lihat_saldo";
    public static final String URL_ADD_SALDO = BASE_INDEX + "kegiatan_c/tambah_saldo";
    public static final String URL_EDIT_SALDO = BASE_INDEX + "kegiatan_c/edit_saldo";
    public static final String URL_LIST_KEGIATAN = BASE_INDEX + "kegiatan_c/data";
    public static final String URL_EDIT_KEGIATAN = BASE_INDEX + "kegiatan_c/edit";
    public static final String URL_LIST_TAAWUN = BASE_INDEX + "Pembayaran_c/data_kegiatan";
    public static final String URL_DANA_TERKUMPUL = BASE_INDEX + "Pembayaran_c/dana_terkumpul";
    public static final String URL_COUNT = BASE_INDEX + "Pembayaran_c/data_status";
    public static final String URL_DETAIL_TAAWUN = BASE_INDEX + "Pembayaran_c/data_by_id";
    public static final String URL_LOGIN = BASE_INDEX + "auth/login";
    public static final String URL_REGISTER = BASE_INDEX + "auth/add";
    public static final String URL_ADD_KEGIATAN = BASE_INDEX + "kegiatan_c/tambah_kegiatan";
    public static final String URL_ADD_TAAWUN = BASE_INDEX + "pembayaran_c/tambah_pembayaran";
    public static final String URL_EDIT_TAAWUN = BASE_INDEX + "pembayaran_c/edit";
    public static final String URL_DELETE_TAAWUN = BASE_INDEX + "pembayaran_c/delete";
    public static final String URL_DELETE_KEGIATAN = BASE_INDEX + "kegiatan_c/delete";
    public static final String URL_LIST_INVENTORY = BASE_INDEX + "inventory_c/data";
    public static final String httpTag = "KillHttp";
}
