package com.example.ruleteacher.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ruleteacher.ApproveActivity;
import com.example.ruleteacher.ApproveEventActivity;
import com.example.ruleteacher.ApproveRequestActivity;
import com.example.ruleteacher.KoqApproval;
import com.example.ruleteacher.R;
import com.example.ruleteacher.ui.report.losModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeInterface{

    ArrayList<paModel> paArrayList= new ArrayList<>();
//    DatabaseReference database;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String userid;
    TextView tvNopt;

    private RecyclerView paRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fStore =FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        tvNopt= root.findViewById(R.id.tv_nopt);
        paRecyclerView = root.findViewById(R.id.paRecyclerView);
        //setUpptModel();
        paRecyclerView.setHasFixedSize(true);
        paRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        paAdapter paAdapter= new paAdapter(this::onItemClick, root.getContext(), paArrayList);
        paRecyclerView.setAdapter(paAdapter);

        fetchPendingApproval();
        return root;
    }

    private void fetchPendingApproval() {
        userid= mAuth.getCurrentUser().getUid(); //docID

        fStore.collection("fsPendingApproval").whereEqualTo("PICuid",userid) //docID
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.isEmpty()){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            paRecyclerView.setVisibility(View.GONE);
                            tvNopt.setVisibility(View.VISIBLE);
                        }
                        else{
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            paRecyclerView.setVisibility(View.VISIBLE);
                            tvNopt.setVisibility(View.GONE);
                        }
                        if (error != null){
                            Log.e("Firestore error", error.getMessage() );
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if (dc.getType()==DocumentChange.Type.ADDED){
                                paArrayList.add(dc.getDocument().toObject(paModel.class));
                            }
                            if (dc.getType()==DocumentChange.Type.MODIFIED){
                                paArrayList.add(dc.getDocument().toObject(paModel.class));
                            }
                            if (dc.getType()==DocumentChange.Type.REMOVED){
                                paArrayList.add(dc.getDocument().toObject(paModel.class));
                            }
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            paRecyclerView.getAdapter().notifyDataSetChanged();

                        }
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        String temp= paArrayList.get(position).getType();
        switch (temp){
            case "event":
                Intent intent= new Intent(getActivity(), ApproveEventActivity.class);
                intent.putExtra("TASK", paArrayList.get(position).getTask());
                intent.putExtra("STUDENT",paArrayList.get(position).getStudentID());
                intent.putExtra("DOTG",paArrayList.get(position).getDateOfTaskGiven());
                intent.putExtra("POT",paArrayList.get(position).getPlace());
                intent.putExtra("DOTU",paArrayList.get(position).getDateOfTaskUploaded());
                intent.putExtra("DOCID",paArrayList.get(position).getDocID());
                intent.putExtra("PICUID",paArrayList.get(position).getPICuid());
                intent.putExtra("TASKDETAIL",paArrayList.get(position).getTaskDetail());
                intent.putExtra("TYPE", paArrayList.get(position).getType());
                intent.putExtra("RESULT", paArrayList.get(position).getResult());
                intent.putExtra("TOTALPOINT", paArrayList.get(position).getTotalPoint());
                intent.putExtra("EVENTID", paArrayList.get(position).getEventID());
                startActivity(intent);
                break;

            case "task":
                Intent intent2= new Intent(getActivity(), ApproveActivity.class);

                intent2.putExtra("TASK", paArrayList.get(position).getTask());
                intent2.putExtra("STUDENT",paArrayList.get(position).getStudentID());
                intent2.putExtra("DOTG",paArrayList.get(position).getDateOfTaskGiven());
                intent2.putExtra("POT",paArrayList.get(position).getPlace());
                intent2.putExtra("DOTU",paArrayList.get(position).getDateOfTaskUploaded());
                intent2.putExtra("DOCID",paArrayList.get(position).getDocID());
                intent2.putExtra("PICUID",paArrayList.get(position).getPICuid());
                intent2.putExtra("TASKDETAIL",paArrayList.get(position).getTaskDetail());
                intent2.putExtra("TYPE", paArrayList.get(position).getType());
                intent2.putExtra("RESULT", paArrayList.get(position).getResult());
                startActivity(intent2);


                break;

            case "request":
                Intent intent3= new Intent(getActivity(), ApproveRequestActivity.class);

                intent3.putExtra("TASK", paArrayList.get(position).getTask());
                intent3.putExtra("STUDENT",paArrayList.get(position).getStudentID());
                intent3.putExtra("DOTG",paArrayList.get(position).getDateOfTaskGiven());
                intent3.putExtra("POT",paArrayList.get(position).getPlace());
//                intent3.putExtra("DOTU",paArrayList.get(position).getDateOfTaskUploaded());
                intent3.putExtra("DOCID",paArrayList.get(position).getDocID());
                intent3.putExtra("PICUID",paArrayList.get(position).getPICuid());
                intent3.putExtra("TASKDETAIL",paArrayList.get(position).getTaskDetail());
                intent3.putExtra("TYPE", paArrayList.get(position).getType());
                intent3.putExtra("RESULT", paArrayList.get(position).getResult());
                startActivity(intent3);
                break;

            case "KoQ":
                Intent intent4= new Intent(getActivity(), KoqApproval.class);

                intent4.putExtra("TASK", paArrayList.get(position).getTask());
                intent4.putExtra("JAWATAN", paArrayList.get(position).getJawatan());
                intent4.putExtra("KEHADIRAN", paArrayList.get(position).getKehadiran());
                intent4.putExtra("KHIDMATSUMBANGAN", paArrayList.get(position).getKhidmatSumbangan());
                intent4.putExtra("KOMITMEN", paArrayList.get(position).getKomitmen());
                intent4.putExtra("PICUID", paArrayList.get(position).getPICuid());
                intent4.putExtra("PENCAPAIAN", paArrayList.get(position).getPencapaian());
                intent4.putExtra("PENGLIBATAN", paArrayList.get(position).getPenglibatan());
                intent4.putExtra("DOCID", paArrayList.get(position).getDocID());
                intent4.putExtra("RESULT", paArrayList.get(position).getResult());
                intent4.putExtra("STUDENTID", paArrayList.get(position).getStudentID());
                intent4.putExtra("TYPE", paArrayList.get(position).getType());
                intent4.putExtra("TOTALPOINT", paArrayList.get(position).getTotalPoint());
                intent4.putExtra("COMPONENT", paArrayList.get(position).getComponent());
                startActivity(intent4);

                break;

        }


    }
}