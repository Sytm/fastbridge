package de.sytm.fastbridge.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.sytm.fastbridge.FastBridge;

public class ConfigManager {

	private static final File file = new File(FastBridge.PLUGINPATH + "config.yml");

	public static void saveSpawns(HashMap<Integer, Location> spawns) {
		for (int i : spawns.keySet()) {
			saveLocation("spawns." + i, spawns.get(i));
		}
	}

	public static Map<Integer, BuildLine> loadBuildLines() {
		Map<Integer, BuildLine> res = new HashMap<>();
		for (int i : new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }) {
			res.put(i, new BuildLine(loadLocation("spawns." + i)));
		}
		return res;
	}

	public static void saveLobby(Location location) {
		saveLocation("lobby", location);
	}

	public static Location loadLobby() {
		return loadLocation("lobby");
	}

	private static boolean saveLocation(String path, Location location) {
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		data.set(path + ".X", location.getX());
		data.set(path + ".Y", location.getY());
		data.set(path + ".Z", location.getZ());
		data.set(path + ".Yaw", location.getYaw());
		data.set(path + ".Pitch", location.getPitch());
		data.set(path + ".World", location.getWorld().getName());
		try {
			data.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static Location loadLocation(String path) {
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		double x = data.getDouble(path + ".X"), y = data.getDouble(path + ".Y"), z = data.getDouble(path + ".Z");
		float yaw = (int) data.getInt(path + ".Yaw"), pitch = (int) data.getInt(path + ".Pitch");
		World world = Bukkit.getWorld(data.getString(path + ".World"));
		return new Location(world, x, y, z, yaw, pitch);
	}
}
