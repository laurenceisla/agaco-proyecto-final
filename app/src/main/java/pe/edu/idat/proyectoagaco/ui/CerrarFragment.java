package pe.edu.idat.proyectoagaco.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pe.edu.idat.proyectoagaco.R;
import pe.edu.idat.proyectoagaco.api.AgacoAPI;

public class CerrarFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener
{

    private TextView tvFechaCierre;
    private Button btnCancelar, btnCerrar, btnFecha;

    private Integer dia, mes, anio, hora, minuto;
    private Integer selDia, selMes, selAnio, selHora, selMinuto;
    private String fecha;
    private Integer idServicio, idVenta;

    private SharedPreferences preferences;

    public CerrarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cerrar, container, false);

        preferences = getContext().getSharedPreferences("agacoApp", Context.MODE_PRIVATE);

        Bundle bundle = getArguments();

        idServicio = bundle.getInt("idServicio", 0);
        idVenta = bundle.getInt("idVenta", 0);

        btnCerrar = view.findViewById(R.id.btnCerrar);
        btnCancelar = view.findViewById(R.id.btnCancelarCierre);
        btnFecha = view.findViewById(R.id.btnFechaCierre);
        tvFechaCierre = view.findViewById(R.id.tvFechaCierre);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateTimeCalendar();
                new DatePickerDialog(
                        getContext(),
                        CerrarFragment.this,
                        anio, mes, dia
                ).show();
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarOperacion();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        return view;
    }

    private void cerrarOperacion() {
        JSONObject data = new JSONObject();
        try {
            data.put("fecha_cierre", fecha);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                AgacoAPI.getBaseUrl() + "cerrar-servicio/" + idServicio,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Se cerró operación correctamente", Toast.LENGTH_SHORT).show();
                        goBack();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al cerrar operación", Toast.LENGTH_SHORT).show();
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
                CerrarFragment.this,
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

        tvFechaCierre.setText(fecha);
    }
}