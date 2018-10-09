package com.smartec.tiendavehiculos.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smartec.tiendavehiculos.R;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class RegistroUsuariosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String path;
    private final String CARPETA_PRINCIPAL ="misImagenesApp/";
    private final String CARPETA_IMAGEN ="imagenes";
    private final String DIRECTORIO_IMAGEN= CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    private final int COD_SELECCIONA = 10;
    private final int COD_FOTO = 20;
    Bitmap bitmap;
    File fileImagen;

    ImageView fotoUsuario;
    TextInputEditText nombres, apellidos,direccion, email, telefono, celular,nombreUsuario, contraseña;
    Button botonRegistrar;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    private OnFragmentInteractionListener mListener;

    public RegistroUsuariosFragment() {
        // Required empty public constructor
    }



    public static RegistroUsuariosFragment newInstance(String param1, String param2) {
        RegistroUsuariosFragment fragment = new RegistroUsuariosFragment();
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
        View vista =  inflater.inflate(R.layout.fragment_registro_usuarios, container, false);

        fotoUsuario = (ImageView)vista.findViewById(R.id.imageView);
        nombres = (TextInputEditText) vista.findViewById(R.id.textNombres);
        apellidos = (TextInputEditText)vista.findViewById(R.id.textApellidos);
        direccion =  (TextInputEditText)vista.findViewById(R.id.textDireccion);
        email = (TextInputEditText)vista.findViewById(R.id.textEmail);
        celular = (TextInputEditText)vista.findViewById(R.id.textCelular);
        contraseña = (TextInputEditText)vista.findViewById(R.id.textContrasenia);
        telefono = (TextInputEditText)vista.findViewById(R.id.textTelefono);
        nombreUsuario = (TextInputEditText)vista.findViewById(R.id.textUsuario);
        botonRegistrar = (Button) vista.findViewById(R.id.buttonRegistrar);


        //requestQueue = Volley.newRequestQueue(getContext());
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
            }
        });

        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

      if(validaPermisos()){
            botonRegistrar.setEnabled(true);

        }else{
            botonRegistrar.setEnabled(false);
        }

        return vista;
    }

    private boolean validaPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return  true;
        }

        if((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))|| (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }
        else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }


        return  false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                botonRegistrar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }
    }
 private void solicitarPermisosManual() {

        final CharSequence[] opciones = {"si",  "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("si")){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                   Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();


    }


    private void cargarImagen(){
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{

                    if(opciones[i].equals("Cargar Imagen")){
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),COD_SELECCIONA);

                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });

        alertOpciones.show();
    }

    private void tomarFotografia() {

        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();
        String nombre ="";

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true) {
            nombre = (System.currentTimeMillis() / 1000) + ".jpg";

        }
            path= Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+
                    File.separator+nombre;

            fileImagen= new File(path);

            Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

           if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
             String authorities = getContext().getApplicationContext().getPackageName()+".provider";
             Uri imageUri = FileProvider.getUriForFile(getContext(),authorities,fileImagen);
             intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            }else {
               intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }

            startActivityForResult(intent,COD_FOTO);
        }


    private void cargarWebService() {
        String url = "http://192.168.0.11/appVehiculo/registroUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equalsIgnoreCase("registra")) {
                    nombres.setText("");
                    apellidos.setText("");
                    direccion.setText("");
                    celular.setText("");
                    telefono.setText("");
                    contraseña.setText("");
                    nombreUsuario.setText("");
                    email.setText("");

                    Toast.makeText(getContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"No se ha registrado con exito",Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                    String campoNombres = nombres.getText().toString();
                    String campoApellidos = apellidos.getText().toString();
                    String campoEmail = email.getText().toString();
                    String campoTelefono = telefono.getText().toString();
                    String campoCelular = celular.getText().toString();
                    String campoNombreUsuario = nombreUsuario.getText().toString();
                    String campoContrasenia = contraseña.getText().toString();
                    String campoDireccion = direccion.getText().toString();
                    String imagen = convertirImagenString(bitmap);

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("nombres",campoNombres);
                    parametros.put("apellidos",campoApellidos);
                    parametros.put("email",campoEmail);
                    parametros.put("telefono",campoTelefono);
                    parametros.put("celular",campoCelular);
                    parametros.put("nombreUsuario",campoNombreUsuario);
                    parametros.put("contrasenia",campoContrasenia);
                    parametros.put("direccion",campoDireccion);
                    parametros.put("imagen",imagen);

                    return parametros;
            }
        };

        requestQueue.add(stringRequest );

    }

    private String convertirImagenString(Bitmap bitmap) {

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,arrayOutputStream);
        byte[] imagenByte = arrayOutputStream .toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){

            switch (requestCode){
                case COD_SELECCIONA:

                    Uri miPath = data.getData();
                    //fotoUsuario.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                        fotoUsuario.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: "+path);
                                }
                            });

                    bitmap = BitmapFactory.decodeFile(path);
                    fotoUsuario.setImageBitmap(bitmap);
                    break;

            }


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
        void onFragmentInteraction(Uri uri);
    }
}
