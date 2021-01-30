package org.tensorflow.lite.examples.classification.view;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.classification.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<String> calories;

    public RecyclerAdapter(ArrayList<String> calories) {
        this.calories = calories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String foodName = calories.get(position);

        if (foodName.equals("Baklava  410 Cal-(100gr)")){
            holder.foodName.setText("Baklava  410 Cal-(100gr)");
            holder.imageView.setImageResource(R.drawable.baklavapng);

        }else if (foodName.equals("Cheesecake  344 Cal-(100gr)")){
            holder.foodName.setText("Cheesecake  344 Cal-(100gr)");
            holder.imageView.setImageResource(R.drawable.cheesecakepng);

        }else if(foodName.equals("Donuts  440 Cal-(100gr)")){
            holder.foodName.setText("Donuts  440 Cal-(100gr)");
            holder.imageView.setImageResource(R.drawable.donutpng);

        }else if(foodName.equals("Pancakes  158 Cal-(100gr)")){
            holder.foodName.setText("Pancakes  158 Cal-(100gr)");
            holder.imageView.setImageResource(R.drawable.pancake);

        }else if(foodName.equals("Tiramisu  225 Cal-(100gr)")){
            holder.foodName.setText("Tiramisu  225 Cal-(100gr)");
            holder.imageView.setImageResource(R.drawable.tiramisupng);

        }else{
            holder.foodName.setText("Tatlı bulunamadı.");
            holder.imageView.setImageResource(R.drawable.selectimage);
        }

    }

    @Override
    public int getItemCount() {
        return calories.size();
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder {

        ImageView imageView;
        TextView foodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
        }
    }
}
