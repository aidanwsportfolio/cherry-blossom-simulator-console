package app;

import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final boolean ENABLE_COLOR = true;
    private static final String RESET = "\u001B[0m";
    private static final String PINK  = "\u001B[38;5;212m";
    private static final String CYAN  = "\u001B[36m";
    private static final String GRAY  = "\u001B[90m";


    public static void main(String[] args) {
        Simulation sim = new Simulation();
        System.out.println("\n🌸 Cherry Blossom Simulator (Console Edition) — One-Night MVP\n");

        boolean running = true;
        while (running) {
            printStatus(sim);
            printMenu();
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> { sim.setSeason(Season.WINTER); pause("Season set to WINTER."); }
                case "2" -> { sim.setSeason(Season.SPRING); pause("Season set to SPRING."); }
                case "3" -> { sim.setSeason(Season.SUMMER); pause("Season set to SUMMER."); }
                case "4" -> { sim.setSeason(Season.FALL);   pause("Season set to FALL."); }
                case "5" -> setTemperature(sim);
                case "6" -> setWind(sim);
                case "7" -> {
                    int days = askInt("How many days to simulate? (1–100)", 1, 100);
                    runDays(sim, days, true);
                }
                case "8" -> {
                    int days = askInt("Auto-cycle seasons for how many days? (10–365)", 10, 365);
                    autoCycle(sim, days);
                }
                case "9" -> {
                    printFinal(sim);
                    running = false;
                }
                case "10" -> saveSummary(sim);
                default -> System.out.println("Please choose 1–9.");
                
            
            }
        }

        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println(" 1) WINTER");
        System.out.println(" 2) SPRING");
        System.out.println(" 3) SUMMER");
        System.out.println(" 4) FALL");
        System.out.println(" 5) Set temperature");
        System.out.println(" 6) Set wind");
        System.out.println(" 7) Simulate N days");
        System.out.println(" 8) Auto-cycle seasons");
        System.out.println(" 9) Quit");
        System.out.print("Choose: ");
        System.out.println(" 8) Auto-cycle seasons");
        System.out.println(" 9) Quit");
        System.out.println("10) Save run summary to file");


    }

    private static void printStatus(Simulation s) {
        System.out.printf(
                "Day %d | Season: %-6s | Temp: %2d°C | Wind: %d | Blossoms: %d | Peak: %d (day %d)%n",
                s.getDay(), s.getSeason(), s.getTemperatureC(), s.getWind(),
                s.getBlossoms(), s.getPeakBlossoms(), s.getPeakDay()
        );

        drawBar(s.getBlossoms(), 60);

        private static void drawTreeTiny(int blossoms) {
        System.out.println("   /\\");
        System.out.println("  /**\\     ~ Blossoms: " + blossoms);
        System.out.println(" /****\\");
        System.out.println("   ||");
}

    }

    private static void drawBar(int value, int width) {
    int capped = Math.min(value, 500);
    int filled = (int) Math.round((capped / 500.0) * width);
    String hashes = "#".repeat(Math.max(0, filled));
    String spaces = " ".repeat(Math.max(0, width - filled));
    String bar = "[" + hashes + spaces + "]";
    if (ENABLE_COLOR) {
        String color = (filled > width * 0.6) ? PINK : (filled > width * 0.3 ? CYAN : GRAY);
        System.out.println("Blossoms " + color + bar + RESET + " " + capped);
    } else {
        System.out.println("Blossoms " + bar + " " + capped);
    }
}


    private static void runDays(Simulation s, int days, boolean show) {
        for (int i = 0; i < days; i++) {
            s.randomizeWeather();
            s.stepDay();
            if (show) {
                printStatus(s);
                sleep(120);
            }
        }
        System.out.println("→ Finished " + days + " days.");
    }

    private static void autoCycle(Simulation s, int days) {
        for (int i = 0; i < days; i++) {
            if (i % 30 == 0 && i > 0)
                s.setSeason(s.getSeason().next());

            s.randomizeWeather();
            s.stepDay();
            printStatus(s);
            sleep(120);
        }
        System.out.println("→ Auto cycle complete.");
    }

    private static void setTemperature(Simulation s) {
        int t = askInt("Set temperature (-10..35 °C)", -10, 35);
        s.setTemperatureC(t);
        pause("Temperature set to " + t + "°C.");
    }

    private static void setWind(Simulation s) {
        int w = askInt("Set wind (0..10)", 0, 10);
        s.setWind(w);
        pause("Wind set to " + w + ".");
    }

    private static int askInt(String prompt, int lo, int hi) {
        while (true) {
            System.out.print(prompt + ": ");
            String line = in.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < lo || v > hi)
                    System.out.println("Enter between " + lo + " and " + hi + ".");
                else
                    return v;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static void pause(String msg) {
        System.out.println(msg);
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private static void printFinal(Simulation s) {
        System.out.println("\n===== Run Summary =====");
        System.out.println("Days simulated: " + s.getDay());
        System.out.println("Peak blossoms : " + s.getPeakBlossoms() + " (day " + s.getPeakDay() + ")");
        System.out.println("Total petals fallen: " + s.getTotalPetalsFallen());
        System.out.println("=======================\n");
    }
    
    private static void saveSummary(Simulation s) {
        String filename = "run-summary-" + System.currentTimeMillis() + ".txt";
        String content = """
            Cherry Blossom Simulator — Run Summary
            =====================================
            Days simulated: %d
            Peak blossoms : %d (day %d)
            Total petals fallen: %d
            """.formatted(
                s.getDay(), s.getPeakBlossoms(), s.getPeakDay(), s.getTotalPetalsFallen()
        );
        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of(filename), content);
            System.out.println("Saved " + filename);
        } catch (Exception e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }

}
