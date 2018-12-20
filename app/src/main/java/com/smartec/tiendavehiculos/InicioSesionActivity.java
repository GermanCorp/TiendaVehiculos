package com.smartec.tiendavehiculos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InicioSesionActivity extends AppCompatActivity {

    Button consultar, registrar;
    TextInputEditText txtUsuario, txtContrasena;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    Usuario usuario = new Usuario();
    int resultado = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtUsuario = findViewById(R.id.textInputUsuario);
        txtContrasena = findViewById(R.id.textInputPasword);

        requestQueue = Volley.newRequestQueue(InicioSesionActivity.this);

        consultar = findViewById(R.id.buttonIniciarSesion);
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtUsuario.getText().toString().isEmpty()
                        && txtContrasena.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese el usuario y la contraseña", Toast.LENGTH_SHORT).show();
                } else if (txtUsuario.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese el Usuario", Toast.LENGTH_SHORT).show();
                } else if (txtContrasena.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese la Contraseña", Toast.LENGTH_SHORT).show();

                } else {

                    consultarUsuario();
                }
            }
        });

        registrar = findViewById(R.id.button);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroUsuariosActivity.class);
                intent.putExtra("funcion", "Guardar");
                startActivity(intent);
            }
        });

    }

    private void consultarUsuario() {
        String url = ServerConfig.URL_BASE + "iniciarSesion.php?";
        resultado = 0;

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //txtUsuario.setText("");
                //txtContrasena.setText("");

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray json = jsonObject.optJSONArray("usuarios");

                    /*if (json.length() <= 0) {
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject object = null;
                            object = json.getJSONObject(i);
                            usuario.setId(object.optInt("idUsuarios"));
                        }
                        Intent intent = new Intent(getApplicationContext(), ActividadPrincipal.class);
                        startActivity(intent);
                    }*/

                    int idUsuario = 0;
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject object = null;
                        object = json.getJSONObject(i);
                        usuario.setId(object.optInt("idUsuarios"));
                        resultado = usuario.getId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mensaje();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombreUsuario = txtUsuario.getText().toString();
                String password = txtContrasena.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", nombreUsuario);
                parametros.put("contrasena", password);

                return parametros;

            }

        };
        requestQueue.add(stringRequest);

    }

    private void mensaje() {
        if (resultado<=0) {
            txtUsuario.setText("");
            txtContrasena.setText("");
            Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            //resultado = 1;
        } else {
            txtUsuario.setText("");
            txtContrasena.setText("");
            Intent intent = new Intent(getApplicationContext(), ActividadPrincipal.class);
            startActivity(intent);
        }
    }
}