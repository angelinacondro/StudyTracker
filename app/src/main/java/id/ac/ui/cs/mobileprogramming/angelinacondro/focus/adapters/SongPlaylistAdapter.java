package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.RoomDB;
import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity.SongPlaylistData;

import java.util.ArrayList;
import java.util.List;

public class SongPlaylistAdapter extends RecyclerView.Adapter<SongPlaylistAdapter.ViewHolder>{
    private List<SongPlaylistData> dataList = new ArrayList<>();
    private Activity context;
    private RoomDB database;
    private OnItemListener mOnItemListener;

    public SongPlaylistAdapter(Activity context, List<SongPlaylistData> dataList){
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list,parent,false);
        return new ViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SongPlaylistData data = dataList.get(position);
//        database = RoomDB.getInstance(context);
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

    public void setData(List<SongPlaylistData> songPlaylistData){
        this.dataList = songPlaylistData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView list;
        Button btStart;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            list = itemView.findViewById(R.id.listitemlagu);
//            btStart = itemView.findViewById((R.id.startNow));
//            this.onItemListener = onItemListener;
//
//            btStart.setOnClickListener(this);

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
