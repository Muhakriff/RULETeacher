package com.example.ruleteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruleteacher.ui.home.paModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KoqApproval extends AppCompatActivity {

    Button btnApprove, btnReject, btnCalc;
    String userid, task, pic, studentID, jawatan, penglibatan, komitmen, khidmatSumbangan, kehadiran, pencapaian, docid, type, result, totalcalc, component;
    TextView tvpe, tvStudent, tvJawatan, tvPenglibatan, tvKomitmen, tvKS, tvKehadiran, tvPencapaian, tvCalc, tvComponent;
    FirebaseFirestore fStore;
    ArrayList<Koku> koku= new ArrayList<>();
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koq_approval);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Co-curriculum Verification");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        tvpe= findViewById(R.id.tv_pe);
        tvStudent= findViewById(R.id.tv_student);
        tvJawatan= findViewById(R.id.tv_jawatan);
        tvPenglibatan= findViewById(R.id.tv_penglibatan);
        tvKomitmen= findViewById(R.id.tv_komitmen);
        tvKS= findViewById(R.id.tv_ks);
        tvKehadiran= findViewById(R.id.tv_kehadiran);
        tvPencapaian= findViewById(R.id.tv_pencapaian);
        btnApprove=findViewById(R.id.btn_approve);
        btnReject=findViewById(R.id.btn_reject);
        tvCalc=findViewById(R.id.tv_calc);
        tvComponent=findViewById(R.id.tv_component);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        task=getIntent().getStringExtra("TASK");
        jawatan=getIntent().getStringExtra("JAWATAN");
        kehadiran=getIntent().getStringExtra("KEHADIRAN");
        khidmatSumbangan=getIntent().getStringExtra("KHIDMATSUMBANGAN");
        komitmen=getIntent().getStringExtra("KOMITMEN");
        pic=getIntent().getStringExtra("PICUID");
        pencapaian=getIntent().getStringExtra("PENCAPAIAN");
        penglibatan=getIntent().getStringExtra("PENGLIBATAN");
        docid=getIntent().getStringExtra("DOCID");
        result=getIntent().getStringExtra("RESULT");
        studentID=getIntent().getStringExtra("STUDENTID");
        type=getIntent().getStringExtra("TYPE");
        tvCalc.setText(getIntent().getStringExtra("TOTALPOINT"));
        tvComponent.setText(getIntent().getStringExtra("COMPONENT"));

        fStore.collection("users").document(studentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvStudent.setText(value.getString("name"));
            }
        });

        tvpe.setText(task);
        fStore.collection("fsKokuSukan").document(jawatan).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvJawatan.setText(value.getString("perkara"));
            }
        });
        fStore.collection("fsKokuSukan").document(penglibatan).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPenglibatan.setText(value.getString("perkara"));
            }
        });fStore.collection("fsKokuSukan").document(komitmen).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvKomitmen.setText(value.getString("perkara"));
            }
        });fStore.collection("fsKokuSukan").document(khidmatSumbangan).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvKS.setText(value.getString("perkara"));
            }
        });fStore.collection("fsKokuSukan").document(kehadiran).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvKehadiran.setText(value.getString("perkara"));
            }
        });fStore.collection("fsKokuSukan").document(pencapaian).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPencapaian.setText(value.getString("perkara"));
            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveKoQ();
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectKoQ();
            }
        });

    }

    private void rejectKoQ() {
        task=getIntent().getStringExtra("TASK");
        jawatan=getIntent().getStringExtra("JAWATAN");
        kehadiran=getIntent().getStringExtra("KEHADIRAN");
        khidmatSumbangan=getIntent().getStringExtra("KHIDMATSUMBANGAN");
        komitmen=getIntent().getStringExtra("KOMITMEN");
        pic=getIntent().getStringExtra("PICUID");
        pencapaian=getIntent().getStringExtra("PENCAPAIAN");
        penglibatan=getIntent().getStringExtra("PENGLIBATAN");
        docid=getIntent().getStringExtra("DOCID");
        result=getIntent().getStringExtra("RESULT");
        studentID=getIntent().getStringExtra("STUDENTID");
        type=getIntent().getStringExtra("TYPE");
        totalcalc=getIntent().getStringExtra("TOTALPOINT");
        component=getIntent().getStringExtra("COMPONENT");
        userid=mAuth.getCurrentUser().getUid();

        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID",studentID);
        uploadTask.put("jawatan", jawatan);
        uploadTask.put("kehadiran", kehadiran);
        uploadTask.put("khidmatSumbangan", khidmatSumbangan);
        uploadTask.put("docID", docid);
        uploadTask.put("PICuid", pic); //namacikgu
        uploadTask.put("komitmen",komitmen);
        uploadTask.put("type", type);
        uploadTask.put("result", "Rejected");
        uploadTask.put("pencapaian", pencapaian);
        uploadTask.put("penglibatan", penglibatan);
        uploadTask.put("totalPoint", totalcalc);
        uploadTask.put("component",component);


        fStore.collection("fsHistory").document(docid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Toast.makeText(KoqApproval.this,"Approved",Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(KoqApproval.this, MainActivity.class);
        startActivity(intent);

    }

    private void approveKoQ() {

        task=getIntent().getStringExtra("TASK");
        jawatan=getIntent().getStringExtra("JAWATAN");
        kehadiran=getIntent().getStringExtra("KEHADIRAN");
        khidmatSumbangan=getIntent().getStringExtra("KHIDMATSUMBANGAN");
        komitmen=getIntent().getStringExtra("KOMITMEN");
        pic=getIntent().getStringExtra("PICUID");
        pencapaian=getIntent().getStringExtra("PENCAPAIAN");
        penglibatan=getIntent().getStringExtra("PENGLIBATAN");
        docid=getIntent().getStringExtra("DOCID");
        result=getIntent().getStringExtra("RESULT");
        studentID=getIntent().getStringExtra("STUDENTID");
        type=getIntent().getStringExtra("TYPE");
        totalcalc=getIntent().getStringExtra("TOTALPOINT");
        component=getIntent().getStringExtra("COMPONENT");
        userid=mAuth.getCurrentUser().getUid();

    Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID",studentID);
        uploadTask.put("jawatan", jawatan);
        uploadTask.put("kehadiran", kehadiran);
        uploadTask.put("khidmatSumbangan", khidmatSumbangan);
        uploadTask.put("docID", docid);
        uploadTask.put("PICuid", pic); //namacikgu
        uploadTask.put("komitmen",komitmen);
        uploadTask.put("type", type);
        uploadTask.put("result", "Approved");
        uploadTask.put("pencapaian", pencapaian);
        uploadTask.put("penglibatan", penglibatan);
        uploadTask.put("totalPoint", totalcalc);
        uploadTask.put("component",component);
//        uploadTask.put("point", pointID);

        fStore.collection("fsHistory").document(docid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Toast.makeText(KoqApproval.this,"Approved",Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(KoqApproval.this, MainActivity.class);
        startActivity(intent);

    }
}