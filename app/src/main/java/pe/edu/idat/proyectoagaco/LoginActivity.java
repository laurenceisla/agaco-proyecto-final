package pe.edu.idat.proyectoagaco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.idat.proyectoagaco.api.AgacoAPI;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button buttonSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("agacoApp", MODE_PRIVATE);

        if (preferences.contains("token")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        buttonSignin = findViewById(R.id.button_signin);

        buttonSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        JSONObject data = new JSONObject();
        try {
            data.put("email", etEmail.getText().toString());
            data.put("password", etPassword.getText().toString());
            data.put("device_name", AgacoAPI.getDeviceName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                AgacoAPI.getBaseUrl() + "login",
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                Integer id = response.getInt("id");
                                String nombre = response.getString("nombre");
                                String perfil = response.getString("perfil");
                                String token = response.getString("token");

                                SharedPreferences.Editor editor = getSharedPreferences("agacoApp", MODE_PRIVATE).edit();

                                editor.putInt("id", id);
                                editor.putString("nombre", nombre);
                                editor.putString("perfil", perfil);
                                editor.putString("token", token);

                                editor.apply();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error al ingresar, verifique sus datos", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}