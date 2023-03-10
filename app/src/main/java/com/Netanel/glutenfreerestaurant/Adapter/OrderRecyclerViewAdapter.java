package com.Netanel.glutenfreerestaurant.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Netanel.glutenfreerestaurant.Model.Order;
import com.Netanel.glutenfreerestaurant.Model.UserDB;
import com.Netanel.glutenfreerestaurant.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderViewHolder> {
    private Context context;
    private OrderClickListener listener;
    private ArrayList<Order> allOrders;
    private final String isActive = "Active!";
    private final String isDelivered = "Delivered!";

    public OrderRecyclerViewAdapter(Context context) {
        this.context = context;
        allOrders = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    public OrderRecyclerViewAdapter setOrderClickListener(OrderClickListener orderClickListener) {
        this.listener = orderClickListener;
        return this;
    }

    public void updateOrderTrucking(final ArrayList<Order> allOrders) {
        this.allOrders = allOrders;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order item = getItem(position);
        holder.order_TXT_orderName.setText("Ordered by: "+UserDB.getInstance().getName());
        holder.order_TXT_totalPrice.setText("Total Price: "+item.totalPrice() + "$");
        holder.order_TXT_orderStatus.setText(item.isActive() ? isActive : isDelivered);
        holder.order_BTN_trackOrder.setClickable(item.isActive());
        holder.order_BTN_trackOrder.setText(item.isActive()?"Track Order":"order delivered!");
        holder.order_TXT_orderTime.setText(item.getOrderTime());
        holder.order_TXT_orderStatus.setTextColor(ContextCompat.getColor(context, item.isActive() ? R.color.OrderActive : R.color.OrderInactive));
    }

    private Order getItem(int position) {
        return UserDB.getInstance().getAllOrders().get(position);

    }

    public interface OrderClickListener {
        void changeScreen(int position);

    }

    @Override
    public int getItemCount() {
        return UserDB.getInstance().getAllOrders() == null ? 0 : UserDB.getInstance().getAllOrders().size();
    }


    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView order_TXT_orderName;
        private TextView order_TXT_orderStatus;
        private TextView order_TXT_totalPrice;
        private MaterialButton order_BTN_trackOrder;
        private TextView order_TXT_orderTime;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
            order_BTN_trackOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     if (order_TXT_orderStatus.getText().equals(isActive)) {
                         // check if order is still active. (active = has at least 1 minute of delivery left)
                         // (callback) when done if still active go to new screen
                         int position = getAdapterPosition();
                         listener.changeScreen(position);
                    }
                }
            });
        }


        private void findViews(View itemView) {
            order_TXT_orderName = itemView.findViewById(R.id.order_TXT_orderName);
            order_TXT_orderStatus = itemView.findViewById(R.id.order_TXT_orderStatus);
            order_TXT_totalPrice = itemView.findViewById(R.id.order_TXT_totalPrice);
            order_BTN_trackOrder = itemView.findViewById(R.id.order_BTN_trackOrder);
            order_TXT_orderTime = itemView.findViewById(R.id.order_TXT_orderTime);

        }
    }


}
