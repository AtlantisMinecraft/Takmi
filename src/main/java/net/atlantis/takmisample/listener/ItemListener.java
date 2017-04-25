package net.atlantis.takmisample.listener;

import net.atlantis.takmisample.TakmiSample;
import net.atlantis.takmisample.effect.CircleEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    public ItemListener(TakmiSample plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.STICK) {
                CircleEffect circleEffect = new CircleEffect(player);
                circleEffect.draw();
            }
        }
    }


}
