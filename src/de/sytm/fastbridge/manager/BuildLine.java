package de.sytm.fastbridge.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.sytm.fastbridge.FastBridge;

public class BuildLine {

	private Player currentuser;
	private Location start;
	private Long startstamp;
	private List<Block> placedblocks;

	public BuildLine(Location start) {
		this.start = start;
		this.placedblocks = new ArrayList<Block>();
	}

	public boolean isFree() {
		return currentuser == null;
	}

	public void join(Player player) {
		this.currentuser = player;
		player.teleport(start);
	}

	public void reset() {
		for (Block b : placedblocks) {
			b.breakNaturally(new ItemStack(Material.AIR));
		}
		if (currentuser != null)
			currentuser.teleport(ConfigManager.loadLobby());
		currentuser = null;
		startstamp = null;
		placedblocks.clear();
	}

	public final Long getStartstamp() {
		return startstamp;
	}

	public final Player getCurrentuser() {
		return currentuser;
	}

	public void onBlockPlace(Player player, Block block) {
		if (block.getType() != Material.SANDSTONE || this.currentuser.getUniqueId() != player.getUniqueId())
			return;
		if (this.currentuser.getUniqueId() == player.getUniqueId()) {
			if (startstamp == null) {
				startstamp = System.currentTimeMillis();
				FastBridge.EXECUTERPOOL.execute(() -> {
						startThread();
				});
			}
			placedblocks.add(block);
		}
	}

	public void onMove(Player player, Location tolocation) {
		if (this.currentuser.getUniqueId() == player.getUniqueId()) {
			if (tolocation.getWorld().getBlockAt(tolocation).getType() == Material.GOLD_PLATE) {
				if (startstamp == null) {
					player.kickPlayer(FastBridge.PREFIX + "\n§cSure Legit?");
					return;
				}
				long now = System.currentTimeMillis();
				player.sendMessage(FastBridge.PREFIX + "§7Du hast das Ziel erreicht! Zeit: "
						+ DateUtils.toSecs(now - startstamp) + " §7Fails: §6" + BuildLineManager.getFails(player));
				BuildLineManager.saveStats(player, now - startstamp, BuildLineManager.getFails(player));
				BuildLineManager.leave(player);
			}
			if (tolocation.getBlockY() <= 110) {
				BuildLineManager.rejoin(player);
			}
		}
	}

	private void startThread() {
		while (true) {
			if (startstamp == null)
				return;
			if (currentuser == null) {
				reset();
				return;
			}
			Utils.sendActionbar(currentuser,
					"§7Deine Zeit: " + DateUtils.toSecs(System.currentTimeMillis() - startstamp));
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
