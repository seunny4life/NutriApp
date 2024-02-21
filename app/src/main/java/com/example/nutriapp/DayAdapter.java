package com.example.nutriapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private final Context context;
    private final List<Day> days;
    private final OnDayClickListener onDayClickListener;

    public DayAdapter(Context context, List<Day> days, OnDayClickListener onDayClickListener) {
        this.context = context;
        this.days = days;
        this.onDayClickListener = onDayClickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.dayNumberTextView.setText(day.getName());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnDayClickListener {
        void onDayClick(String dayName, String date);
    }

    public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayNumberTextView;

        public DayViewHolder(View itemView) {
            super(itemView);
            dayNumberTextView = itemView.findViewById(R.id.dayNumberTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Day day = days.get(position);
                onDayClickListener.onDayClick(day.getName(), day.getDate());
            }
        }
    }
}


