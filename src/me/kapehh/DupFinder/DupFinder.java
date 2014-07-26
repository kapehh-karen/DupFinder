package me.kapehh.DupFinder;

import me.kapehh.main.pluginmanager.logger.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Karen on 27.07.2014.
 */
public class DupFinder extends JavaPlugin {
    private static PluginLogger pluginLogger;

    public static PluginLogger getPluginLogger() {
        return pluginLogger;
    }

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new FindDuperListener(), this);

        pluginLogger = new PluginLogger(this, "dupers");
        pluginLogger.setup();
        pluginLogger.getLog().info("* * * STARTED * * *");
    }

    @Override
    public void onDisable() {
        if (pluginLogger != null) {
            pluginLogger.shutDown();
        }
    }
}
