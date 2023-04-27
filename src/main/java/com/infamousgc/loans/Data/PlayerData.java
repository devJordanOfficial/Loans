package com.infamousgc.loans.Data;

import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.infamousmc.ranktree.Data.Rank;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PlayerData {

    Loans plugin;

    private UUID owner;
    private boolean hasLoan = false;

    private LoanType planCache;
    private double principalCache;

    private LoanType plan;
    private double principal;
    private LocalDateTime createdAt;

    private double balance;
    private double paid;
    private LocalDateTime due;

    public PlayerData(UUID owner) {
        this.plugin = JavaPlugin.getPlugin(Loans.class);
        this.owner = owner;
    }

    public PlayerData(UUID owner, boolean hasLoan, LoanType plan, double principal, LocalDateTime createdAt, double balance) {
        this.owner = owner;
        this.hasLoan = hasLoan;
        this.plan = plan;
        this.principal = principal;
        this.createdAt = createdAt;
        this.balance = balance;
        this.due = createdAt.plusHours(plan.getMaxHours());
    }

    public void createLoan() {
        this.plan = planCache;
        this.principal = principalCache;
        this.balance = principal;
        createdAt = LocalDateTime.now();
        due = createdAt.plusHours(plan.getMaxHours());
        hasLoan = true;
    }

    public boolean hasLoan() {
        return hasLoan;
    }

    private double calculateInterest() {
        Duration duration = Duration.between(createdAt, LocalDateTime.now()); // Get difference between times
        long periods = duration.toHours() / plan.getPeriod(); // A period happens every x hours
        if (periods > plan.getMaxPeriods()) periods = plan.getMaxPeriods();

        double value = principal * Math.pow(1 + plan.getRate(), periods); // Total value calculation (Not interest)

        return value - principal;
    }

    public String getFormattedLimit() {
        return Formatter.formatNumber(getLimit());
    }

    public double getLimit() {
        Player player = Bukkit.getPlayer(owner);

        if (Rank.getNextRank(player) == Rank.MAX)
            return 5000000;

        return plugin.rankTreeAPI.getRankCost(Rank.getNextRank(player)) / 2;
    }

    public double getBalance() {
        updateBalance();
        return balance;
    }

    public void makePayment(double amount) {
        if ((balance - principal) > 0)
            balance -= amount;
        if ((balance - principal) = 0)
    }

    private void updateBalance() {
        balance = calculateInterest() + principal - paid;
    }

    public double getPercentUntilDue() {
        long timeSince = Duration.between(createdAt, LocalDateTime.now()).toHours();
        double hoursUntil = plan.getMaxHours() - timeSince;

        return hoursUntil / plan.getMaxHours();
    }

    public double getPrincipal() {
        return principal;
    }

    public LoanType getPlan() {
        return plan;
    }

    public void setCache(double principal) {
        principalCache = principal;
    }

    public void setCache(LoanType plan) {
        planCache = plan;
    }

    public double getPrincipalCache() {
        return principalCache;
    }

    public LoanType getPlanCache() {
        return planCache;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void resetCache() {
        principalCache = 0;
        planCache = null;
    }

    public String getNextPeriod() {
        int minutesPerPeriod = plan.getPeriod() * 60;

        LocalDateTime nextHour = createdAt.plusHours(plan.getPeriod()).withSecond(0);
        int minutesUntil = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), nextHour) % minutesPerPeriod;

        if (minutesUntil < 0) {
            minutesUntil += minutesPerPeriod;
        }

        return formatMinutes(minutesUntil);
    }

    public String getDue() {
        int minutesUntil = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), due);

        return (isOverdue()) ? "OVERDUE" : "In " + formatMinutes(minutesUntil);
    }

    public boolean isOverdue() {
        return ((int) ChronoUnit.MINUTES.between(LocalDateTime.now(), due)) < 0;
    }

    private String formatMinutes(int minutes) {
        if (minutes >= 60) {
            int hours = minutes / 60;
            int remaining = minutes % 60;
            if (remaining == 0)
                return hours + " Hour" + (hours == 1 ? "" : "s");
            else
                return hours + " Hour" + (hours == 1 ? "" : "s") + " and " + remaining + " Minute" + (remaining == 1 ? "" : "s");
        }
        else
            return minutes + " Minute" + (minutes == 1 ? "" : "s");
    }

}