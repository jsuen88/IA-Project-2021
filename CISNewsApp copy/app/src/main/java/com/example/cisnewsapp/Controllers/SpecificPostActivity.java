package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SpecificPostActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView authorView;
    private TextView categoryView;
    private TextView dateView;
    private TextView infoView;
    private Button signOut;
    private Button goBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String title;
    private String author;
    private String category;
    private String date;
    private String info;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_post);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        titleView = findViewById(R.id.titleView);
        authorView = findViewById(R.id.authorView);
        categoryView = findViewById(R.id.categoryView);
        dateView = findViewById(R.id.dateView);
        infoView = findViewById(R.id.infoView);
        goBack = findViewById(R.id.goBack);
        signOut = findViewById(R.id.signOut);


        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        category = getIntent().getStringExtra("category");
        date = getIntent().getStringExtra("date");
        info = getIntent().getStringExtra("info");
        id = getIntent().getStringExtra("id");

        this.titleView.setText(title);
        this.authorView.setText("Author : " + author);
        this.categoryView.setText("Category : " + category);
        this.dateView.setText("Date : " + date);
        this.infoView.setText("Info : " + info);
    }

    public void backToMain (View v) {
        Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
        startActivity(nextScreen);
    }

    public void seenPost(View v) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    ArrayList<String> seenPosts = user.getSeenPosts();
                    seenPosts.add(id);
                    user.setSeenPosts(seenPosts);
                    firestore.collection("users").document(user.getUid()).set(user);

                    Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(nextScreen);
                }
            }
        });
    }
}