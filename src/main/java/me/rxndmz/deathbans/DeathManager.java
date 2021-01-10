package me.rxndmz.deathbans;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeathManager implements Listener {

    private DeathBans plugin;

    public DeathManager(DeathBans plugin) {
        this.plugin = plugin;
    }

    private String addPlaceholderData(String s, Player player, String time, String date) {
        int x = plugin.getConfig().getInt("ban-time") + (plugin.getConfig().getInt("additional-ban-time") * (plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".deaths") + 1));
        String ss = s.replace("{player}", player.getName());
        String sss = ss.replace("{deaths_total}", this.plugin.data.getConfig().getString("players." + player.getUniqueId().toString() + ".deaths"));
        String ssss = sss.replace("{deaths_remaining}", String.valueOf(x));
        String sssss = ssss.replace("{time_of_unban}", time);
        return sssss.replace("{date_of_unban}", date);
    }

    private void annouceDeath(Player playerThatDied, boolean isPerm, String time, String date) {
        int deaths = this.plugin.data.getConfig().getInt("players." + playerThatDied.getUniqueId().toString() + ".deaths");
        if (isPerm) {
            if (deaths == this.plugin.getConfig().getInt("permanent-ban-after")) {
                String s = addPlaceholderData(this.plugin.getConfig().getString("broadcast-text-perm-banned"), playerThatDied, time, date);
                plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', s));
            } else {
                String s = addPlaceholderData(this.plugin.getConfig().getString("broadcast-text-perm"), playerThatDied, time, date);
                plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        } else {
            String s = addPlaceholderData(this.plugin.getConfig().getString("broadcast-text"), playerThatDied, time, date);
            plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public void addDeath(Player player) {
        int deaths = this.plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".deaths");
        this.plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".deaths", deaths + 1);
        this.plugin.data.saveConfig();
    }

    @EventHandler
    public void onEntityDeath(EntityDamageEvent e) {
        // Checking if the entity is a player
        if (e.getEntity() instanceof Player) {
            // Getting the player
            Player player = (Player) e.getEntity();
            // Checking if player is dead
            if (player.getHealth() - e.getFinalDamage() < 0) {
                // Player has Died //
                addDeath(player);
                String reason;
                String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);
                // Getting the date that the player will be banned
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                if (this.plugin.getConfig().getBoolean("enable-more-ban-time")) {
                    System.out.println(plugin.getConfig().getInt("ban-time") + (plugin.getConfig().getInt("additional-ban-time") * (plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".deaths") + 1)));
                    int x = plugin.getConfig().getInt("ban-time") + (plugin.getConfig().getInt("additional-ban-time") * (plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".deaths")));
                    cal.add(Calendar.SECOND, x);
                } else {
                    cal.add(Calendar.SECOND, plugin.getConfig().getInt("ban-time"));
                }
                // Setting up formats to display the date on
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String time = timeFormat.format(cal.getTime());
                String date = dateFormat.format(cal.getTime());
                if (this.plugin.getConfig().getBoolean("enable-permanent-bans") && this.plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".deaths") == this.plugin.getConfig().getInt("permanent-ban-after")) {
                    String s = addPlaceholderData(this.plugin.getConfig().getString("perm-ban-message"), player, time, date);
                    reason = bumper+ ChatColor.translateAlternateColorCodes('&', s) +bumper;
                    // Broadcasting the death to other players.
                    annouceDeath(player, true, time, date);
                    // Banning the player
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, null, null);
                    // Kicking them from the server
                    String finalReason = reason;
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.kickPlayer(finalReason);
                        }
                    }, 5L);
                } else {
                    String s = addPlaceholderData(this.plugin.getConfig().getString("ban-message"), player, time, date);
                    reason = bumper+ ChatColor.translateAlternateColorCodes('&', s) +bumper;
                    // Broadcasting the death to other players.
                    annouceDeath(player, false, time, date);
                    // Banning the player
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, cal.getTime(), null);
                    // Kicking them from the server
                    String finalReason = reason;
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.kickPlayer(finalReason);
                        }
                    }, 5L);
                }
            }
        }
    }

    /*
    Disables Default Death Messages
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }

}
