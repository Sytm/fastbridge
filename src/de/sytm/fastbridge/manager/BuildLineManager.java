package de.sytm.fastbridge.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.sytm.fastbridge.FastBridge;

public class BuildLineManager {

	private static Map<Integer, BuildLine> bls;
	private static Map<Player, Integer> fails = new HashMap<Player, Integer>();
	private static List<Player> playing = new ArrayList<Player>();

	public static void addFail(Player player) {
		fails.put(player, fails.get(player) + 1);
		ScoreBoardManager.updateScoreboeard();
	}
	
	public static void setupFails(Player player) {
		fails.put(player, 0);
		ScoreBoardManager.updateScoreboeard();
	}
	
	public static Integer getFails(Player player) {
		return fails.containsKey(player) ? fails.get(player) : null;
	}
	
	public static void clearFails(Player player) {
		fails.remove(player);
		ScoreBoardManager.updateScoreboeard();
	}
	
	public static void load() {
		bls = ConfigManager.loadBuildLines();
	}

	public static boolean isPlaying(Player player) {
		return playing.contains(player);
	}

	public static void join(Player player) {
		if (isPlaying(player))
			return;
		for (int i : bls.keySet())
			if (bls.get(i).isFree()) {
				bls.get(i).join(player);
				setupFails(player);
				setupInventory(player);
				player.sendMessage(FastBridge.PREFIX + "§7Du wurdest in den Übungsreihe #" + i + " gemoved!");
				playing.add(player);
				return;
			}
		player.sendMessage(FastBridge.PREFIX + "§cZur Zeit sind keine Übungsreihen frei!");
	}

	public static void rejoin(Player player) {
		leave(player, false);
		for (int i : bls.keySet())
			if (bls.get(i).isFree()) {
				bls.get(i).join(player);
				setupInventory(player);
				addFail(player);
				playing.add(player);
				return;
			}
	}
	
	public static void onMove(Player player, Location toloc) {
		for (int i : bls.keySet())
			if (bls.get(i).getCurrentuser() == player) {
				bls.get(i).onMove(player, toloc);
				return;
			}
	}

	public static void leave(Player player, boolean b) {
		if (b)
			clearFails(player);
		playing.remove(player);
		player.getInventory().clear();
		for (int i : bls.keySet())
			if (bls.get(i).getCurrentuser() == player)
				bls.get(i).reset();
	}
	
	public static void leave(Player player) {
		leave(player, true);
	}
	
	public static void setupInventory(Player player) {
		Inventory inv = player.getInventory();
		inv.clear();
		ItemStack stack = new ItemStack(Material.SANDSTONE, 64);
		for (int i = 0; i <=8; i++) {
			inv.setItem(i, stack);
		}
		player.updateInventory();
	}

	public static void onPlace(Player player, Block block) {
		for (int i : bls.keySet())
			if (bls.get(i).getCurrentuser() == player) {
				bls.get(i).onBlockPlace(player, block);
				return;
			}
	}

	public static void sendAllBuildLines(Player player) {
		player.sendMessage(FastBridge.PREFIX + "§7FastBridge - Alle BuildLines");
		for (int i : bls.keySet()) {
			player.sendMessage(" §7#" + i + " User: "
					+ (bls.get(i).isFree() ? "§6-----" : "§c" + bls.get(i).getCurrentuser().getDisplayName()));
		}
	}
	
	public static void saveStats(Player player, long time, int fails) {
		/* At this point you CAN save the stats */
	}
}
