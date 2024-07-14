package com.example.proyectoufc.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectoufc.R;
import com.example.proyectoufc.actividades.CarritoActivity;
import com.example.proyectoufc.adaptadores.MedicamentoAdapter;
import com.example.proyectoufc.clases.Medicamento;
import com.example.proyectoufc.sqlite.ProyectoUFC;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;

public class FarmaciaFragment extends Fragment implements View.OnClickListener{

    final static String urlMostrarMedicamentos = "http://clinica-consultas.atwebpages.com/servicios/mostrarMedicamentos.php";
    RecyclerView recMedicamentos;
    ArrayList<Medicamento> lista;
    MedicamentoAdapter adapter = null;

    TextInputEditText nombreBusqueda;
    Button btnBuscar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FarmaciaFragment() {
        // Required empty public constructor
    }

    public static FarmaciaFragment newInstance(String param1, String param2) {
        FarmaciaFragment fragment = new FarmaciaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Button frgBtnCarrito = view.findViewById(R.id.btnCarrito);
        /*frgBtnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CarritoActivity.class);
                startActivity(intent);
            }
        });*/

        /*nombreBusqueda = view.findViewById(R.id.nombreMedicina);
        btnBuscar = view.findViewById(R.id.btnBuscarMedicina);
        btnBuscar.setOnClickListener(this);*/

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmacia, container, false);
        recMedicamentos = view.findViewById(R.id.frgBusRecBusqueda);
        lista = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recMedicamentos.setLayoutManager(manager);
        adapter = new MedicamentoAdapter(lista);
        recMedicamentos.setAdapter(adapter);
        mostrarMedicamentos();
        return view;
    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.btnBuscarMedicina) {
            String terminoBusqueda = nombreBusqueda.getText().toString().trim().toLowerCase();
            if (terminoBusqueda.isEmpty()) {
                nombreBusqueda.setError("Por favor, ingrese un término de búsqueda");
            } else {
                guardarBusqueda(terminoBusqueda);
            }
        }*/
    }

    private void guardarBusqueda(String terminoBusqueda) {
        ProyectoUFC proyectoUFC = new ProyectoUFC(getActivity());
        try {
            proyectoUFC.agregarHistorial(terminoBusqueda);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al guardar el historial de búsqueda", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void mostrarMedicamentos() {

        AsyncHttpClient ahcMostrarMedicamentos = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id", "-1");
        ahcMostrarMedicamentos.get(urlMostrarMedicamentos, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {

                if (statusCode==200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        lista.clear();
                        for (int i=0; i < jsonArray.length(); i++){
                            lista.add(new Medicamento(jsonArray.getJSONObject(i).getInt("id_medicamento"),
                                    jsonArray.getJSONObject(i).getString("nombre_medicamento"),
                                    jsonArray.getJSONObject(i).getString("forma_farmaceutica"),
                                    jsonArray.getJSONObject(i).getString("concentracion"),
                                    jsonArray.getJSONObject(i).getDouble("precio")));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

    }
}