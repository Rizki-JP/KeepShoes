package com.example.keepshoes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "keep_shoes";
    private static final String TABLE_CATATAN_SEPATU = "catatan_sepatu";

//   Kolom Tabel (10)
    private static final String KEY_ID = "id";
    private static final String KEY_FOTO_SEPATU = "foto_sepatu";
    private static final String KEY_NAMA_PEMILIK = "nama_pemilik";
    private static final String KEY_NO_TELEPON = "no_telepon";
    private static final String KEY_MERK_SEPATU = "merk_sepatu";
    private static final String KEY_WARNA_SEPATU = "warna_sepatu";
    private static final String KEY_UKURAN_SEPATU = "ukuran_sepatu";
    private static final String KEY_BIAYA = "biaya";
    private static final String KEY_LAMA_PENGERJAAN = "lama_pengerjaan";
    private static final String KEY_CATATAN = "catatan";
    private static final String KEY_DELETED = "deleted";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_create_table =
                "CREATE TABLE " + TABLE_CATATAN_SEPATU + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FOTO_SEPATU + " TEXT,"
                + KEY_NAMA_PEMILIK + " TEXT," + KEY_NO_TELEPON + " TEXT,"
                + KEY_MERK_SEPATU + " TEXT," + KEY_WARNA_SEPATU + " TEXT,"
                + KEY_UKURAN_SEPATU + " TEXT," + KEY_BIAYA + " TEXT,"
                + KEY_LAMA_PENGERJAAN + " TEXT," + KEY_CATATAN + " TEXT,"
                + KEY_DELETED + " INTEGER DEFAULT 0" + ")";

        db.execSQL(sql_create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATATAN_SEPATU);
        onCreate(db);
    }

    public void addRecord(DataModel dataModel) {
        SQLiteDatabase db  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOTO_SEPATU, dataModel.getFotoSepatu());
        values.put(KEY_NAMA_PEMILIK, dataModel.getNamaPemilik());
        values.put(KEY_NO_TELEPON, dataModel.getNomorTelepon());
        values.put(KEY_MERK_SEPATU, dataModel.getMerkSepatu());
        values.put(KEY_WARNA_SEPATU, dataModel.getWarnaSepatu());
        values.put(KEY_UKURAN_SEPATU, dataModel.getUkuranSepatu());
        values.put(KEY_BIAYA, dataModel.getBiaya());
        values.put(KEY_LAMA_PENGERJAAN, dataModel.getLamaPengerjaan());
        values.put(KEY_CATATAN, dataModel.getCatatan());

        db.insert(TABLE_CATATAN_SEPATU, null, values);
        db.close();
    }

    public DataModel getRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATATAN_SEPATU, new String[] {
                KEY_ID, KEY_FOTO_SEPATU, KEY_NAMA_PEMILIK, KEY_NO_TELEPON,
                KEY_MERK_SEPATU, KEY_WARNA_SEPATU, KEY_UKURAN_SEPATU,
                KEY_BIAYA, KEY_LAMA_PENGERJAAN, KEY_CATATAN
                }, KEY_ID + "=?", new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        assert cursor != null;
        DataModel dataModel = new DataModel(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)
        );

        cursor.close();
        db.close();

        return dataModel;
    }

    public List<DataModel> getAllRecord(String searchTerm) {
        return getAllRecord(searchTerm, false);
    }

    public List<DataModel> getAllRecord(String searchTerm, boolean isDeleted) {
        List<DataModel> dataList = new ArrayList<DataModel>();

        // Select Query with the WHERE clause to search for the provided searchTerm
        String selectQuery = "SELECT * FROM " + TABLE_CATATAN_SEPATU + " WHERE " +
                KEY_DELETED + "= ? AND (" +
                KEY_NAMA_PEMILIK + " LIKE ? OR " +
                KEY_MERK_SEPATU + " LIKE ? OR " +
                KEY_WARNA_SEPATU + " LIKE ? OR " +
                KEY_UKURAN_SEPATU + " LIKE ?)";

        searchTerm = "%" + searchTerm + "%";
        String[] selectionArgs = new String[]{
                (isDeleted) ? "1" : "0",
                searchTerm, searchTerm, searchTerm, searchTerm
        };

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                DataModel dataModel = new DataModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
                );

                dataList.add(dataModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public void updateData(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOTO_SEPATU, dataModel.getFotoSepatu());
        values.put(KEY_NAMA_PEMILIK, dataModel.getNamaPemilik());
        values.put(KEY_NO_TELEPON, dataModel.getNomorTelepon());
        values.put(KEY_MERK_SEPATU, dataModel.getMerkSepatu());
        values.put(KEY_WARNA_SEPATU, dataModel.getWarnaSepatu());
        values.put(KEY_UKURAN_SEPATU, dataModel.getUkuranSepatu());
        values.put(KEY_BIAYA, dataModel.getBiaya());
        values.put(KEY_LAMA_PENGERJAAN, dataModel.getLamaPengerjaan());
        values.put(KEY_CATATAN, dataModel.getCatatan());

        // updating row
        db.update(TABLE_CATATAN_SEPATU, values, KEY_ID + " = ?",
                new String[] { String.valueOf(dataModel.getId()) });
        db.close();
    }

    public void setDeletedModel(DataModel dataModel, boolean isDeleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELETED, (isDeleted) ? 1 : 0);
        db.update(TABLE_CATATAN_SEPATU, values, KEY_ID + " = ?",
                new String[] { String.valueOf(dataModel.getId()) });
        db.close();
    }

    public void deleteModel(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATATAN_SEPATU, KEY_ID + " = ?",
                new String[] { String.valueOf(dataModel.getId()) });
        db.close();
    }
}