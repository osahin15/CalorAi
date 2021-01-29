package org.tensorflow.lite.examples.classification.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.examples.classification.R;

import java.io.IOException;


public class BottomSetting extends BottomSheetDialogFragment {



    RadioGroup radioGroup;
    RadioButton erk,kadn;
    TextView nameSurname,boyText,kiloText,cinsiyet,bazalKalori;
    EditText name,surname,boy,kilo;
    Button btnKaydet;


   /* public static BottomSetting newInstance(String param1, String param2) {
        BottomSetting fragment = new BottomSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.nameEditText);
        surname = view.findViewById(R.id.surnameEditText);
        boy = view.findViewById(R.id.boyEditText);
        kilo = view.findViewById(R.id.kiloEditText);
        radioGroup = view.findViewById(R.id.cinsiyetGroup);
        btnKaydet = view.findViewById(R.id.btnKaydet);
     erk = view.findViewById(R.id.erkek);
     kadn = view.findViewById(R.id.kadin);








        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String isim = name.getText().toString();
                String soyisim = surname.getText().toString();
                String boyu = boy.getText().toString();
                String kilosu = kilo.getText().toString();

                String selectedText;
                if (radioGroup.getCheckedRadioButtonId()==erk.getId()){
                    selectedText = "Erkek";
                }else{
                    selectedText = "Kadın";
                }

                Intent intent = new Intent(getActivity(),MainActivity.class);
               BottomSetting fragment = new BottomSetting();
                Bundle args = new Bundle();

                args.putString("isim",isim);
                args.putString("soyisim",soyisim);
                args.putString("boy",boyu);
                args.putString("kilo",kilosu);
                args.putString("cinsiyet",selectedText);

                intent.putExtras(args);
                startActivity(intent);
                dismiss();


            }
        });





    }



    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_bottom_bg));
    }
}