package com.example.examenparcial1.data;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public String fromList(List<String> fotos) {
        return fotos == null ? "" : TextUtils.join("|", fotos);
    }

    @TypeConverter
    public List<String> toList(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(value.split("\\|")));
    }
}
