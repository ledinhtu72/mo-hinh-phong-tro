package com.example.exercise;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> rooms;
    private OnItemClickListener listener;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public interface OnItemClickListener {
        void onItemClick(Room room, int position);
        void onItemLongClick(Room room, int position);
    }

    public RoomAdapter(List<Room> rooms, OnItemClickListener listener) {
        this.rooms = rooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.tvRoomName.setText("Phòng " + room.getName());
        holder.tvRoomPrice.setText(formatter.format(room.getPrice()) + " đ/tháng");
        
        if (room.isRented()) {
            holder.tvRoomStatus.setText("● Đã thuê");
            holder.tvRoomStatus.setTextColor(Color.parseColor("#B71C1C"));
            holder.tvTenantInfo.setVisibility(View.VISIBLE);
            holder.tvTenantInfo.setText("Người thuê: " + room.getTenantName());
            
            // Set light red/orange background for rented
            int colorIndex = position % 3;
            if (colorIndex == 0) holder.cardRoom.setCardBackgroundColor(Color.parseColor("#F9B0A5"));
            else if (colorIndex == 1) holder.cardRoom.setCardBackgroundColor(Color.parseColor("#E5A582"));
            else holder.cardRoom.setCardBackgroundColor(Color.parseColor("#C8E6C9")); // Greenish rented? Image has one
        } else {
            holder.tvRoomStatus.setText("● Còn trống");
            holder.tvRoomStatus.setTextColor(Color.parseColor("#2E7D32"));
            holder.tvTenantInfo.setVisibility(View.GONE);
            
            // Set light teal/green background for empty
            holder.cardRoom.setCardBackgroundColor(Color.parseColor("#D1E9E9"));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(room, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(room, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvRoomStatus, tvTenantInfo;
        CardView cardRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
            tvTenantInfo = itemView.findViewById(R.id.tvTenantInfo);
            cardRoom = itemView.findViewById(R.id.cardRoom);
        }
    }
}
