package com.example.examenparcial1.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "activity_records")
@TypeConverters({Converters.class})
public class ActivityRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String apellido;
    public String cip;
    public String cargo;
    public String edificio;
    public String dia;
    public String hora24;
    public String descripcionActividad;
    public List<String> fotos = new ArrayList<>();
    public String categoriaGasto;
    public double montoGasto;
}
