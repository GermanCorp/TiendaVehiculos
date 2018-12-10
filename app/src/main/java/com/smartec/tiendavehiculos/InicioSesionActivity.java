package com.smartec.tiendavehiculos;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.entidades.Usuario;
import com.smartec.tiendavehiculos.fragments.PerfilUsuarioFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InicioSesionActivity extends AppCompatActivity {

    Button consultar;
    TextInputEditText txtUsuario, txtContrasena;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String idUsuario;
    Usuario usuario =  new Usuario();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        txtUsuario = (TextInputEditText)findViewById(R.id.textInputUsuario);
        txtContrasena = (TextInputEditText)findViewById(R.id.textInputPasword);

        requestQueue = Volley.newRequestQueue(InicioSesionActivity.this);

        consultar = (Button)findViewById(R.id.buttonIniciarSesion);
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultarUsuario();
                
            }
        });

    }

    private void consultarUsuario() {
        String url = ServerConfig.URL_BASE+"iniciarSesion.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                txtUsuario.setText("");
                txtContrasena.setText("");



                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray json = jsonObject.optJSONArray("usuarios");
                    if (json.length()<= 0){
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }else{


                        for (int i = 0; i < json.length(); i++) {
                            JSONObject object = null;
                            object = json.getJSONObject(i);

                            usuario.setId(object.optInt("idUsuarios"));
                        }

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent) ;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error de conexion" + error.toString(),Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombreUsuario = txtUsuario.getText().toString();
                String password = txtContrasena.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario",nombreUsuario);
                parametros.put("contrasena",password);

                return parametros;

            }

        };
        requestQueue.add(stringRequest );

    }



}



