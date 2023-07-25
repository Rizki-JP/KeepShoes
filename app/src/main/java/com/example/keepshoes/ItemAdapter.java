package com.example.keepshoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterVh> {
    private boolean mEventEnabled = true;
    public List<DataModel> mItemModelList;
    public Context mContext;
    public ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void selectedItem(DataModel itemModel);
        void deleteItem(DataModel itemModel, int ind);
    }

    public ItemAdapter(List<DataModel> itemModelList, Context context,
                       ItemClickListener itemClickListener)
    {
        this.mItemModelList = itemModelList;
        this.mContext = context;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_item,
                parent, false);
        return new ItemAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapterVh holder, int position) {
        DataModel itemModel = mItemModelList.get(position);
        String fotoSepatu = itemModel.getFotoSepatu();
        String namaPemilik = itemModel.getNamaPemilik();
        String merkSepatu = itemModel.getMerkSepatu();
        String warnaSepatu = itemModel.getWarnaSepatu();
        String ukuranSepatu = itemModel.getUkuranSepatu();
        File fotoSepatuFile = new File(mContext.getFilesDir(), fotoSepatu);
        String info = merkSepatu + ", " + warnaSepatu + ", " + ukuranSepatu;

        Glide.with(mContext).load(fotoSepatuFile)
                .circleCrop().into(holder.imvFotoSepatu);
        holder.tvNamaPemilik.setText(namaPemilik);
        holder.tvInfo.setText(info);

        ImageButton delButton = holder.itemView.findViewById(R.id.btn_del_item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventEnabled) mItemClickListener.selectedItem(itemModel);
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventEnabled)
                    mItemClickListener.deleteItem(itemModel, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemModelList.size();
    }

    public void setEventEnabled(boolean val) { mEventEnabled = val; }

    public static class ItemAdapterVh extends RecyclerView.ViewHolder {

        private ImageView imvFotoSepatu;
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
