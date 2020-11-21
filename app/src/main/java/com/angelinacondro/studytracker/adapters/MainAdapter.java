package com.angelinacondro.studytracker.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angelinacondro.studytracker.db.entity.ScheduleData;
import com.angelinacondro.studytracker.R;
import com.angelinacondro.studytracker.db.RoomDB;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private List<ScheduleData> dataList = new ArrayList<>();
    private Activity context;
    private RoomDB database;
    private OnItemListener mOnItemListener;

    public MainAdapter(Activity context, List<ScheduleData> dataList){
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ScheduleData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.list.setText(data.getTitle());

//        holder.btStart.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent doTask = new Intent(context, DoTask.class);
//                doTask.putExtra(DoTask.JUDUL_SCHEDULE, (dataList.get(position)));
//                context.startActivity(doTask);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<ScheduleData> scheduleData){
        this.dataList = scheduleData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView list;
        Button btStart;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            list = itemView.findViewById(R.id.list);
            btStart = itemView.findViewById((R.id.startNow));

        }
        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());

        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
