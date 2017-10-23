package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private String correo, contraseña, repcontraseña,nombre;
    private EditText ecorreo, econtraseña, erepcontraseña,enombre;
    int duration = Toast.LENGTH_SHORT;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor_preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Se define el archivo "Preferencias" donde se almacenaran los valores de las preferencias
        preferencias=getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        //se declara instancia el editor de "Preferencias"
        editor_preferencias=preferencias.edit();


        ecorreo = (EditText) findViewById(R.id.eCorreo);
        econtraseña = (EditText) findViewById(R.id.eContraseña);
        erepcontraseña = (EditText) findViewById(R.id.eRepcontraseña);
        enombre = (EditText) findViewById(R.id.eNombre);

    }

    public void Registrarse(View view) {

        if (TextUtils.isEmpty((enombre.getText().toString()))) {
            ecorreo.setError("Llene este espacio");
        } else if (TextUtils.isEmpty((ecorreo.getText().toString()))) {
            econtraseña.setError("Llene este espacio");
        } else if (TextUtils.isEmpty((econtraseña.getText().toString()))) {
            erepcontraseña.setError("Llene este espacio");
        } else if (TextUtils.isEmpty((erepcontraseña.getText().toString()))) {
            enombre.setError("Llene este espacio");
        }else {
            correo = ecorreo.getText().toString();
            contraseña = econtraseña.getText().toString();
            repcontraseña = erepcontraseña.getText().toString();
            nombre = enombre.getText().toString();

            if(isEmailValid(correo)){
                if (contraseña.equals(repcontraseña)) {
                    guardarPreferencias(correo,nombre,contraseña);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Las contraseñas no coinciden";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "El correo no es valido";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }
    void guardarPreferencias(String correo,String nombre,String contraseña){

        editor_preferencias.putString("correo",correo);
        editor_preferencias.putString("nombre",nombre);
        editor_preferencias.putString("contraseña",contraseña);
        editor_preferencias.commit();
    }
}
