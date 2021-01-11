package me.rxndmz.deathbans.commands;

import me.rxndmz.deathbans.DeathBans;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TokenBalance implements CommandExecutor {

    private DeathBans plugin;

    public TokenBalance(DeathBans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this command in the console!");
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            int tokens = this.plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".tokens");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dYou currently have " + tokens + " revive tokens!"));
        }
        return false;
    }

}
