package com.example.birthdayremainder;
public class DataClass {
    String name;
    String birthday;
    String imagePath;
    public DataClass(String name, String birthday, String imagePath) {
        this.name = name;
        this.birthday = birthday;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getImagePath() {
        return imagePath;
    }
}
