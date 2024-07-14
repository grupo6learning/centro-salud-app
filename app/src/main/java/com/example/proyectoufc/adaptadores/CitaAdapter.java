package com.example.proyectoufc.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Citas;

import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.ViewHolder> {
    private List<Citas> listaCitas;

    public CitaAdapter(List<Citas> listaCitas) {
        this.listaCitas = listaCitas;
    }

    @NonNull
    @Override
    public CitaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaAdapter.ViewHolder holder, int position) {
        Citas citas=listaCitas.get(position);
        holder.lblPaciente.setText(citas.getPaciente());
        holder.lblCorreo.setText(citas.getCorreo());
        holder.lblDoctor.setText(citas.getDoctor());
        holder.lblEspecialidad.setText(citas.getEspecialidad());
        holder.lblFecha.setText(citas.getFecha().toString());
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView carItemCita;
        TextView lblDoctor,lblEspecialidad, lblFecha,lblPaciente,lblCorreo;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            //PROBLEMAS
            //carItemCita=itemView.findViewById(R.id.itmCarCita);
            lblPaciente=itemView.findViewById(R.id.cLblPaciente);
            lblCorreo=itemView.findViewById(R.id.cLblCorreo);
            lblDoctor=itemView.findViewById(R.id.cLblDoctor);
            lblEspecialidad=itemView.findViewById(R.id.cLblEspecialidad);
            lblFecha=itemView.findViewById(R.id.cLblFecha);
        }
    }
}
