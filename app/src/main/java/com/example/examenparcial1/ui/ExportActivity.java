package com.example.examenparcial1.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.examenparcial1.MainActivity;
import com.example.examenparcial1.R;
import com.example.examenparcial1.data.ActivityRepository;
import com.example.examenparcial1.util.JsonExporter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Locale;

public class ExportActivity extends AppCompatActivity {
    private ActivityRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        repository = new ActivityRepository(this);
        TextView tvExportTotal = findViewById(R.id.tvExportTotal);
        MaterialButton btnExportShare = findViewById(R.id.btnExportShare);
        View btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnExportShare.setOnClickListener(v -> exportAndShare());

        double total = repository.getTotalExpenses();
        tvExportTotal.setText(String.format(Locale.US, "$ %.2f", total));

        setupBottomNavigation();
    }

    private void exportAndShare() {
        try {
            File file = JsonExporter.export(this, repository.getAll());
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("application/json");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"napoleon.ibarra@utp.ac.pa"});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registros Personal de Aseo");
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sendIntent, "Compartir JSON"));
        } catch (Exception e) {
            Toast.makeText(this, "Error exportando JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_inicio);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            if (id == R.id.nav_registros) {
                startActivity(new Intent(this, ListActivity.class));
                return true;
            }
            if (id == R.id.nav_estadisticas) {
                startActivity(new Intent(this, FilterActivity.class));
                return true;
            }
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            }
            return false;
        });
    }
}
