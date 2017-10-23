package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private String correoR,contraseñaR, correo, contraseña,nombreR,foto,log,prueba;
    private Uri urifoto;
    private EditText ecorreo, econtraseña;
    int duration = Toast.LENGTH_SHORT;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN =1035;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;
    int silog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(LoginActivity.this);
        ecorreo = (EditText) findViewById(R.id.eCorreo);
        econtraseña = (EditText) findViewById(R.id.eContraseña);

        // Se define el archivo "Preferencias" donde se almacenaran los valores de las preferencias
        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        //se declara instancia el editor de "Preferencias"
        editor_preferencias=preferencias.edit();

        //-------------------------------------------------------
        //Si el logggin es con el registro de usuario
        Bundle extras= getIntent().getExtras();
        if (extras != null){

            correoR = extras.getString("correo");
            contraseñaR = extras.getString("contraseña");
            nombreR = extras.getString("nombre");

        }
        //--------------------------------------------------------------------

        loginButton=(LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile"); //Permisos del perfil publico
        callbackManager= CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                log="facebook";
                silog=1;
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {


                        } else {
                            try {
                                nombreR = object.getString("name");
                                correoR = object.getString("email");

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Profile profile=com.facebook.Profile.getCurrentProfile();
                            urifoto=profile.getProfilePictureUri(400,400); //foto de tamaño 400x400

                            if ((urifoto==null))
                                foto=null;
                            else
                                foto=urifoto.toString();

                            guardarPreferencias(silog,correoR,nombreR,foto,log);
                            prueba=preferencias.getString("nombre","noseobtuvo");
                            Toast.makeText(getApplicationContext(),prueba, Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            intent.putExtra("correo",correoR);
                            intent.putExtra("nombre",nombreR);
                            intent.putExtra("foto",foto);
                            intent.putExtra("log",log);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login  cancelado",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error en el login",Toast.LENGTH_SHORT).show();
            }});

        //Para loggin con google
        //-------------------------------------------------------------------------------------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getApplicationContext(),"Error en el loggin",Toast.LENGTH_SHORT);
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }});
        //-------------------------------------------------------------------------------------------
    }

    public void iniciar(View view) {

        correo = ecorreo.getText().toString();
        contraseña = econtraseña.getText().toString();
        log="registro";
        silog=1;
        contraseñaR=preferencias.getString("contraseña","/((&!=");
        correoR=preferencias.getString("correo","/(/%#%//(%#");

        if(correo.equals(correoR) && contraseña.equals(contraseñaR)){
            editor_preferencias.putInt("silog",silog);
            editor_preferencias.putString("log",log);
            editor_preferencias.commit();
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = "Datos incorrectos";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1234 && resultCode==RESULT_OK){
            //registro
            prueba=preferencias.getString("nombre","no se obtuvo");
            Toast.makeText(getApplicationContext(),prueba, Toast.LENGTH_SHORT).show();
            correoR=preferencias.getString("correo","no se obtivo");
            contraseñaR=preferencias.getString("contraseña","no se obtivo");
            nombreR=preferencias.getString("nombre","no se obtivo");
        }

        else if (requestCode == RC_SIGN_IN) {//login con google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            //facebook
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //google
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            correoR=acct.getEmail();//obtener email
            nombreR=acct.getDisplayName();
            urifoto=acct.getPhotoUrl();
            log="google";
            silog=1;


            if ((urifoto==null))
                foto=null;

            else
                foto=urifoto.toString();

            guardarPreferencias(silog,correoR,nombreR,foto,log);
            prueba=preferencias.getString("nombre","noseobtuvo");
            Toast.makeText(getApplicationContext(),prueba, Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(),"Error en el loggin",Toast.LENGTH_SHORT);
        }
    }

    public void Registrarse(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivityForResult(intent,1234);
    }
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void generarHash(){

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.jhonlopera.nerd30",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {}

    }

    void guardarPreferencias(int silog,String correo,String nombre, String foto,String log){
        editor_preferencias.putInt("silog",silog);
        editor_preferencias.putString("correo",correo);
        editor_preferencias.putString("nombre",nombre);
        editor_preferencias.putString("foto",foto);
        editor_preferencias.putString("log",log);
        editor_preferencias.commit();
    }


}