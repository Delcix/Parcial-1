package com.example.examenparcial1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.examenparcial1.data.ActivityRepository;
import com.example.examenparcial1.ui.AboutActivity;
import com.example.examenparcial1.ui.ExportActivity;
import com.example.examenparcial1.ui.FilterActivity;
import com.example.examenparcial1.ui.FormActivity;
import com.example.examenparcial1.ui.ListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityRepository repository;
    private TextView tvMainTotal;
    private TextView tvComidaMonto;
    private TextView tvViaticosMonto;
    private TextView tvOtrosMonto;
    private LinearProgressIndicator progressComida;
    private LinearProgressIndicator progressViaticos;
    private LinearProgressIndicator progressOtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = new ActivityRepository(this);

        bindSummaryViews();

        View btnRegistrar = findViewById(R.id.btnRegistrar);
        View btnVerRegistros = findViewById(R.id.btnVerRegistros);
        View btnFiltrar = findViewById(R.id.btnFiltrar);
        View btnExportar = findViewById(R.id.btnExportar);
        View btnAcerca = findViewById(R.id.btnAcerca);

        btnRegistrar.setOnClickListener(v -> startActivity(new Intent(this, FormActivity.class)));
        btnVerRegistros.setOnClickListener(v -> startActivity(new Intent(this, ListActivity.class)));
        btnFiltrar.setOnClickListener(v -> startActivity(new Intent(this, FilterActivity.class)));
        btnExportar.setOnClickListener(v -> startActivity(new Intent(this, ExportActivity.class)));
        btnAcerca.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardSummary();
    }

    private void bindSummaryViews() {
        tvMainTotal = findViewById(R.id.tvMainTotal);
        tvComidaMonto = findViewById(R.id.tvComidaMonto);
        tvViaticosMonto = findViewById(R.id.tvViaticosMonto);
        tvOtrosMonto = findViewById(R.id.tvOtrosMonto);
        progressComida = findViewById(R.id.progressComida);
        progressViaticos = findViewById(R.id.progressViaticos);
        progressOtros = findViewById(R.id.progressOtros);
    }

    private void loadDashboardSummary() {
        double total = repository.getTotalExpenses();
        double comida = repository.getTotalByCategory("comida");
        double viaticos = repository.getTotalByCategory("viaticos");
        double otros = repository.getTotalByCategory("otros");

        tvMainTotal.setText(String.format(Locale.US, "$ %.2f", total));

        int pComida = total > 0 ? (int) Math.round((comida * 100d) / total) : 0;
        int pViaticos = total > 0 ? (int) Math.round((viaticos * 100d) / total) : 0;
        int pOtros = total > 0 ? (int) Math.round((otros * 100d) / total) : 0;

        tvComidaMonto.setText(String.format(Locale.US, "$ %.2f (%d%%)", comida, pComida));
        tvViaticosMonto.setText(String.format(Locale.US, "$ %.2f (%d%%)", viaticos, pViaticos));
        tvOtrosMonto.setText(String.format(Locale.US, "$ %.2f (%d%%)", otros, pOtros));

        progressComida.setProgressCompat(pComida, true);
        progressViaticos.setProgressCompat(pViaticos, true);
        progressOtros.setProgressCompat(pOtros, true);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_inicio);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
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
                Toast.makeText(this, "Seccion perfil/acerca", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            }
            return false;
        });
    }
}
