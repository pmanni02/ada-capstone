package com.example.phoebemanning.capstone.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.DailyNeeds;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Measures;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.DailyNeeds;
import com.example.phoebemanning.capstone.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.parseColor;

public class RecyclerAdapterNutrients extends RecyclerView.Adapter<RecyclerAdapterNutrients.MyViewHolder> {

    private List<Nutrients> nutrients;
    private Context context;
    private String dailyValCals;
    private Boolean percentBtn;
    private Boolean teaspoonBtn;

    public RecyclerAdapterNutrients(List<Nutrients> nutrients, String dailyValCals, Boolean percentBtn, Boolean teaspoonBtn, Context context){
        this.nutrients = nutrients;
        this.context = context;
        this.dailyValCals = dailyValCals;
        this.percentBtn = percentBtn;
        this.teaspoonBtn = teaspoonBtn;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nutrient_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

//    TODO: refactor this function
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {

        String title = nutrients.get(i).getName().split(",")[0];
        String id = nutrients.get(i).getNutrient_id();
        Integer idInt = Integer.parseInt(id);

//        Measures measure = nutrients.get(i).getMeasures()[0];
//        String value = measure.getValue();
//        String unit = measure.getEunit();

        String value = nutrients.get(i).getValue();
        String unit = nutrients.get(i).getUnit();
        Float valueInt = Float.parseFloat(value);
        
//      rounded to two decimal places
        BigDecimal roundedVal = new BigDecimal(Float.toString(valueInt));
        roundedVal = roundedVal.setScale(2, BigDecimal.ROUND_HALF_UP);

        viewHolder.title.setText(title);

        if(title.equals("Energy")){
            if(dailyValCals != null && percentBtn){
                Float dailyValFloat = Float.parseFloat(dailyValCals);
                Float percentDailyVal = (valueInt/dailyValFloat)*100;
                String s = String.format("%.2f", percentDailyVal);
                viewHolder.amount.setText(s + " % Daily");
            } else {
                viewHolder.amount.setText(roundedVal + " Calories");
            }
        } else if(title.equals("Total lipid (fat)")){

            String color = getBackgroundColor(valueInt ,17.5, 3.0);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
            viewHolder.amount.setText(getAmountVal(valueInt, "fat", unit));

        } else if(title.equals("Sugars")) {

            String color = getBackgroundColor(valueInt ,22.5, 5.0);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
            viewHolder.amount.setText(getAmountVal(valueInt, "sugar", unit));

        } else if(title.equals("Sodium")){

//          convert sodium levels from mg to g
            Float valueIntSodium = valueInt/1000;
            String color = getBackgroundColor(valueIntSodium ,1.5, 0.3);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
            viewHolder.amount.setText(getAmountVal(valueIntSodium, "sodium", "g"));

        } else if(title.equals("Fatty acids")) {

            String color = getBackgroundColor(valueInt, 5.0, 1.5);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
            viewHolder.amount.setText(getAmountVal(valueInt, "sat fat", unit));

        }


    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView amount;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nutrient_item);
            amount = itemView.findViewById(R.id.nutrient_amount);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public String getBackgroundColor(Float valueInt, Double high, Double low) {
        if(valueInt > high ){
            return "#FF0000";
        } else if(valueInt < low){
            return "#32CD32";
        } else {
            return "#FFA500";
        }
    }

    public String getAmountVal(Float value, String category, String unit){

        BigDecimal roundedVal = new BigDecimal(Float.toString(value));
        roundedVal = roundedVal.setScale(2, BigDecimal.ROUND_HALF_UP);

        String amount;

        if(percentBtn){
            Map<String, String> dailyCals2000 = DailyNeeds.getDailyCals2000();
            String nutrientDailyVal = dailyCals2000.get(category);
            Float nutrientDailyValFloat = Float.parseFloat(nutrientDailyVal);
            Float percentDaily = (value/nutrientDailyValFloat)*100;
            String s = String.format("%.2f", percentDaily);
            amount = s + " % Daily";
        } else if(teaspoonBtn){
            double teaspoon = value * 0.2028;
            String teaspoonStr = String.format("%.2f", teaspoon);
            amount = teaspoonStr + " tsp";
        } else {
            amount = roundedVal + " " + unit;
        }
        return amount;
    }

}


