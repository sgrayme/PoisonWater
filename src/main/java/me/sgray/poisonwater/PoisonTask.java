package me.sgray.poisonwater;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonTask implements Runnable {
    PoisonWater plugin;

    public PoisonTask(PoisonWater plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (UUID uuid : plugin.getAffected()) {
                    if (!plugin.getServer().getPlayer(uuid).isOnline()) {
                        plugin.removeAffected(uuid);
                    } else {
                        affectPlayer(plugin.getServer().getPlayer(uuid));
                    }
                }
            }});
    }

    protected void affectPlayer(Player player) {
        if (!player.hasPermission("poisonwater.ignore")) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.getByName(potionType()),
                    potionDuration(),
                    potionMultiplier()
                    ), false);
        }
    }

    private String potionType() {
        return plugin.getConfig().getString("effect.type");
    }

    private int potionDuration() {
        return (plugin.getConfig().getInt("effect.duration") * 20);
    }

    private int potionMultiplier() {
        return plugin.getConfig().getInt("effect.amplifier");
    }

}
