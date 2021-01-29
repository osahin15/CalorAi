package org.tensorflow.lite.examples.classification.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.examples.classification.R;


public class SignInFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    EditText emailText,passwordText;
    private TextView signUpBtn;
    NavController navController;

    Button signIn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        signUpBtn = view.findViewById(R.id.signUpNavigate);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        emailText = view.findViewById(R.id.editTextTextEmailAddress);
        passwordText = view.findViewById(R.id.editTextTextPassword);
        signIn = view.findViewById(R.id.loginButton);

        signUpBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment);
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if (email.matches("")){
                    Toast.makeText(getContext(),"Email boş olamaz!",Toast.LENGTH_LONG).show();

                }else if (password.matches("")){
                    Toast.makeText(getContext(),"Password boş olamaz!",Toast.LENGTH_LONG).show();
                }
                else {
                        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
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