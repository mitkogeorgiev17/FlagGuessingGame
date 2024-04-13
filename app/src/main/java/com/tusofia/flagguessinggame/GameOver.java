package com.tusofia.flagguessinggame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameOver extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    Button logout;

    TextView scoreText;
    TextView highScoreText;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        logout = findViewById(R.id.new_game);
        scoreText = findViewById(R.id.your_score);
        highScoreText = findViewById(R.id.high_score);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        Intent scoreIntent = getIntent();
        scoreText.setText("Your score: " + scoreIntent.getIntExtra("score", 0));
        highScoreText.setText("High score: " + + scoreIntent.getIntExtra("highScore", 0));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlagQuiz.class);
                startActivity(intent);
                finish();
            }
        });
    }
}