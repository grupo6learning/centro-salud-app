package com.example.proyectoufc.adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Medicamento;

import com.loopj.android.http.Base64;
import java.util.List;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    private List<Medicamento> listaMedicamentos;

    public MedicamentoAdapter(List<Medicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    @NonNull
    @Override
    public MedicamentoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoAdapter.ViewHolder holder, int position) {
        Medicamento medicamento = listaMedicamentos.get(position);
        holder.LblNombre.setText(medicamento.getNombre_medicamento());
        holder.LblForma.setText(medicamento.getForma_farmaceutica());
        holder.LblConcentracion.setText(medicamento.getConcentracion());
        holder.LblPrecio.setText(String.format("%.2f", medicamento.getPrecio()));

    }

    @Override
    public int getItemCount() {
        return listaMedicamentos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView carItemMedicamento;
        TextView LblNombre, LblForma, LblConcentracion, LblPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //carItemMedicamento = itemView.findViewById(R.id.itmCarItemMedicamento);
            LblNombre = itemView.findViewById(R.id.itmLblNombreM);
            LblForma = itemView.findViewById(R.id.itmLblFormaM);
            LblConcentracion = itemView.findViewById(R.id.itmLblConcentracionM);
            LblPrecio = itemView.findViewById(R.id.itmLblPrecioM);

        }
    }
}