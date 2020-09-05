package com.example.countrytriviaapp.Fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.countrytriviaapp.Activities.MainActivity;
import com.example.countrytriviaapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final int RC_SIGN_IN = 100987;
    private TextView emailTxt;
    private TextView passwordOneTxt;
    private TextView passwordTwoTxt;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "RegisterFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailTxt = view.findViewById(R.id.registrationFragment_email_textView);
        passwordOneTxt = view.findViewById(R.id.registrationFragment_passwordOne_passwordTextView);
        passwordTwoTxt = view.findViewById(R.id.registrationFragment_passwordTwo_passwordTextView);
        Button googleBtn = view.findViewById(R.id.registrationFragment_GoogleLogin_button);
        Button backButton = view.findViewById(R.id.registrationFragment_back_button);
        Button registerButton = view.findViewById(R.id.registrationFragment_register_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()),gso);

        backButton.setOnClickListener(this);
        googleBtn.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        return view;
    }

    public void createAccount(String email,String password, String passwordTwo){
        if (passwordValidator(password,passwordTwo))
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                assert user != null;
                                user.sendEmailVerification();
                                Toast.makeText(getContext(),"Great please try to login",Toast.LENGTH_LONG).show();
                                MainActivity main = (MainActivity) getActivity();
                                assert main != null;
                                main.mainFragmentManager(new LoginFragment());
                            } else {
                                Toast.makeText(getContext(),"Failed to create new user",Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
        else
            Toast.makeText(getContext(),"Passwords are not match...",Toast.LENGTH_SHORT).show();
    }

    private boolean passwordValidator(String password, String passwordTwo) {
        return password.equals(passwordTwo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registrationFragment_back_button:
                MainActivity main = (MainActivity) getActivity();
                assert main != null;
                main.mainFragmentManager(new LoginFragment());
                break;
            case R.id.registrationFragment_register_button:
                String email = emailTxt.getText().toString().trim();
                String passOne = passwordOneTxt.getText().toString();
                String passTwo = passwordTwoTxt.getText().toString();
                if (!email.equals("") && !passOne.equals("") && !passTwo.equals("")) {
                    createAccount(email, passOne, passTwo);

                }
                else
                    Toast.makeText(getContext(),"Hmm something in missing...",Toast.LENGTH_LONG).show();
                break;
            case R.id.registrationFragment_GoogleLogin_button:
                googleSignIn();
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity mainActivity = (MainActivity) getActivity();
                            assert mainActivity != null;
                            mainActivity.mainFragmentManager(new QuestionsFragment());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }
}