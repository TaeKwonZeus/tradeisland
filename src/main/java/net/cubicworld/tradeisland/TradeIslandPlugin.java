package net.cubicworld.tradeisland;

import lombok.Getter;
import net.cubicworld.tradeisland.trade.TradeManager;
import net.cubicworld.tradeisland.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

@Getter
public class TradeIslandPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        TradeManager tradeManager = new TradeManager(this);

        Bukkit.getPluginManager().registerEvents(tradeManager, this);
        Objects.requireNonNull(getCommand("trade")).setExecutor(tradeManager);

        Message.setBundle(ResourceBundle.getBundle("labels", new Locale("ru", "RU")));

        Bukkit.getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Plugin disabled!");
    }
}
