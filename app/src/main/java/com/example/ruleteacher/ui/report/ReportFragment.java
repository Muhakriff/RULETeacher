package com.example.ruleteacher.ui.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruleteacher.ListStudentActivity;
import com.example.ruleteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReportFragment extends Fragment implements losAdapter.UserClickListener {

    ArrayList<losModel> losArrayList= new ArrayList<>();
    ProgressBar progressBar;
    EditText searchBox;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    ProgressDialog progressDialog;
    private RecyclerView losRecyclerView;
    TextView tvNopt;
    String keyword="";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        fStore =FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        losRecyclerView = root.findViewById(R.id.losRecyclerView);
        tvNopt=root.findViewById(R.id.tv_nopt);
        searchBox=root.findViewById(R.id.search_box);
        progressBar=root.findViewById(R.id.progressBar);

        //setUpptModel();
        losRecyclerView.setHasFixedSize(true);
        losRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        losAdapter losAdapter= new losAdapter(root.getContext(), losArrayList, this::selectedUser);
        losRecyclerView.setAdapter(losAdapter);
        fetchListStudent();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<losModel> filteredList=new ArrayList<>();
                for(losModel item: losArrayList){
                    if(item.getName().toLowerCase().contains(editable.toString().toLowerCase())){
                        filteredList.add(item);
                    }
                }
                losAdapter.filterList(filteredList);
            }
        });

        return root;
    }

    private void filter(String toString) {

    }

    private void fetchListStudent() {
        String status= "student";
        progressBar.setVisibility(View.VISIBLE);
            fStore.collection("users").whereEqualTo("status",status)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            if (error != null){
                                Log.e("Firestore error", error.getMessage() );
                                progressBar.setVisibility(View.GONE);
                            }
                            if(value.isEmpty()){
                                progressBar.setVisibility(View.GONE);
                                losRecyclerView.setVisibility(View.GONE);
                                tvNopt.setVisibility(View.VISIBLE);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                losRecyclerView.setVisibility(View.VISIBLE);
                                tvNopt.setVisibility(View.GONE);
                            }

                            for(DocumentChange dc: value.getDocumentChanges()){
                                if (dc.getType()==DocumentChange.Type.ADDED){
                                    losArrayList.add(dc.getDocument().toObject(losModel.class));
                                }
                                if (dc.getType()==DocumentChange.Type.MODIFIED){
                                    losArrayList.add(dc.getDocument().toObject(losModel.class));
                                }
                                if (dc.getType()==DocumentChange.Type.REMOVED){
                                    losArrayList.add(dc.getDocument().toObject(losModel.class));
                                }
                                progressBar.setVisibility(View.GONE);
                                losRecyclerView.getAdapter().notifyDataSetChanged();
                            }

                        }
                    });
    }

    @Override
    public void selectedUser(losModel losModel) {
        Intent intent= new Intent(getActivity(), ListStudentActivity.class);

        intent.putExtra("STUDENTID", losModel.getID());
        intent.putExtra("EMAIL",losModel.getEmail());
        intent.putExtra("NAME",losModel.getName());
        intent.putExtra("CLASSROOM",losModel.getClassroom());
        intent.putExtra("STATUS",losModel.getStatus());
        intent.putExtra("DOCID",losModel.getDocID()); //studentfsID
        intent.putExtra("GENDER",losModel.getGender()); //studentfsID
        intent.putExtra("AGE",losModel.getAge()); //studentfsID
        startActivity(intent);
    }
}