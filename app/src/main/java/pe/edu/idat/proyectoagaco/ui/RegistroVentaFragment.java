package pe.edu.idat.proyectoagaco.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class RegistroVentaFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Spinner spTipoDocumento, spDistrito, spProducto;
    private EditText etNroDocumento, etApePaterno, etApeMaterno, etNombres, etDireccion, etTelefono;
    private Button btnFechaVenta, btnRegistrarVenta;
    private TextView tvFechaVenta;
    private CheckBox cbTransporte, cbArmado;
    private String fechaVenta;
    private ArrayList<Integer> idsTiposDocumento, idsDistritos, idsProductos;
    private Integer idTipoDocumento, idDistrito, idProducto;
    private Integer dia, mes, anio, hora, minuto;
    private Integer selDia, selMes, selAnio, selHora, selMinuto;

    private SharedPreferences preferences;

    public RegistroVentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_venta, container, false);

        preferences = getContext().getSharedPreferences("agacoApp", Context.MODE_PRIVATE);

        spTipoDocumento = view.findViewById(R.id.spTipoDocumento);
        idsTiposDocumento = new ArrayList<>();
        setDatosSpinner("tipos-documento-identidad", spTipoDocumento, idsTiposDocumento);

        spProducto = view.findViewById(R.id.spProducto);
        idsProductos = new ArrayList<>();
        setDatosSpinner("productos", spProducto, idsProductos);

        spDistrito = view.findViewById(R.id.spDistrito);
        idsDistritos = new ArrayList<>();
        setDatosSpinner("distritos", spDistrito, idsDistritos);

        etNroDocumento = view.findViewById(R.id.etNroDocumento);
        etApePaterno = view.findViewById(R.id.etApePaterno);
        etApeMaterno = view.findViewById(R.id.etApeMaterno);
        etNombres = view.findViewById(R.id.etNombres);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        btnFechaVenta = view.findViewById(R.id.btnFechaVenta);
        tvFechaVenta = view.findViewById(R.id.tvFechaVenta);
        cbTransporte = view.findViewById(R.id.cbTransporte);
        cbArmado = view.findViewById(R.id.cbArmado);
        btnRegistrarVenta = view.findViewById(R.id.btnRegistrarVenta);

        spTipoDocumento.setOnItemSelectedListener(this);
        spProducto.setOnItemSelectedListener(this);
        spDistrito.setOnItemSelectedListener(this);

        btnFechaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateTimeCalendar();
                new DatePickerDialog(
                        getContext(),
                        RegistroVentaFragment.this,
                        anio, mes, dia
                ).show();
            }
        });

        btnRegistrarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarVenta();
            }
        });

        return view;
    }

    private void registrarVenta() {
        JSONObject data = new JSONObject();
        try {
            data.put("tipo_documento_id", idTipoDocumento);
            data.put("nro_documento", etNroDocumento.getText().toString());
            data.put("ape_paterno", etApePaterno.getText().toString());
            data.put("ape_materno", etApeMaterno.getText().toString());
            data.put("nombres", etNombres.getText().toString());
            data.put("telefono", etTelefono.getText().toString());
            data.put("direccion", etDireccion.getText().toString());
            data.put("distrito_id", idDistrito);
            data.put("producto_id", idProducto);
            data.put("fecha_compra", fechaVenta);
            data.put("solicita_entrega", cbTransporte.isChecked());
            data.put("solicita_armado", cbArmado.isChecked());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                AgacoAPI.getBaseUrl() + "ventas",
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();

                        Fragment fragment = new ListaVentasFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al registrar la venta", Toast.LENGTH_SHORT).show();
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

        requestQueue.add(jsonArrayRequest);
    }

    private void setDatosSpinner(String consulta, final Spinner spinner, final ArrayList<Integer> ids) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                AgacoAPI.getBaseUrl() + consulta,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> data = new ArrayList<String>();
                        try {
                            if (response != null) {
                                for (int i=0;i<response.length();i++){
                                    data.add(response.getJSONObject(i).getString("nombre"));
                                    ids.add(response.getJSONObject(i).getInt("id"));
                                }
                                spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, data));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spTipoDocumento:
                idTipoDocumento = idsTiposDocumento.get(i);
                break;
            case R.id.spProducto:
                idProducto = idsProductos.get(i);
                break;
            case R.id.spDistrito:
                idDistrito = idsDistritos.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                RegistroVentaFragment.this,
                hora, minuto,
                true
        ).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        selHora = i;
        selMinuto = i1;

        fechaVenta = selAnio + "-" + selMes + "-" + selDia + " "
                + selHora + ":" + selMinuto + ":00";

        tvFechaVenta.setText(fechaVenta);
    }

}