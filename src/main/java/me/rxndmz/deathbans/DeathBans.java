package me.rxndmz.deathbans;

import me.rxndmz.deathbans.commands.TokenBalance;
import me.rxndmz.deathbans.commands.TokenUse;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class DeathBans extends JavaPlugin {

    public PluginManager pm = getServer().getPluginManager();
    public Logger logger = getLogger();
    public DataManager data;

    @Override
    public void onEnable() {
        /*
        Generating config.yml
         */
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Starting DataManager
        this.data = new DataManager(this);

        /*
        Commands
         */
        this.getCommand("dbtokens").setExecutor(new TokenBalance(this));
        this.getCommand("dbrevive").setExecutor(new TokenUse(this));

        /*
        Registering Events
         */
        pm.registerEvents(new DeathManager(this), this);
        pm.registerEvents(this.data, this);
        pm.registerEvents(new ReviveHandler(this), this);

        // Adding revive recipe to server
        Bukkit.addRecipe(new ReviveHandler(this).getRecipe());
    }

    @Override
    public void onDisable() {
        this.data.saveConfig();
        saveDefaultConfig();
    }
}
