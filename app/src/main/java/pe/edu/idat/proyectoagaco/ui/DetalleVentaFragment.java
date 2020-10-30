package pe.edu.idat.proyectoagaco.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.Map;

import pe.edu.idat.proyectoagaco.R;
import pe.edu.idat.proyectoagaco.api.AgacoAPI;


public class DetalleVentaFragment extends Fragment {

    private TextView tvDetNroDocumento, tvDetApePaterno, tvDetApeMaterno, tvDetNombres, tvDetTelefono,
    tvDetDireccion, tvDetDistrito, tvDetProducto, tvDetFechaVenta, tvDetEstadoTransporte,
    tvDetFechaTransporte, tvDetTransportista, tvDetEstadoArmado, tvDetFechaArmado, tvDetEspecialista;
    private CheckBox cbDetTransporte, cbDetArmado;

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

        Bundle bundle = getArguments();
        Integer id = bundle.getInt("id", 0);

        fetchVenta(id);

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
                            tvDetNroDocumento.setText(response.getJSONObject("cliente").getString("nro_documento"));
                            tvDetApePaterno.setText(response.getJSONObject("cliente").getString("ape_paterno"));
                            tvDetApeMaterno.setText(response.getJSONObject("cliente").getString("ape_materno"));
                            tvDetNombres.setText(response.getJSONObject("cliente").getString("nombres"));
                            tvDetTelefono.setText(response.getString("telefono"));
                            tvDetDireccion.setText(response.getString("direccion"));
                            tvDetDistrito.setText(response.getJSONObject("distrito").getString("nombre"));
                            tvDetProducto.setText(response.getJSONObject("producto").getString("nombre"));
                            tvDetFechaVenta.setText(response.getString("fecha_compra"));
                            cbDetTransporte.setChecked(response.getInt("solicita_entrega") == 1);
                            cbDetArmado.setChecked(response.getInt("solicita_armado") == 1);

                            JSONArray servicios = response.getJSONArray("servicios");
                            JSONObject transporte = new JSONObject();
                            JSONObject armado = new JSONObject();

                            if (servicios.length() > 0) {
                                for (int i = 0; i < servicios.length(); i++) {
                                    if (servicios.getJSONObject(i).getJSONObject("tipo_servicio").getInt("id") == 1) {
                                        tvDetEstadoTransporte.setText(response.getString("estado"));
                                        tvDetFechaTransporte.setText(response.getString("fecha_servicio"));
                                        tvDetTransportista.setText(response.getJSONObject("operador").getString("nombre"));
                                    }
                                    else {
                                        tvDetEstadoArmado.setText(response.getString("estado"));
                                        tvDetFechaArmado.setText(response.getString("fecha_servicio"));
                                        tvDetEspecialista.setText(response.getJSONObject("operador").getString("nombre"));
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