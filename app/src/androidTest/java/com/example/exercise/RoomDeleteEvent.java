package com.example.exercise;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.List;

public class RoomDeleteEvent {
    public static void execute(Context context, Room room, int position, List<Room> roomList, RoomAdapter adapter) {
        String message = "Bạn có chắc chắn muốn xóa phòng " + room.getName() + " không?";
        if (room.isRented()) {
            message = "Phòng đang được cho thuê, bạn có chắc muốn xóa không?";
        }

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage(message)
                .setPositiveButton("XÓA", (d, which) -> {
                    if (room.isRented()) {
                        Toast.makeText(context, "Không thể xóa phòng đang cho thuê!", Toast.LENGTH_SHORT).show();
                    } else {
                        roomList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, roomList.size());
                        Toast.makeText(context, "Đã xóa phòng " + room.getName(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("HỦY", null)
                .create();
        dialog.show();
        
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
    }
}
