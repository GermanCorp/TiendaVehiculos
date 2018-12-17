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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.adapter.VehiculosImagenAdapter;

public class DetalleVehiculo extends AppCompatActivity {

    private RequestQueue request;
    private TextView marca;
    private TextView modelo;
    private TextView anio;
    private TextView color;
    private TextView cilindraje;
    private TextView precio;
    private String rutaImagen;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_vehiculo);


        request = Volley.newRequestQueue(getApplicationContext());
        marca = findViewById(R.id.textViewMarca);
        modelo = findViewById(R.id.textViewModelo);
        anio = findViewById(R.id.textViewAnio);
        color = findViewById(R.id.textViewColor);
        cilindraje = findViewById(R.id.textViewCilindraje);
        precio = findViewById(R.id.textViewPrecio);
        imagen = findViewById(R.id.imageViewCarroDetalle);


        marca.setText(getIntent().getExtras().getString("marca"));
        modelo.setText(getIntent().getExtras().getString("modelo"));
        anio.setText(getIntent().getExtras().getString("anio"));
        color.setText(getIntent().getExtras().getString("color"));
        cilindraje.setText(getIntent().getExtras().getString("cilindraje"));
        precio.setText(getIntent().getExtras().getString("precio"));
        rutaImagen = getIntent().getExtras().getString("imagen");
        //imagen.setImageResource(R.drawable.carr);
        cargarImagen(rutaImagen);
    }



    private void cargarImagen(String rutaImagen) {
        String urlImagen = rutaImagen;
        urlImagen = urlImagen.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagen.setImageBitmap(response);
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
