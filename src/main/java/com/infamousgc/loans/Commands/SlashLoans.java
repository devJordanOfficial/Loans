package com.infamousgc.loans.Commands;

import com.infamousgc.loans.Data.Action;
import com.infamousgc.loans.Data.ActionDataType;
import com.infamousgc.loans.Data.LoanType;
import com.infamousgc.loans.Data.Texture;
import com.infamousgc.loans.Interface.Main;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Error;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SlashLoans implements CommandExecutor {

    Loans plugin;

    public SlashLoans(Loans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Error.isConsole());
            return true;
        }

//        ((Player) sender).getInventory().addItem(shortTerm());

        new Main(plugin, (Player) sender);
        return true;
    }

//    private ItemStack shortTerm() {
//        ItemStack item = Texture.ONE.getHead();
//        ItemMeta meta = item.getItemMeta();
//
//        meta.setDisplayName(Formatter.gradient("&lShort Term"));
//
//        meta.addEnchant(Enchantment.DURABILITY, 1, false);
//        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//
//        List<String> lore = new ArrayList<String>();
//        lore.add(Formatter.parse(""));
//        lore.add(Formatter.parse("&fDeadline #005027&l» &724 hours (1 day)"));
//        lore.add(Formatter.parse("&fPeriod #005027&l» &71 hour"));
//        lore.add(Formatter.parse("&fInterest #005027&l» &70.4% Compound"));
//        lore.add(Formatter.parse(""));
//        lore.add(Formatter.parse("#00984b&lLMB &fto select this plan"));
//        meta.setLore(lore);
//
//        NamespacedKey key = new NamespacedKey(plugin, "action");
//        meta.getPersistentDataContainer().set(key, new ActionDataType(), Action.SHORT_TERM);
//
//        item.setItemMeta(meta);
//
//        return item;
//    }
}
