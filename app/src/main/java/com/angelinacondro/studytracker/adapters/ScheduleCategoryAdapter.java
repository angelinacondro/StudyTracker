package com.angelinacondro.studytracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.angelinacondro.studytracker.R;
import com.angelinacondro.studytracker.db.RoomDB;
import com.angelinacondro.studytracker.db.dao.ScheduleCategoryDao;
import com.angelinacondro.studytracker.db.entity.ScheduleCategoryData;

import java.util.ArrayList;
import java.util.List;

public class ScheduleCategoryAdapter extends ArrayAdapter<ScheduleCategoryData> {

    public ScheduleCategoryAdapter(Context context, ArrayList<ScheduleCategoryData> scheduleList) {
        super(context, 0, scheduleList);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_list, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.category_name);
        ScheduleCategoryData currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem.getCategory());
        }
        return convertView;
    }
}
