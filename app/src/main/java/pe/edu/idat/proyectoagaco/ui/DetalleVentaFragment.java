package pe.edu.idat.proyectoagaco.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pe.edu.idat.proyectoagaco.R;


public class DetalleVentaFragment extends Fragment {

    public DetalleVentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_venta, container, false);

        Bundle bundle = getArguments();
        String nombre = bundle.getString("nombre", "");
        Toast.makeText(getContext(), "Nombre: " + nombre, Toast.LENGTH_SHORT).show();

        return view;
    }
}