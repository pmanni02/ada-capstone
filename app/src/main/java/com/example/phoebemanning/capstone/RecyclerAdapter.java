package com.example.phoebemanning.capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
//        String amount = nutrients.get(i).getNutrient_id();
        Measures measure = nutrients.get(i).getMeasures()[0];
        String amount = measure.getValue();
        String unit = measure.getEunit();

        viewHolder.title.setText(title);
        viewHolder.amount.setText(amount+unit);
//        viewHolder.title.setBackgroundColor(parseColor("#4286f4"));
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
