package com.tusofia.flagguessinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    List<Country> countries;
    List<String> options;
    Country correctCountry;

    int score = 0;

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

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        loadCountries();

        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option1Button.getText().toString());
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option2Button.getText().toString());
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option3Button.getText().toString());
            }
        });

        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option4Button.getText().toString());
            }
        });
    }

    private void loadCountries() {
        countries = new ArrayList<>();

        db.collection("countries")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(com.google.firebase.firestore.QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Country country = document.toObject(Country.class);
                            countries.add(country);
                        }

                        // Start quiz
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
        int score = 0;
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

        // Shuffle the options list
        Collections.shuffle(options);

        // Load the flag image
        loadImage(correctCountry.getFlagUrl());

        // Set the options on buttons
        option1Button.setText(options.get(0));
        option2Button.setText(options.get(1));
        option3Button.setText(options.get(2));
        option4Button.setText(options.get(3));
    }

    private void checkAnswer(String selectedOption) {
        if (selectedOption.equals(correctCountry.getName())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            score++;
        } else {
            Intent intent = new Intent(getApplicationContext(), GameOver.class);
            intent.putExtra("score", score);
            intent.putExtra("highScore", 0);
            startActivity(intent);
            finish();
        }

        startQuiz();
    }

    private void loadImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(flagImageView);
    }
}