package com.example.exercise;

import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static DataRepository instance;
    private List<User> userList;
    private List<Room> roomList;

    private DataRepository() {
        userList = new ArrayList<>();
        roomList = new ArrayList<>();
        // Cập nhật tài khoản mặc định: admin / 1
        userList.add(new User("admin", "1", "Chủ trọ Admin"));
        
        // Dữ liệu mẫu Pxxx
        roomList.add(new Room("R01", "P101", 3500000, false, "", ""));
        roomList.add(new Room("R02", "P102", 3500000, true, "Nguyễn Văn A", "0123456789"));
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public List<User> getUserList() { return userList; }
    public List<Room> getRoomList() { return roomList; }
    
    public void addUser(User user) { userList.add(user); }
    
    public User login(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
