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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.proyectoufc.actividades.CarritoActivity;
import com.example.proyectoufc.actividades.InfoCitaActivity;
import com.example.proyectoufc.R;
import com.example.proyectoufc.actividades.MapsActivity;
import com.example.proyectoufc.adaptadores.CitaAdapter;
import com.example.proyectoufc.clases.Citas;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CitasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitasFragment extends Fragment implements View.OnClickListener {

    Button buscarDoctor;
    RecyclerView recCitas;
    ArrayList<Citas> lista;
    CitaAdapter adapter=null;

    final static String urlMostrarCitas= "http://clinica-consultas.atwebpages.com/servicios/mostrarCitas.php";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CitasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CitasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitasFragment newInstance(String param1, String param2) {
        CitasFragment fragment = new CitasFragment();
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_citas, container, false);

        View view =inflater.inflate(R.layout.fragment_citas,container,false);
        //ImageButton btnPrimeraCita=view.findViewById(R.id.btnPrimeracita);
        //btnPrimeraCita.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent =new Intent(getActivity(), InfoCitaActivity.class);
        //        startActivity(intent);
        //    }
        //});

        //buscarDoctor = view.findViewById(R.id.btnBuscarDoctor);
        //buscarDoctor.setOnClickListener(this);

        recCitas =view.findViewById(R.id.frgCitas);
        lista=new ArrayList<>();
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        recCitas.setLayoutManager(manager);
        adapter=new CitaAdapter(lista);
        recCitas.setAdapter(adapter);

        mostrarCitas();
        return view;


    }

    private void mostrarCitas() {
        AsyncHttpClient ahcMostrarCitas=new AsyncHttpClient();
        RequestParams params =new RequestParams();
        params.add("id","-1");
        ahcMostrarCitas.get(urlMostrarCitas, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode==200){
                    try {
                        JSONArray jsonArray=new JSONArray(rawJsonResponse);
                        lista.clear();
                        for (int i =0; i < jsonArray.length(); i++){
                            DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                            Date fecha=format.parse(jsonArray.getJSONObject(i).getString("fecha"));
                            lista.add(new Citas(jsonArray.getJSONObject(i).getInt("id_cita"),
                                    jsonArray.getJSONObject(i).getString("paciente"),
                                    jsonArray.getJSONObject(i).getString("correo"),
                                    jsonArray.getJSONObject(i).getString("doctor"),
                                    jsonArray.getJSONObject(i).getString("especialidad"),
                                    fecha));

                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException | ParseException e) {
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

    @Override
    public void onClick(View v) {
        //  if (v.getId() == R.id.btnBuscarDoctor) {
        //    Intent intent = new Intent(getActivity(), MapsActivity.class);
        //    startActivity(intent);
        //}
    }
}