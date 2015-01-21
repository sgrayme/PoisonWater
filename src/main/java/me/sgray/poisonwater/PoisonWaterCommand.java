package me.sgray.poisonwater;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PoisonWaterCommand implements CommandExecutor {
    PoisonWater plugin;

    public PoisonWaterCommand(PoisonWater plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("poisonwater")) {
            return false;
        }

        if (!sender.hasPermission("poisonwater.admin")) {
            return false;
        }

        switch (args.length) {
            case 0:
                sender.sendMessage(plugin.getDescription().getName() + "" + plugin.getDescription().getVersion());
                break;
            case 1:
                if (args[0].equals("reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage("Config reloaded for " + plugin.getDescription().getName());
                    break;
                }
            default:
                sender.sendMessage("Invalid command for " + plugin.getDescription().getName());
        }

        return true;
    }
}
