package com.example.cisnewsapp.Controllers;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cisnewsapp.R;

import java.util.ArrayList;

public class MainRecAdapter extends RecyclerView.Adapter<MainViewHolder> {

    ArrayList<String> mData;

    public MainRecAdapter(ArrayList data)
    {
        mData = data;
    }
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row_view, parent, false);
        MainViewHolder mainHolder = new MainViewHolder(myView);
        return mainHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.titleText.setText(mData.get(position));
        holder.authorText.setText("bill");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
