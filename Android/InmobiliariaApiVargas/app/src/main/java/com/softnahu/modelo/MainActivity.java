package com.softnahu.modelo;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.softnahu.modelo.databinding.ActivityMainBinding;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.model.Propietario;
import com.softnahu.modelo.ui.perfil.PerfilViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private MainActivityViewModel vmMain;

    public static ArrayList<Inmueble> listaInmueble = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        solicitarPermisos();
        setSupportActionBar(binding.appBarMain.toolbar);
        setupNavigation();
        vmMain = new ViewModelProvider(this).get(MainActivityViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;


        vmMain.getmPropietario().observe(this, new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {

                NavigationView navigationView = binding.navView;

                View header = navigationView.getHeaderView(0);
                ImageView imagen = header.findViewById(R.id.ivImagen);
                TextView info = header.findViewById(R.id.tvUsuario);

                String saludo = "Bienvenido "+ propietario.getNombre() +" "+propietario.getApellido();
                info.setText(saludo);

                String url = "http://192.168.1.106:5001" + propietario.getAvatar();
                Glide.with(MainActivity.this)
                        .load(url)
                        .placeholder(R.drawable.defaultprofile)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.error)
                        .into(imagen);
            }
        });
        vmMain.getmPropietario().observe(this, propietario -> {
                Log.d("Propietario_Data", "Propietario cargado: " + propietario.getNombre() + " " + propietario.getApellido());
        });
        vmMain.recuperarDatosPerfil();


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                vmMain.recuperarDatosPerfil();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });


    }

    private void setupNavigation(){
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_map,R.id.nav_perfil, R.id.nav_inmueble, R.id.nav_inquilino, R.id.nav_contrato)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp(){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void solicitarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1000);
        }
    }
}