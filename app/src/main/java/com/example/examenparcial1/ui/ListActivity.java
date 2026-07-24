package com.example.examenparcial1.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenparcial1.R;
import com.example.examenparcial1.data.ActivityRecord;
import com.example.examenparcial1.data.ActivityRepository;
import com.example.examenparcial1.ui.adapter.ActivityAdapter;
import com.example.examenparcial1.util.JsonExporter;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity implements ActivityAdapter.OnRecordActionListener {
    private ActivityRepository repository;
    private ActivityAdapter adapter;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        repository = new ActivityRepository(this);
        RecyclerView rv = findViewById(R.id.rvRecords);
        tvTotal = findViewById(R.id.tvTotalGastos);
        Button btnExportarJson = findViewById(R.id.btnExportarJson);

        adapter = new ActivityAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        btnExportarJson.setOnClickListener(v -> exportAndShare());

        if (getIntent().getBooleanExtra("export_direct", false)) {
            exportAndShare();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<ActivityRecord> records = repository.getAll();
        adapter.setRecords(records);
        tvTotal.setText(String.format(Locale.US, "Total gastos: $ %.2f", repository.getTotalExpenses()));
    }

    @Override
    public void onEdit(ActivityRecord record) {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("record_id", record.id);
        startActivity(intent);
    }

    @Override
    public void onDelete(ActivityRecord record) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("Deseas eliminar este registro?")
                .setPositiveButton("Si", (dialog, which) -> {
                    try {
                        repository.delete(record);
                        loadData();
                        Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void exportAndShare() {
        try {
            File file = JsonExporter.export(this, repository.getAll());
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("application/json");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"delcid2812@gmail.com"});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registros Personal de Aseo");
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sendIntent, "Compartir JSON"));
        } catch (Exception e) {
            Toast.makeText(this, "Error exportando JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
