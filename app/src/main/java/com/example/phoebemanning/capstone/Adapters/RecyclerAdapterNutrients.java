package com.example.phoebemanning.capstone.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private String dailyVal;
    private Boolean percent;

    public RecyclerAdapterNutrients(List<Nutrients> nutrients, String dailyVal, Boolean percent, Context context){
        this.nutrients = nutrients;
        this.context = context;
        this.dailyVal = dailyVal;
        this.percent = percent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nutrient_item_layout, viewGroup, false);
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

//      rounded to two decimal places
        BigDecimal roundedVal = new BigDecimal(Float.toString(valueInt));
        roundedVal = roundedVal.setScale(2, BigDecimal.ROUND_HALF_UP);

        viewHolder.title.setText(title);
        if(title.equals("Energy")){
            //      get % daily calories
            if(dailyVal != null && percent){
                Float dailyValFloat = Float.parseFloat(dailyVal);
                Float percentDailyVal = (valueInt/dailyValFloat)*100;
                viewHolder.amount.setText(percentDailyVal + " % Daily");
            } else {
                viewHolder.amount.setText(roundedVal + " Calories");
            }
        } else if(title.equals("Total lipid (fat)")){

            if(dailyVal != null && percent){
                Float dailyValFloat = Float.parseFloat(dailyVal);
                Map<String, String> dailyCals2000 = DailyNeeds.getDailyCals2000();
                String fat= dailyCals2000.get("fat");
                Float fatFloat = Float.parseFloat(fat);
                Float percentDaily = (valueInt/fatFloat)*100;
                viewHolder.amount.setText(percentDaily + " % Daily");
            } else {
                viewHolder.amount.setText(roundedVal + " " + unit);
            }

        } else {
            viewHolder.amount.setText(roundedVal + " " + unit);
        }

//        TODO: break out into separate function
//        setBackgroundColor(high, low);
        if(idInt.equals(204)){
            String color = getBackgroundColor(valueInt ,17.5, 3.0);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
        }

        if(idInt.equals(269)){
            String color = getBackgroundColor(valueInt ,22.5, 5.0);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
        }

        if(idInt.equals(307)){
            String color = getBackgroundColor(valueInt ,1.5, 0.3);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
        }

        if(idInt.equals(606)){
            String color = getBackgroundColor(valueInt ,5.0, 1.5);
            viewHolder.cardView.setCardBackgroundColor(parseColor(color));
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

}


