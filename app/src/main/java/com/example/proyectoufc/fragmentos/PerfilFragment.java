package com.example.proyectoufc.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoufc.R;
import com.example.proyectoufc.actividades.EditarPerfilActivity;
import com.example.proyectoufc.actividades.InicioSesionActivity;
import com.example.proyectoufc.actividades.NuevaSugerenciaActivity;
import com.example.proyectoufc.clases.Paciente;
import com.example.proyectoufc.sqlite.ProyectoUFC;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    CheckBox chkNotificaciones, chkSonido;
    Spinner cboIdiomas;
    Button btnGuardar,btnCerrarSesion,btnEditarPerfil;
    TextInputEditText lblNombre, lblDni, lblCelular, lblCorreo, lblDireccion;
    Paciente paciente;
    private static final String PREFS_NAME = "FontSizePrefs";
    private static final String FONT_SIZE_KEY = "fontSize";
    private Spinner fontSizeSpinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        //return inflater.inflate(R.layout.fragment_perfil, container, false);

        View view =inflater.inflate(R.layout.fragment_perfil,container,false);

        chkNotificaciones=view.findViewById(R.id.frgCfgNotificaciones);
        chkSonido=view.findViewById(R.id.frgCfgSonido);
        cboIdiomas=view.findViewById(R.id.frgCfgIdioma);
        btnGuardar=view.findViewById(R.id.perBtnGuardarCambios);
        btnCerrarSesion=view.findViewById(R.id.perBtnCerrarSesion);
        btnEditarPerfil=view.findViewById(R.id.LogBtnEditarPerfil);

        lblNombre=view.findViewById(R.id.perfTxtNombre);

        if(getArguments()!=null){
            Paciente paciente=(Paciente)getArguments().getSerializable("paciente");
            if (paciente!=null){
                lblNombre.setText(paciente.getNombres()+" "+paciente.getApellidos());
            }else {
                Toast.makeText(getContext(),"Paciente no encontrado",Toast.LENGTH_SHORT).show();
            }
        }

        btnGuardar.setOnClickListener(this);
        btnCerrarSesion.setOnClickListener(this);
        btnEditarPerfil.setOnClickListener(this);


        llenarIdiomas();

        cargarPreferencias();

        fontSizeSpinner = view.findViewById(R.id.font_size_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.font_size_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeSpinner.setAdapter(adapter);

        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        int fontSize = preferences.getInt(FONT_SIZE_KEY, 1); // 1 es para tamaño estándar
        fontSizeSpinner.setSelection(fontSize);

        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(FONT_SIZE_KEY, position);
                editor.apply();
                setAppFontSize(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAppFontSize(fontSize);

        return view;

    }
    private void setAppFontSize(int fontSize) {
        float scale;
        switch (fontSize) {
            case 0: // Pequeña
                scale = 0.85f;
                break;
            case 2: // Grande
                scale = 1.65f;
                break;
            case 1: // Estándar
            default:
                scale = 1.0f;
                break;
        }

        // Aplica el tamaño de fuente globalmente si es necesario
        requireContext().getResources().getConfiguration().fontScale = scale;
        requireContext().getResources().updateConfiguration(requireContext().getResources().getConfiguration(), requireContext().getResources().getDisplayMetrics());
    }

    private void llenarIdiomas(){
        String[] idiomas={"Español","Ingles","Portugues","Qechua"};
        cboIdiomas.setAdapter(new ArrayAdapter<String>(getContext(),
                                                        android.R.layout.simple_spinner_dropdown_item,
                                                        idiomas));
    }

    private void cargarPreferencias(){
        SharedPreferences preferences=getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        boolean notificaciones=preferences.getBoolean("Notificaciones",false);
        boolean sonido=preferences.getBoolean("Sonido",false);
        int idioma=preferences.getInt("Idioma",0);
        chkNotificaciones.setChecked(notificaciones);
        chkSonido.setChecked(sonido);
        cboIdiomas.setSelection(idioma);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.perBtnGuardarCambios){
            guardar();
        }
        else if (v.getId()==R.id.perBtnCerrarSesion) {
            cerrarSesion();
            
        }else {
            abrirEditarPerfil();
        }
    }

    private void abrirEditarPerfil() {
        Intent intent= new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }

    private void cerrarSesion() {
        ProyectoUFC py=new ProyectoUFC(getContext());
        py.eliminarUsuario();
        py.eliminarTablaHistorial();
        getActivity().finish();
        Intent inicioSesion =new Intent(getContext(), InicioSesionActivity.class);
        startActivity(inicioSesion);
    }

    private void guardar() {
        String error="";
        if (cboIdiomas.getSelectedItemPosition()!=0){

            SharedPreferences preferences= getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("Notificaciones",chkNotificaciones.isChecked());
            editor.putBoolean("Sonido",chkSonido.isChecked());
            editor.putInt("Idioma",cboIdiomas.getSelectedItemPosition());
            editor.commit();
            Toast.makeText(getContext(),"Preferencias Guardadas",Toast.LENGTH_SHORT).show();

        }
        else
            error+="Debe elegir un idioma";

        if(error!=null)
            Toast.makeText(getContext(),"Preferencias Guardadas",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(getContext(),error, Toast.LENGTH_SHORT).show();
        }

    }




}