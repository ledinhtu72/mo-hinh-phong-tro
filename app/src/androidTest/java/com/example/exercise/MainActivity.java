package com.example.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener {

    private RecyclerView rvRooms;
    private FloatingActionButton fabAdd;
    private RoomAdapter adapter;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("QUẢN LÝ NHÀ TRỌ");
        }

        initViews();
        initData();
        setupRecyclerView();

        fabAdd.setOnClickListener(v -> showRoomDialog(null, -1));
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initData() {
        roomList = DataRepository.getInstance().getRoomList();
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(roomList, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Đăng xuất");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRoomDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_room, null);
        builder.setView(view);

        TextView tvMãPhòngLabel = view.findViewById(R.id.tvMãPhòngLabel);
        EditText etRoomId = view.findViewById(R.id.etRoomId);
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
            tvMãPhòngLabel.setText("Mã phòng: " + room.getId());
            etRoomId.setVisibility(View.GONE);
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
            
            // Validate Mã phòng (Unique when adding)
            if (room == null) {
                if (TextUtils.isEmpty(id)) {
                    etRoomId.setError("Nhập mã phòng");
                    isValid = false;
                } else {
                    for (Room r : roomList) {
                        if (r.getId().equalsIgnoreCase(id)) {
                            etRoomId.setError("Mã phòng đã tồn tại");
                            isValid = false;
                            break;
                        }
                    }
                }
            }

            // Validate Tên phòng (Pxxx and Unique)
            if (TextUtils.isEmpty(name)) {
                etRoomName.setError("Nhập tên phòng");
                isValid = false;
            } else if (!name.matches("P\\d+")) {
                etRoomName.setError("Định dạng phải là Pxxx (VD: P101)");
                isValid = false;
            } else {
                for (int i = 0; i < roomList.size(); i++) {
                    if (i == position) continue;
                    if (roomList.get(i).getName().equalsIgnoreCase(name)) {
                        etRoomName.setError("Tên phòng đã tồn tại");
                        isValid = false;
                        break;
                    }
                }
            }

            if (TextUtils.isEmpty(priceStr)) {
                etRoomPrice.setError("Nhập giá thuê");
                isValid = false;
            }

            if (isRented) {
                if (TextUtils.isEmpty(tenantName)) {
                    etTenantName.setError("Nhập tên người thuê");
                    isValid = false;
                }
                if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
                    etPhoneNumber.setError("Số điện thoại 10 số");
                    isValid = false;
                }
            }

            if (isValid) {
                double price = Double.parseDouble(priceStr);
                if (room == null) {
                    Room newRoom = new Room(id, name, price, isRented, tenantName, phoneNumber);
                    roomList.add(newRoom);
                    adapter.notifyItemInserted(roomList.size() - 1);
                } else {
                    room.setName(name);
                    room.setPrice(price);
                    room.setRented(isRented);
                    room.setTenantName(isRented ? tenantName : "");
                    room.setPhoneNumber(isRented ? phoneNumber : "");
                    adapter.notifyItemChanged(position);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(Room room, int position) {
        showRoomDialog(room, position);
    }

    @Override
    public void onItemLongClick(Room room, int position) {
        String message = "Bạn có chắc chắn muốn xóa phòng " + room.getName() + " không?";
        if (room.isRented()) {
            message = "Phòng đang được cho thuê, bạn có chắc muốn xóa không?";
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage(message)
                .setPositiveButton("XÓA", (d, which) -> {
                    if (room.isRented()) {
                        Toast.makeText(this, "Không thể xóa phòng đang cho thuê!", Toast.LENGTH_SHORT).show();
                    } else {
                        roomList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, roomList.size());
                        Toast.makeText(this, "Đã xóa phòng " + room.getName(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("HỦY", null)
                .create();
        dialog.show();
        
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
    }
}
