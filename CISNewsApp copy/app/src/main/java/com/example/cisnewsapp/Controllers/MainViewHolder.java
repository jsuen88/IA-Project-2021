package com.example.cisnewsapp.Controllers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cisnewsapp.R;

public class MainViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText;
    protected TextView authorText;
    private ConstraintLayout layout;

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
        this.layout = itemView.findViewById(R.id.main_layout);
        titleText = itemView.findViewById(R.id.titleTextView);
        authorText = itemView.findViewById(R.id.authorTextView);
    }

    public ConstraintLayout getLayout()
    {
        return layout;
    }
}
