package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cisnewsapp.Models.Admin;
import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.Student;
import com.example.cisnewsapp.Models.Teacher;
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

/**
 * Class containing methods concerning the app's main page that allow
 * for the display of posts in recycler views, as well as containing buttons
 * that allow the user to navigate to other pages.
 *
 * Author: Joson Suen
 * Version: 1.0
 */
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAndPopulateData();
        updateStreak();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendIntent = PendingIntent.getService(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendIntent);
        startAlarm(calendar);

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
    /**
     * Method used to start the app's daily notification at 7am
     *
     * @param c: calendar object used to set the correct time for the alarm to ring
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarm(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
    /**
     * Method called in onCreate of this class, which updates the user's
     * current streak and displays the data in TextViews.
     *
     */
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
                    if (user.getUserType().equals("Admin")) {
                        Admin admin = ds.toObject(Admin.class);
                        Date lastVisit = admin.getLastVisit();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastVisit);
                        int streak = admin.getCurrentStreak();

                        if (cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                                cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                cal.get(Calendar.MINUTE) + 1 == Calendar.getInstance().get(Calendar.MINUTE)) {
                            streak += 1;
                            if (streak > admin.getLongestStreak()) {
                                admin.setLongestStreak(streak);
                            }
                            admin.setCurrentStreak(streak);
                        }
                        else if (!(cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                                cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                cal.get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE))) {
                            streak = 1;
                            admin.setCurrentStreak(streak);
                        }
                        admin.setLastVisit(Calendar.getInstance().getTime());
                        firestore.collection("users").document(admin.getUid()).set(admin);
                        streakView.setText("Daily streak: " + admin.getCurrentStreak());
                        highestStreakView.setText("Highest streak: " + admin.getLongestStreak());
                    }
                    else if (user.getUserType().equals("Student")) {
                        Student student = ds.toObject(Student.class);
                        Date lastVisit = student.getLastVisit();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastVisit);
                        int streak = student.getCurrentStreak();

                        if (cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                                cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                cal.get(Calendar.MINUTE) + 1 == Calendar.getInstance().get(Calendar.MINUTE)) {
                            streak += 1;
                            if (streak > student.getLongestStreak()) {
                                student.setLongestStreak(streak);
                            }
                            student.setCurrentStreak(streak);
                        }
                        else if (!(cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                                cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                cal.get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE))) {
                            streak = 1;
                            student.setCurrentStreak(streak);
                        }
                        student.setLastVisit(Calendar.getInstance().getTime());
                        firestore.collection("users").document(student.getUid()).set(student);
                        streakView.setText("Daily streak: " + student.getCurrentStreak());
                        highestStreakView.setText("Highest streak: " + student.getLongestStreak());
                    }
                    else if (user.getUserType().equals("Teacher")) {
                        Teacher teacher = ds.toObject(Teacher.class);
                        Date lastVisit = user.getLastVisit();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastVisit);
                        int streak = teacher.getCurrentStreak();

                        if (cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                                cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                cal.get(Calendar.MINUTE) + 1 == Calendar.getInstance().get(Calendar.MINUTE)) {
                            streak += 1;
                            if (streak > teacher.getLongestStreak()) {
                                teacher.setLongestStreak(streak);
                            }
                            teacher.setCurrentStreak(streak);
                        }
                        else if (!(cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                                cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                cal.get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE))) {
                            streak = 1;
                            teacher.setCurrentStreak(streak);
                        }
                        teacher.setLastVisit(Calendar.getInstance().getTime());
                        firestore.collection("users").document(user.getUid()).set(teacher);
                        streakView.setText("Daily streak: " + teacher.getCurrentStreak());
                        highestStreakView.setText("Highest streak: " + teacher.getLongestStreak());
                    }
                }
            }
        });
    }
    /**
     * Helper method that is called when the user uses the app's search
     * bar and helps to filter for posts with names that match the search
     * bar's contents.
     *
     * @param text: the user's input into the search bar
     */
    public void filter(String text) {
        ArrayList<Post> filteredList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getPostName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(post);
            }
        }
        adapt.filterList(filteredList);
    }
    /**
     * Method that retrieves data from Firebase and filters the returned posts
     * such that it displays posts which fit all of the criteria to be displayed.
     * The posts are then displayed in a recycler view, where each post occupies
     * a row.
     *
     */
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
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
                                Collections.swap(tempPost, i-1, i);
                                z -= 1;
                            }
                        }
                    }
                    helper(tempPost);
                }
            }
        });
    }
    /**
     * Helper method which helps to set up the recycler view
     *
     * @param post: the current post
     */
    public void helper (ArrayList<Post> post)
    {
        recView = findViewById(R.id.mainRecView);
        posts = post;

        mLayoutManager = new LinearLayoutManager(this);
        adapt = new MainRecAdapter(posts, this);
        recView.setAdapter(adapt);
        recView.setLayoutManager(mLayoutManager);
    }
    /**
     * Method called when the user clicks on the sign out button. It directs
     * them back to the login page.
     *
     * @param v
     */
    public void signOut (View v)
    {
        mAuth.signOut();
        Intent nextScreen = new Intent(getBaseContext(), AuthActivity.class);
        startActivity(nextScreen);
        finish();
    }
    /**
     * Method that is called when the user clicks on the CCA button, allowing
     * them to see only posts of the CCA category.
     *
     * @param v: the current view displayed to the user
     * @throws InterruptedException
     */
    public void goToCCA (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("CCA");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("CCA");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("CCA");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "CCA";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method that is called when the user clicks on the service button, allowing
     * them to see only posts of the service category.
     *
     * @param v: the current view displayed to the user
     * @throws InterruptedException
     */
    public void goToService (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("Service");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("Service");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("Service");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "Service";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method that is called when the user clicks on the sports button, allowing
     * them to see only posts of the sports category.
     *
     * @param v: the current view displayed to the user
     * @throws InterruptedException
     */
    public void goToSports (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("Sports");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("Sports");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("Sports");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "Sports";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method that is called when the user clicks on the academics button, allowing
     * them to see only posts of the academics category.
     *
     * @param v: the current view displayed to the user
     * @throws InterruptedException
     */
    public void goToAcademics (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("Academics");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("Academics");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("Academics");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "Academics";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method that is called when the user clicks on the miscellaneous button, allowing
     * them to see only posts of the miscellaneous category.
     *
     * @param v: the current view displayed to the user
     * @throws InterruptedException
     */
    public void goToMisc (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("Miscellaneous");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("Miscellaneous");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("Miscellaneous");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "Miscellaneous";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method that is called when the create post button is pressed,
     * and directs the user to the create post page.
     *
     * @param v: the current view displayed to the user
     */
    public void goToCreatePost (View v) {
        Intent nextScreen = new Intent(getBaseContext(), NewPostActivity.class);
        startActivity(nextScreen);
    }
    /**
     * Method that is called when the admin button is pressed,
     * and directs the user to the moderation page.
     *
     * @param v: the current view displayed to the user
     */
    public void goToAdmin (View v) {
        Intent nextScreen = new Intent(getBaseContext(), ModActivity.class);
        startActivity(nextScreen);
    }
    /**
     * Method that is called when the CIS News logo is pressed,
     * and directs the user to the analytics page.
     *
     * @param v: the current view displayed to the user
     */
    public void goToAnalytics (View v) {
        Intent nextScreen = new Intent(getBaseContext(), AppDataAnalytics.class);
        startActivity(nextScreen);
    }
    /**
     * Method that is called when the "recent posts" button is pressed,
     * allowing them to see all recent posts regardless of category.
     *
     * @param v: the current view displayed to the user
     */
    public void goToHome (View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("All");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("All");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("All");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "All";
        Thread.sleep(2000);
        Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
        startActivity(nextScreen);
    }
    /**
     * Method that is called when the "starred posts" button is pressed,
     * allowing them to see all posts they have marked as starred.
     *
     * @param v: the current view displayed to the user
     */
    public void goToStarredPosts(View v) throws InterruptedException {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            User user = ds.toObject(User.class);
                            if (user.getUserType().equals("Student")) {
                                User student = ds.toObject(Student.class);
                                student.setCurrentlyViewing("Starred");
                                firestore.collection("users").document(student.getUid()).set(student);
                            }
                            if (user.getUserType().equals("Admin")) {
                                User admin = ds.toObject(Admin.class);
                                admin.setCurrentlyViewing("Starred");
                                firestore.collection("users").document(admin.getUid()).set(admin);
                            }
                            if (user.getUserType().equals("Teacher")) {
                                User teacher = ds.toObject(Teacher.class);
                                teacher.setCurrentlyViewing("Starred");
                                firestore.collection("users").document(teacher.getUid()).set(teacher);
                            }
                        }
                    }
                });
        currentlyViewing = "Starred";
        Thread.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}