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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ApproveActivity extends AppCompatActivity {

    //HomeFragement
    private TextView tvTaskName, tvDOTG, tvPOT, tvDOTU, tvStudentName, tvNoteToStudent;
    Button btnApprove, btnReject;
    String task, studentID, dotg, pot, dotu, docid, pic, taskDetail, type, result;
    private ImageView imgDOC;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    StorageReference storageReference, taskRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Task Verification");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        task = getIntent().getStringExtra("TASK");
        studentID = getIntent().getStringExtra("STUDENT");
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        dotu = getIntent().getStringExtra("DOTU");
        docid = getIntent().getStringExtra("DOCID");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        tvTaskName= findViewById(R.id.tv_taskName);
        tvDOTG= findViewById(R.id.tv_dotg);
        tvPOT= findViewById(R.id.tv_pot);
        tvDOTU= findViewById(R.id.tv_dotu);
        imgDOC= findViewById(R.id.img_DOC);
        tvNoteToStudent= findViewById(R.id.tv_noteToStudent);
        tvStudentName = findViewById(R.id.tv_student_name); //studentIDFirestore
        btnApprove= findViewById(R.id.btn_approval);
        btnReject=findViewById(R.id.btn_reject);
        taskRef=storageReference.child("task/"+docid+".jpg");

        taskRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imgDOC);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        fStore.collection("users").document(studentID).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvStudentName.setText(value.getString("name"));
            }
        });
        tvTaskName.setText(task);
        tvDOTG.setText(dotg);
        tvPOT.setText(pot);
        tvDOTU.setText(dotu);
        tvNoteToStudent.setText(taskDetail);

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveTask();
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectTask();
            }
        });

    }

    private void rejectTask() {
        task = getIntent().getStringExtra("TASK");
        studentID = getIntent().getStringExtra("STUDENT");
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        dotu = getIntent().getStringExtra("DOTU");
        docid = getIntent().getStringExtra("DOCID");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID",studentID);
        uploadTask.put("DateOfTaskGiven", dotg);
        uploadTask.put("Place", pot);
        uploadTask.put("DateOfTaskUploaded", dotu);
        uploadTask.put("docID", docid);
        uploadTask.put("PICuid", pic); //namacikgu
        uploadTask.put("taskDetail",taskDetail);
        uploadTask.put("type", type);
        uploadTask.put("result", "Rejected");

        fStore.collection("fsHistory").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been approved successfully by "+pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        });
        fStore.collection("fsPendingTask").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been approved successfully by "+pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
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
        Toast.makeText(ApproveActivity.this,"Task Rejected. Check history for more info",Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(ApproveActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void approveTask() {

        task = getIntent().getStringExtra("TASK");
        studentID = getIntent().getStringExtra("STUDENT");
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        dotu = getIntent().getStringExtra("DOTU");
        docid = getIntent().getStringExtra("DOCID");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        String userid= mAuth.getCurrentUser().getUid();

        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID",studentID);
        uploadTask.put("DateOfTaskGiven", dotg);
        uploadTask.put("Place", pot);
        uploadTask.put("DateOfTaskUploaded", dotu);
        uploadTask.put("docID", docid);
        uploadTask.put("PICuid", pic); //namacikgu
        uploadTask.put("taskDetail",taskDetail);
        uploadTask.put("type", type);
        uploadTask.put("result", "Approved");

        fStore.collection("fsHistory").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been approved successfully by "+pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
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
        Toast.makeText(ApproveActivity.this,"Task approved. Check history for more info.",Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(ApproveActivity.this, MainActivity.class);
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