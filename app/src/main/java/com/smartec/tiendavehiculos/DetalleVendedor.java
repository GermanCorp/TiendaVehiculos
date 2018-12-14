package com.smartec.tiendavehiculos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalleVendedor extends AppCompatActivity {

    private RequestQueue request;
    private TextView nombres;
    private String nombreCompleto;
    private TextView celular;
    private TextView telefono;
    private TextView email;
    private TextView direccion;
    private String rutaFoto;
    private CircleImageView fotoVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_vendedor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        request = Volley.newRequestQueue(getApplicationContext());
        nombres = findViewById(R.id.textViewNombresVen);
        celular = findViewById(R.id.textViewCelularVen);
        telefono = findViewById(R.id.textViewTelefonoVen);
        email = findViewById(R.id.textViewEmailVen);
        direccion = findViewById(R.id.textViewDireccionVen);
        fotoVendedor = findViewById(R.id.imagenPerfilVendedor);

        nombreCompleto = (getIntent().getExtras().getString("nombres")) + " " +(getIntent().getExtras().getString("apellidos"));
        nombres.setText(nombreCompleto);
        celular.setText(getIntent().getExtras().getString("celular"));
        telefono.setText(getIntent().getExtras().getString("telefono"));
        email.setText(getIntent().getExtras().getString("email"));
        direccion.setText(getIntent().getExtras().getString("direccion"));
        rutaFoto = getIntent().getExtras().getString("foto");
        cargarImagen(rutaFoto);

    }

    private void cargarImagen(String rutaImagen) {
        String urlImagen = rutaImagen;
        urlImagen = urlImagen.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoVendedor.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        request.add(imageRequest);
    }

}
