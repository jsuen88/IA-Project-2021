package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Button adminButton;
    private TextView currentlyViewingText;
    private TextView streakView;
    private TextView highestStreakView;
    private ArrayList<Post> posts = new ArrayList<>();
    //MainRecAdapter adapt = new MainRecAdapter(posts, this);
    private MainRecAdapter adapt;
    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerView recView;
    String currentlyViewing;

    ArrayList<String> seenPosts = new ArrayList<>();
    ArrayList<String> starredPosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAndPopulateData();

        updateStreak();

        //calendar.set(Calendar.HOUR_OF_DAY, 17);
        //calendar.set(Calendar.MINUTE, 37);
        //calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendIntent = PendingIntent.getService(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 37);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendIntent);

        EditText editText = findViewById(R.id.searchBar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        recView = findViewById(R.id.mainRecView);
        adminButton = findViewById(R.id.adminButton);
        adminButton.setVisibility(View.GONE);
        currentlyViewingText = findViewById(R.id.currentlyViewingText);
        streakView = findViewById(R.id.streakView);
        highestStreakView = findViewById(R.id.highestStreakView);

        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    if (String.valueOf(user.getUserType()).equals("Admin"))
                    {
                        adminButton.setVisibility(View.VISIBLE);
                    }

                    currentlyViewingText.setText("You are currently viewing: " + currentlyViewing + " posts");
                }
            }
        });

    }

    public void updateStreak()
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
                    Date lastVisit = user.getLastVisit();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastVisit);
                    int streak = user.getCurrentStreak();

                    System.out.println(cal.get((Calendar.MINUTE))+1);
                    System.out.println(Calendar.getInstance().get(Calendar.MINUTE));
                    if (cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                            cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                            cal.get(Calendar.MINUTE)+1 == Calendar.getInstance().get(Calendar.MINUTE)) {
                        streak+=1;
                        if (streak > user.getLongestStreak())
                        {
                            user.setLongestStreak(streak);
                        }
                        user.setCurrentStreak(streak);
                    }
                    else if (!(cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                            cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                            cal.get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE))) {
                        streak = 1;
                        user.setCurrentStreak(streak);
                    }
                    user.setLastVisit(Calendar.getInstance().getTime());
                    firestore.collection("users").document(user.getUid()).set(user);
                    streakView.setText("Daily streak: " + user.getCurrentStreak());
                    highestStreakView.setText("Highest streak: " + user.getLongestStreak());
                }
            }
        });
    }

    public void filter(String text) {
        ArrayList<Post> filteredList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getPostName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(post);
            }
        }
        adapt.filterList(filteredList);
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
                    currentlyViewing = user.getCurrentlyViewing();
                    starredPosts = user.getStarredPosts();
                }
            }
        });
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    final ArrayList<Post> tempPost = new ArrayList<>();
                    for (DocumentSnapshot ds : task.getResult().getDocuments())
                    {
                        Post post = ds.toObject(Post.class);
                        if (!currentlyViewing.equals("Starred"))
                        {
                            Date d = Calendar.getInstance().getTime();
                            if (!seenPosts.contains(post.getId()) && post.getApprovalStatus().equals("approved")
                                    && ((post.getPostCategory().equals(currentlyViewing)) || currentlyViewing.equals("All"))
                                    && (post.getPostDate().before(d)) && post.getLastsUntil().after(d))
                            {
                                tempPost.add(post);
                            }
                        }
                        else {
                            if (starredPosts.contains(post.getId()))
                            {
                                tempPost.add(post);
                            }
                        }
                        for (int i = 1; i < tempPost.size(); i++) {
                            int z = i;
                            while (z > 0 && tempPost.get(i-1).getPostDate().before(tempPost.get(i).getPostDate())) {
                                //Post p = tempPost.get(i-1);
                                //tempPost.set(i-1, tempPost.get(i));
                                //tempPost.set(i, p);
                                Collections.swap(tempPost, i-1, i);
                                z -= 1;
                            }
                        }
                    }
                    help(tempPost);
                }
            }
        });
    }

    public void help (ArrayList<Post> post)
    {
        recView = findViewById(R.id.mainRecView);
        posts = post;
        //MainRecAdapter adapt = new MainRecAdapter(posts, this);

        mLayoutManager = new LinearLayoutManager(this);
        adapt = new MainRecAdapter(posts, this);
        recView.setAdapter(adapt);
        recView.setLayoutManager(mLayoutManager);
    }

    public void signOut (View v)
    {
        mAuth.signOut();
        Intent nextScreen = new Intent(getBaseContext(), AuthActivity.class);
        startActivity(nextScreen);
        finish();
    }

    public void goToCCA (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("CCA");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "CCA";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToService (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("Service");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "Service";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToSports (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("Sports");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "Sports";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToAcademics (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("Academics");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "Academics";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToMisc (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("Miscellaneous");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "Miscellaneous";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToCreatePost (View v) {
        Intent nextScreen = new Intent(getBaseContext(), NewPostActivity.class);
        startActivity(nextScreen);
    }

    public void goToAdmin (View v) {
        Intent nextScreen = new Intent(getBaseContext(), ModActivity.class);
        startActivity(nextScreen);
    }

    public void goToHome (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("All");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "All";
        Thread.sleep(2000);
        Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
        startActivity(nextScreen);
    }

    public void goToStarredPosts(View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            user.setCurrentlyViewing("Starred");
                            firestore.collection("users").document(user.getUid()).set(user);
                        }
                    }
                });
        currentlyViewing = "Starred";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}