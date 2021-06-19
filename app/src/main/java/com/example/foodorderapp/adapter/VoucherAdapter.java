package com.example.foodorderapp.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.DialogVoucherBinding;
import com.example.foodorderapp.databinding.ItemVoucherBinding;
import com.example.foodorderapp.event.IOnClickItemVoucher;
import com.example.foodorderapp.helper.ConvertAvatarHelper;
import com.example.foodorderapp.model.Voucher;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    List<Voucher> voucherList;
    IOnClickItemVoucher iOnClick;

    public VoucherAdapter(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }

    public void setIOnClick(IOnClickItemVoucher iOnClick) {
        this.iOnClick = iOnClick;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemVoucherBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_voucher, parent, false);
        VoucherViewHolder viewHolder = new VoucherViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);

        holder.binding.ivImageVoucher.setImageResource(voucher.getImage());
        holder.binding.tvName.setText(voucher.getName());
        holder.itemView.setOnClickListener(v-> iOnClick.onClickItem(voucher));
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        ItemVoucherBinding binding;

        public VoucherViewHolder(@NonNull ItemVoucherBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
