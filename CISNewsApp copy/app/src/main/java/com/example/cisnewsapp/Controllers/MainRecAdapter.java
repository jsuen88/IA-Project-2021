package com.example.cisnewsapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.GridLayout;

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

        holder.getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SpecificPostActivity.class);
                intent.putExtra("title", mData.get(position).getPostName());
                intent.putExtra("author", mData.get(position).getPostCreator());
                intent.putExtra("category", mData.get(position).getPostCategory());
                intent.putExtra("date", mData.get(position).getPostDate());
                intent.putExtra("info", mData.get(position).getInfo());
                intent.putExtra("id", mData.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
