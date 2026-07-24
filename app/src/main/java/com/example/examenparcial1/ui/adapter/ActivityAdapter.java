package com.example.examenparcial1.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenparcial1.R;
import com.example.examenparcial1.data.ActivityRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.RecordViewHolder> {
    public interface OnRecordActionListener {
        void onEdit(ActivityRecord record);

        void onDelete(ActivityRecord record);
    }

    private final List<ActivityRecord> records = new ArrayList<>();
    private final OnRecordActionListener listener;

    public ActivityAdapter(OnRecordActionListener listener) {
        this.listener = listener;
    }

    public void setRecords(List<ActivityRecord> newRecords) {
        records.clear();
        records.addAll(newRecords);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        ActivityRecord record = records.get(position);
        holder.tvNombre.setText(record.nombre + " " + record.apellido);
        holder.tvDetalle.setText(record.cargo + " - " + record.edificio + " - " + record.categoriaGasto);
        holder.tvMonto.setText(String.format(Locale.US, "$ %.2f", record.montoGasto));
        holder.btnEditar.setOnClickListener(v -> listener.onEdit(record));
        holder.btnEliminar.setOnClickListener(v -> listener.onDelete(record));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDetalle, tvMonto;
        Button btnEditar, btnEliminar;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreItem);
            tvDetalle = itemView.findViewById(R.id.tvDetalleItem);
            tvMonto = itemView.findViewById(R.id.tvMontoItem);
            btnEditar = itemView.findViewById(R.id.btnEditarItem);
            btnEliminar = itemView.findViewById(R.id.btnEliminarItem);
        }
    }
}
