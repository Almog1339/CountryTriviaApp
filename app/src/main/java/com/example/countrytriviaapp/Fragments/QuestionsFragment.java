package com.example.countrytriviaapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.countrytriviaapp.Activities.MainActivity;
import com.example.countrytriviaapp.Classes.Country;
import com.example.countrytriviaapp.Classes.QueryCreator;
import com.example.countrytriviaapp.Interfaces.IAsyncCallback;
import com.example.countrytriviaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionsFragment extends Fragment implements View.OnClickListener{

    //Firebase
    private FirebaseUser user;
    //Widgets
    private TextView email;
    private Button logoff;
    private CardView questionsCard;
    private TextView questionsText;
    private Button answerOne;
    private Button answerTwo;
    private Button answerThree;
    private Button answerFour;
    private Button nextQuestion;
    private Country CHOSEN_COUNTRY;
    private String CORRECT_ANSWER;

    final ArrayList<Country> countries = new ArrayList<>();
    public IAsyncCallback asyncCallback;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionsFragment newInstance(String param1, String param2) {
        QuestionsFragment fragment = new QuestionsFragment();
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
        final View view = inflater.inflate(R.layout.fragment_questions, container, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                generateQuestionHandler();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = MainActivity.getCurrentUser();
                email = view.findViewById(R.id.questionFragment_userName_textView);
                email.setText(user.getEmail());
            }
        }).start();

        logoff = view.findViewById(R.id.questionFragment_logout_button);
        questionsCard = view.findViewById(R.id.questionsFragment_questions_cardView);
        questionsText = view.findViewById(R.id.question_textView);

        answerOne = view.findViewById(R.id.answerOneButton);
        answerTwo = view.findViewById(R.id.answerTwoButton);
        answerThree = view.findViewById(R.id.answerThreeButton);
        answerFour = view.findViewById(R.id.answerFourButton);
        nextQuestion = view.findViewById(R.id.next_question);

        logoff.setOnClickListener(this);
        answerOne.setOnClickListener(this);
        answerTwo.setOnClickListener(this);
        answerThree.setOnClickListener(this);
        answerFour.setOnClickListener(this);
        nextQuestion.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.answerOneButton:
                if (checkAnswer(answerOne.getText().toString())){
                    Toast.makeText(getContext(),"Correct",Toast.LENGTH_SHORT).show();
                generateQuestionHandler();}
                else
                    Toast.makeText(getContext(),"False",Toast.LENGTH_SHORT).show();
                break;
            case R.id.answerTwoButton:
                if (checkAnswer(answerTwo.getText().toString())){
                    Toast.makeText(getContext(),"Correct",Toast.LENGTH_SHORT).show();
                    generateQuestionHandler();
                }
                else
                    Toast.makeText(getContext(),"False",Toast.LENGTH_SHORT).show();
                break;
            case R.id.answerThreeButton:
                if (checkAnswer(answerThree.getText().toString())) {
                    Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                    generateQuestionHandler();
                }
                else
                    Toast.makeText(getContext(),"False",Toast.LENGTH_SHORT).show();
                break;
            case R.id.answerFourButton:
                if (checkAnswer(answerFour.getText().toString())){
                    Toast.makeText(getContext(),"Correct",Toast.LENGTH_SHORT).show();
                    generateQuestionHandler();
                }
                else
                    Toast.makeText(getContext(),"False",Toast.LENGTH_SHORT).show();
                break;
            case R.id.next_question:
                generateQuestionHandler();
                break;
            case R.id.questionFragment_logout_button:
                MainActivity main = (MainActivity) getActivity();
                assert main != null;
                main.mainFragmentManager(new LoginFragment());
                FirebaseAuth.getInstance().signOut();
                break;
        }
    }

    public boolean checkAnswer(String btnText){
        return btnText.equals(CORRECT_ANSWER);
    }

    public void generateQuestionHandler(){
        countries.clear();

        final String[] subjects = {"capital", "region", "subRegion"};
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String subject = subjects[(int) Math.floor(Math.random() * (subjects.length-1))];

        asyncCallback = new IAsyncCallback() {
            @Override
            public <T> void processFinished(T response) {
                Country res = (Country) response;
                switch (subject) {
                    case "capital":
                        if (!res.getCapital().equals(""))
                            countries.add(res);
                        else
                            QueryCreator.generateTriviaQuery(databaseReference, 1, asyncCallback);
                        break;
                    case "region":
                        if (!res.getRegion().equals(""))
                            countries.add(res);
                        else
                            QueryCreator.generateTriviaQuery(databaseReference, 1, asyncCallback);
                        break;
                    case "subRegion":
                        if (!res.getSubRegion().equals(""))
                            countries.add(res);
                        else
                            QueryCreator.generateTriviaQuery(databaseReference, 1, asyncCallback);
                        break;
                }

                if (countries.size()==4){
                    CHOSEN_COUNTRY = countries.get((int) Math.floor(Math.random()* (countries.size()-1)));
                    questionsText.setText(composeTriviaQuestion(subject,CHOSEN_COUNTRY.getName()));
                    switch (subject){
                        case "capital":
                            answerOne.setText(countries.get(0).getCapital());
                            answerTwo.setText(countries.get(1).getCapital());
                            answerThree.setText(countries.get(2).getCapital());
                            answerFour.setText(countries.get(3).getCapital());
                            CORRECT_ANSWER = CHOSEN_COUNTRY.getCapital();
                            break;
                        case "region":
                            answerOne.setText(countries.get(0).getRegion());
                            answerTwo.setText(countries.get(1).getRegion());
                            answerThree.setText(countries.get(2).getRegion());
                            answerFour.setText(countries.get(3).getRegion());
                            CORRECT_ANSWER = CHOSEN_COUNTRY.getRegion();
                            break;
                        case "subRegion":
                            answerOne.setText(countries.get(0).getSubRegion());
                            answerTwo.setText(countries.get(1).getSubRegion());
                            answerThree.setText(countries.get(2).getSubRegion());
                            answerFour.setText(countries.get(3).getSubRegion());
                            CORRECT_ANSWER = CHOSEN_COUNTRY.getSubRegion();
                            break;
                    }
                }
            }
        };
        QueryCreator.generateTriviaQuery(databaseReference,4,asyncCallback);
    }

    private String composeTriviaQuestion(String subject,String name) {
        return String.format("What is the %s of %s",subject,name);
    }


}