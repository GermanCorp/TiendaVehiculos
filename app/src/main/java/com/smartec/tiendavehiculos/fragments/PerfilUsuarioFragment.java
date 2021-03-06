package com.smartec.tiendavehiculos.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.ActualizarUsuarioActivity;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.RegistroUsuariosActivity;
import com.smartec.tiendavehiculos.ServerConfig;
import com.smartec.tiendavehiculos.entidades.Modelo;
import com.smartec.tiendavehiculos.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilUsuarioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerfilUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilUsuarioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView campoNombres, campoApellidos, campoDireccion, campoEmail, campoTelefono, campoCelular, campoNombreUsuario, campoContrasenia;
    ImageView fotoUsuario;
    Button botonEditar;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    Usuario usuario = new Usuario();
    ActualizarUsuarioActivity actualizarUsuarioActivity = new ActualizarUsuarioActivity();
    private SwipeRefreshLayout refreshLayout;

    private OnFragmentInteractionListener mListener;

    public PerfilUsuarioFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static PerfilUsuarioFragment newInstance(String param1, String param2) {
        PerfilUsuarioFragment fragment = new PerfilUsuarioFragment();
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

        View vista = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        fotoUsuario = vista.findViewById(R.id.imagenUsuario);
        campoNombres = vista.findViewById(R.id.nombres);
        campoApellidos = vista.findViewById(R.id.apellidos);
        campoDireccion = vista.findViewById(R.id.direccion);
        campoEmail = vista.findViewById(R.id.email);
        campoCelular = vista.findViewById(R.id.celular);
        campoContrasenia = vista.findViewById(R.id.contrasena);
        campoTelefono = vista.findViewById(R.id.telefono);
        campoNombreUsuario = vista.findViewById(R.id.nombreUsuario);
        botonEditar = vista.findViewById(R.id.botoneditar);

        refreshLayout = (SwipeRefreshLayout) vista.findViewById(R.id.refreshPerfil);

        // Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        consultaUsuario();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );


        requestQueue = Volley.newRequestQueue(getContext());
        consultaUsuario();
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActualizarUsuarioActivity.class);
                startActivity(intent);
            }

        });

        return vista;

    }



    private void consultaUsuario() {
        String url = ServerConfig.URL_BASE + "consultarUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    llenarUsuario(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", usuario.getId().toString());//""+usuario.getId()

                return parametros;

            }

        };

        requestQueue.add(stringRequest);


    }

    private void llenarUsuario(JSONObject jsonObject) {
        JSONArray json = jsonObject.optJSONArray("usuarios");

        try {
            for (int i = 0; i < json.length(); i++) {

                JSONObject object = null;
                object = json.getJSONObject(i);

                usuario.setNombres(object.optString("nombres"));
                usuario.setApellidos(object.optString("apellidos"));
                usuario.setNombreUsuario(object.optString("nombreUsuario"));
                usuario.setDireccion(object.optString("direccion"));
                usuario.setTelefono(object.optString("telefono"));
                usuario.setCelular(object.optString("celular"));
                usuario.setEmail(object.optString("email"));
                usuario.setContrasenia(object.optString("pasword"));
                usuario.setFotoPerfil(object.optString("fotoPerfil"));

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        campoNombres.setText(usuario.getNombres());
        campoApellidos.setText(usuario.getApellidos());
        campoDireccion.setText(usuario.getDireccion());
        campoTelefono.setText(usuario.getTelefono());
        campoCelular.setText(usuario.getCelular());
        campoEmail.setText(usuario.getEmail());
        campoNombreUsuario.setText(usuario.getNombreUsuario());
        campoContrasenia.setText(usuario.getContrasenia());

        String urlImagen = ServerConfig.URL_BASE + usuario.getFotoPerfil();
        cargarWebServiceImagen(urlImagen);

        actualizarUsuarioActivity.nombresU = usuario.getNombres();
        actualizarUsuarioActivity.apellidosU = usuario.getApellidos();
        actualizarUsuarioActivity.direccionU = usuario.getDireccion();
        actualizarUsuarioActivity.telefonoU = usuario.getTelefono();
        actualizarUsuarioActivity.celularU = usuario.getCelular();
        actualizarUsuarioActivity.nombreUsuarioU = usuario.getNombreUsuario();
        actualizarUsuarioActivity.contrasenaU = usuario.getContrasenia();
        actualizarUsuarioActivity.emailU = usuario.getEmail();
        actualizarUsuarioActivity.foto = ServerConfig.URL_BASE + usuario.getFotoPerfil();


    }

    private void cargarWebServiceImagen(String urlImagen) {
        urlImagen = urlImagen.replace(" ", "%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoUsuario.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );
        requestQueue.add(imageRequest);
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
