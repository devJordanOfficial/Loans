package com.infamousgc.loans.Commands;

import com.infamousgc.loans.Interface.Menu;
import com.infamousgc.loans.Loans;
import com.infamousgc.loans.Utilities.Error;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(Error.isConsole());
            return true;
        }

        new Menu().open((Player) sender);
        return true;
    }
}
