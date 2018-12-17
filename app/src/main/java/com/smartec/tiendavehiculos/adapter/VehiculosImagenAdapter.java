package com.smartec.tiendavehiculos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.DetalleVehiculo;
import com.smartec.tiendavehiculos.DetalleVendedor;
import com.smartec.tiendavehiculos.R;
import com.smartec.tiendavehiculos.ServerConfig;
import com.smartec.tiendavehiculos.entidades.VehiculoCard;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    public void onBindViewHolder(final VehiculosViewHolder holder, final int position) {
        final VehiculoCard vehiculo = listaVehiculos.get(position);

        holder.carPrecioCard.setText(vehiculo.getPrecioVenta());
        holder.carDescriptionCard.setText(vehiculo.getDescripcionMarca() + ", " + vehiculo.getDescripcionModelo() + ", " + vehiculo.getAnio());
        holder.nombreVendedor.setText(vehiculo.getNombres() + " " + vehiculo.getApellidos());
        holder.direccionVendedor.setText(vehiculo.getDireccion());

        //cargarImagen(listaVehiculos.get(position).getRutaImagen(), holder);
        //cargarImagenVendedor(listaVehiculos.get(position).getFotoPerfil(),holder);
        if(listaVehiculos.get(position).getRutaImagen()!=null){
            //holder.imagenVehiculo.setImageBitmap(listaVehiculos.get(position).getImagen());
            cargarImagen(listaVehiculos.get(position).getRutaImagen(), holder);
            cargarImagenVendedor(listaVehiculos.get(position).getFotoPerfil(),holder);
        }else {
            holder.imagenVehiculo.setImageResource(R.drawable.carro_predeterminado);
            holder.fotoVendedor.setImageResource(R.drawable.carro_predeterminado);
        }

        holder.imagenVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetalleVehiculo.class);
                intent.putExtra("marca", listaVehiculos.get(position).getDescripcionMarca());
                intent.putExtra("modelo", listaVehiculos.get(position).getDescripcionModelo());
                intent.putExtra("anio", listaVehiculos.get(position).getAnio());
                intent.putExtra("color", listaVehiculos.get(position).getColor());
                intent.putExtra("cilindraje", listaVehiculos.get(position).getCilindraje());
                intent.putExtra("precio", listaVehiculos.get(position).getPrecioVenta());
                intent.putExtra("imagen", ServerConfig.URL_BASE + listaVehiculos.get(position).getRutaImagen());
                activity.startActivity(intent);
            }
        });

        holder.fotoVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetalleVendedor.class);
                intent.putExtra("nombres", listaVehiculos.get(position).getNombres());
                intent.putExtra("apellidos", listaVehiculos.get(position).getApellidos());
                intent.putExtra("celular", listaVehiculos.get(position).getCelular());
                intent.putExtra("telefono", listaVehiculos.get(position).getTelefono());
                intent.putExtra("email", listaVehiculos.get(position).getEmail());
                intent.putExtra("direccion", listaVehiculos.get(position).getDireccion());
                intent.putExtra("foto",ServerConfig.URL_BASE + listaVehiculos.get(position).getFotoPerfil());
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



    private void cargarImagenVendedor(String rutaFotoPerfil, final VehiculosViewHolder holder) {
        //String servidor = (R.string.servidor);
        String urlImagenPerfil = ServerConfig.URL_BASE + rutaFotoPerfil;
        urlImagenPerfil = urlImagenPerfil.replace(" ","%20");
        ImageRequest imageRequest = new ImageRequest(urlImagenPerfil, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.fotoVendedor.setImageBitmap(response);
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
        private TextView nombreVendedor;
        private TextView direccionVendedor;
        private CircleImageView fotoVendedor;

        public VehiculosViewHolder(View itemView) {
            super(itemView);

            imagenVehiculo = itemView.findViewById(R.id.imagen_carr);
            carPrecioCard = itemView.findViewById(R.id.card_precio_vehiculo);
            carDescriptionCard = itemView.findViewById(R.id.card_descripcion_vehiculo);
            nombreVendedor = itemView.findViewById(R.id.nombreVendedor);
            direccionVendedor = itemView.findViewById(R.id.direccionVendedor);
            fotoVendedor = itemView.findViewById(R.id.profile_image);
        }
    }


    /*
    Añade una lista completa de items
     */
    public void addAll(ArrayList<VehiculoCard> lista){
        listaVehiculos.addAll(lista);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear(){
        listaVehiculos.clear();
        notifyDataSetChanged();
    }
}
