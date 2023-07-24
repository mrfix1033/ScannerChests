import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class main extends JavaPlugin implements CommandExecutor, Listener {
    public static main plugin;
    public static int i = 0;
    public static Set<Location> locset = new HashSet<>();
    public static List<Location> loclist = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("scan").setExecutor(new scan());
        getCommand("next").setExecutor(new next());
        getLogger().info("Плагин ScannerChests включен");
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин ScannerChests выключен");
    }

    public main getPlugin() {
        return plugin;
    }
}
