package pe.edu.idat.proyectoagaco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.idat.proyectoagaco.R;
import pe.edu.idat.proyectoagaco.model.Venta;

public class VentaAdapter
        extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    private ArrayList<Venta> listaVentas;
    private Context context;

    public VentaAdapter(Context context) {
        this.context = context;
        listaVentas = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_venta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Venta venta = listaVentas.get(position);

        holder.tvVentaNombresCliente.setText(venta.getNombreCliente());
        holder.tvVentaProducto.setText(venta.getProducto());
        holder.tvVentaDireccion.setText(venta.getDireccion());
        holder.tvVentaFecha.setText(venta.getFechaVenta());


    }

    public void agregarVenta(ArrayList<Venta> listaVentas) {
        this.listaVentas.addAll(listaVentas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaVentas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvVentaNombresCliente, tvVentaProducto, tvVentaDireccion, tvVentaFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvVentaNombresCliente = itemView.findViewById(R.id.tvVentaNombresCliente);
            tvVentaProducto = itemView.findViewById(R.id.tvVentaProducto);
            tvVentaDireccion = itemView.findViewById(R.id.tvVentaDireccion);
            tvVentaFecha = itemView.findViewById(R.id.tvVentaFecha);

        }
    }

}
