package com.example.countrytriviaapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginFragment";
    public Button loginBtn;
    public Button registerBtn;
    public TextView passwordTxt;
    public TextView emailTxt;
    //public Button facebookBtn;
    public Button googleBtn;
    public CheckBox rememberChk;
    public Button forgotPassword;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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


        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailTxt = view.findViewById(R.id.loginFragment_email_textInput);
        passwordTxt = view.findViewById(R.id.loginFragment_password_textInput);
        loginBtn = view.findViewById(R.id.loginFragment_Login_button);
        registerBtn = view.findViewById(R.id.loginFragment_Register_Button);
        //facebookBtn = view.findViewById(R.id.loginFragment_FacebookLogin_button);
        googleBtn = view.findViewById(R.id.loginFragment_GoogleLogin_button);
        rememberChk = view.findViewById(R.id.loginFragment_remember_chk);
        forgotPassword = view.findViewById(R.id.forgot_password_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        googleBtn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        return view;
    }


    private void signIn(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (registerBtn.isPressed()) {
                                SharedPreferences sharedPreferences = (SharedPreferences) Objects.requireNonNull(getActivity()).getSharedPreferences(MainActivity.USER_INFO_SP, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("UID", mAuth.getUid()).putString("Email", email).putString("Password", password).apply();
                            }
                            MainActivity main = (MainActivity) getActivity();
                            assert main != null;
                            main.mainFragmentManager(new QuestionsFragment());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("Firebase", "signInWithEmail:unsuccessful!!!");
                        }
                    }
                });
        Toast.makeText((MainActivity) getActivity(), "Sorry can't find your information please try to SignIn :)", Toast.LENGTH_LONG).show();
        moveFragment(new RegisterFragment());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginFragment_Login_button:
                String email = emailTxt.getText().toString();
                String pass = passwordTxt.getText().toString();
                if (!email.equals(String.valueOf("")) && !pass.equals(String.valueOf("")))
                    signIn(emailTxt.getText().toString().trim(), passwordTxt.getText().toString());
                else {
                    Toast.makeText(getContext(), "Email and Password are blank", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.loginFragment_Register_Button:
                moveFragment(new RegisterFragment());
                break;
            case R.id.loginFragment_GoogleLogin_button:
                googleSignIn();
                break;
            case R.id.forgot_password_btn:
                moveFragment(new PasswordRestFragment());
                break;
        }
    }

    private void moveFragment(Fragment newFragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.mainFragmentManager(newFragment);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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