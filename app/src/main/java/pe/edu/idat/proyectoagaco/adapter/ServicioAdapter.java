package pe.edu.idat.proyectoagaco.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.idat.proyectoagaco.R;
import pe.edu.idat.proyectoagaco.model.Servicio;

public class ServicioAdapter
        extends RecyclerView.Adapter<ServicioAdapter.ViewHolder> {

    private ArrayList<Servicio> listaServicios;
    private Context context;

    public ServicioAdapter(Context context) {
        this.context = context;
        listaServicios = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Servicio servicio = listaServicios.get(position);

        holder.tvDetEstadoServicio.setText(servicio.getNombreEspecialista());
        holder.tvDetFechaServicio.setText(servicio.getFecha());
        holder.tvDetEspecialista.setText(servicio.getNombreEspecialista());
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }

    public void agregarServicio(ArrayList<Servicio> listaServicios) {
        this.listaServicios.addAll(listaServicios);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDetEstadoServicio, tvDetFechaServicio, tvDetEspecialista;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*tvDetEstadoServicio = itemView.findViewById(R.id.tvDetEstadoServicio);
            tvDetFechaServicio = itemView.findViewById(R.id.tvDetFechaServicio);
            tvDetEspecialista = itemView.findViewById(R.id.tvDetEspecialista);*/
        }
    }
}
