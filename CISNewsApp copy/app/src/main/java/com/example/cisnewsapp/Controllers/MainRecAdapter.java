package com.example.cisnewsapp.Controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.R;

import java.util.ArrayList;

public class MainRecAdapter extends RecyclerView.Adapter<MainViewHolder> {

    ArrayList<Post> mData;
    private Context context;

    public MainRecAdapter(ArrayList data, Context context)
    {
        this.mData = data;
        this.context = context;
    }
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row_view, parent, false);
        MainViewHolder mainHolder = new MainViewHolder(myView);
        return mainHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        holder.titleText.setText(mData.get(position).getPostName());
        holder.authorText.setText(mData.get(position).getPostCreator());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
