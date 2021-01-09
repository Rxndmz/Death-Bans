package me.rxndmz.deathbans;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager implements Listener {

    private DeathBans plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(DeathBans plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "playerData.yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("playerData.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();
        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;
        try {
            this.getConfig().save(this.configFile);
            this.plugin.logger.log(Level.INFO, "Successfully Saved Config!");
        } catch (Exception e) {
            plugin.logger.log(Level.SEVERE, "Couldn't Save Config To " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "playerData.yml");
        if (!this.configFile.exists()) {
            this.plugin.saveResource("playerData.yml", false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        int deaths = 0;

        if (this.plugin.data.getConfig().contains("players." + player.getUniqueId().toString() + ".deaths"))
            deaths = this.plugin.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".deaths");
        if (deaths != 0)
            this.plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".deaths", deaths + 1);
        this.plugin.data.getConfig().set("players." + player.getUniqueId().toString() + ".deaths", deaths);
        this.plugin.data.saveConfig();
    }

}
