package org.tensorflow.lite.examples.classification.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.examples.classification.CameraActivity;
import org.tensorflow.lite.examples.classification.ClassifierActivity;
import org.tensorflow.lite.examples.classification.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Bitmap selectedImage;
    ImageView selectImage;
    Uri image;
    Bundle bundle;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    TextView nameSurname,boy,kilo,cinsiyet,bazalKalori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth  = FirebaseAuth.getInstance();
        selectImage = findViewById(R.id.profilePhoto);

        nameSurname = findViewById(R.id.nameSurname);
        boy = findViewById(R.id.boy);
        kilo = findViewById(R.id.kilo);
        cinsiyet = findViewById(R.id.cinsiyetValue);
        bazalKalori = findViewById(R.id.dailyGoal);


        bundle = this.getIntent().getExtras();

        if (bundle!=null){
            String isimSoyisim = bundle.getString("isim","isim")+" "+bundle.getString("soyisim","soyisim");
            System.out.println(isimSoyisim);
            nameSurname.setText(isimSoyisim);
            boy.setText(bundle.getString("boy","150"));
            kilo.setText(bundle.getString("kilo","50"));
            cinsiyet.setText(bundle.getString("cinsiyet","yok"));
            if (cinsiyet.getText().toString().equals("Erkek")){
                bazalKalori.setText("2000");
            }else{
                bazalKalori.setText("1500");
            }
        }
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2 && resultCode == Activity.RESULT_OK && data !=null){
             image =   data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source  = ImageDecoder.createSource(this.getContentResolver(),image);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    selectImage.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
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
                                    String downloadUrl = uri.toString();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}