package com.aladziviesoft.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aladziviesoft.data.model.FileModel;
import com.aladziviesoft.data.utils.AndLog;
import com.aladziviesoft.data.utils.GlobalToast;
import com.aladziviesoft.data.utils.MediaProcess;
import com.aladziviesoft.data.utils.OwnProgressDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aladziviesoft.data.utils.AppConf.URL_REGISTER;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.btTake)
    Button btTake;
    @BindView(R.id.btExplore)
    Button btExplore;
    @BindView(R.id.txNama)
    EditText txNama;
    @BindView(R.id.txAlamat)
    EditText txAlamat;
    @BindView(R.id.txUsername)
    EditText txUsername;
    @BindView(R.id.txPassword)
    EditText txPassword;
    @BindView(R.id.txNoTelp)
    EditText txNoTelp;
    @BindView(R.id.btRegister)
    AppCompatButton btRegister;
    OwnProgressDialog pDialog;
    int PICK_IMAGE_REQUEST = 1, PICK_IMAGE_REQUEST2 = 2;
    Bitmap bitmap, decoded;
    File outFileKtp, sdCard, dir;
    String image, fileName;
    int bitmap_size = 50;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    private RequestQueue requestQueue;
    private Uri mHighQualityImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        pDialog = new OwnProgressDialog(RegisterActivity.this);
        requestQueue = Volley.newRequestQueue(RegisterActivity.this);
    }

    private void SimpanData() {
        pDialog.show();
        if (txNama.getText().length() > 1) {
            StringRequest strReq = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    String values="log";
                    AndLog.ShowLog(values, response);
                    if (response.equals("1")) {
                        Snackbar snackbar = Snackbar
                                .make(linearlayout, "Username tersebut telah terdaftar, mohon gunakan username yang lain", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        pDialog.dismiss();
                    } else {
//                        Snackbar snackbar = Snackbar
//                                .make(linearlayout, "Success :" + response, Snackbar.LENGTH_LONG);
//                        snackbar.show();
                        GlobalToast.ShowToast(RegisterActivity.this, "Berhasil Di Input Ke database");
                        pDialog.dismiss();
                        finish();
                    }


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

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("full_name", txNama.getText().toString());
                    params.put("username", txUsername.getText().toString());
                    params.put("password", txPassword.getText().toString());
                    params.put("level", "user");
                    params.put("alamat", txAlamat.getText().toString());
                    params.put("phone", txNoTelp.getText().toString());
                    if (decoded != null) {
                        params.put("image", getStringImage(decoded));
                    }
                    AndLog.ShowLog("params", String.valueOf(params));
                    return params;
                }

            };
            requestQueue.add(strReq);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sdCard = Environment.getExternalStorageDirectory();
        dir = new File(sdCard.getAbsolutePath() + "/ImageRS");
        dir.mkdirs();
        fileName = String.format("%d.jpg", System.currentTimeMillis());


        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK) {
            if (btTake.isClickable()) {
                try {
                    if (mHighQualityImageUri == null) {
                        GlobalToast.ShowToast(RegisterActivity.this, "Gagal memuat gambar, silahkan coba kembali.");
                    } else {
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mHighQualityImageUri);
                        MediaProcess.bitmapToFile(bmp, outFileKtp.getAbsolutePath(), 30);
                        bmp = MediaProcess.scaledBitmap(outFileKtp.getAbsolutePath());
                        MediaProcess.bitmapToFile(bmp, outFileKtp.getAbsolutePath(), 30);
                        Glide.with(RegisterActivity.this).load(outFileKtp.getAbsolutePath()).into(imgProfile);
                        image = outFileKtp.getAbsolutePath();

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
                        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imgProfile.setImageBitmap(decoded);
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, "/ImageKeuangan");
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    private FileModel generateTimeStampPhotoFileUri() {

        FileModel fileModel = null;
        Uri photoFileUri = null;
        File photoFile = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null) {
            photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = FileProvider.getUriForFile(RegisterActivity.this, "com.aladziviesoft.data", photoFile);
//                    Uri.fromFile(photoFile);

            fileModel = new FileModel(photoFileUri, photoFile);
        }

        return fileModel;
    }

    public void takeImageFromCamera(View view) {
        FileModel fileModel = generateTimeStampPhotoFileUri();
        mHighQualityImageUri = fileModel.getUriPath();
        outFileKtp = fileModel.getFilePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, PICK_IMAGE_REQUEST2);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @OnClick({R.id.btTake, R.id.btExplore, R.id.btRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btTake:
                takeImageFromCamera(view);
                break;
            case R.id.btExplore:
                showFileChooser();
                break;
            case R.id.btRegister:
                SimpanData();
                break;
        }
    }
}
