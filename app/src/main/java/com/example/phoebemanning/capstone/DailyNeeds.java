package com.example.phoebemanning.capstone;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DailyNeeds {

//  sugar daily amount is from http://sugarscience.ucsf.edu/the-growing-concern-of-overconsumption.html#.W0rFsthKjOQ
//  all other daily amounts from https://www.accessdata.fda.gov/scripts/InteractiveNutritionFactsLabel/pdv.html

//  ALL values are in GRAMS

    private static Map<String,String> dailyCals2000 = new HashMap<>();
    static {
        dailyCals2000.put("fat", "65");
        dailyCals2000.put("sat fat", "20");
        dailyCals2000.put("sodium", "2.4");
        dailyCals2000.put("sugar", "25");
        dailyCals2000 = Collections.unmodifiableMap(dailyCals2000);
    }

    private static Map<String,String> dailyCals2500 = new HashMap<>();
    static {
        dailyCals2500.put("fat", "80");
        dailyCals2500.put("sat fat", "25");
        dailyCals2500.put("sodium", "2.4");
        dailyCals2500.put("sugar", "31.25");
        dailyCals2500 = Collections.unmodifiableMap(dailyCals2500);
    }

    public DailyNeeds() {
    }

    public static Map<String, String> getDailyCals2000() {
        return dailyCals2000;
    }

    public static Map<String, String> getDailyCals2500() {
        return dailyCals2500;
    }

}
