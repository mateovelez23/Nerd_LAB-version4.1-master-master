package com.jhonlopera.nerd30;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


public class PerfilFragment extends Fragment{

    private String correoR,nombreR, foto;

    private TextView tvcorreo,tvnombre;
    private ImageView imagen_perfil;



    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle= getArguments();
        nombreR=bundle.getString("nombre");
        correoR=bundle.getString("correo");
        foto=bundle.getString("foto");


        View view= inflater.inflate(R.layout.fragment_perfil, container, false);
        tvcorreo=(TextView) view.findViewById(R.id.tvfcorreo);
        tvnombre=(TextView) view.findViewById(R.id.tvfnombre);
        imagen_perfil=(ImageView) view.findViewById(R.id.imagen_perfilf);
        cambiarnombres(nombreR,correoR);

        if (foto!=null){
            try {
                loadImageFromUrl(foto);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            imagen_perfil.setImageDrawable(getResources().getDrawable(R.drawable.perfil5));
        }

        return view;
    }

    public void cambiarnombres(String nombreR, String correoR) {

        tvnombre.setText(nombreR);
        tvcorreo.setText(correoR);
    }

    private void loadImageFromUrl(String foto) {
        Glide.with(getActivity()).load(foto).transform(new CircleTransform(getActivity())).into(imagen_perfil);

    }
}
