package com.example.keepshoes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class DetailTSActivity extends AppCompatActivity {
    private DataModel mDataModel;
    private ImageView imvFotoSepatu;
    private EditText etNamaPemilik;
    private EditText etNomorTelepon;
    private EditText etMerkSepatu;
    private EditText etWarnaSepatu;
    private EditText etUkuranSepatu;
    private EditText etBiaya;
    private EditText etLamaPengerjaan;
    private EditText etCatatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ts);

        Toolbar toolbar = findViewById(R.id.custom_toolbar_2);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView tbTitle = toolbar.findViewById(R.id.tv_title);
        tbTitle.setText("Detail Catatan");

        imvFotoSepatu = findViewById(R.id.imageViewUpload);
        etNamaPemilik = findViewById(R.id.et_nama_pemilik);
        etNomorTelepon = findViewById(R.id.et_nomor_telepon);
        etMerkSepatu = findViewById(R.id.et_merk_sepatu);
        etWarnaSepatu = findViewById(R.id.et_warna_sepatu);
        etUkuranSepatu = findViewById(R.id.et_ukuran_sepatu);
        etBiaya = findViewById(R.id.et_biaya);
        etLamaPengerjaan = findViewById(R.id.et_lama_pengerjaan);
        etCatatan = findViewById(R.id.et_catatan);
        Button btnPulihkan = findViewById(R.id.btn_pulihkan);

        initData();

        btnPulihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { recoverModel(); }
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

    private void initData() {
        int itemId = getIntent().getExtras().getInt("item_id");
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        mDataModel = databaseHandler.getRecord(itemId);

        etNamaPemilik.setText(mDataModel.getNamaPemilik());
        etNomorTelepon.setText(mDataModel.getNomorTelepon());
        etMerkSepatu.setText(mDataModel.getMerkSepatu());
        etWarnaSepatu.setText(mDataModel.getWarnaSepatu());
        etUkuranSepatu.setText(mDataModel.getUkuranSepatu());
        etBiaya.setText(mDataModel.getBiaya());
        etLamaPengerjaan.setText(mDataModel.getLamaPengerjaan());

        String catatan = mDataModel.getCatatan();
        etCatatan.setText((catatan.isEmpty()) ? "-" : catatan);

        File fileFotoSepatu = new File(getFilesDir(), mDataModel.getFotoSepatu());
        setImageView(Uri.fromFile(fileFotoSepatu));
    }

    private void setImageView(Uri imageUri) {
        if (imageUri != null) Glide.with(this).load(imageUri).into(imvFotoSepatu);
    }

    private void recoverModel() {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.setDeletedModel(mDataModel, false);
        Toast.makeText(this, "Item berhasil dipulihkan", Toast.LENGTH_SHORT).show();
        finish();
    }
}