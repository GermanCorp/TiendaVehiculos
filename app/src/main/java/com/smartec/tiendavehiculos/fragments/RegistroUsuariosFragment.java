package com.smartec.tiendavehiculos.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.R;

import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroUsuariosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RegistroUsuariosFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String path;

    private final String CARPETA_RAIZ ="misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    ImageView fotoUsuario;

    TextInputEditText nombres, apellidos, email, telefono, celular,nombreUsuario, contraseña;
    Button botonRegistrar;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private OnFragmentInteractionListener mListener;

    public RegistroUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters

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

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =  inflater.inflate(R.layout.fragment_registro_usuarios, container, false);

        fotoUsuario = (ImageView)vista.findViewById(R.id.imageView);
        nombres = (TextInputEditText) vista.findViewById(R.id.textNombres);
        apellidos = (TextInputEditText)vista.findViewById(R.id.textApellidos);
        email = (TextInputEditText)vista.findViewById(R.id.textEmail);
        celular = (TextInputEditText)vista.findViewById(R.id.textCelular);
        contraseña = (TextInputEditText)vista.findViewById(R.id.textContrasenia);
        telefono = (TextInputEditText)vista.findViewById(R.id.textTelefono);
        nombreUsuario = (TextInputEditText)vista.findViewById(R.id.textUsuario);
        botonRegistrar = (Button) vista.findViewById(R.id.buttonRegistrar);


        requestQueue = Volley.newRequestQueue(getContext());
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
        return vista;
    }

    private void cargarImagen(){
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    Toast.makeText(getContext(),"Tomar Foto",Toast.LENGTH_SHORT).show();
                }else{
                    if(opciones[i].equals("Cargar Imagen")){
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),COD_SELECCIONA);

                    }
                }
            }
        });

        alertOpciones.show();
    }

    private void cargarWebService() {
        String url = "http://192.168.0.12/appVehiculo/registroUsuario.php?nombres="+nombres.getText().toString()+
                "&apellidos="+apellidos.getText().toString()+"&nombreUsuario="+nombreUsuario.getText().toString()+
                "&pasword="+contraseña.getText().toString()+"&celular="+celular.getText().toString()+
                "&telefono="+telefono.getText().toString()+"&email="+email.getText().toString();


        url=url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){

            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    fotoUsuario.setImageURI(miPath);
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path:"+path);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(path);
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

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
