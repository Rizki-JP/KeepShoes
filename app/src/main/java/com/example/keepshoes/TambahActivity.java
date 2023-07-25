package com.example.keepshoes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TambahActivity extends AppCompatActivity {

    private Uri uriFotoSepatu;
    private LinearLayout imageEmptyInfo;
    private ImageView imvFotoSepatu;
    private EditText etNamaPemilik;
    private EditText etNomorTelepon;
    private EditText etMerkSepatu;
    private EditText etWarnaSepatu;
    private EditText etUkuranSepatu;
    private EditText etBiaya;
    private EditText etLamaPengerjaan;
    private EditText etCatatan;

    ActivityResultLauncher<Intent> uploadImgActivity = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    Uri imageUri = result.getData().getData();
                    setImageView(imageUri);
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        Toolbar toolbar = findViewById(R.id.custom_toolbar_2);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView tbTitle = toolbar.findViewById(R.id.tv_title);
        tbTitle.setText("Tambah Catatan");

        imageEmptyInfo = findViewById(R.id.imageEmptyInfo);
        imvFotoSepatu = findViewById(R.id.imageViewUpload);
        etNamaPemilik = findViewById(R.id.et_nama_pemilik);
        etNomorTelepon = findViewById(R.id.et_nomor_telepon);
        etMerkSepatu = findViewById(R.id.et_merk_sepatu);
        etWarnaSepatu = findViewById(R.id.et_warna_sepatu);
        etUkuranSepatu = findViewById(R.id.et_ukuran_sepatu);
        etBiaya = findViewById(R.id.et_biaya);
        etLamaPengerjaan = findViewById(R.id.et_lama_pengerjaan);
        etCatatan = findViewById(R.id.et_catatan);
        Button btnSubmit = findViewById(R.id.btn_submit);

        setImageView(uriFotoSepatu);

        FrameLayout image_frame_upload = findViewById(R.id.image_frame_upload);
        image_frame_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                uploadImgActivity.launch(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { inputValidate(); }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImageView(Uri imageUri) {
        if (imageUri != null) {
            imageEmptyInfo.setVisibility(View.GONE);
            Glide.with(this).load(imageUri).into(imvFotoSepatu);
            uriFotoSepatu = imageUri;
        } else {
            imageEmptyInfo.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getBitmapFromUri(Context context, Uri imageUri) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(imageUri);
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return bmp;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        String imageName = "IMG_" + System.currentTimeMillis() + "." + getFileExtension(imageUri);
        Bitmap originalBitmap = getBitmapFromUri(this, imageUri);

        int compressionQuality = 50; // Set the desired compression quality (0-100)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, outputStream);
        byte[] compressedData = outputStream.toByteArray();

        File storageDir = getFilesDir();
        File imageFile = new File(storageDir, imageName);
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
        fileOutputStream.write(compressedData);
        fileOutputStream.close();

        return imageName;
    }

    private String uploadImage(Uri imageUri) {
        if (imageUri == null) return "";
        try {
            return saveImageToInternalStorage(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void inputValidate() {
        String fotoSepatu = uploadImage(uriFotoSepatu);
        String namaPemilik = etNamaPemilik.getText().toString().trim();
        String nomorTelepon = etNomorTelepon.getText().toString().trim();
        String merkSepatu = etMerkSepatu.getText().toString().trim();
        String warnaSepatu = etWarnaSepatu.getText().toString().trim();
        String ukuranSepatu = etUkuranSepatu.getText().toString().trim();
        String biaya = etBiaya.getText().toString().trim();
        String lamaPengerjaan = etLamaPengerjaan.getText().toString().trim();
        String catatan = etCatatan.getText().toString().trim();

        if (fotoSepatu.isEmpty()) {
            Toast.makeText(this, "Maaf gambar masih belum dipilih",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (namaPemilik.isEmpty() || nomorTelepon.isEmpty() || merkSepatu.isEmpty() ||
            warnaSepatu.isEmpty() || ukuranSepatu.isEmpty() || biaya.isEmpty() ||
            lamaPengerjaan.isEmpty()
        ) {
            Toast.makeText(this, "Maaf masih ada input yang kosong",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DataModel dataModel = new DataModel(0, fotoSepatu, namaPemilik, nomorTelepon,
                merkSepatu, warnaSepatu, ukuranSepatu, biaya, lamaPengerjaan, catatan);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.addRecord(dataModel);

        Toast.makeText(this, "Item berhasil ditambahkan",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}