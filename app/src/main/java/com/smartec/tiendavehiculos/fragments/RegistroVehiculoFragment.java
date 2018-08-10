package com.smartec.tiendavehiculos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.entidades.Marca;
import com.smartec.tiendavehiculos.entidades.Modelo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroVehiculoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroVehiculoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RegistroVehiculoFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Spinner spinnerMarca;
    ArrayList<Marca> listaMarca;
    private Spinner spinnerModelo;
    ArrayList<Modelo> listaModelo;

    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegistroVehiculoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroVehiculoFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRVehiculos = inflater.inflate(R.layout.fragment_registro_vehiculo, container, false);
        listaMarca = new ArrayList<>();
        spinnerMarca = viewRVehiculos.findViewById(R.id.spinnerMarca);

        listaModelo = new ArrayList<>();
        spinnerModelo = viewRVehiculos.findViewById(R.id.spinnerModelo);
        request = Volley.newRequestQueue(getContext());
        cargarMarcas();
        return viewRVehiculos;
    }

    private void cargarMarcas() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();
        String url = "http://192.168.47.1:9001/appVehiculos/getMarcas.php";
        String url2 = "http://192.168.47.1:9001/appVehiculos/getModelos.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url2,null,this,this);
        request.add(jsonObjectRequest);
    }

    private void cargarModelos() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando...");
        progress.show();
        String url = "http://192.168.0.19:9001/appVehiculos/getModelos.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede Conectar" + error.toString(),Toast.LENGTH_LONG).show();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        llenarSpinnerMarca(response);
        //llenarSpinnerModelo(response);

    }

    private void llenarSpinnerMarca(JSONObject response){
        Marca marca = null;
        JSONArray json = response.optJSONArray("marcas");
        int vuelta = 0;
        try {
            for (int i = 0; i<json.length(); i ++){
                marca = new Marca();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                marca.setDescripcion(jsonObject.optString("descripcionMarca"));
                listaMarca.add(marca);
                vuelta = i;
            }
            progress.hide();
            ArrayAdapter<Marca> arrayAdapterMarca = new ArrayAdapter<Marca>(getContext(),android.R.layout.simple_dropdown_item_1line, listaMarca);
            spinnerMarca.setAdapter(arrayAdapterMarca);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), " Vuelta" + vuelta+"No se ha podido establecer conexión con el servidor. " + e, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void llenarSpinnerModelo(JSONObject response){
        Modelo modelo = null;
        JSONArray json = response.optJSONArray("modelos");

        try {
            for (int i = 0; i<json.length(); i ++){
                modelo = new Modelo();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                modelo.setDescripcionModelo(jsonObject.optString("descripcionModelo"));
                listaModelo.add(modelo);
            }
            progress.hide();
            ArrayAdapter<Modelo> arrayAdapterModelo = new ArrayAdapter<Modelo>(getContext(),android.R.layout.simple_dropdown_item_1line, listaModelo);
            spinnerModelo.setAdapter(arrayAdapterModelo);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" + e, Toast.LENGTH_LONG).show();
        }
    }
}
