package org.tensorflow.lite.examples.classification.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassificationSessionFactory;
import android.widget.ImageView;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.CameraActivity;
import org.tensorflow.lite.examples.classification.ClassifierActivity;
import org.tensorflow.lite.examples.classification.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Bitmap selectedImage;
    ImageView selectImage;
    Uri image;
    Bundle bundle;
    String userId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ArrayList<String> calories;
    private  RecyclerAdapter adapter;
    RecyclerView caloriRepo;

    TextView nameSurname,boy,kilo,cinsiyet,bazalKalori,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calories = new ArrayList<>();
        caloriRepo = findViewById(R.id.caloriRepo);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        selectImage = findViewById(R.id.profilePhoto);
        userName = findViewById(R.id.userName);
        nameSurname = findViewById(R.id.nameSurname);
        boy = findViewById(R.id.boy);
        kilo = findViewById(R.id.kilo);
        cinsiyet = findViewById(R.id.cinsiyetValue);
        bazalKalori = findViewById(R.id.dailyGoal);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
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
                        String isimSoyisim = (String) gelenData.get("isim") +" "+(String) gelenData.get("soyisim");
                        userName.setText((String)gelenData.get("useremail"));
                        nameSurname.setText(isimSoyisim);
                        boy.setText((String)gelenData.get("boy"));
                        kilo.setText((String)gelenData.get("kilo"));
                        cinsiyet.setText((String)gelenData.get("cinsiyet"));
                        bazalKalori.setText((String)gelenData.get("bazal"));

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
                Toast.makeText(getApplicationContext(), "Profil YÜklenirken Sorun Oluştu", Toast.LENGTH_SHORT).show();
            }
        });

        getCalorieData();


        caloriRepo.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(calories);
        caloriRepo.setAdapter(adapter);


    }

    public void openCamera(View view) {
        Intent intent = new Intent(this, ClassifierActivity.class);
        startActivity(intent);
    }

    public void profileSettingClick(View view) {
        BottomSetting settingFragment = new BottomSetting();
        settingFragment.show(getSupportFragmentManager(),"bottom");
    }

    public void logOut(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void selectImage(View view) {


    }

    public void getCalorieData(){


        CollectionReference collectionReference = firebaseFirestore.collection("CaloriRepo").document(userId).collection(userId);

        collectionReference.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value !=null){
                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object> data = snapshot.getData();
                        String food = (String) data.get("food");

                        System.out.println(food);

                        calories.add(food);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }






}