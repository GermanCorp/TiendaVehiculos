package com.smartec.tiendavehiculos.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartec.tiendavehiculos.entidades.Vehiculo;

import java.util.ArrayList;

public class VehiculosImagenAdapter extends RecyclerView.Adapter<VehiculosImagenAdapter.VehiculosViewHolder> {

    private ArrayList<Vehiculo> listaVehiculos;
    private int resource;
    private Activity activity;


    public VehiculosImagenAdapter(ArrayList<Vehiculo> listaVehiculos, int resource, Activity activity) {
        this.listaVehiculos = listaVehiculos;
        this.resource = resource;
        this.activity = activity;
    }


    @Override
    public VehiculosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new VehiculosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VehiculosViewHolder holder, int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class VehiculosViewHolder extends RecyclerView.ViewHolder {
        public VehiculosViewHolder(View itemView) {
            super(itemView);
        }
    }
}
