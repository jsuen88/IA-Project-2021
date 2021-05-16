package com.example.cisnewsapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.R;

import java.util.ArrayList;

public class ModRecAdapter extends RecyclerView.Adapter<ModViewHolder>
{
    ArrayList<Post> mData;
    private Context context;

    public ModRecAdapter(ArrayList data, Context context)
    {
        this.mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ModViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mod_row_view, parent, false);
        ModViewHolder holder = new ModViewHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ModViewHolder holder, final int position) {
        holder.titleModView.setText(mData.get(position).getPostName());
        holder.authorModView.setText(mData.get(position).getPostCreator());

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
                intent.putExtra("approval", mData.get(position).getApprovalStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
