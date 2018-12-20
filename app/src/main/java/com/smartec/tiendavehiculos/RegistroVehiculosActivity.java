package com.smartec.tiendavehiculos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.entidades.Combustible;
import com.smartec.tiendavehiculos.entidades.Marca;
import com.smartec.tiendavehiculos.entidades.Modelo;
import com.smartec.tiendavehiculos.entidades.Tipo;
import com.smartec.tiendavehiculos.entidades.Transmision;
import com.smartec.tiendavehiculos.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegistroVehiculosActivity extends AppCompatActivity {

    private final String            CARPETA_RAIZ = "misImagenesPrueba/";
    private final String            RUTA_IMAGEN = CARPETA_RAIZ+"misFotos";
    private final int               COD_SELECCIONAR = 10;
    private final int               COD_CAMARA = 20;
    private String                  path;
    private Spinner                 spinnerMarca;
    private Spinner                 spinnerModelo;
    private Spinner                 spinnerTipo;
    private Spinner                 spinnerTransmision;
    private Spinner                 spinnerCombustible;
    private ArrayList<Marca>        listaMarca;
    private ArrayList<Modelo>       listaModelo;
    private ArrayList<Tipo>         listaTipo;
    private ArrayList<Transmision>  listaTransmision;
    private ArrayList<Combustible>  listaCombustible;
    private EditText                imputColor;
    private EditText                imputAnio;
    private EditText                imputCilindraje;
    private EditText                imputPrecioVenta;
    private Button                  btnRegistrar;
    private ProgressDialog          progress;
    private Bitmap                  bitmap;
    ImageView                       imagen1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_vehiculos);

        listaMarca          = new ArrayList<>();
        listaModelo         = new ArrayList<>();
        listaTipo           = new ArrayList<>();
        listaTransmision    = new ArrayList<>();
        listaCombustible    = new ArrayList<>();
        spinnerMarca        = findViewById(R.id.spinnerMarca);
        spinnerModelo       = findViewById(R.id.spinnerModelo);
        spinnerTipo         = findViewById(R.id.spinnerTipo);
        spinnerTransmision  = findViewById(R.id.spinnerTransmision);
        spinnerCombustible  = findViewById(R.id.spinnerCombustible);
        imputColor          = findViewById(R.id.imput_color);
        imputAnio           = findViewById(R.id.imput_anio);
        imputCilindraje     = findViewById(R.id.imput_cilindraje);
        imputPrecioVenta    = findViewById(R.id.imput_precio_venta);
        btnRegistrar        = findViewById(R.id.btnGuardar);
        imagen1             = findViewById(R.id.imagen_carr_1);

        if(validarPermisos()){
            imagen1.setEnabled(true);
        }else {
            imagen1.setEnabled(false);
        }

        imagen1.setOnClickListener(new View.OnClickListener() {
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
                if(
                        imputColor.getText().toString().isEmpty()
                        &&imputAnio.getText().toString().isEmpty()
                        &&imputCilindraje.getText().toString().isEmpty()
                        &&imputPrecioVenta.getText().toString().isEmpty()
                        &&bitmap == null
                        ){
                    Toast.makeText(getApplicationContext(), "Ingrese toda la información",Toast.LENGTH_LONG).show();
                    }else if(imputColor.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Describa el color",Toast.LENGTH_LONG).show();
                }else if(imputAnio.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Describa el año",Toast.LENGTH_LONG).show();
                }else if(imputCilindraje.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Describa el cilindraje",Toast.LENGTH_LONG).show();
                }else if(imputPrecioVenta.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Describa el precio de venta",Toast.LENGTH_LONG).show();
                }else if(bitmap == null){
                    Toast.makeText(getApplicationContext(), "Seleccione una imagen para el vehículo",Toast.LENGTH_LONG).show();
                }else{
                    registrarVehiculo();
                    finish();
                }
            }
        });

        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Marca m = (Marca) adapterView.getAdapter().getItem(i);
                //Toast.makeText(this, ""+m.getId(), Toast.LENGTH_LONG).show();
                getModelos(m.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    
        //********************************************************************
        //Validar permisos para acceder al almacenamiento del movil
        //********************************************************************
        private boolean validarPermisos() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true;
            }

            if ((this.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                    (this.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                return true;
            }

            if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                    (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
                cargarDialogoRecomendacion();
            } else {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
            return false;
        }




        //********************************************************************
        //Solicitar el permiso para acceder a la memoria de manera manual
        //********************************************************************
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if(requestCode == 100){
                if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    imagen1.setEnabled(true);
                }else {
                    solicitarPermisosManual();
                }
            }
        }




        //********************************************************************
        //Solicitar permisos de manera manual
        //********************************************************************
        private void solicitarPermisosManual() {
            final CharSequence[] opciones = {"Si", "No"};
            final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
            alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
            alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (opciones[i].equals("Si")) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Los Permisos no fueron aceptados",Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss(); // cierra el diálogo
                    }
                }
            });

            alertOpciones.show();
        }




        //********************************************************************
        //Recomienda al usuario aceptar las recomendaciones de aceptar los permisos
        //********************************************************************
        private void cargarDialogoRecomendacion() {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Permisos Desactivados");
            dialogo.setMessage("Debe aceptar los permisos para el corrector funcionamiento de la aplicación");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
                    }
                }
            });
            dialogo.show();
        }






        //********************************************************************
        //Alert Dialog con las opciones de Cargar la Imagen
        //********************************************************************
        private void cargarImagen(){
            final CharSequence[] opciones = {"Tomar Foto", "Seleccionar Imagen", "Cancelar"};
            final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
            alertOpciones.setTitle("Seleccione una opción");
            alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (opciones[i].equals("Tomar Foto")) {
                        tomarFotografia();
                    } else if (opciones[i].equals("Seleccionar Imagen")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione una aplicación"), COD_SELECCIONAR);
                    } else {
                        dialogInterface.dismiss(); // cierra el diálogo
                    }
                }
            });

            alertOpciones.show();
        }






        //********************************************************************
        // Tomar fotografía
        //********************************************************************
        private void tomarFotografia() {
            File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
            boolean isCreada = fileImagen.exists();
            String nombreImagen = "";

            if (!isCreada) {
                isCreada = fileImagen.mkdirs();
            }
            if (isCreada) {
                nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
            }

            path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen;
            File imagen = new File(path);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = this.getPackageName() + ".provider";
                Uri imageUri = FileProvider.getUriForFile(this, authorities, imagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
            }
            startActivityForResult(intent, COD_CAMARA);
        }





        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==RESULT_OK){
                switch (requestCode){

                    case COD_SELECCIONAR:
                        Uri miPath = data.getData();
                        imagen1.setImageURI(miPath);
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case COD_CAMARA:
                        MediaScannerConnection.scanFile(this, new String[]{path}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("Ruta de almacenamiento", "Path" + path);
                                    }
                                });
                        bitmap = BitmapFactory.decodeFile(path);
                        imagen1.setImageBitmap(bitmap);
                        break;
                }
                bitmap = redimencionarImagen(bitmap, 800, 600);
            }
        }





    //********************************************************************
    // Redimensionar imagen
    //********************************************************************
        private Bitmap redimencionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
            int ancho = bitmap.getWidth();
            int alto = bitmap.getHeight();

            if(ancho>anchoNuevo || alto>altoNuevo){
                float escalaAncho = anchoNuevo /ancho;
                float escalaAlto = altoNuevo/alto;
                Matrix matrix = new Matrix();
                matrix.postScale(escalaAncho, escalaAlto);
                return Bitmap.createBitmap(bitmap, 0,0, ancho, alto, matrix, false);
            }else {
                return bitmap;
            }
        }


        //********************************************************************
        // Registro de los vehículos al servidor
        //********************************************************************
        private void registrarVehiculo(){
            progress = new ProgressDialog(this);
            progress.setMessage("Registrando...");
            progress.show();
            final Marca marca = (Marca)spinnerMarca.getSelectedItem();
            final Modelo modelo = (Modelo)spinnerModelo.getSelectedItem();
            final Combustible combustible = (Combustible) spinnerCombustible.getSelectedItem();
            final Transmision transmision = (Transmision) spinnerTransmision.getSelectedItem();
            final Tipo tipo = (Tipo) spinnerTipo.getSelectedItem();

            String url = ServerConfig.URL_BASE+ "addCar.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    imputColor.setText("");
                    imputAnio.setText("");
                    imputCilindraje.setText("");
                    imputPrecioVenta.setText("");
                    imagen1.setImageBitmap(null);
                    if(response.trim().equalsIgnoreCase("registra")){
                        Toast.makeText(getApplicationContext(),"Registro Completo",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"No se pudo registrar",Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"No se pudo conectar",Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String imagen = convertirImgString(bitmap);

                    Map<String, String> params = new HashMap<>();
                    params.put("fkMarca", String.valueOf(marca.getId()));
                    params.put("fkModelo", String.valueOf(modelo.getModelo()));
                    params.put("fkTransmision", String.valueOf(transmision.getIdTransmision()));
                    params.put("fkCombustible", String.valueOf(combustible.getIdCombustible()));
                    params.put("fkTipo", String.valueOf(tipo.getIdTipo()));
                    params.put("color", imputColor.getText().toString());
                    params.put("anio", imputAnio.getText().toString());
                    params.put("cilindraje", imputCilindraje.getText().toString());
                    params.put("precioVenta", imputPrecioVenta.getText().toString());
                    params.put("imagen", imagen);
                    params.put("usuario", Usuario.getId().toString());
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            progress.hide();
            RequestQueue request = Volley.newRequestQueue(this);
            request.add(stringRequest);
        }


        //********************************************************************
        //Convirete la imagen en un string
        //********************************************************************
        private String convertirImgString(Bitmap bitmap){
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
            byte[] imagenByte = array.toByteArray();
            String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

            return imagenString;
        }




        //********************************************************************
        // obtiene las marcas de vehículos desde la base de datos remota
        //********************************************************************

        private void getMarcas() {
            progress = new ProgressDialog(this);
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
                    Toast.makeText(getApplicationContext(),"Error al cargar las marcas",Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            });
            RequestQueue request = Volley.newRequestQueue(this);
            request.add(jsonObjectRequest);
        }





        //********************************************************************
        // obtiene los modelos de vehículos desde la base de datos remota
        //********************************************************************

        private void getModelos(int id) {
            progress = new ProgressDialog(this);
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

            RequestQueue request = Volley.newRequestQueue(this);
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
                    Toast.makeText(getApplicationContext(),"Error al cargar los tipos",Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            });
            RequestQueue request = Volley.newRequestQueue(this);
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
                    Toast.makeText(getApplicationContext(),"Error al cargar la transmision",Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            });
            RequestQueue request = Volley.newRequestQueue(this);
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
                    Toast.makeText(getApplicationContext(),"Error al cargar el combustible",Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            });
            RequestQueue request = Volley.newRequestQueue(this);
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
                ArrayAdapter<Marca> arrayAdapterMarca = new ArrayAdapter<Marca>(this,android.R.layout.simple_spinner_dropdown_item, listaMarca);
                spinnerMarca.setAdapter(arrayAdapterMarca);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al llenar spinner Marca\n" + e, Toast.LENGTH_LONG).show();
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
                ArrayAdapter<Modelo> arrayAdapterModelo = new ArrayAdapter<Modelo>(this,android.R.layout.simple_dropdown_item_1line, listaModelo);
                spinnerModelo.setAdapter(arrayAdapterModelo);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al llenar spinner Modelo\n" + e, Toast.LENGTH_LONG).show();
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
                ArrayAdapter<Tipo> arrayAdapterTipo = new ArrayAdapter<Tipo>(this,android.R.layout.simple_dropdown_item_1line, listaTipo);
                spinnerTipo.setAdapter(arrayAdapterTipo);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al llenar spinner Tipo\n" + e, Toast.LENGTH_LONG).show();
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
                ArrayAdapter<Transmision> arrayAdapterTransmision = new ArrayAdapter<Transmision>(this,android.R.layout.simple_dropdown_item_1line, listaTransmision);
                spinnerTransmision.setAdapter(arrayAdapterTransmision);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al llenar spinner Transmision\n" + e, Toast.LENGTH_LONG).show();
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
                ArrayAdapter<Combustible> arrayAdapterCombustible = new ArrayAdapter<Combustible>(this,android.R.layout.simple_dropdown_item_1line, listaCombustible);
                spinnerCombustible.setAdapter(arrayAdapterCombustible);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al llenar spinner Combustible\n" + e, Toast.LENGTH_LONG).show();
                progress.hide();
            }
        }
}
