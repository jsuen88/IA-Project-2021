package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cisnewsapp.Models.AcademicsPost;
import com.example.cisnewsapp.Models.Admin;
import com.example.cisnewsapp.Models.CCAPost;
import com.example.cisnewsapp.Models.MiscPost;
import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.ServicePost;
import com.example.cisnewsapp.Models.SportsPost;
import com.example.cisnewsapp.Models.Student;
import com.example.cisnewsapp.Models.Teacher;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.Random;

import io.grpc.Context;

/**
 * An activity where the user can create and submit a post.
 * Features several input boxes for title, description, and
 * other traits as well as allowing the user to select a date
 * and image.
 *
 * Author: Joson Suen
 * Version: 1.0
 */
public class NewPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    public Button chooseImage;
    public Button uploadImage;
    public ImageView imageView;
    public EditText imageNameEdit;
    public Uri imageUri;
    public String picURL;

    public StorageReference mStorageRef;
    public DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

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

        chooseImage = findViewById(R.id.chooseImage);
        uploadImage = findViewById(R.id.uploadImage);
        imageView = findViewById(R.id.imageView);
        imageNameEdit = findViewById(R.id.imageNameEdit);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(NewPostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
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
    /**
     * Method that uploads the selected image file to the database.
     *
     */
    public void uploadFile()
    {
        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getFileExtension(imageUri));

            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(NewPostActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            if (taskSnapshot.getMetadata() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        picURL = uri.toString();
                                        Upload upload = new Upload(imageNameEdit.getText().toString().trim(), picURL);
                                        String uploadId = mDatabaseRef.push().getKey();
                                        mDatabaseRef.child(uploadId).setValue(upload);
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        }
                    });
        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver content = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(content.getType(uri));
    }
    /**
     * Method that, when called, opens the file chooser where
     * the user can then select the image they would like
     * to upload.
     *
     */
    public void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
    /**
     * Method that helps to display the image on the screen once the user
     * has selected it from the file chooser
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
            imageView.getLayoutParams().width = 250;
            imageView.getLayoutParams().height = 250;
            imageView.setAdjustViewBounds(true);
        }
    }
    /**
     * Method called when the user selects a date from the calendar fragment.
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
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
    /**
     * Helper method called within the inputValid() method that
     * checks if the user's input for year groups is valid, returning
     * either true or false depending on the result.
     *
     * @return boolean
     */
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
    /**
     * Helper method called when the user clicks on the submit post
     * button which checks if all inputs have been filled in and
     * that the input is valid.
     *
     * @return boolean
     */
    public boolean inputValid()
    {
        if (c == null) {
            Toast.makeText(this, "Please enter a date", Toast.LENGTH_SHORT).show();
            return false;
        }
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
    /**
     * Method called when user clicks on submit post button. If
     * inputValid() returns true, then the post object is stored in
     * Firebase and the user is redirected back to the home page.
     *
     * @param v: the current view displayed to the user
     */
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
                    String contactEmail = user.getEmail();
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
                            CCAPost post = new CCAPost(title, postCategory, owner, info, Calendar.getInstance().getTime(),
                                    c.getTime(), yearGroups, extraEdit2.getText().toString(), id,
                                    "awaiting", picURL, contactEmail);
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("approved");
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
                            ServicePost post = new ServicePost(title, postCategory, owner, info,
                                    Calendar.getInstance().getTime(), c.getTime(), cantonese, extraEdit2.getText().toString(),
                                    id, "awaiting", picURL, extraEdit3.getText().toString(), contactEmail);
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("approved");
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

                            SportsPost post = new SportsPost(title, postCategory, owner, info, Calendar.getInstance().getTime(),
                                    c.getTime(), yearGroups, extraEdit2.getText().toString(), extraEdit3.getText().toString(),
                                    id, "awaiting", picURL, contactEmail);
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("approved");
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

                            AcademicsPost post = new AcademicsPost(title, postCategory, owner, info,
                                    Calendar.getInstance().getTime(), c.getTime(), yearGroups, id,
                                    "awaiting", picURL, contactEmail);
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("approved");
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
                            Post post = new Post(title, postCategory, owner, info,
                                    Calendar.getInstance().getTime(), c.getTime(), id,
                                    "awaiting", picURL, contactEmail);
                            if (user.getUserType().equals("Admin") || user.getUserType().equals("Teacher"))
                            {
                                post.setApprovalStatus("approved");
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
                        if (user.getUserType().equals("Student")) {
                            User student = ds.toObject(Student.class);
                            user.setCreatedPosts(createdPosts);
                            firestore.collection("users").document(student.getUid()).set(student);
                        }
                        if (user.getUserType().equals("Admin")) {
                            User admin = ds.toObject(Admin.class);
                            user.setCreatedPosts(createdPosts);
                            firestore.collection("users").document(admin.getUid()).set(admin);
                        }
                        if (user.getUserType().equals("Teacher")) {
                            User teacher = ds.toObject(Teacher.class);
                            user.setCreatedPosts(createdPosts);
                            firestore.collection("users").document(teacher.getUid()).set(teacher);
                        }

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
    /**
     * Method called when an item is selected from the post
     * categories spinner.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
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
            extraEdit3.setVisibility(View.VISIBLE);
            extraEdit3.setHint("Enter day: ");
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
    /**
     * Method called if no item is selected from the
     * post categories spinner
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        extraEdit1.setVisibility(View.INVISIBLE);
        extraEdit2.setVisibility(View.GONE);
        extraEdit3.setVisibility(View.INVISIBLE);
    }
    /**
     * Method called if the user clicks on the "cancel" button.
     * Redirects the user back to the home page.
     *
     * @param v: the current view displayed to the user
     */
    public void cancel (View v) {
        Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
        startActivity(nextScreen);
    }

    /**
     * Method called if the user clicks on the "sign out" button.
     * Redirects the user back to the user authentication page.
     *
     * @param v: the current view displayed to the user
     */
    public void signOut (View v)
    {
        mAuth.signOut();
        Intent nextScreen = new Intent(getBaseContext(), AuthActivity.class);
        startActivity(nextScreen);
        finish();
    }
}