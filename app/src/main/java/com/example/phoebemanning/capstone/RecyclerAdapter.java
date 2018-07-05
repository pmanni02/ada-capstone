package com.example.phoebemanning.capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phoebemanning.capstone.Models.Nutrient_Models.Measures;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;

import java.util.List;

import static android.graphics.Color.parseColor;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Nutrients> nutrients;
    private Context context;

    public RecyclerAdapter(List<Nutrients> nutrients, Context context){
        this.nutrients = nutrients;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        String title = nutrients.get(i).getName().split(",")[0];
        String id = nutrients.get(i).getNutrient_id();
        Integer idInt = Integer.parseInt(id);

        Measures measure = nutrients.get(i).getMeasures()[0];
        String value = measure.getValue();
        Float valueInt = Float.parseFloat(value);

        String unit = measure.getEunit();

        viewHolder.title.setText(title);
        viewHolder.amount.setText(value + " " + unit);

//        TODO: break out into separate function
        if(idInt.equals(204)){
            if(valueInt > 17.5 ){
                viewHolder.title.setBackgroundColor(parseColor("#FF0000"));
            } else if(valueInt < 3){
                viewHolder.title.setBackgroundColor(parseColor("#32CD32"));
            } else {
//                Log.i("onBindViewHolder", "INSIDE ELSE STATEMENT");
                viewHolder.title.setBackgroundColor(parseColor("#FFA500"));
            }
        }

        if(idInt.equals(269)){
            if(valueInt > 22.5){
                viewHolder.title.setBackgroundColor(parseColor("#FF0000"));
            } else if(valueInt < 5){
                viewHolder.title.setBackgroundColor(parseColor("#32CD32"));
            } else {
                viewHolder.title.setBackgroundColor(parseColor("#FFA500"));
            }
        }

        if(idInt.equals(307)){
            if(valueInt > 1.5){
                viewHolder.title.setBackgroundColor(parseColor("#FF0000"));
            } else if(valueInt < 0.3){
                viewHolder.title.setBackgroundColor(parseColor("#32CD32"));
            } else {
                viewHolder.title.setBackgroundColor(parseColor("#FFA500"));
            }
        }

        if(idInt.equals(606)){
            if(valueInt > 5){
                viewHolder.title.setBackgroundColor(parseColor("#FF0000"));
            } else if(valueInt < 1.5){
                viewHolder.title.setBackgroundColor(parseColor("#32CD32"));
            } else {
                viewHolder.title.setBackgroundColor(parseColor("#FFA500"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nutrient_item);
            amount = itemView.findViewById(R.id.nutrient_amount);
        }
    }

}
