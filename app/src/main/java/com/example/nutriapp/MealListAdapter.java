package com.example.nutriapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private final List<MealItem> mealItemList;

    public MealListAdapter(List<MealItem> mealItemList) {
        this.mealItemList = mealItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealItem mealItem = mealItemList.get(position);
        holder.mealNameTextView.setText(mealItem.getMealName());
        holder.caloriesTextView.setText(String.valueOf(mealItem.getCalories()));
    }

    @Override
    public int getItemCount() {
        return mealItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView;
        TextView caloriesTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mealNameTextView = itemView.findViewById(R.id.mealNameTextView);
            caloriesTextView = itemView.findViewById(R.id.caloriesTextView);
        }
    }
}
