package org.tensorflow.lite.examples.classification.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.examples.classification.R;


public class SignUpFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    NavController navController;
    EditText emailText,passwordText;
    ImageView backButton;
    Button signUp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        backButton = view.findViewById(R.id.signUpBack);
        signUp = view.findViewById(R.id.signUpButton);

        emailText = view.findViewById(R.id.editTextTextEmailAddressSignUp);
        passwordText = view.findViewById(R.id.editTextTextPasswordSignUp);

        backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if (email.matches("")){
                    Toast.makeText(getContext(),"Email boş olamaz!",Toast.LENGTH_LONG).show();
                }else if (password.matches("")){
                    Toast.makeText(getContext(),"Password boş olamaz!",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getContext(),"Kullanıcı Oluştu!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

    }


}