package app;

import java.util.Random;

public class Simulation {
    private final Random rng = new Random(); //randomizes every run for realism

    private Season season = Season.WINTER;
    private int day = 0;               // day counter
    private int blossoms = 0;          // blossoms currently on tree
    private int peakBlossoms = 0;      // max blossoms observed
    private int peakDay = 0;           // day of peak
    private int totalPetalsFallen = 0; // cumulative fallen petals

    // environment
    private int temperatureC = 5;      // -10 .. 30 typical
    private int wind = 0;              // 0 .. 10 arbitrary units

    // tuning max blossoms to avoid console freezing
    private final int MAX_BLOSSOMS = 4500;
    //set variables values and their return values
    public void setSeason(Season s) { this.season = s; }
    public Season getSeason() { return season; }
    public int getDay() { return day; }
    public int getTemperatureC() { return temperatureC; }
    public int getWind() { return wind; }
    public int getBlossoms() { return blossoms; }
    public int getPeakBlossoms() { return peakBlossoms; }
    public int getPeakDay() { return peakDay; }
    public int getTotalPetalsFallen() { return totalPetalsFallen; }

    public void setTemperatureC(int t) { this.temperatureC = t; }
    public void setWind(int w) { this.wind = Math.max(0, Math.min(10, w)); } //Protects against invalid input (e.g., negative wind, or wind > 10)

    public void randomizeWeather() {
        // season-based temperature swings
        int baseT = switch (season) { //enhanced switch statement so each case provides a value and avoids break
            case WINTER -> -2;
            case SPRING -> 14;
            case SUMMER -> 26;
            case FALL   -> 12;
        };
        temperatureC = baseT + rng.nextInt(7) - 3; // ±3 variation for realism effect
        wind = rng.nextInt(6); // 0..5 wind
    }

    public void stepDay() { //move simulation ahead by 1 day
        day++;

        boolean spring = (season == Season.SPRING);
        boolean comfy = (temperatureC >= 10 && temperatureC <= 20);

        int growth = 0;
        int decay = 0;

        if (spring && comfy) {
            // comfort temp = biggest bloom
            int warmth = temperatureC - 10; // 0..10
            growth = 10 + warmth * 4 + rng.nextInt(6); // ~10–50/day
        } else if (spring) {
            // not perfect spring weather
            growth = Math.max(0, 5 - Math.abs(temperatureC - 15)) + rng.nextInt(3);
        } else {
            // non-spring seasons decay blossoms
            decay = 8 + wind * 2 + rng.nextInt(6);
        }

        // wind always removes some petals
        decay += (int) Math.round(blossoms * (0.002 * wind));

        int before = blossoms;
        blossoms = Math.max(0, Math.min(MAX_BLOSSOMS, blossoms + growth - decay));
        totalPetalsFallen += Math.max(0, before - blossoms);
        //updates the max if it's "passed" 
        if (blossoms > peakBlossoms) {
            peakBlossoms = blossoms;
            peakDay = day;
        }
    }
}
