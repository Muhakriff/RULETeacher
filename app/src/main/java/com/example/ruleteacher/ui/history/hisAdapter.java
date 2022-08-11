package com.example.ruleteacher.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruleteacher.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class hisAdapter extends RecyclerView.Adapter<hisAdapter.MyViewHolder> {
    Context context;
    ArrayList<hisModel> hisArrayList;
    private final RecyclerViewInterface recyclerViewInterface;
    FirebaseFirestore fStore;

    public hisAdapter(Context context, ArrayList<hisModel> hisArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.hisArrayList = hisArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public hisAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frame_history, parent, false);
        fStore=FirebaseFirestore.getInstance();
        return new hisAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull hisAdapter.MyViewHolder holder, int position) {
        holder.tvHistory.setText(hisArrayList.get(position).getTask());
        String name=hisArrayList.get(position).getStudentID();
        fStore.collection("users").document(name).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.tvStudent.setText(value.getString("name"));
            }
        });
        String result=hisArrayList.get(position).getResult();
        String type= hisArrayList.get(position).getType();
        switch (result){
            case "Approved":
                holder.imgIcon.setImageResource(R.drawable.ic_baseline_check_24);
                break;
            case "Rejected":
                holder.imgIcon.setImageResource(R.drawable.ic_baseline_close_24);
                break;
            case "Pending Approval":
                holder.imgIcon.setImageResource(R.drawable.ic_baseline_question_mark_24);
                break;
            case "Pending Response":
                holder.imgIcon.setImageResource(R.drawable.ic_baseline_question_mark_24);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return hisArrayList.size();    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvHistory, tvStudent;
        ImageView imgIcon;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tvHistory=itemView.findViewById(R.id.tv_history);
            tvStudent=itemView.findViewById(R.id.tv_student);
            imgIcon=itemView.findViewById(R.id.img_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos= getBindingAdapterPosition();

                        if (pos!= RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
