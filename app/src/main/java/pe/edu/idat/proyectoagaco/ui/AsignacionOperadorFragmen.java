package pe.edu.idat.proyectoagaco.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pe.edu.idat.proyectoagaco.R;
import pe.edu.idat.proyectoagaco.api.AgacoAPI;

public class AsignacionOperadorFragmen extends Fragment
        implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private TextView tvTituloAsignacionOperador, tvOperador, tvFechaOperacion;
    private Spinner spOperador;
    private Button btnFechaOperacion, btnAsignarOperador, btnCancelarAsignacion;

    private Integer idVenta, idOperador, idServicio;
    private Integer dia, mes, anio, hora, minuto;
    private Integer selDia, selMes, selAnio, selHora, selMinuto;
    private String tipoServicio, nombreOperador, fecha;
    private ArrayList<Integer> idsOperadores;

    private SharedPreferences preferences;

    public AsignacionOperadorFragmen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignacion_operador, container, false);

        preferences = getContext().getSharedPreferences("agacoApp", Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        tipoServicio = bundle.getString("tipoServicio", "");
        idServicio = bundle.getInt("idServicio", 0);
        idVenta = bundle.getInt("idVenta", 0);
        idOperador = bundle.getInt("idOperador", 0);
        nombreOperador = bundle.getString("nombreOperador", "");
        fecha = bundle.getString("fecha", "");
        spOperador = view.findViewById(R.id.spOperador);
        idsOperadores = new ArrayList<>();
        setOperadoresSpinner();
        spOperador.setOnItemSelectedListener(this);

        tvTituloAsignacionOperador = view.findViewById(R.id.tvTituloAsignacionOperador);
        tvOperador = view.findViewById(R.id.tvOperador);
        tvFechaOperacion = view.findViewById(R.id.tvFechaOperacion);
        btnFechaOperacion = view.findViewById(R.id.btnFechaOperacion);
        btnAsignarOperador = view.findViewById(R.id.btnAsignarOperador);
        btnCancelarAsignacion = view.findViewById(R.id.btnCancelarAsignacion);

        String titulo, lblOperador;

        if (tipoServicio.equals("TRANSPORTE")) {
            titulo = "Asignación de transportista";
            lblOperador = "Transportista";
        }
        else {
            titulo = "Asignación de especialista";
            lblOperador = "Especialista";
        }

        tvTituloAsignacionOperador.setText(titulo);
        tvOperador.setText(lblOperador);
        tvFechaOperacion.setText(fecha);

        btnFechaOperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateTimeCalendar();
                new DatePickerDialog(
                        getContext(),
                        AsignacionOperadorFragmen.this,
                        anio, mes, dia
                ).show();
            }
        });
        
        btnAsignarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarOperador();
            }
        });

        btnCancelarAsignacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        return view;
    }

    private void asignarOperador() {
        JSONObject data = new JSONObject();
        try {
            data.put("servicio_id", idServicio);
            data.put("venta_id", idVenta);
            data.put("tipo_servicio_id", tipoServicio.equals("TRANSPORTE") ? 1 : 2);
            data.put("operador_id", idOperador);
            data.put("fecha_servicio", fecha);
        }
        catch (JSONException e) {
           e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                AgacoAPI.getBaseUrl() + "asignar-operador",
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Se asignó correctamente", Toast.LENGTH_SHORT).show();
                        goBack();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al asignar operador", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");

                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                params.put("Accept", "application/json");
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void goBack() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DetalleVentaFragment detalleVentaFragment = new DetalleVentaFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("id", idVenta);

        detalleVentaFragment.setArguments(bundle);
        transaction.replace(R.id.nav_host_fragment, detalleVentaFragment);
        transaction.commit();
    }

    private void setOperadoresSpinner() {
        String url = AgacoAPI.getBaseUrl() + (tipoServicio.equals("TRANSPORTE") ? "transportistas" : "especialistas");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                 url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> data = new ArrayList<String>();
                        try {
                            if (response != null) {
                                for (int i=0;i<response.length();i++){
                                    data.add(response.getJSONObject(i).getString("nombre"));
                                    idsOperadores.add(response.getJSONObject(i).getInt("id"));
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, data);
                                spOperador.setAdapter(adapter);
                                if (!nombreOperador.equals("")) {
                                    spOperador.setSelection(adapter.getPosition(nombreOperador));
                                }
                            }
                        } catch (JSONException e) {
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");

                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void getDateTimeCalendar() {
        Calendar cal = Calendar.getInstance();
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        hora = cal.get(Calendar.HOUR);
        minuto = cal.get(Calendar.MINUTE);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        selAnio = i;
        selMes = i1 + 1;
        selDia = i2;

        getDateTimeCalendar();

        new TimePickerDialog(
                getContext(),
                AsignacionOperadorFragmen.this,
                hora, minuto,
                true
        ).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        selHora = i;
        selMinuto = i1;

        fecha = selAnio + "-" + selMes + "-" + selDia + " "
                + selHora + ":" + selMinuto + ":00";

        tvFechaOperacion.setText(fecha);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        idOperador = idsOperadores.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}