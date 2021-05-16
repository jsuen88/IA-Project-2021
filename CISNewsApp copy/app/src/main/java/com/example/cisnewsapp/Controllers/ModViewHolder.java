package com.example.cisnewsapp.Controllers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cisnewsapp.R;

public class ModViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleModView;
    protected TextView authorModView;
    private ConstraintLayout layout;

    public ModViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.layout = itemView.findViewById(R.id.mod_layout);
        titleModView = itemView.findViewById(R.id.titleModView);
        authorModView = itemView.findViewById(R.id.authorModView);
    }

    public ConstraintLayout getLayout()
    {
        return layout;
    }
}
