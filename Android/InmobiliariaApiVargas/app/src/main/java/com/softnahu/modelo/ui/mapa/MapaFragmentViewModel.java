package com.softnahu.modelo.ui.mapa;



import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;


import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MapaFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<MapaActual> mapaActual;
    private FusedLocationProviderClient fusedLocationClient;

    public MapaFragmentViewModel(@NonNull Application application) {
        super(application);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public LiveData<MapaActual> getMapaActual() {
        if (mapaActual == null) {
            mapaActual = new MutableLiveData<>();
        }
        return mapaActual;
    }

    public void mostrarMapa() {
        mapaActual.setValue(new MapaActual());
    }

    public class MapaActual implements OnMapReadyCallback {
        LatLng FARMACIA1 = new LatLng(-33.27914445457883, -66.32798996407993);
        LatLng FARMACIA2 = new LatLng(-33.283758005771695, -66.32977571362416);

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            obtenerUbicacionActual(googleMap);
        }

    }

    private void obtenerUbicacionActual(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            com.google.android.gms.location.LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(10000)
                    .setMaxUpdateDelayMillis(10000)
                    .build();
            if (location != null) {
                LatLng ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 14));
            } else {
                Log.e("MapaViewModel", "No se pudo obtener la ubicación actual.");

            }
        });
    }
    }

