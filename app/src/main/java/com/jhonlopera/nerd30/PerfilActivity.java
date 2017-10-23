package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity {


    private String correoR,contraseñaR,nombreR, foto,log;
    int duration = Toast.LENGTH_SHORT;
    private TextView tvcorreo,tvnombre;
    private ImageView imagen_perfil;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int silog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvcorreo=(TextView) findViewById(R.id.tNombre);
        tvnombre=(TextView) findViewById(R.id.tvCorreo);
        imagen_perfil=(ImageView) findViewById(R.id.imagen_perfil);

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();



        log=preferencias.getString("log","error");

        if (log.equals("facebook") || log.equals("google")){
            correoR=preferencias.getString("correo","Su correo no es público");
            nombreR=preferencias.getString("nombre","Su nombre no es público");
            foto=preferencias.getString("foto",null);
        }
        else{
            correoR=preferencias.getString("correo","Su correo no es público");
            nombreR=preferencias.getString("nombre","Su nombre no es público");
            foto=null;

        }


        if (foto!=null) {
            loadImageFromUrl(foto);
        }
        else
            Toast.makeText(getApplicationContext(),"Su cuenta no tiene foto", Toast.LENGTH_SHORT).show();

        if (correoR!=null)
            tvcorreo.setText("Correo: "+correoR);

        tvnombre.setText("Nombre: "+nombreR);

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

    private void loadImageFromUrl(String foto) {
        Picasso.with(this).load(foto).placeholder(R.mipmap.ic_launcher).into(imagen_perfil, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuperfil,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        Intent intent;

        switch (id){
            case R.id.mPrincipal:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.mCerrar:
                log=preferencias.getString("log","error");
                if(log.equals("facebook")){
                    silog=0;
                    editor_preferencias.putInt("silog",silog);
                    editor_preferencias.commit();

                    intent=new Intent(this,LoginActivity.class);
                    LoginManager.getInstance().logOut();// cerrar sesion en facebook
                    Toast.makeText(getApplicationContext(),"Saliendo de facebook", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
                else if(log.equals("google")){
                    silog=0;

                    editor_preferencias.putInt("silog",silog);
                    editor_preferencias.commit();

                    signOut(); //cerrar sesion en google
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    silog=0;
                    editor_preferencias.putInt("silog",silog);
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
}
