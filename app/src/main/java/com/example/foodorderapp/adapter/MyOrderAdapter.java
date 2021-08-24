package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemMyOrderBinding;
import com.example.foodorderapp.event.ISeeDetailCart;
import com.example.foodorderapp.event.IVoucher;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.VoucherPresenter;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> implements IVoucher {

    List<Cart> cartList;
    List<Food> foodList;
    Context context;
    VoucherPresenter presenter;
    CartDatabasePresenter cartPresenter;
    MyOrderListFoodAdapter listFoodAdapter;
    ISeeDetailCart iSeeDetailCart;

    public MyOrderAdapter(List<Cart> cartList, List<Food> foodList, Context context) {
        this.cartList = cartList;
        this.foodList = foodList;
        this.context = context;
        notifyDataSetChanged();
    }

    public MyOrderAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setISeeDetailCart(ISeeDetailCart iSeeDetailCart) {
        this.iSeeDetailCart = iSeeDetailCart;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyOrderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_my_order, parent, false);

        MyOrderViewHolder viewHolder = new MyOrderViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        presenter = new VoucherPresenter(this, context);
        Glide.with(context).load(cart.getRestaurant().getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivResImage);
        holder.binding.tvIdCart.setText("ID - " + cart.getIdCart());
        holder.binding.tvResName.setText(cart.getRestaurant().getName());
        holder.binding.tvResAddress.setText(cart.getRestaurant().getAddress().getAddress());

        holder.binding.tvResRate.setText(cart.getRestaurant().getRate()+"");
        holder.binding.rbResRate.setRating((float) cart.getRestaurant().getRate());

        holder.binding.tvDateOrder.setText(cart.getDate());
        holder.binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
        presenter.showVoucher(holder, cart);


        List<Food> foodList = cart.getRestaurant().getFoodList();
        listFoodAdapter = new MyOrderListFoodAdapter(foodList, cart, context, "my_order");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.binding.rvFoodOrder.setAdapter(listFoodAdapter);
        holder.binding.rvFoodOrder.setLayoutManager(layoutManager);

        holder.binding.tvSeeDetail.setOnClickListener(v->iSeeDetailCart.onShowDetailCart(cart));
    }

    @Override
    public int getItemCount() {
        return cartList.isEmpty() ? 0 : cartList.size();
    }

    @Override
    public void onShowListVoucher(List<Voucher> voucherList, Cart cart) {
        // not thing
    }

    @Override
    public void onShowVoucherOrder(MyOrderViewHolder holder, Voucher voucher, Cart cart) {
        long price = cart.getTotalPrice();
        String titleDiscount = context.getString(R.string.cart_discount);

        long discount = (long) ((voucher.getDiscount() / 100.0f) * price);
        holder.binding.tvTitleDiscount.setText(titleDiscount + "(" + voucher.getDiscount() + "%)");
        holder.binding.tvDiscount.setText("-" + FormatHelper.formatPrice(discount));

        long fee = cart.getDeliveryFee();
        if (fee > 0)
            holder.binding.tvDeliveryFee.setText("+" + FormatHelper.formatPrice(fee));
        holder.binding.tvTotalPrice.setText(FormatHelper.formatPrice((long) (price - discount + fee)));
    }

    @Override
    public void onEmptyVoucherOrder(MyOrderAdapter.MyOrderViewHolder holder, Cart cart) {
        long price = cart.getTotalPrice();
        long fee = cart.getDeliveryFee();
        if (fee > 0)
            holder.binding.tvDeliveryFee.setText("+" + FormatHelper.formatPrice(fee));
        holder.binding.tvTotalPrice.setText(FormatHelper.formatPrice((long) (price + fee)));
    }


    public class MyOrderViewHolder extends RecyclerView.ViewHolder {
        ItemMyOrderBinding binding;

        public MyOrderViewHolder(@NonNull ItemMyOrderBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
