package com.example.phoebemanning.capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.graphics.Color.parseColor;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<String> nutrientTitles;
    private Context context;

    public RecyclerAdapter(List<String> nutrientTitles, Context context){
        this.nutrientTitles = nutrientTitles;
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
        String title = nutrientTitles.get(i);
        viewHolder.title.setText(title);
//        viewHolder.title.setBackgroundColor(parseColor("#4286f4"));
    }

    @Override
    public int getItemCount() {
        return nutrientTitles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nutrient_item);
        }
    }
}
