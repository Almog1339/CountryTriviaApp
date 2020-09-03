package com.example.countrytriviaapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.countrytriviaapp.Activities.MainActivity;
import com.example.countrytriviaapp.Classes.Country;
import com.example.countrytriviaapp.Classes.QueryCreator;
import com.example.countrytriviaapp.Interfaces.IAsyncCallback;
import com.example.countrytriviaapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionsFragment extends Fragment {
    private TextView email;
    private Button logoff;
    private CardView questionsCard;
    private TextView questionsText;
    private CardView answerOneCard;
    private TextView answerOneText;
    private CardView answerTwoCard;
    private TextView answerTwoText;
    private CardView answerThreeCard;
    private TextView answerThreeText;
    private CardView answerFourCard;
    private TextView answerFourText;
    private Button nextQuestion;

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
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        FirebaseUser user = MainActivity.getCurrentUser();
        email = view.findViewById(R.id.questionFragment_userName_textView);
        logoff = view.findViewById(R.id.questionFragment_logout_button);
        questionsCard = view.findViewById(R.id.questionsFragment_questions_cardView);
        questionsText = view.findViewById(R.id.question_textView);

        answerOneCard = view.findViewById(R.id.answerOne);
        answerOneText = view.findViewById(R.id.cardView_textView_answerOne);
        final ImageView imageViewOne = view.findViewById(R.id.imageView);
        answerTwoCard = view.findViewById(R.id.answerTwo);
        answerTwoText = view.findViewById(R.id.cardView_textView_answerTwo);
        final ImageView imageViewTwo = view.findViewById(R.id.imageView2);
        answerThreeCard = view.findViewById(R.id.answerThree);
        answerThreeText = view.findViewById(R.id.cardView_textView_answerThree);
        final ImageView imageViewThree = view.findViewById(R.id.imageView3);
        answerFourCard = view.findViewById(R.id.answerFour);
        answerFourText = view.findViewById(R.id.cardView_textView_answerFour);
        final ImageView imageViewFour = view.findViewById(R.id.imageView4);
        nextQuestion = view.findViewById(R.id.next_question);

        email.setText(user.getEmail());

        final String[] subjects = {"capital", "flag", "region", "subRegion"};
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String subject = subjects[(int) Math.floor(Math.random() * (subjects.length - 1))];

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
                    case "flag":
                        if (!res.getFlag().equals(""))
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
                    String countryName = countries.get((int) Math.floor(Math.random()* (countries.size()-1))).getName();
                    questionsText.setText(generateTriviaQuestion(subject,countryName));
                    switch (subject){
                        case "capital":
                            answerOneText.setText(countries.get(0).getCapital());
                            answerTwoText.setText(countries.get(1).getCapital());
                            answerThreeText.setText(countries.get(2).getCapital());
                            answerFourText.setText(countries.get(3).getCapital());
                            break;
                        case "flag":
                            //TODO:Need to check how to convert String to URI
//                            imageViewOne.setImageURI(countries.get(0).getFlag());
//                            imageViewTwo.setImageURI(countries.get(1).getFlag());
//                            imageViewThree.setImageURI(countries.get(2).getFlag());
//                            imageViewFour.setImageURI(countries.get(3).getFlag());
                            break;
                        case "region":
                            answerOneText.setText(countries.get(0).getRegion());
                            answerTwoText.setText(countries.get(1).getRegion());
                            answerThreeText.setText(countries.get(2).getRegion());
                            answerFourText.setText(countries.get(3).getRegion());
                            break;
                        case "subRegion":
                            answerOneText.setText(countries.get(0).getSubRegion());
                            answerTwoText.setText(countries.get(1).getSubRegion());
                            answerThreeText.setText(countries.get(2).getSubRegion());
                            answerFourText.setText(countries.get(3).getSubRegion());
                            break;
                    }
                }
            }
        };

        QueryCreator.generateTriviaQuery(databaseReference, 4, asyncCallback);


        return view;
    }

    private String generateTriviaQuestion(String subject,String name) {
        return String.format("What is the %s of %s",subject,name);
    }


}