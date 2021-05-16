package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    RecyclerView adminRecView;
    ArrayList<String> mData;

    ArrayList<Post> posts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        getAndPopulateData();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        adminRecView = findViewById(R.id.adminRecView);

    }

    public void getAndPopulateData() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final ArrayList<Post> tempPost = new ArrayList<>();
                for (DocumentSnapshot ds : task.getResult().getDocuments())
                {
                    Post post = ds.toObject(Post.class);
                    if (post.getApprovalStatus().equals("awaiting"))
                    {
                        tempPost.add(post);
                    }
                }
                help(tempPost);
            }
        });
    }

    public void help (ArrayList<Post> post)
    {
        adminRecView = findViewById(R.id.adminRecView);
        posts = post;
        ModRecAdapter adapt = new ModRecAdapter(posts, this);
        adminRecView.setAdapter(adapt);
        adminRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}