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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class BottomSetting extends BottomSheetDialogFragment {


    ImageView selectImage;
    Bitmap selectedImage;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    Uri image;
    RadioGroup radioGroup;
    RadioButton erk,kadn;
    String bazalKalori,isim,soyisim,boyu,kilosu,selectedText,downloadUrl;
    EditText name,surname,boy,kilo;
    Button btnKaydet;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

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
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
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

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userId = user.getUid();
        String userEmail = user.getEmail();

        selectImage = view.findViewById(R.id.selectImage);
        name = view.findViewById(R.id.nameEditText);
        surname = view.findViewById(R.id.surnameEditText);
        boy = view.findViewById(R.id.boyEditText);
        kilo = view.findViewById(R.id.kiloEditText);
        radioGroup = view.findViewById(R.id.cinsiyetGroup);
        btnKaydet = view.findViewById(R.id.btnKaydet);
        erk = view.findViewById(R.id.erkek);
        kadn = view.findViewById(R.id.kadin);




        DocumentReference docRef = firebaseFirestore.collection("Profils").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){

                        Map<String,Object> gelenData = document.getData();
                        String downloadUrl = (String) gelenData.get("profilphoto");
                        Picasso.get().load(downloadUrl).placeholder(R.drawable.selectimage).into(selectImage);
                        name.setText((String) gelenData.get("isim"));
                        surname.setText((String) gelenData.get("soyisim"));
                        boy.setText((String)gelenData.get("boy"));
                        kilo.setText((String)gelenData.get("kilo"));

                        String cinsiyet = (String)gelenData.get("cinsiyet");

                        if (cinsiyet.equals(erk.getText().toString())){
                            erk.setChecked(true);
                        }else if(cinsiyet.equals(kadn.getText().toString())){
                            kadn.setChecked(true);
                        }else{
                            erk.setChecked(false);
                            kadn.setChecked(false);
                        }


                    }else{
                        Log.d("documentErr", "No such data");
                    }
                }else{
                    Log.d("taskError", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Profil YÜklenirken Sorun Oluştu", Toast.LENGTH_SHORT).show();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentToGallery,2);
                }
            }
        });



        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                 isim = name.getText().toString();
                 soyisim = surname.getText().toString();
                 boyu = boy.getText().toString();
                 kilosu = kilo.getText().toString();


                if (radioGroup.getCheckedRadioButtonId()==erk.getId()){
                    selectedText = "Erkek";
                }else{
                    selectedText = "Kadın";
                }

                Intent intent = new Intent(getActivity(),MainActivity.class);

                if (selectedText == "Erkek"){
                    bazalKalori = "2000";
                }else{
                    bazalKalori = "1500";
                }





                HashMap<String,Object> postImage = new HashMap<>();
                postImage.put("profilphoto",downloadUrl);
                postImage.put("useremail",userEmail);
                postImage.put("isim",isim);
                postImage.put("soyisim",soyisim);
                postImage.put("kilo",kilosu);
                postImage.put("boy",boyu);
                postImage.put("cinsiyet",selectedText);
                postImage.put("bazal",bazalKalori);

                CollectionReference usersRef = firebaseFirestore.collection("Profils");
                usersRef.document(userId).set(postImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Profil Güncellendi.", Toast.LENGTH_LONG).show();

                        startActivity(intent);
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2 && resultCode == Activity.RESULT_OK && data !=null){
            image =   data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source  = ImageDecoder.createSource(getActivity().getContentResolver(),image);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    selectImage.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),image);
                    selectImage.setImageBitmap(selectedImage);
                }

                String imageName ="profilephoto/" + firebaseAuth.getCurrentUser().getUid();
                if (image !=null){
                    storageReference.child(imageName).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                            newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_bottom_bg));
    }
}