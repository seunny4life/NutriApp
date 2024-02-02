package com.example.nutriapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    private List<FoodItem> foodItems;

    // Constructor to initialize the adapter with a list of food items
    public FoodItemAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each food item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the food item at the current position
        FoodItem item = foodItems.get(position);

        // Bind the food item data to the views in the ViewHolder
        holder.foodNameTextView.setText(item.getName());
        // You can also bind other fields like calories, description, etc. here
    }

    @Override
    public int getItemCount() {
        // Return the total number of food items in the list
        return foodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        // Declare other views here (e.g., caloriesTextView, descriptionTextView, etc.)

        public ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views in the ViewHolder by finding their IDs
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            // You can also initialize other views here by finding their respective IDs
        }
    }
}
