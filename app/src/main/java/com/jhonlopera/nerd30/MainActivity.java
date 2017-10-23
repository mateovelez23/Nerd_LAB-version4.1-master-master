package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private  String correoR,contraseñaR,nombreR,log,foto,fotoR;
    private Uri urifoto;
    int duration = Toast.LENGTH_SHORT;
  private ImageButton puntaje;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int silog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();
        puntaje=(ImageButton) findViewById(R.id.bpuntaje);
        puntaje.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        Intent intent;
        switch (id){
            case R.id.mPerfil:

                intent = new Intent(this, PerfilActivity.class);
                startActivity(intent);
                break;

            case R.id.mCerrar:
                log=preferencias.getString("log","error");
                if(log.equals("facebook")){
                    silog=0;
                    log="facebook";
                    editor_preferencias.putInt("silog",silog);
                    editor_preferencias.putString("log",log);
                    editor_preferencias.commit();

                    intent=new Intent(this,LoginActivity.class);
                    LoginManager.getInstance().logOut();// cerrar sesion en facebook
                    Toast.makeText(getApplicationContext(),"Saliendo de facebook", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
                else if(log.equals("google")){
                    silog=0;
                    log="google";
                    editor_preferencias.putInt("silog",silog);
                    editor_preferencias.putString("log",log);
                    editor_preferencias.commit();

                    signOut(); //cerrar sesion en google
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    silog=0;
                    log="registro";
                    editor_preferencias.putInt("silog",silog);
                    editor_preferencias.putString("log",log);
                    editor_preferencias.commit();
                    intent=new Intent(this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Context context = getApplicationContext();
                        CharSequence text = "Saliendo de google";
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v==puntaje) {

            Intent intent = new Intent(this, CreditosActivity.class);
            startActivity(intent);

        }
    }
}