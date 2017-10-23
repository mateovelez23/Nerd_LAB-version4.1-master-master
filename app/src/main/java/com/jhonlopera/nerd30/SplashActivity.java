package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private static final long SPlASH_DELAY=2000;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Solo permitir vista portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        // Se define el archivo "Preferencias" donde se almacenaran los valores de las preferencias
        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        //se declara instancia el editor de "Preferencias"
        editor_preferencias=preferencias.edit();

        final int silog=preferencias.getInt("silog",0);
        if (silog==0){
            TimerTask task =new TimerTask() {
                @Override
                public void run() {
                    Intent intent =new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            Timer timer =new Timer();
            timer.schedule(task,SPlASH_DELAY);

        }
        else{
            Intent intent=new Intent(this,PrincipalActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
