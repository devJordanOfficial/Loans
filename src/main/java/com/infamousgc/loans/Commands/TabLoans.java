package com.infamousgc.loans.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabLoans implements TabCompleter {

    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (this.arguments.isEmpty()) {
            this.arguments.add("invite"); arguments.add("accept"); arguments.add("deny"); arguments.add("revoke");
        }

        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return Collections.emptyList();
    }
}
