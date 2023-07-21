package me.baraban4ik.ecosalary.utils;

import java.util.HashMap;

public class Data {
    private static final HashMap<String, Long> data = new HashMap();

    public static HashMap<String, Long> getData() {
        return data;
    }
    public static void setData(String key, Long value) {
        data.put(key, value);
    }

}
