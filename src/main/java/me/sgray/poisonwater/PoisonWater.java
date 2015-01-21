package me.sgray.poisonwater;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.sgray.poisonwater.events.EnterWaterEvent;
import me.sgray.poisonwater.events.LeaveWaterEvent;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PoisonWater extends JavaPlugin implements Listener {
    private Set<UUID> affectedPlayers;
    private int taskId = 0;

    @Override
    public void onDisable() {
        disablePoisontask();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        affectedPlayers = new HashSet<UUID>();
        getCommand("poisonwater").setExecutor(new PoisonWaterCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
        enablePoisonTask();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!activeWorlds().contains(e.getPlayer().getWorld().getName())) {
            return;
        }

        if (inWater(e.getFrom()) && !inWater(e.getTo())) {
            LeaveWaterEvent leaveWater = new LeaveWaterEvent(e.getPlayer());
            getServer().getPluginManager().callEvent(leaveWater);
        }

        if (inWater(e.getTo()) && !affectedPlayers.contains(e.getPlayer().getUniqueId())) {
            EnterWaterEvent enterWater = new EnterWaterEvent(e.getPlayer());
            getServer().getPluginManager().callEvent(enterWater);
        }
    }

    @EventHandler
    public void onEnterWater(EnterWaterEvent e) {
        if (affectedPlayers.contains(e.getPlayer().getUniqueId())) {
            return;
        }
        addAffected(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeaveWater(LeaveWaterEvent e) {
        if (affectedPlayers.contains(e.getPlayer().getUniqueId())) {
            removeAffected(e.getPlayer().getUniqueId());
        }
    }

    protected Set<UUID> getAffected() {
        return this.affectedPlayers;
    }

    protected void addAffected(UUID uuid) {
        affectedPlayers.add(uuid);
    }

    protected void removeAffected(UUID uuid) {
        affectedPlayers.remove(uuid);
    }

    private void enablePoisonTask() {
       this.taskId = getServer().getScheduler().runTaskTimerAsynchronously(
               this,
               new PoisonTask(this),
               20L,
               20L).getTaskId();
    }

    private void disablePoisontask() {
        if (this.taskId > 0) {
            getServer().getScheduler().cancelTask(this.taskId);
            this.taskId = 0;
        }
    }

    private boolean inWater(Location loc) {
        Material mat = loc.getBlock().getType();
        if (mat.equals(Material.WATER) || mat.equals(Material.STATIONARY_WATER)) {
            return true;
        }
        return false;
    }

    private List<String> activeWorlds() {
        return getConfig().getStringList("worlds");
    }
}
