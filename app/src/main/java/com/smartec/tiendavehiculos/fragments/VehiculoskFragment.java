package com.smartec.tiendavehiculos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.ServerConfig;
import com.smartec.tiendavehiculos.adapter.VehiculosImagenAdapter;
import com.smartec.tiendavehiculos.entidades.Marca;
import com.smartec.tiendavehiculos.entidades.VehiculoCard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VehiculoskFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /*private RecyclerView recyclerVehiculos;
    private ArrayList<Vehiculo> listaVehiculos;
    private ProgressDialog progress;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;*/

    RecyclerView recyclerVehiculos;
    ArrayList<VehiculoCard> listaVehiculos;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;


    public VehiculoskFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VehiculoskFragment newInstance(String param1, String param2) {
        VehiculoskFragment fragment = new VehiculoskFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_vehiculosk,container,false);
        listaVehiculos  = new ArrayList<>();

        recyclerVehiculos = vista.findViewById(R.id.vehiculosRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerVehiculos.setLayoutManager(linearLayoutManager);
        recyclerVehiculos.setHasFixedSize(true);
        request = Volley.newRequestQueue(getContext());

        cargarListaVehiculos();

        //VehiculosImagenAdapter vehiculosAdapterRecyclerView = new VehiculosImagenAdapter(buildVehiculo(), R.layout.cardview_vehiculo, getActivity());
        //recyclerVehiculos.setAdapter(vehiculosAdapterRecyclerView);
        return vista;
    }

    private void cargarListaVehiculos() {
        String url = ServerConfig.URL_BASE + "getVehiculosCard.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VehiculoCard vehiculo= null;
                JSONArray json = response.optJSONArray("vehiculo");

                try {
                    for (int i = 0; i<json.length(); i ++){
                        vehiculo = new VehiculoCard();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        vehiculo.setDescripcionMarca(jsonObject.optString("descripcionMarca"));
                        vehiculo.setDescripcionModelo(jsonObject.optString("descripcionModelo"));
                        vehiculo.setAnio(jsonObject.optString("anio"));
                        vehiculo.setColor(jsonObject.optString("color"));
                        vehiculo.setCilindraje(jsonObject.optString("cilindraje"));
                        vehiculo.setPrecioVenta(jsonObject.optString("precioVenta"));
                        vehiculo.setRutaImagen(jsonObject.optString("imagen"));
                        vehiculo.setIdUsuario(jsonObject.optInt("idUsuario"));
                        vehiculo.setNombres(jsonObject.optString("nombres"));
                        vehiculo.setApellidos(jsonObject.optString("apellidos"));
                        vehiculo.setCelular(jsonObject.optString("celular"));
                        vehiculo.setTelefono(jsonObject.optString("telefono"));
                        vehiculo.setEmail(jsonObject.optString("email"));
                        vehiculo.setFotoPerfil(jsonObject.optString("fotoPerfil"));
                        vehiculo.setDireccion(jsonObject.optString("direccion"));
                        //vehiculo.setImagen(jsonObject.optString("imagen"));
                        listaVehiculos.add(vehiculo);
                    }
                    VehiculosImagenAdapter vehiculosAdapterRecyclerView = new VehiculosImagenAdapter(listaVehiculos, R.layout.cardview_vehiculo, getActivity(), getContext());
                    recyclerVehiculos.setAdapter(vehiculosAdapterRecyclerView);
                    //progress.hide();
                   // ArrayAdapter<Marca> arrayAdapterMarca = new ArrayAdapter<Marca>(getContext(),android.R.layout.simple_spinner_dropdown_item, listaMarca);
                    //spinnerMarca.setAdapter(arrayAdapterMarca);

                }catch (Exception e){
                    //e.printStackTrace();
                    //Toast.makeText(getContext(), "Error al cargar los datos\n" + e, Toast.LENGTH_LONG).show();
                    //progress.hide();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar con el servidor",Toast.LENGTH_LONG).show();
            }
        });
        request = Volley.newRequestQueue(getContext());
        request.add(jsonObjectRequest);

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
