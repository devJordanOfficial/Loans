package com.infamousgc.loans.Events;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import com.infamousgc.loans.Data.PlayerData;
import com.infamousgc.loans.Data.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobPaymentListener implements Listener {

    @EventHandler
    public void onJobPayment(JobsPrePaymentEvent event) {
        PlayerData data = Players.get(event.getPlayer().getUniqueId());
        if (!data.isOverdue()) return;

        double income;
        double payment;

        if (data.getBalance() < event.getAmount()) {
            income = event.getAmount() - data.getBalance();
            payment = data.getBalance();
        } else {
            income = event.getAmount() * 0.2;
            payment = event.getAmount() * 0.8;
        }

        event.setAmount(income);
        data.jewIncome(payment);
    }
}
