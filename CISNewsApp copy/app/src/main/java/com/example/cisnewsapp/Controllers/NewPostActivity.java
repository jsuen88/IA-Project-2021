package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    public EditText editPostName;
    public EditText editInfo;
    public EditText extraEdit1;
    public EditText extraEdit2;
    public EditText extraEdit3;
    public Spinner mySpinner;
    Date dateEnd;
    String currentDateString;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Button button = (Button) findViewById(R.id.selectDate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

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

        extraEdit1.setVisibility(View.GONE);
        extraEdit2.setVisibility(View.GONE);
        extraEdit3.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth)
    {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 7);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView dateView = (TextView) findViewById(R.id.dateView);
        dateView.setText(currentDateString);
    }


    public boolean yearsValid()
    {
        for (char c : extraEdit1.getText().toString().toCharArray())
        {
            System.out.println((int)c);
            if (!((int)c > 47 && (int)c < 58 || ((int)c == 32) || (int)c == 44)) {
                Toast message = Toast.makeText(this, "Please make sure your input for the year groups only contains numbers and spaces", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
        }
        return true;
    }

    public boolean inputValid()
    {
        String postCategory = mySpinner.getSelectedItem().toString();
        if (postCategory.equals("CCA")) {
            if (extraEdit1.getText().toString().isEmpty() || editInfo.getText().toString().isEmpty() || editPostName.getText().toString().isEmpty() || extraEdit2.getText().toString().isEmpty()) {
                Toast message = Toast.makeText(this, "Please make sure you've filled in all the input boxes", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
            if (!yearsValid())
            {
                return false;
            }
            String s = extraEdit2.getText().toString();
            if (!s.equals("Monday") && !s.equals("Tuesday") && !s.equals("Wednesday") && !s.equals("Thursday") &&!s.equals("Friday")) {
                Toast message = Toast.makeText(this, "Please enter a valid day of the week into the day input box", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
        }
        if (postCategory.equals("Service")) {
            if (extraEdit1.getText().toString().isEmpty() || editInfo.getText().toString().isEmpty() || editPostName.getText().toString().isEmpty() || extraEdit2.getText().toString().isEmpty()) {
                Toast message = Toast.makeText(this, "Please make sure you've filled in all the input boxes", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
        }
        if (postCategory.equals("Sports")) {
            if (extraEdit1.getText().toString().isEmpty() || editInfo.getText().toString().isEmpty() || editPostName.getText().toString().isEmpty() || extraEdit2.getText().toString().isEmpty() || extraEdit3.getText().toString().isEmpty()) {
                Toast message = Toast.makeText(this, "Please make sure you've filled in all the input boxes", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
            if (!yearsValid())
            {
                return false;
            }
        }
        if (postCategory.equals("Academics")) {
            if (extraEdit1.getText().toString().isEmpty() || editInfo.getText().toString().isEmpty() || editPostName.getText().toString().isEmpty()) {
                Toast message = Toast.makeText(this, "Please make sure you've filled in all the input boxes", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
            if (!yearsValid())
            {
                return false;
            }
        }
        if (postCategory.equals("Miscellaneous")) {
            if (editInfo.getText().toString().isEmpty() || editPostName.getText().toString().isEmpty()) {
                Toast message = Toast.makeText(this, "Please make sure you've filled in all the input boxes", Toast.LENGTH_SHORT);
                message.show();
                return false;
            }
        }
        return true;
    }

    public void addNewPost(View v)
    {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    String owner = user.getName();
                    String title = editPostName.getText().toString();
                    String info = editInfo.getText().toString();
                    String postCategory = mySpinner.getSelectedItem().toString();
                    String id = UUID.randomUUID().toString();
                    ArrayList<Integer> yearGroups = new ArrayList();
                    boolean cantonese = false;
                    if (inputValid())
                    {
                        if (postCategory.equals("CCA")) {
                            String noSpaceStr = extraEdit1.getText().toString().replaceAll("\\s", "");
                            String[] years = noSpaceStr.split(",");
                            for (int i = 0; i < years.length; i++) {
                                yearGroups.add(Integer.parseInt(years[i]));
                            }
                            CCAPost post = new CCAPost(title, postCategory, owner, info, Calendar.getInstance().getTime(), c.getTime(), yearGroups, extraEdit2.getText().toString(), id, "awaiting");
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("Approved");
                                Calendar cal = Calendar.getInstance();
                                Date date = post.getPostDate();
                                cal.setTime(date);
                                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                                cal.set(Calendar.HOUR_OF_DAY, 7);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                date = cal.getTime();
                                post.setPostDate(date);
                            }
                            firestore.collection("Posts").document(title).set(post);
                        }

                        if (postCategory.equals("Service")) {
                            if (extraEdit1.getText().toString().equals("yes")) {
                                cantonese = true;
                            }
                            ServicePost post = new ServicePost(title, postCategory, owner, info, Calendar.getInstance().getTime(), c.getTime(), cantonese, extraEdit2.getText().toString(), id, "awaiting");
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("Approved");
                                Calendar cal = Calendar.getInstance();
                                Date date = post.getPostDate();
                                cal.setTime(date);
                                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                                cal.set(Calendar.HOUR_OF_DAY, 7);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                date = cal.getTime();
                                post.setPostDate(date);
                            }
                            firestore.collection("Posts").document(title).set(post);
                        }

                        if (postCategory.equals("Sports")) {
                            String noSpaceStr = extraEdit1.getText().toString().replaceAll("\\s", "");
                            String[] years = noSpaceStr.split(",");
                            for (int i = 0; i < years.length; i++) {
                                yearGroups.add(Integer.parseInt(years[i]));
                            }

                            SportsPost post = new SportsPost(title, postCategory, owner, info, Calendar.getInstance().getTime(), c.getTime(), yearGroups, extraEdit2.getText().toString(), extraEdit3.getText().toString(), id, "awaiting");
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("Approved");
                                Calendar cal = Calendar.getInstance();
                                Date date = post.getPostDate();
                                cal.setTime(date);
                                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                                cal.set(Calendar.HOUR_OF_DAY, 7);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                date = cal.getTime();
                                post.setPostDate(date);
                            }
                            firestore.collection("Posts").document(title).set(post);
                        }
                        if (postCategory.equals("Academics")) {
                            String noSpaceStr = extraEdit1.getText().toString().replaceAll("\\s", "");
                            String[] years = noSpaceStr.split(",");
                            for (int i = 0; i < years.length; i++) {
                                yearGroups.add(Integer.parseInt(years[i]));
                            }

                            AcademicsPost post = new AcademicsPost(title, postCategory, owner, info, Calendar.getInstance().getTime(), c.getTime(), yearGroups, id, "awaiting");
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("Approved");
                                Calendar cal = Calendar.getInstance();
                                Date date = post.getPostDate();
                                cal.setTime(date);
                                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                                cal.set(Calendar.HOUR_OF_DAY, 7);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                date = cal.getTime();
                                post.setPostDate(date);
                            }
                            firestore.collection("Posts").document(title).set(post);
                        }
                        if (postCategory.equals("Miscellaneous")) {
                            Post post = new Post(title, postCategory, owner, info, Calendar.getInstance().getTime(), c.getTime(), id, "awaiting");
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("Approved");
                                Calendar cal = Calendar.getInstance();
                                Date date = post.getPostDate();
                                cal.setTime(date);
                                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                                cal.set(Calendar.HOUR_OF_DAY, 7);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                date = cal.getTime();
                                post.setPostDate(date);
                            }
                            firestore.collection("Posts").document(title).set(post);
                        }
                        ArrayList<String> createdPosts = user.getCreatedPosts();
                        createdPosts.add(title);
                        user.setCreatedPosts(createdPosts);
                        firestore.collection("users").document(user.getUid()).set(user);

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        String postCategory = mySpinner.getSelectedItem().toString();
        if (postCategory.equals("CCA")) {
            extraEdit1.setVisibility(View.VISIBLE);
            extraEdit1.setHint("Enter year groups: ");
            extraEdit2.setVisibility(View.VISIBLE);
            extraEdit2.setHint("Enter day: ");
            extraEdit3.setVisibility(View.INVISIBLE);
        }
        if (postCategory.equals("Service")) {
            extraEdit1.setVisibility(View.VISIBLE);
            extraEdit1.setHint("Is cantonese required (yes/no): ");
            extraEdit2.setVisibility(View.VISIBLE);
            extraEdit2.setHint("Enter target demographic: ");
            extraEdit3.setVisibility(View.INVISIBLE);
        }
        if (postCategory.equals("Sports")) {
            extraEdit1.setVisibility(View.VISIBLE);
            extraEdit1.setHint("Enter year groups: ");
            extraEdit2.setVisibility(View.VISIBLE);
            extraEdit2.setHint("Enter sport: ");
            extraEdit3.setVisibility(View.VISIBLE);
            extraEdit3.setHint("Enter ability level: ");
        }
        if (postCategory.equals("Academics")) {
            extraEdit1.setVisibility(View.VISIBLE);
            extraEdit1.setHint("Enter year groups: ");
            extraEdit2.setVisibility(View.GONE);
            extraEdit3.setVisibility(View.INVISIBLE);
        }
        if (postCategory.equals("Miscellaneous")) {
            extraEdit1.setVisibility(View.INVISIBLE);
            extraEdit2.setVisibility(View.GONE);
            extraEdit3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        extraEdit1.setVisibility(View.INVISIBLE);
        extraEdit2.setVisibility(View.GONE);
        extraEdit3.setVisibility(View.INVISIBLE);
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