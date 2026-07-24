package com.example.examenparcial1.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenparcial1.R;
import com.example.examenparcial1.data.ActivityRepository;
import com.example.examenparcial1.ui.adapter.ActivityAdapter;

import java.util.Locale;

public class FilterActivity extends AppCompatActivity {
    private ActivityRepository repository;
    private ActivityAdapter adapter;
    private Spinner spCategoriaFiltro;
    private TextView tvTotalFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        repository = new ActivityRepository(this);
        spCategoriaFiltro = findViewById(R.id.spCategoriaFiltro);
        tvTotalFiltro = findViewById(R.id.tvTotalFiltro);
        Button btnAplicarFiltro = findViewById(R.id.btnAplicarFiltro);

        ArrayAdapter<String> categorias = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"comida", "viaticos", "otros"});
        spCategoriaFiltro.setAdapter(categorias);

        RecyclerView rv = findViewById(R.id.rvFiltrados);
        adapter = new ActivityAdapter(new ActivityAdapter.OnRecordActionListener() {
            @Override
            public void onEdit(com.example.examenparcial1.data.ActivityRecord record) {
            }

            @Override
            public void onDelete(com.example.examenparcial1.data.ActivityRecord record) {
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        btnAplicarFiltro.setOnClickListener(v -> applyFilter());
        applyFilter();
    }

    private void applyFilter() {
        String categoria = spCategoriaFiltro.getSelectedItem().toString();
        adapter.setRecords(repository.getByCategory(categoria));
        double totalCat = repository.getTotalByCategory(categoria);
        tvTotalFiltro.setText(String.format(Locale.US, "Total %s: $ %.2f", categoria, totalCat));
    }
}
