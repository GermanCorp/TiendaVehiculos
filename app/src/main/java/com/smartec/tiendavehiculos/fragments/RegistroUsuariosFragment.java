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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.ServerConfig;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static android.app.Activity.RESULT_OK;



public class RegistroUsuariosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

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
    private ProgressDialog progressDialog;

    ImageView fotoUsuario;
    TextInputEditText campoNombres, campoApellidos,campoDireccion, campoEmail, campoTelefono, campoCelular,campoNombreUsuario, campoContrasenia;
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
        campoNombres = (TextInputEditText) vista.findViewById(R.id.textNombres);
        campoApellidos= (TextInputEditText)vista.findViewById(R.id.textApellidos);
        campoDireccion =  (TextInputEditText)vista.findViewById(R.id.textDireccion);
        campoEmail = (TextInputEditText)vista.findViewById(R.id.textEmail);
        campoCelular = (TextInputEditText)vista.findViewById(R.id.textCelular);
        campoContrasenia = (TextInputEditText)vista.findViewById(R.id.textContrasenia);
        campoTelefono = (TextInputEditText)vista.findViewById(R.id.textTelefono);
        campoNombreUsuario = (TextInputEditText)vista.findViewById(R.id.textUsuario);
        botonRegistrar = (Button) vista.findViewById(R.id.buttonRegistrar);


        requestQueue = Volley.newRequestQueue(getContext());
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });


        return vista;
    }

    private void registrarUsuario() {
        /*progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registrando...");
        progressDialog.show();*/

        String url = ServerConfig.URL_BASE+"registroUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("registra")) {
                    campoNombres.setText("");
                    campoApellidos.setText("");
                    campoDireccion.setText("");
                    campoTelefono.setText("");
                    campoCelular.setText("");
                    campoEmail.setText("");
                    campoNombreUsuario.setText("");
                    campoContrasenia.setText("");
                    fotoUsuario.setImageResource(R.mipmap.ic_launcher);


                    Toast.makeText(getContext(), "Registro Exitoso"+response, Toast.LENGTH_SHORT).show();
                    /*}else {
                    Toast.makeText(getContext(), "No se ha registrado con exito", Toast.LENGTH_SHORT).show();
                }*/
                } else {
                    Toast.makeText(getContext(), "No registra"+response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombres =campoNombres.getText().toString();
                String apellidos = campoApellidos.getText().toString();
                String email = campoEmail.getText().toString();
                String telefono = campoTelefono.getText().toString();
                String celular = campoCelular.getText().toString();
                String nombreUsuario = campoNombreUsuario.getText().toString();
                String contrasena =campoContrasenia.getText().toString();
                String direccion = campoDireccion.getText().toString();
                String imagen = convertirImagenString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombres",nombres);
                parametros.put("apellidos",apellidos);
                parametros.put("telefono",telefono);
                parametros.put("celular",celular);
                parametros.put("email",email);
                parametros.put("nombreUsuario",nombreUsuario);
                parametros.put("contrasena",contrasena);
                parametros.put("direccion",direccion);
                parametros.put("imagen",imagen);

                return parametros;

            }

        };
        //progressDialog.hide();
        requestQueue.add(stringRequest );


    }


    private String convertirImagenString(Bitmap bitmap) {

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100
                ,arrayOutputStream);
        byte[] imagenByte = arrayOutputStream .toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
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
