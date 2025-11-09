package app;

public enum Season {
    WINTER, SPRING, SUMMER, FALL;

    public Season next() {
        return switch (this) {
            case WINTER -> SPRING;
            case SPRING -> SUMMER;
            case SUMMER -> FALL;
            case FALL -> WINTER;
        };
    }
}
