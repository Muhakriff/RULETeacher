package com.example.ruleteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ListStudentActivity extends AppCompatActivity {

    //ReportFragment
    private TextView tvID, tvEmail, tvName, tvClassroom, tvGender, tvAge;
    ImageView imgProfPic;
    Button btnReport;
    String studentID, studentEmail, studentName, studentClassroom, studentFSID, status, gender, age;
    FirebaseFirestore fStore;
    StorageReference storageReference, profileRef;
    private FirebaseAuth mAuth;
    public static final String TAG= "TAG";
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Student's Detail");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);


        studentID= getIntent().getStringExtra("STUDENTID");
        studentEmail= getIntent().getStringExtra("EMAIL");
        studentName= getIntent().getStringExtra("NAME");
        studentClassroom= getIntent().getStringExtra("CLASSROOM");
        studentFSID= getIntent().getStringExtra("DOCID");
        status= getIntent().getStringExtra("STATUS");
        gender= getIntent().getStringExtra("GENDER");
        age= getIntent().getStringExtra("AGE");

        imgProfPic= findViewById(R.id.img_profPic);
        tvID= findViewById(R.id.tv_profID);
        tvName= findViewById(R.id.tv_profName);
        tvEmail= findViewById(R.id.tv_profEmail);
        tvClassroom= findViewById(R.id.tv_profClassroom);
        btnReport= findViewById(R.id.btn_report);
        tvGender=findViewById(R.id.tv_profGender);
        tvAge=findViewById(R.id.tv_profAge);
        progressBar2=findViewById(R.id.progressBar2);

        fStore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference();

        profileRef= storageReference.child("users/"+studentFSID+"/profile.jpg");
        tvID.setText(studentID);
        tvName.setText(studentName);
        tvEmail.setText(studentEmail);
        tvClassroom.setText(studentClassroom);
        tvGender.setText(gender);
        if(gender.equals("Male")){
            tvGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_male_24,0,0,0);
        }
        else if (gender.equals("Female")){
            tvGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_female_24,0,0,0);
        }
        tvAge.setText(age);

        progressBar2.setVisibility(View.VISIBLE);

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imgProfPic);
                progressBar2.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListStudentActivity.this, "No picture found", Toast.LENGTH_SHORT).show();
                progressBar2.setVisibility(View.GONE);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(ListStudentActivity.this, ReportActivity.class);
                intent.putExtra("DOCID",studentFSID); //studentfsID
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}