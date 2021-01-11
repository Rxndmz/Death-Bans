package me.rxndmz.deathbans.commands;

import me.rxndmz.deathbans.DeathBans;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TokenUse implements CommandExecutor {

    private DeathBans plugin;

    public TokenUse(DeathBans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this command in the console!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lERROR: Wrong usage of command!"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /dbrevive {player}"));
            return true;
        } else {
            OfflinePlayer playerToRevive = this.plugin.getServer().getOfflinePlayer(args[0]);
            System.out.println(playerToRevive.getName());
            int tokens = this.plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".tokens");
            if (tokens == 0 || args[0].length() < 3 || args[0] == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lERROR: Wrong usage of command!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /dbrevive {player}"));
                return true;
            }
            if (!playerToRevive.isBanned()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dPlayer isn't dead!"));
                return true;
            }
            tokens = tokens - 1;
            this.plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".tokens", tokens);
            this.plugin.data.saveConfig();
            Bukkit.getBanList(BanList.Type.NAME).pardon(playerToRevive.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d" + playerToRevive.getName() + " has been revived! you now have " + tokens + " revive tokens."));
        }
        return false;
    }

}
