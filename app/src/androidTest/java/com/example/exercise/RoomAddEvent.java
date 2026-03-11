package com.example.exercise;

import java.util.List;

public class RoomAddEvent {
    public static void execute(String id, String name, double price, boolean isRented, String tenantName, String phoneNumber, List<Room> roomList, RoomAdapter adapter) {
        Room newRoom = new Room(id, name, price, isRented, tenantName, phoneNumber);
        roomList.add(newRoom);
        adapter.notifyItemInserted(roomList.size() - 1);
    }
}
