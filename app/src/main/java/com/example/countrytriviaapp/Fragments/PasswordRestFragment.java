package com.example.countrytriviaapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.countrytriviaapp.Activities.MainActivity;
import com.example.countrytriviaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordRestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordRestFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PasswordRestFragment";
    private TextView email;
    private TextView passwordOne;
    private TextView passwordTwo;
    private Button reset;
    private Button cancel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PasswordRestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasswordRestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordRestFragment newInstance(String param1, String param2) {
        PasswordRestFragment fragment = new PasswordRestFragment();
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
        View view = inflater.inflate(R.layout.fragment_password_rest, container, false);
        email = view.findViewById(R.id.registrationFragment_email_textView);
        passwordOne = view.findViewById(R.id.registrationFragment_passwordOne_passwordTextView);
        passwordTwo = view.findViewById(R.id.registrationFragment_passwordTwo_passwordTextView);
        cancel = view.findViewById(R.id.passwordFragment_cancel_Button);
        reset = view.findViewById(R.id.passwordFragment_ready_button);

        cancel.setOnClickListener(this);
        reset.setOnClickListener(this);

        return view;
    }
    private boolean passwordValidator(String password, String passwordTwo) {
        return password.equals(passwordTwo);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.passwordFragment_cancel_Button:
                MainActivity main = (MainActivity) getActivity();
                assert main != null;
                main.mainFragmentManager(new LoginFragment());
                break;
            case R.id.passwordFragment_ready_button:
                if (passwordValidator(passwordOne.getText().toString(),passwordTwo.getText().toString())){
                    Toast.makeText(getContext(),"We are not ready yet to reset the password.....sorry",Toast.LENGTH_LONG).show();
//                    mAuth.sendPasswordResetEmail(email.getText().toString().trim())
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "Email sent.");
//                                    }
//                                }
//                            });
//                    Toast.makeText(getContext(),"Your email was not verified yet please check your inbox",Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getContext(),"Your passwords are not match...",Toast.LENGTH_SHORT).show();

                break;
        }
    }
}