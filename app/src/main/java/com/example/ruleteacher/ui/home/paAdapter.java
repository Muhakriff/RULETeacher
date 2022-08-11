package com.example.ruleteacher.ui.home;

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

public class paAdapter extends  RecyclerView.Adapter<paAdapter.MyViewHolder>{

    private final HomeInterface homeInterface;
    Context context;
    ArrayList<paModel> paArrayList;
    FirebaseFirestore fStore;

    public paAdapter(HomeInterface homeInterface, Context context, ArrayList<paModel> paArrayList) {
        this.homeInterface = homeInterface;
        this.context = context;
        this.paArrayList = paArrayList;
    }


    @NonNull
    @Override
    public paAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frame_pending_approval, parent, false);
        fStore=FirebaseFirestore.getInstance();
        return new paAdapter.MyViewHolder(view, homeInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull paAdapter.MyViewHolder holder, int position) {
        paModel paModel= paArrayList.get(position);
        holder.tvTaskName.setText(paArrayList.get(position).getTask());
        String tempID=paArrayList.get(position).getStudentID();
        fStore.collection("users").document(tempID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.tvStudName.setText(value.getString("name"));
                holder.tvStudClass.setText(value.getString("classroom"));
            }
        });
        String tempIcon= paArrayList.get(position).getType();

        switch (tempIcon){
            case "event":
                holder.imgPendIcon.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                break;
            case "task":
                holder.imgPendIcon.setImageResource(R.drawable.ic_baseline_pending_actions_24);
                break;
            case "request":
                holder.imgPendIcon.setImageResource(R.drawable.ic_baseline_pending_actions_24);
                break;
            case "KoQ":
                holder.imgPendIcon.setImageResource(R.drawable.ic_baseline_add_24);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return paArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskName, tvStudName, tvStudClass;
        ImageView imgPendIcon;

        public MyViewHolder(@NonNull View itemView, HomeInterface homeInterface) {
            super(itemView);

            tvTaskName= itemView.findViewById(R.id.tv_pa);
            tvStudName= itemView.findViewById(R.id.tv_studName);
            tvStudClass= itemView.findViewById(R.id.tv_studClass);
            imgPendIcon=itemView.findViewById(R.id.img_pendIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(homeInterface != null){
                        int pos= getBindingAdapterPosition();
                        if (pos!= RecyclerView.NO_POSITION){
                            homeInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
