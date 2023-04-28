package com.infamousgc.loans.Events;

import com.infamousgc.loans.Data.PlayerData;
import com.infamousgc.loans.Data.Players;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = Players.get(player);

        if (!data.hasLoan()) return;

        if (data.isOverdue()) {
            player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.reminder) + " &7You currently have an " +
                    "outstanding loan of &c$" + Formatter.formatNumber(data.getBalance()) + "&7! For this reason, you " +
                    "will experience certain restrictions. See the warning in the top left of /loans to find out " +
                    "what these are!"));
        } else {
            data.alert();
        }
    }

}
