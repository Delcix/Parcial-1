package com.example.examenparcial1.ui;

import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.examenparcial1.R;
import com.example.examenparcial1.data.ActivityRecord;
import com.example.examenparcial1.data.ActivityRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {
    private EditText etNombre, etApellido, etCip, etCargo, etDia, etHora, etDescripcion, etMonto;
    private AutoCompleteTextView spEdificio, spCategoria;
    private TextView tvFotos;
    private LinearLayout layoutFotoPreview;
    private final List<String> fotos = new ArrayList<>();
    private ActivityRepository repository;
    private int editingId = -1;

    private final ActivityResultLauncher<String> imagePicker = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(), uris -> {
                try {
                    // Se guardan solo 4 URIs como maximo para cumplir el requisito.
                    fotos.clear();
                    if (uris != null) {
                        for (Uri uri : uris) {
                            if (fotos.size() < 4) {
                                fotos.add(uri.toString());
                            }
                        }
                    }
                    tvFotos.setText(getString(R.string.fotos_cargadas, fotos.size()));
                    renderPhotoPreview();
                } catch (Exception e) {
                    Toast.makeText(this, "Error cargando imagenes", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        repository = new ActivityRepository(this);
        bindViews();
        setupSpinners();

        MaterialButton btnHora = findViewById(R.id.btnHora);
        MaterialButton btnCargarFotos = findViewById(R.id.btnCargarFotos);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardar);
        MaterialCardView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        etDia.setOnClickListener(v -> openDatePicker());
        etDia.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                openDatePicker();
            }
        });
        btnHora.setOnClickListener(v -> openTimePicker());
        btnCargarFotos.setOnClickListener(v -> imagePicker.launch("image/*"));
        btnGuardar.setOnClickListener(v -> saveOrUpdate());

        editingId = getIntent().getIntExtra("record_id", -1);
        if (editingId != -1) {
            loadRecord(editingId);
        }
    }

    private void bindViews() {
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCip = findViewById(R.id.etCip);
        etCargo = findViewById(R.id.etCargo);
        etDia = findViewById(R.id.etDia);
        etHora = findViewById(R.id.etHora);
        etDescripcion = findViewById(R.id.etDescripcion);
        etMonto = findViewById(R.id.etMonto);
        spEdificio = findViewById(R.id.spEdificio);
        spCategoria = findViewById(R.id.spCategoria);
        tvFotos = findViewById(R.id.tvFotos);
        layoutFotoPreview = findViewById(R.id.layoutFotoPreview);
    }

    private void setupSpinners() {
        ArrayAdapter<String> edificios = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new String[]{"A", "B", "C", "Otros"});
        spEdificio.setAdapter(edificios);

        ArrayAdapter<String> categorias = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new String[]{"comida", "viaticos", "otros"});
        spCategoria.setAdapter(categorias);
    }

    private void openTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) ->
                etHora.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute)),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                etDia.setText(String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, month + 1, year)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadRecord(int id) {
        ActivityRecord record = repository.getById(id);
        if (record == null) return;

        etNombre.setText(record.nombre);
        etApellido.setText(record.apellido);
        etCip.setText(record.cip);
        etCargo.setText(record.cargo);
        etDia.setText(record.dia);
        etHora.setText(record.hora24);
        etDescripcion.setText(record.descripcionActividad);
        etMonto.setText(String.format(Locale.US, "%.2f", record.montoGasto));
        setSpinnerValue(spEdificio, record.edificio);
        setSpinnerValue(spCategoria, record.categoriaGasto);
        fotos.clear();
        fotos.addAll(record.fotos);
        tvFotos.setText(getString(R.string.fotos_cargadas, fotos.size()));
        renderPhotoPreview();
    }

    private void setSpinnerValue(AutoCompleteTextView spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equalsIgnoreCase(value)) {
                spinner.setText(adapter.getItem(i), false);
                return;
            }
        }
    }

    private void saveOrUpdate() {
        if (!validateForm()) return;
        try {
            // Se reutiliza la misma pantalla para crear y actualizar.
            ActivityRecord record = new ActivityRecord();
            record.id = editingId;
            record.nombre = etNombre.getText().toString().trim();
            record.apellido = etApellido.getText().toString().trim();
            record.cip = etCip.getText().toString().trim();
            record.cargo = etCargo.getText().toString().trim();
            record.edificio = spEdificio.getText().toString().trim();
            record.dia = etDia.getText().toString().trim();
            record.hora24 = etHora.getText().toString().trim();
            record.descripcionActividad = etDescripcion.getText().toString().trim();
            record.fotos = new ArrayList<>(fotos);
            record.categoriaGasto = spCategoria.getText().toString().trim();
            record.montoGasto = Double.parseDouble(etMonto.getText().toString().trim());

            if (editingId == -1) {
                repository.insert(record);
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
            } else {
                repository.update(record);
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar/actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        if (etNombre.getText().toString().trim().isEmpty() ||
                etApellido.getText().toString().trim().isEmpty() ||
                etCip.getText().toString().trim().isEmpty() ||
                etCargo.getText().toString().trim().isEmpty() ||
                spEdificio.getText().toString().trim().isEmpty() ||
                etDia.getText().toString().trim().isEmpty() ||
                etHora.getText().toString().trim().isEmpty() ||
                etDescripcion.getText().toString().trim().isEmpty() ||
                spCategoria.getText().toString().trim().isEmpty() ||
                etMonto.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fotos.size() > 4) {
            Toast.makeText(this, "Solo puedes cargar hasta 4 fotos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void renderPhotoPreview() {
        layoutFotoPreview.removeAllViews();
        for (String foto : fotos) {
            try {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(84), dp(84));
                params.setMarginEnd(dp(10));
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageURI(Uri.parse(foto));
                imageView.setBackgroundResource(R.drawable.bg_soft_circle);
                imageView.setClipToOutline(true);
                layoutFotoPreview.addView(imageView);
            } catch (Exception e) {
                Toast.makeText(this, "Error cargando imagen de vista previa", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
