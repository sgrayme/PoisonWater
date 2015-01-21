package me.sgray.poisonwater;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWater extends JavaPlugin implements Listener {
    @Override
    public void onDisable() {}

    @Override
    public void onEnable() {
        getCommand("poisonwater").setExecutor(new PoisonWaterCommand(this));
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!activeWorlds().contains(e.getPlayer().getWorld().getName())) {
            return;
        }

        Location feet = e.getTo();
        Player player = e.getPlayer();
        if (feet.getBlock().getType().equals(Material.WATER) 
                || feet.getBlock().getType().equals(Material.STATIONARY_WATER)) {
            if (!player.hasPermission("poisonwater.ignore")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1), false);
            }
        }
    }

    private List<String> activeWorlds() {
    	return getConfig().getStringList("worlds");
    }
}
