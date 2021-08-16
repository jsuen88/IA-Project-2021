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
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
/**
 * A class regarding the Adapter, which is necessary in the function of the recycler
 * view.
 */
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
        String substring = mData.get(position).getInfo();
        if (substring.length() > 100)
        {
            substring = substring.substring(0, 100) + "...";
        }
        holder.titleText.setText("Post Title: " + mData.get(position).getPostName());
        holder.authorText.setText("Post Details: " + substring);


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
                intent.putExtra("url", mData.get(position).getPicURL());
                intent.putExtra("email", mData.get(position).getContactEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void filterList(ArrayList<Post> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}
