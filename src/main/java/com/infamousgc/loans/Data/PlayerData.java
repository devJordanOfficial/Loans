package com.infamousgc.loans.Data;

import org.bukkit.Bukkit;
import org.infamousmc.ranktree.Data.Rank;
import org.infamousmc.ranktree.RankTreeAPI;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerData {

    private UUID owner;
    private boolean hasLoan = false;
    private double limit;

    private LoanType type;
    private double principal;
    private double rate;
    private int periodLength;
    private LocalDateTime createdAt;

    public void createLoan(LoanType type, double principal) {
        this.type = type;
        this.principal = principal;
        createdAt = LocalDateTime.now();
        update();
    }

    private double calculateOwing() {
        Duration duration = Duration.between(createdAt, LocalDateTime.now()); // Get difference between times
        long periods = duration.toHours() / periodLength; // A period happens every x hours

        double value = principal * Math.pow(1 + rate, periods); // Compounded value calculation (Not interest)

        return value;
    }

    private void update() {
        switch (type) {
            case SHORT:
                rate = 0.004;
                periodLength = 1;
                break;
            case LONG:
                rate = 0.01;
                periodLength = 2;
        }
    }

    public String getFormattedLimit() {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(getLimit());
    }

    public double getLimit() {
        RankTreeAPI api = new RankTreeAPI();
        return api.getRankCost(Rank.getNextRank(Bukkit.getPlayer(owner))) / 2;
    }

}