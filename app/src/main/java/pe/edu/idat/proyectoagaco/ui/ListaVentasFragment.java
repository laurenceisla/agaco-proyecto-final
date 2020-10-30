package pe.edu.idat.proyectoagaco.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import pe.edu.idat.proyectoagaco.adapter.VentaAdapter;
import pe.edu.idat.proyectoagaco.api.AgacoAPI;
import pe.edu.idat.proyectoagaco.model.Venta;

public class ListaVentasFragment extends Fragment {

    private RecyclerView rvListaVentas;
    private VentaAdapter adapter;

    private boolean cargaOK = false;
    private String sgteUrl = null;

    public ListaVentasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_ventas, container, false);

        adapter = new VentaAdapter(
                getContext(),
                new VentaAdapter.VerDetalleClickListener() {
                    @Override
                    public void onBtnVerDetalleClick(View view, int position) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack("what");
                        DetalleVentaFragment detalleVentaFragment = new DetalleVentaFragment();
                        String nombre = adapter.getVenta(position).getNombreCliente();

                        Bundle bundle = new Bundle();
                        bundle.putString("nombre", nombre);
                        detalleVentaFragment.setArguments(bundle);
                        transaction.replace(R.id.nav_host_fragment, detalleVentaFragment);
                        transaction.commit();
                    }
                }
        );

        rvListaVentas = view.findViewById(R.id.rvListaVentas);
        rvListaVentas.setAdapter(adapter);
        rvListaVentas.setLayoutManager(new GridLayoutManager(getContext(), 2));

        rvListaVentas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int itemsVisibles = rvListaVentas.getLayoutManager().getChildCount();
                    int itemsTotales = rvListaVentas.getLayoutManager().getItemCount();
                    int posPrimerItemVisible = ((GridLayoutManager) rvListaVentas.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    if (cargaOK) {
                        if (itemsVisibles + posPrimerItemVisible >= itemsTotales ) {
                            cargaOK = false;
                            fetchVentasSiguientes();
                        }
                    }
                }
            }
        });

        fetchVentas();

        return view;
    }

    private void fetchVentasSiguientes() {
        if (sgteUrl == null) {
            return;
        }

        fetchVentas();
    }

    private void fetchVentas() {
        // String url = sgteUrl == null ? AgacoAPI.getBaseUrl() + "ventas" : sgteUrl;
        String url = AgacoAPI.getBaseUrl() + "ventas";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            if (jsonArray.length() > 0) {
                                ArrayList<Venta> listaVentas = new ArrayList<>();
                                cargaOK = true;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    listaVentas.add(new Venta(
                                            jsonObject.getInt("id"),
                                            jsonObject.getJSONObject("cliente").getString("nombre_completo"),
                                            jsonObject.getJSONObject("producto").getString("nombre"),
                                            jsonObject.getString("direccion_completa"),
                                            jsonObject.getString("fecha_compra")
                                    ));
                                }

                                sgteUrl = response.getString("next_page_url");

                                adapter.agregarVenta(listaVentas);
                            }
                        }
                        catch (JSONException e) {
                            cargaOK = false;
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