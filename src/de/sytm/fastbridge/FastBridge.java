package de.sytm.fastbridge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import de.sytm.fastbridge.commands.CMD_FastBridge;
import de.sytm.fastbridge.listener.PlayerListener;
import de.sytm.fastbridge.manager.BuildLineManager;
import de.sytm.fastbridge.manager.ScoreBoardManager;

public class FastBridge extends JavaPlugin {

	public static final String PLUGINPATH = "plugins/FastBrdige/";
	public static final String PREFIX = "§8[§6FastBridge§8] ";
	public static final ExecutorService EXECUTERPOOL = Executors.newCachedThreadPool();
	public static final Scoreboard SCOREBOARD = Bukkit.getScoreboardManager().getNewScoreboard();

	@Override
	public void onEnable() {
		this.getCommand("fastbridge").setExecutor(new CMD_FastBridge());
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		BuildLineManager.load();
		ScoreBoardManager.setScoreboard();
	}
}