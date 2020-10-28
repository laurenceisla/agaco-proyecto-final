package pe.edu.idat.proyectoagaco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.idat.proyectoagaco.api.AgacoAPI;

public class RegistroVentaActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Spinner spTipoDocumento, spDistrito, spProducto;
    private EditText etNroDocumento, etApePaterno, etApeMaterno, etNombres, etDireccion;
    private Button btnFechaVenta, btnRegistrarVenta;
    private TextView tvFechaVenta;
    private CheckBox cbTransporte, cbArmado;
    private String fechaVenta;
    private ArrayList<Integer> idsTiposDocumento, idsDistritos, idsProductos;
    private Integer idTipoDocumento, idDistrito, idProducto;
    private Integer dia, mes, anio, hora, minuto;
    private Integer selDia, selMes, selAnio, selHora, selMinuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_venta);

        spTipoDocumento = findViewById(R.id.spTipoDocumento);
        idsTiposDocumento = new ArrayList<>();
        setDatosSpinner("tipos-documento-identidad", spTipoDocumento, idsTiposDocumento);

        spProducto = findViewById(R.id.spProducto);
        idsProductos = new ArrayList<>();
        setDatosSpinner("productos", spProducto, idsProductos);

        spDistrito = findViewById(R.id.spDistrito);
        idsDistritos = new ArrayList<>();
        setDatosSpinner("distritos", spDistrito, idsDistritos);

        etNroDocumento = findViewById(R.id.etNroDocumento);
        etApePaterno = findViewById(R.id.etApePaterno);
        etApeMaterno = findViewById(R.id.etApeMaterno);
        etNombres = findViewById(R.id.etNombres);
        etDireccion = findViewById(R.id.etDireccion);
        btnFechaVenta = findViewById(R.id.btnFechaVenta);
        tvFechaVenta = findViewById(R.id.tvFechaVenta);
        cbTransporte = findViewById(R.id.cbTransporte);
        cbArmado = findViewById(R.id.cbArmado);
        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);

        spTipoDocumento.setOnItemSelectedListener(this);
        spProducto.setOnItemSelectedListener(this);
        spDistrito.setOnItemSelectedListener(this);

        btnFechaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateTimeCalendar();
                new DatePickerDialog(
                        RegistroVentaActivity.this,
                        RegistroVentaActivity.this,
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
    }

    private void registrarVenta() {
        JSONObject data = new JSONObject();
        try {
            data.put("tipo_documento_id", idTipoDocumento);
            data.put("nro_documento", etNroDocumento.getText().toString());
            data.put("ape_paterno", etApePaterno.getText().toString());
            data.put("ape_materno", etApeMaterno.getText().toString());
            data.put("nombres", etNombres.getText().toString());
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                AgacoAPI.getBaseUrl() + "ventas",
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al registrar la venta", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + AgacoAPI.obtenerToken());
                return params;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void setDatosSpinner(String consulta, final Spinner spinner, final ArrayList<Integer> ids) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                                spinner.setAdapter(new ArrayAdapter<String>(RegistroVentaActivity.this, R.layout.support_simple_spinner_dropdown_item, data));
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + AgacoAPI.obtenerToken());
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
                RegistroVentaActivity.this,
                RegistroVentaActivity.this,
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