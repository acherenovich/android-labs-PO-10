package com.example.lab1;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    private static final int CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8; // 1/8 памяти (1024 - LruCache работает с килобайтами)
    private static LruCache<String, Bitmap> cache = new LruCache<>(CACHE_SIZE);

    public static void put(String key, Bitmap bitmap) {
        if (get(key) == null) {
            cache.put(key, bitmap);
        }
    }

    public static Bitmap get(String key) {
        return cache.get(key);
    }
}
