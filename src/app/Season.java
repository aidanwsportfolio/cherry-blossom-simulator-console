package app;

public enum Season { //define a fixed set of constants
    WINTER, SPRING, SUMMER, FALL;

    public Season next() { //next() method transitions states, Finate State Machine (FSM) logic
        return switch (this) { //refers to CURRENT enum value
            case WINTER -> SPRING; 
            case SPRING -> SUMMER;
            case SUMMER -> FALL;
            case FALL -> WINTER;
        }; //no break statement needed
    }
}
