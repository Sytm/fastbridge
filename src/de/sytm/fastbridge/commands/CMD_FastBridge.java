package de.sytm.fastbridge.commands;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;

import de.sytm.fastbridge.FastBridge;
import de.sytm.fastbridge.manager.BuildLineManager;
import de.sytm.fastbridge.manager.ConfigManager;
import de.sytm.fastbridge.manager.Utils;

public class CMD_FastBridge implements CommandExecutor {

	private HashMap<Integer, Location> spawns = new HashMap<Integer, Location>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("fastbridge.admin")) {
				if (args.length == 0) {
					player.sendMessage(FastBridge.PREFIX + "§7FastBridge - Commands");
					player.sendMessage(" §7> §6/fastbridge setlobby §7| Setzt die Lobby");
					player.sendMessage(" §7> §6/fastbridge setspawn <0-9> §7| Setzt die Spawns");
					player.sendMessage(" §7> §6/fastbridge save §7| Speichert alles im Dateisystem ab");
					player.sendMessage(" §7> §6/fastbridge list §7| Zeigt alle BuildLines und deren Spieler an");
					player.sendMessage(
							" §7> §6/fastbridge spawnjoinmob §7| Spawnt einen Zombie, den man anclicken muss, um zu joinen");
					return true;
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("setlobby")) {
						ConfigManager.saveLobby(player.getLocation());
						player.sendMessage(FastBridge.PREFIX + "§7Die Lobby wurde erfolgreich gesetzt!");
						return true;
					}
					if (args[0].equalsIgnoreCase("list")) {
						BuildLineManager.sendAllBuildLines(player);
						return true;
					}
					if (args[0].equalsIgnoreCase("save")) {
						if (!(spawns.containsKey(0) && spawns.containsKey(1) && spawns.containsKey(2)
								&& spawns.containsKey(3) && spawns.containsKey(4) && spawns.containsKey(5)
								&& spawns.containsKey(6) && spawns.containsKey(7) && spawns.containsKey(8)
								&& spawns.containsKey(9))) {
							player.sendMessage(
									FastBridge.PREFIX + "§cDu musst alle 10 Spawns (0 - 9) zwischenspeichern!");
							return true;
						}
						ConfigManager.saveSpawns(spawns);
						BuildLineManager.load();
						player.sendMessage(FastBridge.PREFIX + "§7Die Spawns wurden erfolgreich gespeichert!");
						return true;
					}
					if (args[0].equalsIgnoreCase("spawnjoinmob")) {
						Cow c = player.getLocation().getWorld().spawn(player.getLocation(), Cow.class);
						c.setCustomName("§6Join");
						c.setCustomNameVisible(true);
						c.setAgeLock(true);
						c.setRemoveWhenFarAway(false);
						Utils.freezeEntity(c);
						player.sendMessage(FastBridge.PREFIX + "§7Der Joinmob wurde erfolgreich gespawnt");
						return true;
					}
				}
				if (args.length == 2 && args[0].equalsIgnoreCase("setspawn")) {
					int id = 0;
					try {
						id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						player.sendMessage(FastBridge.PREFIX + "§cBitte gebe eine gültige ID an!");
						return true;
					}
					spawns.put(id, player.getLocation());
					player.sendMessage(FastBridge.PREFIX + "§7Der Spawn wurde erfolgreich zwischengespeichert!");
					return true;
				}
				player.sendMessage(FastBridge.PREFIX + "§cBitte gebe einen gültigen Subcommand an!");
			} else {
				player.sendMessage(FastBridge.PREFIX + "§cDu hast für diesen Command keine Rechte!");
			}
		} else {
			sender.sendMessage(FastBridge.PREFIX + "§cDu musst ein Spieler sein um diesen Command auszuf§hren!");
		}
		return false;
	}
}
