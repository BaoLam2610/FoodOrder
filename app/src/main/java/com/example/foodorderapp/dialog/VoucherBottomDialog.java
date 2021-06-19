package com.example.foodorderapp.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.VoucherAdapter;
import com.example.foodorderapp.databinding.DialogVoucherBinding;
import com.example.foodorderapp.event.IOnClickItemVoucher;
import com.example.foodorderapp.model.Voucher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class VoucherBottomDialog extends BottomSheetDialogFragment {
    List<Voucher> voucherList;
    DialogVoucherBinding binding;
    VoucherAdapter adapter;
    IOnClickItemVoucher iOnClick;

    public VoucherBottomDialog(List<Voucher> voucherList, IOnClickItemVoucher iOnClick) {
        this.voucherList = voucherList;
        this.iOnClick = iOnClick;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_voucher, null, false);
//        View view = layoutInflater.inflate(R.layout.layout_bottom_sheet,null);
        bottomSheetDialog.setContentView(binding.getRoot());
        adapter = new VoucherAdapter(voucherList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.rvVoucher.setAdapter(adapter);
        binding.rvVoucher.setLayoutManager(layoutManager);
        adapter.setIOnClick(new IOnClickItemVoucher() {
            @Override
            public void onClickItem(Voucher voucher) {
                iOnClick.onClickItem(voucher);
            }
        });


        return bottomSheetDialog;
    }
    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }


}
