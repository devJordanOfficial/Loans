package com.infamousgc.loans.Data;

public enum Plan {
    SHORT("Short Term (1 day)", 0.004, 1, 24),
    LONG("Long Term (3 days)", 0.01, 2, 72);

    private final String description;
    private final double rate;
    private final int period;
    private final int maxHours;

    Plan(String description, double rate, int period, int maxHours) {
        this.description = description;
        this.rate = rate;
        this.period = period;
        this.maxHours = maxHours;
    }

    public String getDescription() {
        return description;
    }

    public double getRate() {
        return rate;
    }

    public int getPeriod() {
        return period;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public int getMaxPeriods() {
        return maxHours / period;
    }

    public static Plan valueOf(int ordinal) {
        if (ordinal == 0)
            return SHORT;
        else if (ordinal == 1)
            return LONG;
        else
            return null;
    }
}
