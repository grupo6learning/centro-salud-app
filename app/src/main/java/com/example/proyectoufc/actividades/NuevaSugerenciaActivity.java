package com.example.proyectoufc.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Hash;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class NuevaSugerenciaActivity extends AppCompatActivity implements View.OnClickListener {

    private final String urlMostrarSugerencia="http://clinica-consultas.atwebpages.com/servicios/agregarSugerencia.php";
    Button sugBtnCancelar, sugBtnAgregar;
    TextInputEditText sugTxtPaciente, sugTxtSugerencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_sugerencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sugBtnCancelar=findViewById(R.id.SugBtnCancelar);
        sugBtnAgregar=findViewById(R.id.SugBtnAgregar);
        sugTxtPaciente=findViewById(R.id.sugTxtNombreSugerencia);
        sugTxtSugerencia=findViewById(R.id.sugTxtComentarioSugerencia);

        sugBtnCancelar.setOnClickListener(this);
        sugBtnAgregar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.SugBtnCancelar){
            cancelar();
        } else if (v.getId()==R.id.SugBtnAgregar) {
            procesarSugerencia();
        }
    }

    private void procesarSugerencia() {
        AsyncHttpClient ahcregistrar=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        Hash hash=new Hash();

        if (validarFormulario()){
            params.add("paciente", sugTxtPaciente.getText().toString());
            params.add("sugerencia", sugTxtSugerencia.getText().toString());

            ahcregistrar.post(urlMostrarSugerencia, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if (statusCode == 200) {
                        try {
                            // Verifica si la respuesta contiene el error en HTML
                            if (rawJsonResponse.contains("<b>Fatal error</b>")) {
                                // Maneja el error de entrada duplicada
                                if (rawJsonResponse.contains("Duplicate entry")) {
                                    Toast.makeText(getApplicationContext(), "Error: Entrada duplicada", Toast.LENGTH_LONG).show();
                                } else {
                                    // Maneja otros errores
                                    Toast.makeText(getApplicationContext(), "Error en el servidor: " + rawJsonResponse, Toast.LENGTH_LONG).show();
                                }
                                return;
                            }

                            // Si la respuesta es válida, realiza la conversión
                            int retVal = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                            if (retVal == 1) {
                                Toast.makeText(getApplicationContext(), "Sugerencia Registrada", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent iBienvenida = new Intent(getApplicationContext(), BienvenidaActivity.class);
                                startActivity(iBienvenida);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Formato de número inválido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                    Toast.makeText(getApplicationContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });


        }else {
            Toast.makeText(this, "Complete Correctamente el formulario", Toast.LENGTH_SHORT).show();
        }


    }

    private void cancelar() {
        Intent iCancelar = new Intent(this, BienvenidaActivity.class);
        startActivity(iCancelar);
        finish();
    }

    private boolean validarFormulario(){
        if(sugTxtPaciente.getText().toString().isEmpty())
            return false;
        if(sugTxtSugerencia.getText().toString().isEmpty())
            return false;

        return true;
    }
}