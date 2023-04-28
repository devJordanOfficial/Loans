package com.infamousgc.loans.Interface;

import com.infamousgc.loans.Data.*;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Main extends GUI {

    private PlayerData playerData;

    public Main(Loans plugin, Player player) {
        super(plugin, player);
    }

    @Override
    protected void create() {
        inv = Bukkit.createInventory(null, 27, Formatter.gradient("&lLoans"));
        playerData = Players.get(player);
        initialize();
    }

    @Override
    protected void initialize() {
        if (playerData.hasLoan()) {
            inv.setItem(13, activeLoan());
            fillIndicator();
        } else
            inv.setItem(13, noLoan());

        inv.setItem(0, warning());
        inv.setItem(8, info());
        fillSpace();
    }

    private ItemStack noLoan() {
        ItemStack item = Texture.MONEY_BAG.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("-=- &lYou have no active loan&r -=-"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fWithdraw Limit #005027&l» &7$" + Players.get(player).getFormattedLimit()));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("#00984b&lLMB &fto take out a loan"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.NEW_LOAN);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack activeLoan() {
        ItemStack item = Texture.MONEY_BAG.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.gradient("-=- &lYou have an active loan&r -=-"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&fRemaining Balance #005027&l» &7$" + Formatter.formatNumber(playerData.getBalance())));
        if (!playerData.isOverdue())
            lore.add(Formatter.parse("&fNext Period #005027&l» &7In " + playerData.getNextPeriod()));
        lore.add(Formatter.parse("&fPayment Due #005027&l» &7" + playerData.getDue()));
        lore.add(Formatter.parse(""));
        if (playerData.isOverdue()) {
            lore.add(Formatter.parse("&fPrincipal Amount #005027&l» &7$" + Formatter.formatNumber(playerData.getPrincipal())));
            lore.add(Formatter.parse("&fInterest Accrued #005027&l» &7$" + Formatter.formatNumber(playerData.getBalance() - playerData.getPrincipal())));
            lore.add(Formatter.parse("&fInterest #005027&l» &7" + (playerData.getPlan().getRate() * 100)) + "%");
            lore.add(Formatter.parse("&fCompound Period #005027&l» &7" + playerData.getPlan().getPeriod() + " Hour" + (playerData.getPlan().getPeriod() == 1 ? "" : "s")));
            lore.add(Formatter.parse(""));
        }
        lore.add(Formatter.parse("#00984b&lLMB &fto make a payment"));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, "action");
        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.PAY);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack info() {
        ItemStack item = Texture.INFO.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&f&lInfo"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&f&lDescription&7 »"));
        lore.add(Formatter.parse("&7Loans provide a way ot instantly getting access to money."));
        lore.add(Formatter.parse("&7The catch is that this money must be paid back and interest"));
        lore.add(Formatter.parse("&7will be charged. If the loan is not paid back on time"));
        lore.add(Formatter.parse("&7restrictions will be put into place until paid back."));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&f&lTerms&7 »"));
        lore.add(Formatter.parse("&7» &lPrincipal Amount&7 is the initial lone amount"));
        lore.add(Formatter.parse("&7» &lLoan Balance&7 is the total amount to pay back"));
        lore.add(Formatter.parse("&7» &lPeriod&7 is how often interest will be applied"));
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack warning() {
        ItemStack item = Texture.WARNING.getHead();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Formatter.parse("&f&lRestrictions"));

        List<String> lore = new ArrayList<String>();
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&f&lDescription&7 »"));
        lore.add(Formatter.parse("&7When a player fails to pay off their loan by the end of the"));
        lore.add(Formatter.parse("&7due date restrictions will be implemented until the loan"));
        lore.add(Formatter.parse("&7balance is paid."));
        lore.add(Formatter.parse(""));
        lore.add(Formatter.parse("&f&lRestrictions&7 »"));
        lore.add(Formatter.parse("&7» 80% of Jobs income will be taken and placed towards the loan"));
        lore.add(Formatter.parse("&7» Access will be lost to the server shop"));
        lore.add(Formatter.parse("&7» Access will be lost to chest shops"));
        lore.add(Formatter.parse("&7» Access will be lost to the auction house"));
        lore.add(Formatter.parse("&7» Access will be lost to withdrawing money and xp"));
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    private void fillIndicator() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Formatter.parse("&r"));
        item.setItemMeta(meta);

        if (playerData.getPercentUntilDue() >= 0.33 && playerData.getPercentUntilDue() < 0.66)
            item.setType(Material.YELLOW_STAINED_GLASS_PANE);
        else if (playerData.getPercentUntilDue() < 0.33)
            item.setType(Material.RED_STAINED_GLASS_PANE);

        inv.setItem(4, item);
        inv.setItem(12, item);
        inv.setItem(14, item);
        inv.setItem(22, item);
    }

}
