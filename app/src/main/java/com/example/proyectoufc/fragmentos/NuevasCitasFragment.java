package com.example.proyectoufc.fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.health.connect.datatypes.StepsCadenceRecord;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.proyectoufc.R;
import com.example.proyectoufc.actividades.InicioSesionActivity;
import com.example.proyectoufc.clases.Especialidad;
import com.example.proyectoufc.clases.Hash;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class NuevasCitasFragment extends Fragment implements View.OnClickListener {

    final String urlMostrarDoctor = "http://clinica-consultas.atwebpages.com/servicios/mostrarMedicos.php";
    final String urlMostrarEspecialidad = "http://clinica-consultas.atwebpages.com/servicios/mostrarEspecialidades.php";

    private final String urlAgregarCita="http://clinica-consultas.atwebpages.com/servicios/agregarCita.php";
    TextInputEditText txtPaciente, txtFechaCita, txtCorreo;
    Button btnReservar, btnCancelar;
    Spinner cboEspecialidad, cboDoctor;

    private TextView textView;
    private TextToSpeech textToSpeech;
    private Button speakButton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NuevasCitasFragment() {
        // Required empty public constructor
    }

    public static NuevasCitasFragment newInstance(String param1, String param2) {
        NuevasCitasFragment fragment = new NuevasCitasFragment();
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
        View view = inflater.inflate(R.layout.fragment_nuevas_citas, container, false);
        txtPaciente = view.findViewById(R.id.logTxtPaciente);
        txtCorreo = view.findViewById(R.id.logTxtCorreo);
        txtFechaCita = view.findViewById(R.id.logTxtFechaCita);
        cboDoctor = view.findViewById(R.id.cboDoctor);
        cboEspecialidad = view.findViewById(R.id.cboEspecialidad);
        btnReservar = view.findViewById(R.id.logBtnReservar);
        btnCancelar = view.findViewById(R.id.logBtnCancelar);
        textView = view.findViewById(R.id.frgCitaTitulo);

        // Asignar adaptadores iniciales
        cboDoctor.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"-- Seleccione al Doctor --"}));
        cboEspecialidad.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"-- Seleccione la Especialidad --"}));

        btnReservar.setOnClickListener(this);

        // Asignar listener de clic a txtFechaCita
        txtFechaCita.setOnClickListener(this);

        // Llenar datos

        llenarEspecialidad();
        llenarDoctor();


        speakButton = view.findViewById(R.id.speakButton);

        // Inicializar TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Maneja el error si el idioma no es soportado
                    }
                } else {
                    // Maneja el error de inicialización
                }
            }
        });

        // Configurar el botón para leer el texto
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });





        return view;
    }
    private void speakText() {
        String text = textView.getText().toString();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logTxtFechaCita)
            mostrarSelectorfechas();
        else if (v.getId()==R.id.logBtnReservar)
            procesarCita();
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void mostrarSelectorFechaHora() {
        final Calendar fechaActual = Calendar.getInstance();
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mes = fechaActual.get(Calendar.MONTH);
        int year = fechaActual.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                // Ahora mostrar el TimePickerDialog para seleccionar la hora
                mostrarSelectorHora(y, m, d);
            }
        }, year, mes, dia);
        dpd.show();
    }

    private void mostrarSelectorHora(final int year, final int month, final int day) {
        final Calendar horaActual = Calendar.getInstance();
        int hora = horaActual.get(Calendar.HOUR_OF_DAY);
        int minuto = horaActual.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int h, int m) {
                // Actualizar el campo de texto con la fecha y hora seleccionadas
                txtFechaCita.setText(year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + (day < 10 ? "0" + day : day) + " " + (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m));
            }
        }, hora, minuto, true);
        tpd.show();
    }

    private void llenarEspecialidad() {
        AsyncHttpClient ahcEspecialidad = new AsyncHttpClient();
        ahcEspecialidad.get(urlMostrarEspecialidad, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        String[] especialidad = new String[jsonArray.length() + 1];
                        especialidad[0] = "-- Seleccione la Especialidad --";
                        for (int i = 1; i < jsonArray.length() + 1; i++) {
                            especialidad[i] = jsonArray.getJSONObject(i - 1).getString("nombre_especialidad");
                        }
                        if (getContext() != null) {
                            cboEspecialidad.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, especialidad));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }




    private void llenarDoctor() {
        AsyncHttpClient ahcDoctor = new AsyncHttpClient();
        ahcDoctor.get(urlMostrarDoctor, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        String[] doctores = new String[jsonArray.length() + 1];
                        doctores[0] = "-- Seleccione al Doctor --";
                        for (int i = 1; i < jsonArray.length() + 1; i++) {
                            doctores[i] = jsonArray.getJSONObject(i - 1).getString("nombre_medico");
                        }
                        if (getContext() != null) {
                            cboDoctor.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, doctores));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }




    private void procesarCita(){
        AsyncHttpClient ahcregistrarCita=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        Hash hash =new Hash();


        if (validarFormulario()){
            params.add("paciente", txtPaciente.getText().toString());
            params.add("id_especialidad", String.valueOf(cboEspecialidad.getSelectedItemPosition()));
            params.add("id_doctor",String.valueOf((cboDoctor.getSelectedItemPosition())));
            params.add("fecha",txtFechaCita.getText().toString());
            params.add("correo", txtCorreo.getText().toString());

            ahcregistrarCita.post(urlAgregarCita, params, new BaseJsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if (statusCode==200){
                        try {
                            // Verifica si la respuesta contiene el error en HTML
                            if (rawJsonResponse.contains("<b>Fatal error</b>")) {
                                // Maneja el error de entrada duplicada
                                if (rawJsonResponse.contains("Duplicate entry")) {
                                    Toast.makeText(getContext(), "Error: Entrada duplicada", Toast.LENGTH_LONG).show();
                                } else {
                                    // Maneja otros errores
                                    Toast.makeText(getContext(), "Error en el servidor: " + rawJsonResponse, Toast.LENGTH_LONG).show();
                                }
                                return;
                            }

                            // Si la respuesta es válida, realiza la conversión
                            int retVal = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                            if (retVal == 1) {
                                Toast.makeText(getContext(), "Cita Registrada", Toast.LENGTH_SHORT).show();
                                // Cambia finish() y startActivity a las acciones correspondientes en tu fragmento
                            }
                        }catch (NumberFormatException e){
                            Toast.makeText(getContext().getApplicationContext(), "Formato de numero invalido: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                    Toast.makeText(getContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });
        }else {
            Toast.makeText(getContext(),"Complete Correctamente el formulario", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validarFormulario(){
        if(txtPaciente.getText().toString().isEmpty())
            return false;
        if (txtCorreo.getText().toString().isEmpty())
            return false;
        if (txtFechaCita.getText().toString().isEmpty())
            return false;
        if (cboEspecialidad.getSelectedItemPosition()==0)
            return false;
        if (cboDoctor.getSelectedItemPosition()==0)
            return false;

        return true;
    }

    private void mostrarSelectorfechas() {
        final Calendar fechaActual = Calendar.getInstance();
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mes = fechaActual.get(Calendar.MONTH);
        int year = fechaActual.get(Calendar.YEAR);
        DatePickerDialog dpd = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                txtFechaCita.setText(y + "-" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "-" + (d < 10 ? "0" + d : d));
            }
        }, year, mes, dia);
        dpd.show();
    }
}
