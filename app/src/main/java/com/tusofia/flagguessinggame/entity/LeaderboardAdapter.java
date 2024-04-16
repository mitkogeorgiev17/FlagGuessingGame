package com.tusofia.flagguessinggame.entity;

import static java.lang.String.valueOf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tusofia.flagguessinggame.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<LeaderboardEntry> entries;

    public LeaderboardAdapter(List<LeaderboardEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardEntry entry = entries.get(position);
        holder.placeNameTextView.setText(valueOf(entry.getPosition()).concat(". ").concat(entry.getName()));
        switch (entry.getPosition()) {
            case 1: holder.placePointsTextView.setText(valueOf(entry.getHighScore()).concat("🥇")); break;
            case 2: holder.placePointsTextView.setText(valueOf(entry.getHighScore()).concat("🥈")); break;
            case 3: holder.placePointsTextView.setText(valueOf(entry.getHighScore()).concat("🥉")); break;
            default: holder.placePointsTextView.setText(valueOf(entry.getHighScore())); break;
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeNameTextView;
        public TextView placePointsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.placeNameTextView);
            placePointsTextView = itemView.findViewById(R.id.placePointsTextView);
        }
    }
}

