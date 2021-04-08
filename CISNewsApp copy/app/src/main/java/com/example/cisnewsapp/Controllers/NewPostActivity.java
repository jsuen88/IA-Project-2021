package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cisnewsapp.Models.AcademicsPost;
import com.example.cisnewsapp.Models.CCAPost;
import com.example.cisnewsapp.Models.MiscPost;
import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.ServicePost;
import com.example.cisnewsapp.Models.SportsPost;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    public EditText editPostName;
    public EditText editInfo;
    public EditText extraEdit1;
    public EditText extraEdit2;
    public EditText extraEdit3;
    public Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Spinner spinner = findViewById(R.id.rolespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.post_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        editPostName = findViewById(R.id.editPostName);
        editInfo = findViewById(R.id.editInfo);
        extraEdit1 = findViewById(R.id.extraEdit1);
        extraEdit2 = findViewById(R.id.extraEdit2);
        extraEdit3 = findViewById(R.id.extraEdit3);
        mySpinner = findViewById(R.id.rolespinner);
    }

    public boolean inputValid()
    {
        return true;
    }

    public void addNewPost(View v)
    {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    String owner = user.getName();
                    String title = editPostName.getText().toString();
                    String info = editInfo.getText().toString();
                    String postCategory = mySpinner.getSelectedItem().toString();
                    ArrayList<Integer> yearGroups = new ArrayList();
                    boolean cantonese = false;
                    if (inputValid())
                    {
                        if (postCategory.equals("CCA")) {
                            CCAPost post = new CCAPost(title, postCategory, owner, info, "post date here", "lasts until here", yearGroups, extraEdit2.getText().toString());

                            firestore.collection("Posts").document(title).set(post);
                        }
                        if (postCategory.equals("Service")) {
                            if (extraEdit1.getText().toString().equals("yes")) {
                                cantonese = true;
                            }
                            ServicePost post = new ServicePost(title, postCategory, owner, info, "post date here", "lasts until here", cantonese, extraEdit2.getText().toString());
                            firestore.collection("Posts").document(title).set(post);
                        }
                        if (postCategory.equals("Sports")) {
                            SportsPost post = new SportsPost(title, postCategory, owner, info, "post date here", "lasts until here", yearGroups, extraEdit2.getText().toString(), extraEdit3.getText().toString());
                            firestore.collection("Posts").document(title).set(post);
                        }
                        if (postCategory.equals("Academics")) {
                            AcademicsPost post = new AcademicsPost(title, postCategory, owner, info, "post date here", "lasts until here", yearGroups);
                            firestore.collection("Posts").document(title).set(post);
                        }
                        if (postCategory.equals("Miscellaneous")) {
                            Post post = new Post(title, postCategory, owner, info, "post date here", "lasts until here");
                            firestore.collection("Posts").document(title).set(post);
                        }
                        ArrayList<String> createdPosts = user.getCreatedPosts();
                        createdPosts.add(title);
                        user.setCreatedPosts(createdPosts);
                        firestore.collection("users").document(user.getUid()).set(user);
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void cancel (View v) {
        Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
        startActivity(nextScreen);
    }

    public void signOut (View v)
    {
        mAuth.signOut();
        Intent nextScreen = new Intent(getBaseContext(), AuthActivity.class);
        startActivity(nextScreen);
        finish();
    }
}