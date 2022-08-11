package com.example.ruleteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruleteacher.ui.profile.ProfileFragment;
import com.example.ruleteacher.ui.profile.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText tietteacherID, tietemail, tietname, tietpassword, tietposition;
    Button btnSignUp;
    TextView loginText;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String dbID;
    RadioGroup radioBtnGroup;
    RadioButton radioSexBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registration Form");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        tietteacherID= findViewById(R.id.registerID);
        tietemail= findViewById(R.id.registerEmail);
        tietname= findViewById(R.id.registerName);
        tietpassword= findViewById(R.id.registerPassword);
        tietposition= findViewById(R.id.registerPosition);
        btnSignUp= findViewById(R.id.btn_register);
        radioBtnGroup=(RadioGroup)findViewById(R.id.radio_btngroup);


        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedID= radioBtnGroup.getCheckedRadioButtonId();
                radioSexBtn=findViewById(selectedID);

                String teacherID, email, name, password, position;
                teacherID= String.valueOf(tietteacherID.getText());
                email= String.valueOf(tietemail.getText());
                name= String.valueOf(tietname.getText());
                password= String.valueOf(tietpassword.getText());
                position= String.valueOf(tietposition.getText());
                String gender= String.valueOf(radioSexBtn.getText());

                if(!teacherID.equals("") && !email.equals("") && !name.equals("") && !password.equals("") && !position.equals("")) {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dbID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(dbID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("name", name);
                                user.put("teacherID", teacherID);
                                //user.put("password", password);
                                user.put("position", position);
                                user.put("docID", dbID);
                                user.put("status", "teacher");
                                user.put("gender", gender);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user profile is created for " + dbID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });

                                Toast.makeText(SignUpActivity.this, "User has been created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Unsuccessful Registration! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
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