package com.example.ruleteacher.ui.history;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruleteacher.EventHistory;
import com.example.ruleteacher.R;
import com.example.ruleteacher.RequestHistory;
import com.example.ruleteacher.TaskHistory;
import com.example.ruleteacher.ui.home.paAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryFragment extends Fragment implements RecyclerViewInterface{

    ArrayList<hisModel> hisArrayList= new ArrayList<>();
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String userid;
    TextView tvNopt, tvTotalRes, tvTotalApproved, tvtotalRejected;
    ImageView imgProf;
    private RecyclerView hisRecyclerView;
    public static final String TAG = "TAG";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        fStore =FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        tvNopt= root.findViewById(R.id.tv_nopt);
        hisRecyclerView = root.findViewById(R.id.hRecyclerView);
        //setUpptModel();
        hisRecyclerView.setHasFixedSize(true);
        hisRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        hisAdapter hisAdapter= new hisAdapter(root.getContext(), hisArrayList, this);
        hisRecyclerView.setAdapter(hisAdapter);
        imgProf=root.findViewById(R.id.img_prof);
        tvTotalApproved=root.findViewById(R.id.tv_TotalApproved);
        tvTotalRes=root.findViewById(R.id.tv_totalRes);
        tvtotalRejected=root.findViewById(R.id.tv_totalRejected);
        fetchHistory();


        return root;
    }

    private void fetchHistory() {
        userid= mAuth.getCurrentUser().getUid();
        fStore.collection("fsHistory").whereEqualTo("PICuid",userid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.isEmpty()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            hisRecyclerView.setVisibility(View.GONE);
                            tvNopt.setVisibility(View.VISIBLE);
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            hisRecyclerView.setVisibility(View.VISIBLE);
                            tvNopt.setVisibility(View.GONE);
                        }
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                        int totalRes=0;
                        int totalRej=0;
                        int totalApp=0;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                hisArrayList.add(dc.getDocument().toObject(hisModel.class));
                                String temp=dc.getDocument().getString("result");
                                if(temp.equals("Approved")){
                                    totalApp++;
                                }
                                if(temp.equals("Rejected")){
                                    totalRej++;
                                }

                            }
                            if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                hisArrayList.add(dc.getDocument().toObject(hisModel.class));
                                String temp=dc.getDocument().getString("result");
                                if(temp.equals("Approved")){
                                    totalApp++;
                                }
                                if(temp.equals("Rejected")){
                                    totalRej++;
                                }

                            }
                            if (dc.getType() == DocumentChange.Type.REMOVED) {
                                hisArrayList.add(dc.getDocument().toObject(hisModel.class));
                                String temp=dc.getDocument().getString("result");
                                if(temp.equals("Approved")){
                                    totalApp++;
                                }
                                if(temp.equals("Rejected")){
                                    totalRej++;
                                }

                            }
                            int count = hisArrayList.size();
                            tvTotalRes.setText(String.valueOf(count));
                            hisRecyclerView.getAdapter().notifyDataSetChanged();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                        tvtotalRejected.setText(String.valueOf(totalRej));
                        tvTotalApproved.setText(String.valueOf(totalApp));
                    }
                });


    }

    @Override
    public void onItemClick(int position) {


//        String temp= hisArrayList.get(position).getType();
//        switch (temp){
//            case "event":
//                Intent intent= new Intent(getActivity(), EventHistory.class);
//
//                intent.putExtra("TASK", hisArrayList.get(position).getTask());
//                intent.putExtra("STUDENT",hisArrayList.get(position).getStudentID());
//                intent.putExtra("DOTG",hisArrayList.get(position).getDateOfTaskGiven());
//                intent.putExtra("POT",hisArrayList.get(position).getPlace());
//                intent.putExtra("DOTU",hisArrayList.get(position).getDateOfTaskUploaded());
//                intent.putExtra("DOCID",hisArrayList.get(position).getDocID());
//                intent.putExtra("PICUID",hisArrayList.get(position).getPICuid());
//                intent.putExtra("TASKDETAIL",hisArrayList.get(position).getTaskDetail());
//                intent.putExtra("TYPE", hisArrayList.get(position).getType());
//                intent.putExtra("RESULT", hisArrayList.get(position).getResult());
//                startActivity(intent);
//                break;
//
//            case "task":
//                Intent intent2= new Intent(getActivity(), TaskHistory.class);
//
//                intent2.putExtra("TASK", hisArrayList.get(position).getTask());
//                intent2.putExtra("STUDENT",hisArrayList.get(position).getStudentID());
//                intent2.putExtra("DOTG",hisArrayList.get(position).getDateOfTaskGiven());
//                intent2.putExtra("POT",hisArrayList.get(position).getPlace());
//                intent2.putExtra("DOTU",hisArrayList.get(position).getDateOfTaskUploaded());
//                intent2.putExtra("DOCID",hisArrayList.get(position).getDocID());
//                intent2.putExtra("PICUID",hisArrayList.get(position).getPICuid());
//                intent2.putExtra("TASKDETAIL",hisArrayList.get(position).getTaskDetail());
//                intent2.putExtra("TYPE", hisArrayList.get(position).getType());
//                intent2.putExtra("RESULT", hisArrayList.get(position).getResult());
//                startActivity(intent2);
//                break;
//
//            case "request":
//                Intent intent3= new Intent(getActivity(), RequestHistory.class);
//
//                intent3.putExtra("TASK", hisArrayList.get(position).getTask());
//                intent3.putExtra("STUDENT",hisArrayList.get(position).getStudentID());
//                intent3.putExtra("DOTG",hisArrayList.get(position).getDateOfTaskGiven());
//                intent3.putExtra("POT",hisArrayList.get(position).getPlace());
////                intent3.putExtra("DOTU",paArrayList.get(position).getDateOfTaskUploaded());
//                intent3.putExtra("DOCID",hisArrayList.get(position).getDocID());
//                intent3.putExtra("PICUID",hisArrayList.get(position).getPICuid());
//                intent3.putExtra("TASKDETAIL",hisArrayList.get(position).getTaskDetail());
//                intent3.putExtra("TYPE", hisArrayList.get(position).getType());
//                intent3.putExtra("RESULT", hisArrayList.get(position).getResult());
//                startActivity(intent3);
//                break;

//        }


    }
}