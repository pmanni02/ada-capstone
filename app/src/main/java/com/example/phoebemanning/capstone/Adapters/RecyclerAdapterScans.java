package com.example.phoebemanning.capstone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.Activities.ProductActivity;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.Models.Scan;
import com.example.phoebemanning.capstone.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterScans extends RecyclerView.Adapter<RecyclerAdapterScans.MyViewHolder> {

    private ArrayList<Scan> scans;
    private Context context;

    public RecyclerAdapterScans(ArrayList<Scan> scans, Context context){
        this.scans = scans;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scan_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String scanName = scans.get(i).getProductName();
        myViewHolder.scanName.setText(scanName);
    }

    @Override
    public int getItemCount() {
        return scans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView scanName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            scanName = itemView.findViewById(R.id.scan_item);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Scan scan = scans.get(pos);
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("name", scan.getProductName());
            intent.putExtra("upc", scan.getUpcCode());
            intent.putExtra("ndbno", scan.getNdbno());
            context.startActivity(intent);
//            Toast.makeText(context, scan.getProductName(), Toast.LENGTH_SHORT).show();
        }
    }
}
