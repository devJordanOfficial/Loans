package com.infamousgc.loans.Commands;

import com.infamousgc.loans.Interface.Main;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Error;
import com.infamousgc.loans.Utilities.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlashLoans implements CommandExecutor {

    Loans plugin;

    public SlashLoans(Loans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("loans.use")) {
            sender.sendMessage(Formatter.parse("&c&lOops! &7You do not have the permission &cloans.use&7!"));
            return true;
        }

        Player player;

        if (args.length == 1) {
            if (!sender.hasPermission("loans.admin")) {
                sender.sendMessage(Formatter.parse("&c&lOops! &7You do not have the permission &cloans.admin&7!"));
                return true;
            }
            player = Bukkit.getPlayer(args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Error.isConsole());
                return true;
            }
            player = (Player) sender;
        }

        new Main(plugin, player);
        return true;
    }
}
