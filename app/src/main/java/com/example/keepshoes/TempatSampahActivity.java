package com.example.keepshoes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TempatSampahActivity extends AppCompatActivity
        implements ItemAdapter.ItemClickListener
{
    MenuItem searchItem;
    SearchView searchView;
    RecyclerView rvItems;
    View emptyLayout;
    ItemAdapter itemAdapter;
    List<DataModel> itemModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempat_sampah);

        Toolbar toolbar = findViewById(R.id.custom_toolbar_2);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView tbTitle = toolbar.findViewById(R.id.tv_title);
        tbTitle.setText("Tempat Sampah");

        rvItems = findViewById(R.id.rv_daftar_catatan);
        emptyLayout = findViewById(R.id.daftar_kosong);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        if (searchItem != null) searchItem.collapseActionView();
        getDeletedData("");
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ts, menu);
        searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDeletedData(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedItem(DataModel itemModel) {
        Intent intent = new Intent(this, DetailTSActivity.class);
        intent.putExtra("item_id", itemModel.getId());
        startActivity(intent);
    }

    @Override
    public void deleteItem(DataModel itemModel, int ind) {
        showConfirmDelete(itemModel, ind);
    }

    private void getDeletedData(String searchTerm) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        itemModelList = databaseHandler.getAllRecord(searchTerm, true);

        itemAdapter = new ItemAdapter(itemModelList, this, this);
        rvItems.setAdapter(itemAdapter);

        if (itemAdapter.getItemCount() == 0) {
            rvItems.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            rvItems.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    private void showConfirmDelete(DataModel itemModel, int ind) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin ingin menghapus item ini secara permanen?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!deleteFile(itemModel.getFotoSepatu())) return;
                DatabaseHandler databaseHandler =
                        new DatabaseHandler(TempatSampahActivity.this);
                databaseHandler.deleteModel(itemModel);

                Toast.makeText(TempatSampahActivity.this, "Item berhasil dihapus",
                        Toast.LENGTH_SHORT).show();
                itemModelList.remove(ind);
                Objects.requireNonNull(rvItems.getAdapter()).notifyItemRemoved(ind);

                if (itemAdapter.getItemCount() == 0) {
                    rvItems.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) { itemAdapter.setEventEnabled(true); }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        itemAdapter.setEventEnabled(false);
    }
}