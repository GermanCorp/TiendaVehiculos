package com.smartec.tiendavehiculos.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.ServerConfig;
import com.smartec.tiendavehiculos.entidades.Combustible;
import com.smartec.tiendavehiculos.entidades.Marca;
import com.smartec.tiendavehiculos.entidades.Modelo;
import com.smartec.tiendavehiculos.entidades.Tipo;
import com.smartec.tiendavehiculos.entidades.Transmision;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class RegistroVehiculoFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ+"misFotos";
    private final int COD_SELECCIONAR = 10;
    private final int COD_CAMARA = 20;

    String path;

    private Spinner spinnerMarca;
    private Spinner spinnerModelo;
    private Spinner spinnerTipo;
    private Spinner spinnerTransmision;
    private Spinner spinnerCombustible;
    private ArrayList<Marca> listaMarca;
    private ArrayList<Modelo> listaModelo;
    private ArrayList<Tipo> listaTipo;
    private ArrayList<Transmision> listaTransmision;
    private ArrayList<Combustible> listaCombustible;


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText imputId;
    private EditText imputColor;
    private EditText imputAnio;
    private EditText imputCilindraje;
    private EditText imputPrecioVenta;

    private Button btnRegistrar;
    private ProgressDialog progress;

    private ImageView img1;

    public RegistroVehiculoFragment() {
        // Required empty public constructor
    }

    public static RegistroVehiculoFragment newInstance(String param1, String param2) {
        RegistroVehiculoFragment fragment = new RegistroVehiculoFragment();
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




    //********************************************************************
    //onCreateView
    //********************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRVehiculos = inflater.inflate(R.layout.fragment_registro_vehiculo, container, false);
        listaMarca = new ArrayList<>();
        listaModelo = new ArrayList<>();
        listaTipo = new ArrayList<>();
        listaTransmision = new ArrayList<>();
        listaCombustible = new ArrayList<>();
        spinnerMarca = viewRVehiculos.findViewById(R.id.spinnerMarca);
        spinnerModelo = viewRVehiculos.findViewById(R.id.spinnerModelo);
        spinnerTipo = viewRVehiculos.findViewById(R.id.spinnerTipo);
        spinnerTransmision = viewRVehiculos.findViewById(R.id.spinnerTransmision);
        spinnerCombustible = viewRVehiculos.findViewById(R.id.spinnerCombustible);

        imputId = viewRVehiculos.findViewById(R.id.imput_id);
        imputColor= viewRVehiculos.findViewById(R.id.imput_color);
        imputAnio= viewRVehiculos.findViewById(R.id.imput_anio);
        imputCilindraje= viewRVehiculos.findViewById(R.id.imput_cilindraje);
        imputPrecioVenta= viewRVehiculos.findViewById(R.id.imput_precio_venta);
        btnRegistrar = viewRVehiculos.findViewById(R.id.btnGuardar);

        img1 = viewRVehiculos.findViewById(R.id.imagen_carr_1);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

        getMarcas();
        getTipos();
        getTransmision();
        getCombustible();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarVehiculo();
            }
        });

        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Marca m = (Marca) adapterView.getAdapter().getItem(i);
                //Toast.makeText(getContext(), ""+m.getId(), Toast.LENGTH_LONG).show();
                getModelos(m.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return viewRVehiculos;
    }







    //********************************************************************
    //Alert Dialog de Cargar Imagen
    //********************************************************************

    private void cargarImagen(){
        final CharSequence[] opciones = {"Tomar Foto", "Seleccionar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                } else if (opciones[i].equals("Seleccionar Imagen")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccione una aplicación"), COD_SELECCIONAR);
                } else {
                    dialogInterface.dismiss();
                }
            }
            });

        alertOpciones.show();
    }






    //********************************************************************
    // Tomar fotografía
    //********************************************************************

    private void tomarFotografia(){
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen="";

        if(!isCreada){
            isCreada = fileImagen.mkdir();
        }
        if(isCreada){
            nombreImagen = (System.currentTimeMillis()/1000)+".jpg";
        }

       path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent, COD_CAMARA);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONAR:
                    Uri miPath = data.getData();
                    img1.setImageURI(miPath);
                    break;
                case COD_CAMARA:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                        @Override
                        public void onMediaScannerConnected() {
                            Log.i("Ruta de almacenamiento", "Path: " + path);
                        }
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });

                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    img1.setImageBitmap(bitmap);
                    break;
            }
        }
    }





    //********************************************************************
    // Registro de los vehículos al servidor
    //********************************************************************

    private void registrarVehiculo(){
        progress = new ProgressDialog(getContext());
        progress.setMessage("Registrando...");
        progress.show();
        final Marca marca = (Marca)spinnerMarca.getSelectedItem();
        final Modelo modelo = (Modelo)spinnerModelo.getSelectedItem();
        final Combustible combustible = (Combustible) spinnerCombustible.getSelectedItem();
        final Transmision transmision = (Transmision) spinnerTransmision.getSelectedItem();
        final Tipo tipo = (Tipo) spinnerTipo.getSelectedItem();

        String url = ServerConfig.URL_BASE+ "addCar.php?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    /*imputId.setText("");
                    imputColor.setText("");
                    imputAnio.setText("");
                    imputCilindraje.setText("");
                    imputPrecioVenta.setText("");
                    progress.hide();*/
                    Toast.makeText(getContext(),"Registro Completo",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se pudo registrar",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idVehiculo", imputId.getText().toString());
                params.put("fkMarca", String.valueOf(marca.getId()));
                params.put("fkModelo", String.valueOf(modelo.getModelo()));
                params.put("fkTransmision", String.valueOf(transmision.getIdTransmision()));
                params.put("fkCombustible", String.valueOf(combustible.getIdCombustible()));
                params.put("fkTipo", String.valueOf(tipo.getIdTipo()));
                params.put("color", imputColor.getText().toString());
                params.put("anio", imputAnio.getText().toString());
                params.put("cilindraje", imputCilindraje.getText().toString());
                params.put("precioVenta", imputPrecioVenta.getText().toString());
                return params;
            }
        };
        progress.hide();
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(stringRequest);
    }





    //********************************************************************
    // obtiene las marcas de vehículos desde la base de datos remota
    //********************************************************************

    private void getMarcas() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();
        String url = ServerConfig.URL_BASE + "getMarcas.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                llenarSpinnerMarca(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error al cargar las marcas",Toast.LENGTH_LONG).show();
                progress.hide();
            }
        });
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(jsonObjectRequest);
    }





    //********************************************************************
    // obtiene los modelos de vehículos desde la base de datos remota
    //********************************************************************

    private void getModelos(int id) {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();
        final Marca marca = (Marca)spinnerMarca.getSelectedItem();
        String url = ServerConfig.URL_BASE + "getModelos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject id = new JSONObject(response);
                    llenarSpinnerModelo(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(marca.getId()));
                return params;
            }
        };

        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(stringRequest);
    }





    //********************************************************************
    // obtiene los tipos de vehículos desde la base de datos remota
    //********************************************************************

    private void getTipos() {
        String url = ServerConfig.URL_BASE + "getTipo.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                llenarSpinnerTipo(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error al cargar los tipos",Toast.LENGTH_LONG).show();
                progress.hide();
            }
        });
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(jsonObjectRequest);
    }






    //********************************************************************
    // obtiene los tipos de transmiision de vehículos desde la base de datos remota
    //********************************************************************

    private void getTransmision() {
        String url = ServerConfig.URL_BASE + "getTransmision.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                llenarSpinnerTransmision(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error al cargar la transmision",Toast.LENGTH_LONG).show();
                progress.hide();
            }
        });
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(jsonObjectRequest);
    }





    //********************************************************************
    // obtiene los tipos de Combustible desde la base de datos remota
    //********************************************************************

    private void getCombustible() {
        String url = ServerConfig.URL_BASE + "getCombustible.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                llenarSpinnerCombustible(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error al cargar el combustible",Toast.LENGTH_LONG).show();
                progress.hide();
            }
        });
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(jsonObjectRequest);
    }





    //********************************************************************
    // llenar el spnner de la marca del vehículo
    //********************************************************************

    private void llenarSpinnerMarca(JSONObject response){
        Marca marca = null;
        JSONArray json = response.optJSONArray("datos");

        try {
            for (int i = 0; i<json.length(); i ++){
                marca = new Marca();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                marca.setId(jsonObject.optInt("id"));
                marca.setDescripcion(jsonObject.optString("descripcion"));
                listaMarca.add(marca);
            }
            progress.hide();
            ArrayAdapter<Marca> arrayAdapterMarca = new ArrayAdapter<Marca>(getContext(),android.R.layout.simple_dropdown_item_1line, listaMarca);
            spinnerMarca.setAdapter(arrayAdapterMarca);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al llenar spinner Marca\n" + e, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }






    //********************************************************************
    //llenar el spinner del modelo del vehículo
    //********************************************************************

    private void llenarSpinnerModelo(JSONObject response){
        listaModelo.clear();
        Modelo modelo = null;
        JSONArray json = response.optJSONArray("modelo");

        try {
            for (int i = 0; i<json.length(); i ++){
                modelo = new Modelo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                modelo.setDescripcionModelo(jsonObject.optString("descripcionModelo"));
                modelo.setModelo(jsonObject.optInt("idModelo"));
                //modelo.setMarca(jsonObject.optInt("idMarca"));
                listaModelo.add(modelo);
            }
            progress.hide();
            ArrayAdapter<Modelo> arrayAdapterModelo = new ArrayAdapter<Modelo>(getContext(),android.R.layout.simple_dropdown_item_1line, listaModelo);
            spinnerModelo.setAdapter(arrayAdapterModelo);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al llenar spinner Modelo\n" + e, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }






    //********************************************************************
    // llenar el spinner de tipo de vehiculo
    //********************************************************************
    private void llenarSpinnerTipo(JSONObject response){
        listaTipo.clear();
        Tipo tipo = null;
        JSONArray json = response.optJSONArray("tipo");

        try {
            for (int i = 0; i<json.length(); i ++){
                tipo = new Tipo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                tipo.setDescripcionTipo(jsonObject.optString("descripcionTipo"));
                tipo.setIdTipo(jsonObject.optInt("idTipo"));
                listaTipo.add(tipo);
            }
            progress.hide();
            ArrayAdapter<Tipo> arrayAdapterTipo = new ArrayAdapter<Tipo>(getContext(),android.R.layout.simple_dropdown_item_1line, listaTipo);
            spinnerTipo.setAdapter(arrayAdapterTipo);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al llenar spinner Tipo\n" + e, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }






    //********************************************************************
    // llenar el spinner de tipo de transmision
    //********************************************************************
    private void llenarSpinnerTransmision(JSONObject response){
        listaTransmision.clear();
        Transmision transmision = null;
        JSONArray json = response.optJSONArray("transmision");

        try {
            for (int i = 0; i<json.length(); i ++){
                transmision = new Transmision();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                transmision.setDescripcionTransmision(jsonObject.optString("descripcionTransmision"));
                transmision.setIdTransmision(jsonObject.optInt("idTransmision"));
                listaTransmision.add(transmision);
            }
            progress.hide();
            ArrayAdapter<Transmision> arrayAdapterTransmision = new ArrayAdapter<Transmision>(getContext(),android.R.layout.simple_dropdown_item_1line, listaTransmision);
            spinnerTransmision.setAdapter(arrayAdapterTransmision);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al llenar spinner Transmision\n" + e, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }






    //********************************************************************
    // llenar el spinner de tipo de combustible
    //********************************************************************

    private void llenarSpinnerCombustible(JSONObject response){
        listaCombustible.clear();
        Combustible combustible = null;
        JSONArray json = response.optJSONArray("combustible");

        try {
            for (int i = 0; i<json.length(); i ++){
                combustible = new Combustible();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                combustible.setDescripcionCombustible(jsonObject.optString("descripcionCombustible"));
                combustible.setIdCombustible(jsonObject.optInt("idCombustible"));
                listaCombustible.add(combustible);
            }
            progress.hide();
            ArrayAdapter<Combustible> arrayAdapterCombustible = new ArrayAdapter<Combustible>(getContext(),android.R.layout.simple_dropdown_item_1line, listaCombustible);
            spinnerCombustible.setAdapter(arrayAdapterCombustible);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al llenar spinner Combustible\n" + e, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
