package com.example.ruleteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ApproveRequestActivity extends AppCompatActivity {

    ImageView imgDOC;
    TextView tvStudentName, tvTaskName, tvDOTG, tvPOT, tvNoteToStudent;
    String task, studentID, dotg, pot, docid, picuid, taskDetail, type, result;
    Button btnApproval, btnReject;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    StorageReference storageReference, posterRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_request);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Poster Verification");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        fStore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        task= getIntent().getStringExtra("TASK");
        studentID= getIntent().getStringExtra("STUDENT");
        dotg= getIntent().getStringExtra("DOTG");
        pot= getIntent().getStringExtra("POT");
        docid= getIntent().getStringExtra("DOCID");
        picuid= getIntent().getStringExtra("PICUID");
        taskDetail= getIntent().getStringExtra("TASKDETAIL");
        type= getIntent().getStringExtra("TYPE");
        result= getIntent().getStringExtra("RESULT");

        tvStudentName=findViewById(R.id.tv_student_name);
        tvTaskName=findViewById(R.id.tv_taskName);
        tvDOTG=findViewById(R.id.tv_dotg);
        tvPOT=findViewById(R.id.tv_pot);
        tvNoteToStudent=findViewById(R.id.tv_noteToStudent);
        btnApproval= findViewById(R.id.btn_approval);
        btnReject=findViewById(R.id.btn_reject);
        imgDOC=findViewById(R.id.img_DOC);

        posterRef=storageReference.child("poster/"+docid+".jpg");

        posterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imgDOC);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        fStore.collection("users").document(studentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvStudentName.setText(value.getString("name"));
            }
        });
        tvTaskName.setText(task);
        tvDOTG.setText(dotg);
        tvPOT.setText(pot);
        tvNoteToStudent.setText(taskDetail);

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectPoster();
            }
        });
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvePoster();
            }
        });
    }

    private void rejectPoster() {
        task= getIntent().getStringExtra("TASK");
        studentID= getIntent().getStringExtra("STUDENT");
        dotg= getIntent().getStringExtra("DOTG");
        pot= getIntent().getStringExtra("POT");
        docid= getIntent().getStringExtra("DOCID");
        picuid= getIntent().getStringExtra("PICUID");
        taskDetail= getIntent().getStringExtra("TASKDETAIL");
        type= getIntent().getStringExtra("TYPE");
        result= getIntent().getStringExtra("RESULT");

        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID", studentID);
        uploadTask.put("DateOfTaskGiven", dotg);
        //user.put("password", password);
        uploadTask.put("Place", pot);
//        uploadTask.put("DateOfTaskUploaded", dotu);
//        FirebaseUser dbID = mAuth.getCurrentUser();
//        String currentUserID = dbID.getUid();
        uploadTask.put("PICuid", picuid);
        uploadTask.put("taskDetail", taskDetail);
        uploadTask.put("docID", docid);
        uploadTask.put("type", type);
        uploadTask.put("result", "Rejected");

        fStore.collection("fsHistory").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been approved successfully by "+picuid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+ e.toString());
            }
        });
        fStore.collection("fsPendingApproval").document(docid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been uploaded with id "+docid);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "On Failure: "+e.toString());

            }
        });

        Toast.makeText(ApproveRequestActivity.this, "You rejected the poster", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ApproveRequestActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void approvePoster() {
        task= getIntent().getStringExtra("TASK");
        studentID= getIntent().getStringExtra("STUDENT");
        dotg= getIntent().getStringExtra("DOTG");
        pot= getIntent().getStringExtra("POT");
        docid= getIntent().getStringExtra("DOCID");
        picuid= getIntent().getStringExtra("PICUID");
        taskDetail= getIntent().getStringExtra("TASKDETAIL");
        type= getIntent().getStringExtra("TYPE");
        result= getIntent().getStringExtra("RESULT");

        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID", studentID);
        uploadTask.put("DateOfTaskGiven", dotg);
        //user.put("password", password);
        uploadTask.put("Place", pot);
//        uploadTask.put("DateOfTaskUploaded", dotu);
//        FirebaseUser dbID = mAuth.getCurrentUser();
//        String currentUserID = dbID.getUid();
        uploadTask.put("PICuid", picuid);
        uploadTask.put("taskDetail", taskDetail);
        uploadTask.put("docID", docid);
        uploadTask.put("type", type);
        uploadTask.put("result", "Approved");

        fStore.collection("fsHistory").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been approved successfully by "+picuid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+ e.toString());
            }
        });
        fStore.collection("fsPosterEvent").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been approved successfully by "+picuid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+ e.toString());
            }
        });
        fStore.collection("fsPendingApproval").document(docid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been uploaded with id "+docid);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "On Failure: "+e.toString());

            }
        });
        Toast.makeText(ApproveRequestActivity.this, "Poster has been approved successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ApproveRequestActivity.this, MainActivity.class);
        startActivity(intent);
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