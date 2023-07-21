package com.example.keepshoes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterVh> {

    public List<DataModel> mItemModelList;
    public Context mContext;
    public ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void selectedItem(DataModel itemModel);
    }

    public ItemAdapter(List<DataModel> itemModelList, Context context, ItemClickListener itemClickListener) {
        this.mItemModelList = itemModelList;
        this.mContext = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        return new ItemAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapterVh holder, int position) {
        DataModel itemModel = mItemModelList.get(position);
        byte[] fotoSepatu = itemModel.getFotoSepatu();
        String namaPemilik = itemModel.getNamaPemilik();
        String merkSepatu = itemModel.getMerkSepatu();
        String warnaSepatu = itemModel.getWarnaSepatu();
        String ukuranSepatu = itemModel.getUkuranSepatu();

        holder.imvFotoSepatu.setImageBitmap(
                BitmapFactory.decodeByteArray(fotoSepatu, 0, fotoSepatu.length));
        holder.tvNamaPemilik.setText(namaPemilik);
        holder.tvInfo.setText(merkSepatu + ", " + warnaSepatu + ", " + ukuranSepatu);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.selectedItem(itemModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemModelList.size();
//        return 0;
    }

    public static class ItemAdapterVh extends RecyclerView.ViewHolder {

        private ShapeableImageView imvFotoSepatu;
        private TextView tvNamaPemilik;
        private TextView tvInfo;

        public ItemAdapterVh(@NonNull View itemView) {
            super(itemView);
            imvFotoSepatu = itemView.findViewById(R.id.imv_foto_sepatu);
            tvNamaPemilik = itemView.findViewById(R.id.tv_nama_pemilik);
            tvInfo = itemView.findViewById(R.id.tv_info);
        }
    }
}
