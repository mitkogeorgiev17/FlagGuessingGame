package com.tusofia.flagguessinggame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tusofia.flagguessinggame.entity.Country;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FlagQuiz extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;

    ImageView flagImageView;
    Button option1Button, option2Button, option3Button, option4Button;
    TextView scoreTextView, multiplierTextView, livesTextView;

    List<Country> countries;
    List<String> options;
    Country correctCountry;

    int score = 0;
    int streak = 0;
    int multiplier = 1;
    int lives = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flag_quiz);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        flagImageView = findViewById(R.id.flagImageView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        scoreTextView = findViewById(R.id.scoreTextView);
        multiplierTextView = findViewById(R.id.multiplierTextView);
        livesTextView = findViewById(R.id.livesTextView);
        updateViews();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        loadCountries();

        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                checkAnswer(clickedButton.getText().toString());
            }
        };

        option1Button.setOnClickListener(answerClickListener);
        option2Button.setOnClickListener(answerClickListener);
        option3Button.setOnClickListener(answerClickListener);
        option4Button.setOnClickListener(answerClickListener);
    }

    private void loadCountries() {
        countries = new ArrayList<>();

        db.collection("countries")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Country country = document.toObject(Country.class);
                            countries.add(country);
                        }
                        startQuiz();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FlagQuiz.this, "Failed to load countries", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startQuiz() {
        Random random = new Random();
        correctCountry = countries.get(random.nextInt(countries.size()));

        options = new ArrayList<>();
        options.add(correctCountry.getName());
        while (options.size() < 4) {
            Country randomCountry = countries.get(random.nextInt(countries.size()));
            if (!options.contains(randomCountry.getName())) {
                options.add(randomCountry.getName());
            }
        }

        Collections.shuffle(options);
        loadImage(correctCountry.getFlagUrl());
    }

    private void checkAnswer(String selectedOption) {
        Button selectedButton = findButtonByText(selectedOption);
        if (selectedOption.equals(correctCountry.getName())) {
            streak++;
            multiplier = Math.min(streak, 5);
            score += 1 * multiplier;
            selectedButton.setBackgroundColor(Color.GREEN);
        } else {
            streak = 0;
            multiplier = 1;
            lives--;
            selectedButton.setBackgroundColor(Color.RED);
            Button correctButton = findButtonByText(correctCountry.getName());
            correctButton.setBackgroundColor(Color.GREEN);
            if (lives <= 0) {
                Intent intent = new Intent(getApplicationContext(), GameOver.class);
                intent.putExtra("score", score);
                startActivity(intent);
                finish();
            }
        }
        updateViews();
        new Handler().postDelayed(this::startQuiz, 1000);
    }

    private void loadImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(flagImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                setOptions();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(FlagQuiz.this, "Error loading image.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOptions() {
        option1Button.setText(options.get(0));
        option2Button.setText(options.get(1));
        option3Button.setText(options.get(2));
        option4Button.setText(options.get(3));
        resetButtonColors();
    }

    private void resetButtonColors() {
        int defaultColor = Color.parseColor("#FF8291FF");
        option1Button.setBackgroundColor(defaultColor);
        option2Button.setBackgroundColor(defaultColor);
        option3Button.setBackgroundColor(defaultColor);
        option4Button.setBackgroundColor(defaultColor);
    }

    private void updateViews() {
        scoreTextView.setText("Score: " + score);
        multiplierTextView.setText("Multiplier: x" + multiplier);
        livesTextView.setText("Lives: " + lives);
    }

    private Button findButtonByText(String text) {
        if (option1Button.getText().toString().equals(text)) return option1Button;
        if (option2Button.getText().toString().equals(text)) return option2Button;
        if (option3Button.getText().toString().equals(text)) return option3Button;
        if (option4Button.getText().toString().equals(text)) return option4Button;
        return null;
    }
}
