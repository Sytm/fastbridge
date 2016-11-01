package de.sytm.fastbridge.listener;

import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.sytm.fastbridge.manager.BuildLineManager;
import de.sytm.fastbridge.manager.ConfigManager;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().teleport(ConfigManager.loadLobby());
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Material mat = event.getClickedBlock() == null ? null : event.getClickedBlock().getType();
		if (mat != null) {
			if (mat == Material.DOUBLE_PLANT || mat == Material.LONG_GRASS) {
				event.setCancelled(true);
				return;
			}
			Material ba = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType();
			if (ba == Material.DOUBLE_PLANT || ba == Material.LONG_GRASS || ba == Material.WATER || ba == Material.STATIONARY_WATER || ba == Material.LAVA || ba == Material.STATIONARY_LAVA) {
				event.setCancelled(true);
			}

		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		if (!BuildLineManager.isPlaying((Player) event.getWhoClicked())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (BuildLineManager.isPlaying(event.getPlayer()))
			BuildLineManager.leave(event.getPlayer());
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof Cow) {
			event.setCancelled(true);
			BuildLineManager.join(event.getPlayer());
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (BuildLineManager.isPlaying(event.getPlayer()))
			BuildLineManager.onMove(event.getPlayer(), event.getTo());
	}

	@EventHandler
	public void onBPlace(BlockPlaceEvent event) {
		if (BuildLineManager.isPlaying(event.getPlayer()))
			BuildLineManager.onPlace(event.getPlayer(), event.getBlock());
		else {
			event.setBuild(false);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}
}