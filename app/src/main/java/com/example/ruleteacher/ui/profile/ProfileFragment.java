package com.example.ruleteacher.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.ruleteacher.LoginActivity;
import com.example.ruleteacher.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView tietteacherID, tietemail, tietname, tietpassword, tietposition, tvProfGender, tvProfAge;
    private Button btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String dbID;
    ImageButton imgbtnProfile;
    ActivityResultLauncher<String> mGetContent;
    StorageReference storageReference, profileRef;
    FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout= root.findViewById(R.id.btn_LogOut);
        tietemail= root.findViewById(R.id.profEmail);
        tietname= root.findViewById(R.id.profName);
        tietteacherID= root.findViewById(R.id.profID);
        tietposition= root.findViewById(R.id.profPosition);
        tvProfGender=root.findViewById(R.id.profGender);
        imgbtnProfile= (ImageButton)root.findViewById(R.id.img_btn_prof);
        floatingActionButton=root.findViewById(R.id.floatingActionButton);

        progressDialog= new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        dbID= mAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        profileRef= storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");

        DocumentReference documentReference= fStore.collection("users").document(dbID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(imgbtnProfile);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
                tietemail.setText(value.getString("email"));
                tietname.setText(value.getString("name"));
                tietteacherID.setText(value.getString("teacherID"));
                tietposition.setText(value.getString("position"));
                tvProfGender.setText(value.getString("gender"));
                if(value.getString("gender").equals("Male")){
                    tvProfGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_male_24,0,0,0);
                }
                else if(value.getString("gender").equals("Female")){
                    tvProfGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_female_24,0,0,0);
                }
            }
        });
        btnLogout.setOnClickListener(this);

        mGetContent= registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //imgbtnProfile.setImageURI(result);
                if(result!= null){
                    uploadImageToFirebase(result);
                }
                else {
                    Toast.makeText(getActivity(), "No picture selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
        return root;
    }

    private void uploadImageToFirebase(Uri result) {
        profileRef.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(imgbtnProfile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

}