package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.security.Principal;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fm;
    FragmentTransaction ft;

    private  String correoR,contraseñaR,nombreR,log,foto,fotoR;
    private Uri urifoto;
    int duration = Toast.LENGTH_SHORT;

    private ImageButton puntaje;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    Fragment fragment1;
    int silog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor_preferencias=preferencias.edit();

        //Para cerrar cesion con google
//______________________________________________________________________________________________________
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
//________________________________________________________________________________________________________


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

         fragment1 = new MenuPrincipalFragment();
        getSupportActionBar().setTitle("Menu principal");
        ft.add(R.id.frameprincipal, fragment1).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        Fragment fragment = null;
        Intent intent;

        if (id == R.id.nav_main) {

            fragment = new MenuPrincipalFragment();
            FragmentTransaction = true;

        } else if (id == R.id.nav_profile) {

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

            ft = fm.beginTransaction();
            Bundle args=new Bundle();
            fragment = new PerfilFragment();
            args.putString("nombre",nombreR);
            args.putString("correo",correoR);
            args.putString("foto",foto);
            fragment.setArguments(args);
            ft.addToBackStack("nombre");
            ft.addToBackStack("correo");
            ft.addToBackStack("foto");
            getSupportActionBar().setTitle(item.getTitle());
            ft.replace(R.id.frameprincipal, fragment).commit();
            FragmentTransaction = false;

        } else if (id == R.id.nav_ranking) {

            intent = new Intent(this, CreditosActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_config) {
            intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_juegos) {

            intent = new Intent(this, BottomActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_csesion){

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

        }


        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameprincipal,fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

