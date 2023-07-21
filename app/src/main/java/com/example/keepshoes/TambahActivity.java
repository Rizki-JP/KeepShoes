package com.example.keepshoes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TambahActivity extends AppCompatActivity {

    private byte[] fotoSepatu = null;
    private LinearLayout imageEmptyInfo;
    private ImageView imvFotoSepatu;
    private EditText etNamaPemilik;
    private EditText etNomorTelepon;
    private EditText etMerkSepatu;
    private EditText etWarnaSepatu;
    private EditText etUkuranSepatu;
    private Button btnSubmit;

    private DataModel dataModel;

    ActivityResultLauncher<Intent> uploadImgActivity = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    doUploadImg(data);
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
        actionBar.setTitle("Tambah Catatan");

        imageEmptyInfo = findViewById(R.id.imageEmptyInfo);
        imvFotoSepatu = findViewById(R.id.imageViewUpload);
        etNamaPemilik = findViewById(R.id.et_nama_pemilik);
        etNomorTelepon = findViewById(R.id.et_nomor_telepon);
        etMerkSepatu = findViewById(R.id.et_merk_sepatu);
        etWarnaSepatu = findViewById(R.id.et_warna_sepatu);
        etUkuranSepatu = findViewById(R.id.et_ukuran_sepatu);
        btnSubmit = findViewById(R.id.btn_submit);

        setImageView();

        FrameLayout image_frame_upload = findViewById(R.id.image_frame_upload);
        image_frame_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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

    public void inputValidate() {
        String namaPemilik = etNamaPemilik.getText().toString();
        String nomorTelepon = etNomorTelepon.getText().toString();
        String merkSepatu = etMerkSepatu.getText().toString();
        String warnaSepatu = etWarnaSepatu.getText().toString();
        String ukuranSepatu = etUkuranSepatu.getText().toString();

        if (fotoSepatu == null || namaPemilik.isEmpty() || nomorTelepon.isEmpty() ||
                merkSepatu.isEmpty() || warnaSepatu.isEmpty() || ukuranSepatu.isEmpty()
        ) {
            Toast.makeText(this, "Maaf masih ada input yang kosong.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        dataModel = new DataModel(0, null, namaPemilik, nomorTelepon,
                merkSepatu, warnaSepatu, ukuranSepatu);

        new SubmitProcess().execute();
    }

    public byte[] imgUriToArrayByte(Uri imageUri) {
        byte[] imageBytes = null;
        InputStream inputStream = null;

        try {
            inputStream = getContentResolver().openInputStream(imageUri);
            imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imageBytes;
    }

    public void doUploadImg(Intent data) {
        Uri selectedImageUri = data.getData();
        fotoSepatu = imgUriToArrayByte(selectedImageUri);
        setImageView();
    }

    public void setImageView() {
        if (fotoSepatu != null) {
            imageEmptyInfo.setVisibility(View.GONE);
            imvFotoSepatu.setImageBitmap(
                    BitmapFactory.decodeByteArray(fotoSepatu, 0, fotoSepatu.length));
        } else {
            imageEmptyInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SubmitProcess extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {
            btnSubmit.setText("Loading...");
            btnSubmit.setClickable(false);
            return compressImage(fotoSepatu);
        }

        @Override
        protected void onPostExecute(byte[] compressedData) {
            DatabaseHandler databaseHandler = new DatabaseHandler(TambahActivity.this);
            dataModel.setFotoSepatu(compressedData);
            databaseHandler.addRecord(dataModel);
            TambahActivity.this.finish();
        }

        private byte[] compressImage(byte[] image_img) {
            while (image_img.length > 2000000){
                Bitmap bitmap = BitmapFactory.decodeByteArray(image_img, 0, image_img.length);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image_img = stream.toByteArray();
            }
            return image_img;
        }
    }
}