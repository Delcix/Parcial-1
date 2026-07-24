package com.example.examenparcial1.util;

import android.content.Context;

import com.example.examenparcial1.data.ActivityRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class JsonExporter {
    public static File export(Context context, List<ActivityRecord> records) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(records);
        File out = new File(context.getFilesDir(), "registros_aseo.json");
        FileWriter writer = new FileWriter(out, false);
        writer.write(json);
        writer.flush();
        writer.close();
        return out;
    }
}
