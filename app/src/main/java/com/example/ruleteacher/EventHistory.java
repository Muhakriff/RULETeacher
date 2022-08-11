package com.example.ruleteacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class EventHistory extends AppCompatActivity {

    ImageView imgDOC;
    TextView tvStudentName, tvTaskName, tvDOTU, tvPOT, tvDOTG, tvNoteToStudent;
    String task, studentID, dotg, pot, dotu, docid, pic, taskDetail, type, result ;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);

        task = getIntent().getStringExtra("TASK");
        studentID = getIntent().getStringExtra("STUDENT");
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        dotu = getIntent().getStringExtra("DOTU");
        docid = getIntent().getStringExtra("DOCID");
        pic = getIntent().getStringExtra("PICUID");
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        imgDOC= findViewById(R.id.img_DOC);
        tvStudentName=findViewById(R.id.tv_student_name);
        tvTaskName=findViewById(R.id.tv_taskName);
        tvDOTU=findViewById(R.id.tv_dotu);
        tvDOTG=findViewById(R.id.tv_dotg);
        tvPOT=findViewById(R.id.tv_pot);
        tvNoteToStudent=findViewById(R.id.tv_noteToStudent);
        fStore=FirebaseFirestore.getInstance();

        fStore.collection("users").document(studentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvStudentName.setText(value.getString("name"));
            }
        });
        tvTaskName.setText(task);
        tvDOTU.setText(dotu);
        tvDOTG.setText(dotg);
        tvPOT.setText(pot);
        tvNoteToStudent.setText(taskDetail);


    }
}