package com.example.exercise;

public class RoomUpdateEvent {
    public static void execute(Room room, String name, double price, boolean isRented, String tenantName, String phoneNumber, int position, RoomAdapter adapter) {
        room.setName(name);
        room.setPrice(price);
        room.setRented(isRented);
        room.setTenantName(isRented ? tenantName : "");
        room.setPhoneNumber(isRented ? phoneNumber : "");
        adapter.notifyItemChanged(position);
    }
}
