package com.tusofia.flagguessinggame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tusofia.flagguessinggame.entity.LeaderboardAdapter;
import com.tusofia.flagguessinggame.entity.LeaderboardEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button mainMenu;

    FirebaseFirestore db;

    RecyclerView leaderboardRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        mainMenu = findViewById(R.id.return_to_menu);
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        leaderboardRecyclerView.setLayoutManager(layoutManager);

        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        db.collection("users")
                .orderBy("highScore", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int position = 1;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            long highScore = document.getLong("highScore");
                            String name = document.getString("name");
                            leaderboardEntries.add(new LeaderboardEntry(position++, name, highScore));
                        }
                        // Create and set the adapter after fetching the data
                        LeaderboardAdapter adapter = new LeaderboardAdapter(leaderboardEntries);
                        leaderboardRecyclerView.setAdapter(adapter);
                    } else {
                        Log.e("Leaderboard", "Error fetching user data.", task.getException());
                        Toast.makeText(Leaderboard.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
