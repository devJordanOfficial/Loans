package com.infamousgc.loans.Events;

import com.infamousgc.loans.Data.PlayerData;
import com.infamousgc.loans.Data.Players;
import com.infamousgc.loans.Interface.NewLoan;
import com.infamousgc.loans.Interface.Payment;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatListener implements Listener {

    private final Loans plugin;

    public ChatListener(Loans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (!plugin.listen.contains(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);

        String message = event.getMessage().trim();

        if (!message.equalsIgnoreCase("cancel")) {
            double amount;
            try {
                amount = Double.parseDouble(message);
            } catch (NumberFormatException e) {
                event.getPlayer().sendMessage(Formatter.parse("&c&lOops! &7Your message \"&c" + event.getMessage() + "&7\"" +
                        " is not a number! Please try again or type &ccancel&7 to cancel. Please ensure:\n" +
                        "&c- &7There are no dollar signs ($)\n" +
                        "&c- &7There are no commas (,)\n" +
                        "&c- &7There are not other characters besides numbers (1-9)"));
                return;
            }

            if (plugin.user.get(event.getPlayer().getUniqueId()) instanceof NewLoan) {
                if (Players.get(event.getPlayer()).getLimit() < amount) {
                    event.getPlayer().sendMessage((Formatter.parse("&c&lOops! &7The amount &c$" + Formatter.formatNumber(amount)
                            + " is more than your" + " limit of &c$" + Players.get(event.getPlayer()).getFormattedLimit())));
                    return;
                }

                Players.get(event.getPlayer()).setCache(amount);
                ((NewLoan) plugin.user.get(event.getPlayer().getUniqueId())).update();
            }

            if (plugin.user.get(event.getPlayer().getUniqueId()) instanceof Payment) {
                PlayerData data = Players.get(event.getPlayer());
                Payment paymentGUI = (Payment) plugin.user.get(event.getPlayer().getUniqueId());
                if (data.getBalance() < amount) amount = data.getBalance();
                if (0 > amount) amount = 0;
                paymentGUI.setPayment(amount);
                paymentGUI.update();
            }
        }
        plugin.listen.remove(event.getPlayer().getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.user.get(event.getPlayer().getUniqueId()).open();
            }
        }.runTask(plugin);
    }

}
