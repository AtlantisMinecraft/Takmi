package net.atlantis.takmisample;

import net.atlantis.takmisample.listener.ItemListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TakmiSample extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new ItemListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
