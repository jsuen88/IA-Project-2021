package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    RecyclerView recView;
    ArrayList<String> allTheStuff;

    ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAndPopulateData();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        recView = findViewById(R.id.mainRecView);

        allTheStuff = new ArrayList();
        allTheStuff.add("enchanted virus");
        allTheStuff.add("enchanted virus 2.0");
        allTheStuff.add("enchanted virus 3.0");
        
    }

    public void getAndPopulateData()
    {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Post> tempPost = new ArrayList<>();
                for (DocumentSnapshot ds : task.getResult().getDocuments())
                {
                    Post post = ds.toObject(Post.class);
                    tempPost.add(post);
                }
                help(tempPost);
            }
        });
    }

    public void help (ArrayList<Post> post)
    {
        recView = findViewById(R.id.mainRecView);
        posts = post;
        MainRecAdapter adapt = new MainRecAdapter(posts, this);
        recView.setAdapter(adapt);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void signOut (View v)
    {
        mAuth.signOut();
        Intent nextScreen = new Intent(getBaseContext(), AuthActivity.class);
        startActivity(nextScreen);
        finish();
    }

    public void goToCCA (View v) {
        Intent nextScreen = new Intent(getBaseContext(), CCAActivity.class);
        startActivity(nextScreen);
    }

    public void goToService (View v) {
        Intent nextScreen = new Intent(getBaseContext(), ServiceActivity.class);
        startActivity(nextScreen);
    }

    public void goToSports (View v) {
        Intent nextScreen = new Intent(getBaseContext(), SportsActivity.class);
        startActivity(nextScreen);
    }

    public void goToAcademics (View v) {
        Intent nextScreen = new Intent(getBaseContext(), AcademicsActivity.class);
        startActivity(nextScreen);
    }

    public void goToMisc (View v) {
        Intent nextScreen = new Intent(getBaseContext(), MiscActivity.class);
        startActivity(nextScreen);
    }

    public void goToCreatePost (View v) {
        Intent nextScreen = new Intent(getBaseContext(), NewPostActivity.class);
        startActivity(nextScreen);
    }
}