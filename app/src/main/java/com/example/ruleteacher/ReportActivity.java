package com.example.ruleteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruleteacher.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private TextView tvReporterName, tvCurrentDate, tvStudentName;
    private EditText etTask, etmNoteToStudent, etPlaceOfTask;
    private Button btnReport;
    private String task, studentID, dotg, pot, dotu, docid, pic, taskDetail, type, result;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public static final String TAG= "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Report Form");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);
        studentID=getIntent().getStringExtra("DOCID");

        tvReporterName= findViewById(R.id.tv_reporterName);
        tvCurrentDate= findViewById(R.id.tv_currentDate);
        etTask= findViewById(R.id.et_task);
        etmNoteToStudent = findViewById(R.id.etm_noteToStudent);
        etPlaceOfTask= findViewById(R.id.et_placeOfTask);
        btnReport= findViewById(R.id.btn_report);
        tvStudentName= findViewById(R.id.tv_student_name);

        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        Calendar calendar= Calendar.getInstance();
        dotg = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());


        tvCurrentDate.setText(dotg);
        pic= mAuth.getCurrentUser().getUid();

        fStore.collection("users").document(pic).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvReporterName.setText(value.getString("name"));
            }
        });
        fStore.collection("users").document(studentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvStudentName.setText(value.getString("name"));
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //9data

                task= etTask.getText().toString();
                taskDetail= etmNoteToStudent.getText().toString();
                pot= etPlaceOfTask.getText().toString();

                String temp= fStore.collection("fsPendingTask").document().getId();
                Map<String, Object> uploadTask= new HashMap<>();
                uploadTask.put("task", task);
                uploadTask.put("studentID",studentID);
                uploadTask.put("DateOfTaskGiven", dotg);
                uploadTask.put("Place", pot);
                //uploadTask.put("DateOfTaskUploaded", dotu);
                uploadTask.put("docID", temp);
                uploadTask.put("PICuid", pic); //namacikgu
                uploadTask.put("taskDetail",taskDetail);
                uploadTask.put("type", "task");
                uploadTask.put("result", "Pending Response");
                fStore.collection("fsPendingTask").document(temp).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Task uploaded by "+ pic);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.toString());
                    }
                });
                fStore.collection("fsHistory").document(temp).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Task uploaded by "+ pic);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.toString());
                    }
                });
                Toast.makeText(ReportActivity.this, "Task uploaded. Kindly check history for more info.", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ReportActivity.this, MainActivity.class);
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