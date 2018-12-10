package com.smartec.tiendavehiculos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.ActividadPrincipal;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.ServerConfig;
import com.smartec.tiendavehiculos.entidades.VehiculoCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehiculosImagenAdapter extends RecyclerView.Adapter<VehiculosImagenAdapter.VehiculosViewHolder> {

    private ArrayList<VehiculoCard> listaVehiculos;
    private int resource;
    private Activity activity;
    private RequestQueue request;
    private Context context;



    public VehiculosImagenAdapter(ArrayList<VehiculoCard> listaVehiculos, int resource, Activity activity, Context context) {
        this.context = context;
        this.listaVehiculos = listaVehiculos;
        this.resource = resource;
        this.activity = activity;
        request = Volley.newRequestQueue(context);
    }


    @Override
    public VehiculosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        Log.d("CREADO", ""+viewType);
        return new VehiculosViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VehiculosViewHolder holder, final int position) {
        Log.d("BIND", ""+position);

        VehiculoCard vehiculo = listaVehiculos.get(position);

        holder.carPrecioCard.setText(vehiculo.getPrecioVenta());
        holder.carDescriptionCard.setText(vehiculo.getDescripcionMarca());

        cargarImagen(listaVehiculos.get(position).getRutaImagen(), holder);
        /*if(listaVehiculos.get(position).getRutaImagen()!=null){
            //holder.imagenVehiculo.setImageBitmap(listaVehiculos.get(position).getImagen());
            cargarImagen(listaVehiculos.get(position).getRutaImagen(), holder);
        }else {
            holder.imagenVehiculo.setImageResource(R.drawable.carro_predeterminado);
        }*/

        holder.imagenVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActividadPrincipal.class);
                
                activity.startActivity(intent);
            }
        });
    }


    private void cargarImagen(String rutaImagen, final VehiculosViewHolder holder) {
        //String servidor = (R.string.servidor);
        String urlImagen = ServerConfig.URL_BASE + rutaImagen;
        urlImagen = urlImagen.replace(" ","%20");
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagenVehiculo.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Error al cargar la imagen\n", Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }


    @Override
    public int getItemCount() {
        return listaVehiculos.size();
    }

    public class VehiculosViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenVehiculo;
        private TextView carPrecioCard;
        private TextView carDescriptionCard;

        public VehiculosViewHolder(View itemView) {
            super(itemView);

            imagenVehiculo = itemView.findViewById(R.id.imagen_carr);
            carPrecioCard = itemView.findViewById(R.id.card_precio_vehiculo);
            carDescriptionCard = itemView.findViewById(R.id.card_descripcion_vehiculo);
        }
    }
}
