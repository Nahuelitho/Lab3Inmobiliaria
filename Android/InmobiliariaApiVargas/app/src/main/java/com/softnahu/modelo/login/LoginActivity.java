package com.softnahu.modelo.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityViewModel vm;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
        setContentView(binding.getRoot());
        Glide.with(this)
                .load(R.drawable.loginimage) // Carga la imagen de drawable
                .centerCrop()
                .into(binding.imgFondo);
        binding.ivLogo.setImageResource(R.drawable.logoinmobiliaria);
        vm.getUsuario().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.llamarLogin(binding.etUsuario.getText().toString(),binding.etClave.getText().toString());
            }
        });



    }
}