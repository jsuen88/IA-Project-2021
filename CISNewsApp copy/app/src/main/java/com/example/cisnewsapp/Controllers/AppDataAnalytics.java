package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cisnewsapp.Models.Admin;
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
import java.util.Calendar;
import java.util.Date;

/**
 * Class that retrieves data from a database and processes it in order to show statistics regarding
 * users and posts. Contains buttons that direct the user to other activities
 *
 * Author: Joson Suen
 * Version: 1.0
 */
public class AppDataAnalytics extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Button postsAnalysis;
    private Button userAnalysis;
    private TextView userView1;
    private TextView userView2;
    private TextView userView3;
    private TextView postView1;
    private TextView postView2;
    private TextView postView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_data_analytics);

        postsAnalysis = findViewById(R.id.postsAnalysisButton);
        userAnalysis = findViewById(R.id.userAnalysisButton);
        userView1 = findViewById(R.id.userView1);
        userView2 = findViewById(R.id.userView2);
        userView3 = findViewById(R.id.userView3);
        postView1 = findViewById(R.id.postView1);
        postView2 = findViewById(R.id.postView2);
        postView3 = findViewById(R.id.postView3);

        setUpUserAnalytics();
        setUpPostsAnalytics();
    }

    /**
     * retrieves data from the database and sets Textviews to show statistics about users of the app
     *
     */
    public void setUpUserAnalytics() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                    final ArrayList<User> totalUsers = new ArrayList<>();
                    final ArrayList<User> todayUsers = new ArrayList<>();
                    final ArrayList<User> newUsers = new ArrayList<>();

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -7);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.set(Calendar.HOUR_OF_DAY, 0);
                    cal2.set(Calendar.MINUTE, 0);
                    cal2.set(Calendar.SECOND, 0);
                    System.out.println(cal.getTime());
                    System.out.println(cal2.getTime());
                    for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                        User user = ds.toObject(User.class);
                        totalUsers.add(user);
                        System.out.println(user.getLastVisit());
                        if (user.getLastVisit().after(cal2.getTime())) {
                            todayUsers.add(user);
                        }
                        if (user.getAccountCreated().after(cal.getTime())) {
                            newUsers.add(user);
                        }
                    }
                    userView1.setText("Total number of app users: " + totalUsers.size());
                    userView2.setText("Number of users active today: " + todayUsers.size());
                    userView3.setText("New users this week: " + newUsers.size());

                }
            }
        });
    }

    /**
     * retrieves data from the database and sets TextViews to show statistics about posts in the app
     *
     */
    public void setUpPostsAnalytics() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final ArrayList<Post> totalPosts = new ArrayList<>();
                    final ArrayList<Post> todayPosts = new ArrayList<>();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    for (DocumentSnapshot ds : task.getResult().getDocuments())
                    {
                        Post post = ds.toObject(Post.class);
                        totalPosts.add(post);
                        if (post.getPostDate().after(cal.getTime())) {
                            todayPosts.add(post);
                        }
                    }
                    postView1.setText("Total number of posts: " + totalPosts.size());
                    postView2.setText("Number of new posts today: " + todayPosts.size());
                }
            }
        });
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    Admin user = ds.toObject(Admin.class);
                    postView3.setText("Posts you've processed: " + (user.getApprovedPosts() + user.getDeniedPosts()));
                }
            }
        });
    }

    /**
     * directs user to UserAnalyticsActivity
     *
     * @param v: view
     */
    public void goToUserAnalytics(View v) {
        Intent nextScreen = new Intent(getBaseContext(), UserDataAnalytics.class);
        startActivity(nextScreen);
    }

    /**
     * directs user to PostAnalyticsActivity
     *
     * @param v: view
     */
    public void goToPostsAnalytics(View v) {
        Intent nextScreen = new Intent(getBaseContext(), PostsDataAnalytics.class);
        startActivity(nextScreen);
    }
}