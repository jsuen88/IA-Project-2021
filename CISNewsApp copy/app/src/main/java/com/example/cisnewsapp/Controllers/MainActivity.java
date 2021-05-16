package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Button adminButton;

    RecyclerView recView;
    ArrayList<String> allTheStuff;

    ArrayList<Post> posts = new ArrayList<>();
    ArrayList<String> seenPosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAndPopulateData();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        recView = findViewById(R.id.mainRecView);
        adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.GONE);

        allTheStuff = new ArrayList();
        allTheStuff.add("enchanted virus");
        allTheStuff.add("enchanted virus 2.0");
        allTheStuff.add("enchanted virus 3.0");

        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (String.valueOf(user.getUserType()).equals("Admin"))
                            {
                                adminButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    public void getAndPopulateData()
    {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    seenPosts = user.getSeenPosts();
                }
            }
        });
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final ArrayList<Post> tempPost = new ArrayList<>();
                for (DocumentSnapshot ds : task.getResult().getDocuments())
                {
                    Post post = ds.toObject(Post.class);
                    if (!seenPosts.contains(post.getId()) && post.getApprovalStatus().equals("approved"))
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

    public void goToAdmin (View v) {
        Intent nextScreen = new Intent(getBaseContext(), ModActivity.class);
        startActivity(nextScreen);
    }
}