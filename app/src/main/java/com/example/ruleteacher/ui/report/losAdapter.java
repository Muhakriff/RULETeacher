package com.example.ruleteacher.ui.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruleteacher.R;

import java.util.ArrayList;

public class losAdapter extends RecyclerView.Adapter<losAdapter.MyViewHolder> {

//    private final HomeInterface recyclerViewInterface;
    Context context;
    ArrayList<losModel> losArrayList;
    public UserClickListener userClickListener;

    public losAdapter(Context context, ArrayList<losModel> losArrayList, UserClickListener userClickListener) {
        this.context = context;
        this.losArrayList = losArrayList;
        this.userClickListener = userClickListener;
    }

    public interface UserClickListener{
        void selectedUser(losModel losModel);
    }

    @NonNull
    @Override
    public losAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frame_list_student, parent, false);
        return new losAdapter.MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull losAdapter.MyViewHolder holder, int position) {
        losModel losModel= losArrayList.get(position);
        holder.name.setText(losArrayList.get(position).getName());
        holder.ID.setText(losArrayList.get(position).getID());
        holder.kelas.setText(losArrayList.get(position).getClassroom());
        String gend=losArrayList.get(position).getGender();
        if(gend.equals("Male")){
            holder.imgGender.setImageResource(R.drawable.ic_baseline_male_24);
        }
        else if(gend.equals("Female")){
            holder.imgGender.setImageResource(R.drawable.ic_baseline_female_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClickListener.selectedUser(losModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return losArrayList.size();
    }

    public void filterList(ArrayList<losModel> filteredList){
        losArrayList=filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, ID, kelas;
        ImageView imgGender;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.tv_list_name);
            ID=itemView.findViewById(R.id.tv_listID);
            kelas=itemView.findViewById(R.id.tv_listClass);
            imgGender=itemView.findViewById(R.id.img_gender);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(recyclerViewInterface != null){
//                        int pos= getBindingAdapterPosition();
//
//                        if (pos!= RecyclerView.NO_POSITION){
//                            recyclerViewInterface.onItemClick(pos);
//                        }
//                    }
//                }
//            });
        }
    }
}
