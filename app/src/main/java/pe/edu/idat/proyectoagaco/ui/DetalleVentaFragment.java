package pe.edu.idat.proyectoagaco.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pe.edu.idat.proyectoagaco.R;
import pe.edu.idat.proyectoagaco.api.AgacoAPI;
import pe.edu.idat.proyectoagaco.model.Servicio;
import pe.edu.idat.proyectoagaco.model.Venta;


public class DetalleVentaFragment extends Fragment {

    private TextView tvDetNroDocumento, tvDetApePaterno, tvDetApeMaterno, tvDetNombres, tvDetTelefono,
    tvDetDireccion, tvDetDistrito, tvDetProducto, tvDetFechaVenta, tvDetEstadoTransporte,
    tvDetFechaTransporte, tvDetTransportista, tvDetEstadoArmado, tvDetFechaArmado, tvDetEspecialista;
    private CheckBox cbDetTransporte, cbDetArmado;
    private Button btnAsignarTransportista, btnAsignarEspecialista;

    private Venta venta;
    private Servicio servicioTransporte;
    private Servicio servicioArmado;

    public DetalleVentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_venta, container, false);

        tvDetNroDocumento = view.findViewById(R.id.tvDetNroDocumento);
        tvDetApePaterno = view.findViewById(R.id.tvDetApePaterno);
        tvDetApeMaterno = view.findViewById(R.id.tvDetApeMaterno);
        tvDetNombres = view.findViewById(R.id.tvDetNombres);
        tvDetTelefono = view.findViewById(R.id.tvDetTelefono);
        tvDetDireccion = view.findViewById(R.id.tvDetDireccion);
        tvDetDistrito = view.findViewById(R.id.tvDetDistrito);
        tvDetProducto = view.findViewById(R.id.tvDetProducto);
        tvDetFechaVenta = view.findViewById(R.id.tvDetFechaVenta);

        cbDetTransporte = view.findViewById(R.id.cbDetTransporte);
        cbDetArmado = view.findViewById(R.id.cbDetArmado);

        tvDetEstadoTransporte = view.findViewById(R.id.tvDetEstadoTransporte);
        tvDetFechaTransporte = view.findViewById(R.id.tvDetFechaTransporte);
        tvDetTransportista = view.findViewById(R.id.tvDetTransportista);

        tvDetEstadoArmado = view.findViewById(R.id.tvDetEstadoArmado);
        tvDetFechaArmado = view.findViewById(R.id.tvDetFechaArmado);
        tvDetEspecialista = view.findViewById(R.id.tvDetEspecialista);

        btnAsignarTransportista = view.findViewById(R.id.btnAsignarTransportista);
        btnAsignarEspecialista = view.findViewById(R.id.btnAsignarEspecialista);


        Bundle bundle = getArguments();
        Integer id = bundle.getInt("id", 0);

        fetchVenta(id);

        btnAsignarTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack("");
                AsignacionOperadorFragmen asignacionOperadorFragmen = new AsignacionOperadorFragmen();

                Bundle bundle = new Bundle();
                bundle.putInt("idVenta", venta.getId());
                bundle.putString("tipoServicio", "TRANSPORTE");

                if (servicioTransporte != null) {
                    bundle.putInt("idServicio", servicioTransporte.getId());
                    bundle.putInt("idOperador", servicioTransporte.getIdEspecialista());
                    bundle.putString("nombreOperador", servicioTransporte.getNombreEspecialista());
                    bundle.putString("fecha", servicioTransporte.getFecha());
                }

                asignacionOperadorFragmen.setArguments(bundle);
                transaction.replace(R.id.nav_host_fragment, asignacionOperadorFragmen);
                transaction.commit();
            }
        });

        btnAsignarEspecialista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack("");
                AsignacionOperadorFragmen asignacionOperadorFragmen = new AsignacionOperadorFragmen();

                Bundle bundle = new Bundle();
                bundle.putInt("idVenta", venta.getId());
                bundle.putString("tipoServicio", "ARMADO");

                if (servicioArmado != null) {
                    bundle.putInt("idOperador", servicioArmado.getIdEspecialista());
                    bundle.putString("nombreOperador", servicioArmado.getNombreEspecialista());
                    bundle.putString("fecha", servicioArmado.getFecha());
                }

                asignacionOperadorFragmen.setArguments(bundle);
                transaction.replace(R.id.nav_host_fragment, asignacionOperadorFragmen);
                transaction.commit();
            }
        });

        return view;
    }

    private void fetchVenta(int id) {
        String url = AgacoAPI.getBaseUrl() + "ventas/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            venta = new Venta(
                                    response.getInt("id"),
                                    "",
                                    response.getJSONObject("cliente").getJSONObject("tipo_documento_identidad").getString("abreviatura"),
                                    response.getJSONObject("cliente").getString("nro_documento"),
                                    response.getJSONObject("cliente").getString("ape_paterno"),
                                    response.getJSONObject("cliente").getString("ape_materno"),
                                    response.getJSONObject("cliente").getString("nombres"),
                                    response.getString("telefono"),
                                    response.getJSONObject("producto").getString("nombre"),
                                    response.getString("direccion"),
                                    response.getJSONObject("distrito").getString("nombre"),
                                    response.getString("fecha_compra"),
                                    response.getInt("solicita_entrega") == 1,
                                    response.getInt("solicita_armado") == 1,
                                    new ArrayList<Servicio>()
                            );

                            tvDetNroDocumento.setText(venta.getNroDocumentoIdentidad());
                            tvDetApePaterno.setText(venta.getApePaterno());
                            tvDetApeMaterno.setText(venta.getApeMaterno());
                            tvDetNombres.setText(venta.getNombres());
                            tvDetTelefono.setText(venta.getTelefono());
                            tvDetDireccion.setText(venta.getDireccion());
                            tvDetDistrito.setText(venta.getDistrito());
                            tvDetProducto.setText(venta.getProducto());
                            tvDetFechaVenta.setText(venta.getFechaVenta());
                            cbDetTransporte.setChecked(venta.isSolicitaEntrega());
                            cbDetArmado.setChecked(venta.isSolicitaArmado());

                            JSONArray servicios = response.getJSONArray("servicios");

                            if (servicios.length() > 0) {
                                for (int i = 0; i < servicios.length(); i++) {

                                    JSONObject servicio = servicios.getJSONObject(i);

                                    if (servicio.getJSONObject("tipo_servicio").getInt("id") == 1) {
                                        servicioTransporte = new Servicio(
                                                servicio.getInt("id"),
                                                servicio.getJSONObject("tipo_servicio").getString("nombre"),
                                                servicio.getJSONObject("operador").getInt("id"),
                                                servicio.getJSONObject("operador").getString("nombre"),
                                                servicio.getString("fecha_servicio"),
                                                servicio.getString("estado")
                                        );

                                        venta.getServicios().add(servicioTransporte);

                                        tvDetEstadoTransporte.setText(servicioTransporte.getEstado());
                                        tvDetFechaTransporte.setText(servicioTransporte.getFecha());
                                        tvDetTransportista.setText(servicioTransporte.getNombreEspecialista());
                                    }
                                    else {
                                        servicioArmado = new Servicio(
                                                servicio.getInt("id"),
                                                servicio.getJSONObject("tipo_servicio").getString("nombre"),
                                                servicio.getJSONObject("operador").getInt("id"),
                                                servicio.getJSONObject("operador").getString("nombre"),
                                                servicio.getString("fecha_servicio"),
                                                servicio.getString("estado")
                                        );

                                        venta.getServicios().add(servicioArmado);

                                        tvDetEstadoArmado.setText(servicioArmado.getEstado());
                                        tvDetFechaArmado.setText(servicioArmado.getFecha());
                                        tvDetEspecialista.setText(servicioArmado.getNombreEspecialista());
                                    }
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences preferences = getContext().getSharedPreferences("agacoApp", Context.MODE_PRIVATE);
                String token = preferences.getString("token", "");

                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}