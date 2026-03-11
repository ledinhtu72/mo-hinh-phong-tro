package com.example.exercise;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class RoomEditEvent {
    public static void showRoomDialog(AppCompatActivity activity, Room room, int position, List<Room> roomList, RoomAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        
        // Tách layout: dialog_room_add cho thêm mới, dialog_room_edit cho chỉnh sửa
        int layoutId = (room == null) ? R.layout.dialog_room_add : R.layout.dialog_room_edit;
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        builder.setView(view);

        EditText etRoomId = view.findViewById(R.id.etRoomId); // Chỉ có trong dialog_room_add
        EditText etRoomName = view.findViewById(R.id.etRoomName);
        EditText etRoomPrice = view.findViewById(R.id.etRoomPrice);
        EditText etTenantName = view.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        RadioGroup rgStatus = view.findViewById(R.id.rgStatus);
        RadioButton rbEmpty = view.findViewById(R.id.rbEmpty);
        RadioButton rbRented = view.findViewById(R.id.rbRented);
        
        View tvTenantLabel = view.findViewById(R.id.tvTenantLabel);
        View tvPhoneLabel = view.findViewById(R.id.tvPhoneLabel);

        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isRented = checkedId == R.id.rbRented;
            etTenantName.setVisibility(isRented ? View.VISIBLE : View.GONE);
            tvTenantLabel.setVisibility(isRented ? View.VISIBLE : View.GONE);
            etPhoneNumber.setVisibility(isRented ? View.VISIBLE : View.GONE);
            tvPhoneLabel.setVisibility(isRented ? View.VISIBLE : View.GONE);
        });

        if (room != null) {
            TextView tvMãPhòngLabel = view.findViewById(R.id.tvMãPhòngLabel);
            tvMãPhòngLabel.setText("Mã phòng: " + room.getId());
            etRoomName.setText(room.getName());
            etRoomPrice.setText(String.valueOf((long)room.getPrice()));
            if (room.isRented()) {
                rbRented.setChecked(true);
            } else {
                rbEmpty.setChecked(true);
            }
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());
            builder.setTitle("SỬA THÔNG TIN PHÒNG");
        } else {
            builder.setTitle("THÊM PHÒNG MỚI");
            rbEmpty.setChecked(true);
            etRoomName.setText("P");
            etRoomName.setSelection(1);
        }

        builder.setPositiveButton(room == null ? "THÊM MỚI" : "LƯU CẬP NHẬT", null);
        builder.setNegativeButton("HỦY", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            String id = room == null ? etRoomId.getText().toString().trim() : room.getId();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();
            boolean isRented = rbRented.isChecked();
            String tenantName = etTenantName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            boolean isValid = true;
            if (room == null) {
                if (TextUtils.isEmpty(id)) { etRoomId.setError("Nhập mã phòng"); isValid = false; }
                else {
                    for (Room r : roomList) {
                        if (r.getId().equalsIgnoreCase(id)) { etRoomId.setError("Mã phòng đã tồn tại"); isValid = false; break; }
                    }
                }
            }
            if (TextUtils.isEmpty(name)) { etRoomName.setError("Nhập tên phòng"); isValid = false; }
            else if (!name.matches("P\\d+")) { etRoomName.setError("Định dạng phải là Pxxx (VD: P101)"); isValid = false; }
            if (TextUtils.isEmpty(priceStr)) { etRoomPrice.setError("Nhập giá thuê"); isValid = false; }
            if (isRented) {
                if (TextUtils.isEmpty(tenantName)) { etTenantName.setError("Nhập tên người thuê"); isValid = false; }
                if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) { etPhoneNumber.setError("Số điện thoại 10 số"); isValid = false; }
            }

            if (isValid) {
                double price = Double.parseDouble(priceStr);
                if (room == null) {
                    RoomAddEvent.execute(id, name, price, isRented, tenantName, phoneNumber, roomList, adapter);
                } else {
                    RoomUpdateEvent.execute(room, name, price, isRented, tenantName, phoneNumber, position, adapter);
                }
                dialog.dismiss();
            }
        });
    }
}
