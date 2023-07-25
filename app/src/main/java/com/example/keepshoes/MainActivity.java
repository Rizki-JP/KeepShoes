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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemClickListener {
    MenuItem searchItem;
    SearchView searchView;
    RecyclerView rvItems;
    View emptyLayout;
    ItemAdapter itemAdapter;
    List<DataModel> itemModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);

        rvItems = findViewById(R.id.rv_daftar_catatan);
        emptyLayout = findViewById(R.id.daftar_kosong);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(linearLayoutManager);

        FloatingActionButton fab_tambah_catatan = findViewById(R.id.fab_tambah_catatan);
        fab_tambah_catatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        if (searchItem != null) searchItem.collapseActionView();
        getData("");
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) searchItem.getActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                setItemsVisibility(menu, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                setItemsVisibility(menu, true);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_tempat_sampah) {
            Intent intent = new Intent(this, TempatSampahActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.app_bar_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedItem(DataModel itemModel) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("item_id", itemModel.getId());
        startActivity(intent);
    }

    @Override
    public void deleteItem(DataModel itemModel, int ind) {
        showConfirmDelete(itemModel, ind);
    }

    private void setItemsVisibility(Menu menu, boolean visible) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setVisible(visible);
        }
    }

    private void getData(String searchTerm) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        itemModelList = databaseHandler.getAllRecord(searchTerm);

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
        builder.setMessage("Apakah anda yakin ingin membuang item ini ke tempat sampah?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
                databaseHandler.setDeletedModel(itemModel, true);

                Toast.makeText(MainActivity.this, "Item berhasil dibuang",
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