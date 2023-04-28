package com.infamousgc.loans.Events;

import com.infamousgc.loans.Data.*;
import com.infamousgc.loans.Interface.NewLoan;
import com.infamousgc.loans.Interface.Payment;
import com.infamousgc.loans.Interface.SetPlan;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import com.infamousgc.loans.Utilities.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public class InterfaceListener implements Listener {

    private final Loans plugin;

    private boolean saveCache;

    public InterfaceListener(Loans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        ////////////////////////////////
        // GUI Checks
        ////////////////////////////////
        if (!plugin.user.containsKey(event.getWhoClicked().getUniqueId())) return;
        event.setCancelled(true);

        if (event.getClick() != ClickType.LEFT) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;

        NamespacedKey key = new NamespacedKey(plugin, "action");
        ItemStack item = event.getCurrentItem();
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        if (!container.has(key, new ActionDataType())) return;

        Player player = (Player) event.getWhoClicked();

        ////////////////////////////////
        // Create New Loan
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.NEW_LOAN) {
            player.closeInventory();
            new NewLoan(plugin, player);
        }

        ////////////////////////////////
        // Set Principal
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.SET_PRINCIPAL) {
            plugin.listen.add(player.getUniqueId());
            saveCache();
            player.closeInventory();
            player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.prefix) + " &7Type " +
                    "the amount you wish to take out as a loan. Type #008642cancel &7to cancel."));
        }

        ////////////////////////////////
        // Set Payment Plan
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.SET_PAYMENT) {
            saveCache();
            player.closeInventory();
            new SetPlan(plugin, player);
        }

        ////////////////////////////////
        // Select Short Term
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.SHORT_TERM) {
            Players.get(player).setCache(Plan.SHORT);
            ((SetPlan) plugin.user.get(player.getUniqueId())).update();
        }

        ////////////////////////////////
        // Select Long Term
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.LONG_TERM) {
            Players.get(player).setCache(Plan.LONG);
            ((SetPlan) plugin.user.get(player.getUniqueId())).update();
        }

        ////////////////////////////////
        // Confirm Payment Plan
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.PLAN_CONFIRM) {
            saveCache();
            player.closeInventory();
            new NewLoan(plugin, player);
        }

        ////////////////////////////////
        // Create Loan (Finalize)
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.CREATE_LOAN) {
            PlayerData data = Players.get(player);
            data.createLoan();
            player.closeInventory();
            plugin.getEconomy().depositPlayer(player, data.getPrincipal());
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
            player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.prefix) + " &7Your loan of #008642$" +
                    Formatter.formatNumber(data.getPrincipal()) + " &7has been deposited into your account. You will " +
                    "accrue interest every #008642" + data.getPlan().getPeriod() + " Hour" +
                    (data.getPlan().getPeriod() == 1 ? "" : "s") + "&7."));
        }

        ////////////////////////////////
        // Make Payment
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.PAY) {
            player.closeInventory();
            new Payment(plugin, player);
        }

        ////////////////////////////////
        // Confirm Payment
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.CONFIRM) {
            PlayerData data = Players.get(player);
            player.closeInventory();
            if (plugin.getEconomy().getBalance(player) < data.getBalance()) {
                player.playSound(player, Sound.ENTITY_ITEM_BREAK, SoundCategory.MASTER, 1, 1);
                player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.prefix) + " &7You do not have &c$" +
                        Formatter.formatNumber(data.getBalance()) + "&7! Your payment " +
                        "has been &ccancelled&7."));
            } else {
                Logger.log(player.getName() + " has made a loan payment of $" + Formatter.formatNumber(data.getBalance()));
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
                player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.prefix) + " &7Your payment " +
                        "has been &areceived&7. You are no longer in dept!"));
                plugin.getEconomy().withdrawPlayer(player, data.getBalance());
                data.pay();
            }
        }

        ////////////////////////////////
        // Cancel Payment
        ////////////////////////////////
        if (container.get(key, new ActionDataType()) == Action.CANCEL) {
            player.closeInventory();
            player.playSound(player, Sound.ENTITY_ITEM_BREAK, SoundCategory.MASTER, 1, 1);
            player.sendMessage(Formatter.parse(Formatter.gradient(Formatter.prefix) + " &7Your payment " +
                    "has been &ccancelled&7."));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (plugin.listen.contains(event.getPlayer().getUniqueId()))
            return;

        if (!saveCache)
            Players.get(event.getPlayer().getUniqueId()).resetCache();
        else
            saveCache = false;

        plugin.user.remove(event.getPlayer().getUniqueId());
    }

    private void saveCache() {
        saveCache = true;
    }

}
