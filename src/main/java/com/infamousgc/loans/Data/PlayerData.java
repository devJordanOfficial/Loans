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

    private Plan planCache;
    private double principalCache;

    private Plan plan;
    private double principal;
    private LocalDateTime createdAt;

    private double balance;
    private LocalDateTime due;

    private boolean upToDate;

    public PlayerData(UUID owner) {
        this.plugin = JavaPlugin.getPlugin(Loans.class);
        this.owner = owner;
    }

    public PlayerData(UUID owner, boolean hasLoan, Plan plan, double principal, LocalDateTime createdAt, double balance) {
        this.plugin = JavaPlugin.getPlugin(Loans.class);
        this.owner = owner;
        this.hasLoan = hasLoan;
        if (hasLoan) {
            this.plan = plan;
            this.principal = principal;
            this.createdAt = createdAt;
            this.balance = balance;
            this.due = createdAt.plusHours(plan.getMaxHours());
        }
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
        Duration duration;

        if (isOverdue())
            duration = Duration.between(createdAt, due); // Difference is max if overdue
        else
            duration = Duration.between(createdAt, LocalDateTime.now()); // Get difference between times

        long periods = duration.toHours() / plan.getPeriod(); // A period happens every x hours
        if (periods > plan.getMaxPeriods()) periods = plan.getMaxPeriods();

        double value = principal * Math.pow(1 + plan.getRate(), periods); // Total value calculation (Not interest)

        return value;
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

    private void updateBalance() {
        if (!hasLoan) return;
        if (upToDate) return;
        if (isOverdue()) upToDate = true;

        balance = calculateInterest();
    }

    public void pay() {
        hasLoan = false;
        plan = null;
        principal = 0;
        createdAt = null;
        balance = 0;
        due = null;
    }

    public void jewIncome(double amount) {
        balance -= amount;
        if (balance == 0) pay();
    }

    public double getPercentUntilDue() {
        long timeSince = Duration.between(createdAt, LocalDateTime.now()).toHours();
        double hoursUntil = plan.getMaxHours() - timeSince;

        return hoursUntil / plan.getMaxHours();
    }

    public double getPrincipal() {
        return principal;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setCache(double principal) {
        principalCache = principal;
    }

    public void setCache(Plan plan) {
        planCache = plan;
    }

    public double getPrincipalCache() {
        return principalCache;
    }

    public Plan getPlanCache() {
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

    public String getColorfulDue() {
        int minutesUntil = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), due);

        return formatAndColorMinutes(minutesUntil);
    }

    public void alert() {
        Player player = Bukkit.getPlayer(owner);

        if (plan == Plan.SHORT) {
            if (ChronoUnit.HOURS.between(LocalDateTime.now(), due) > 12) return;
            player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.reminder) + " &7You currently have a " +
                    "loan of #008642$" + Formatter.formatNumber(getBalance()) + "&7 which is due in #008642" +
                    getColorfulDue()));
        }

        if (plan == Plan.LONG) {
            if (ChronoUnit.HOURS.between(LocalDateTime.now(), due) > 48) return;
            player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.reminder) + " &7You currently have a " +
                    "loan of #008642$" + Formatter.formatNumber(getBalance()) + "&7 which is due in #008642" +
                    getColorfulDue()));
        }
    }

    public String isOverdueString() {
        return isOverdue() ? "true" : "false";
    }

    public boolean isOverdue() {
        if (!hasLoan) return false;
        return ((int) ChronoUnit.MINUTES.between(LocalDateTime.now(), due)) < 0;
    }

    public boolean isUpToDate() {
        return upToDate;
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

    private String formatAndColorMinutes(int minutes) {
        if (minutes >= 60) {
            int hours = minutes / 60;
            int remaining = minutes % 60;
            if (remaining == 0)
                return hours + " Hour" + (hours == 1 ? "" : "s");
            else
                return hours + " Hour" + (hours == 1 ? "" : "s") + " &7and #008642" + remaining + " Minute" + (remaining == 1 ? "" : "s");
        }
        else
            return minutes + " Minute" + (minutes == 1 ? "" : "s");
    }

}