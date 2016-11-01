package de.sytm.fastbridge.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import de.sytm.fastbridge.FastBridge;

public class ScoreBoardManager {

	public static void setScoreboard() {
		Objective objective = FastBridge.SCOREBOARD.registerNewObjective("fails", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("§3Fails:");
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (BuildLineManager.getFails(p) == null)
				continue;
			Score score = objective.getScore(p.getDisplayName());
			score.setScore(BuildLineManager.getFails(p));
		}
		for (Player p : Bukkit.getOnlinePlayers())
			p.setScoreboard(FastBridge.SCOREBOARD);
	}

	public static void updateScoreboeard() {
		Objective objective = FastBridge.SCOREBOARD.getObjective("fails");
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (BuildLineManager.getFails(p) == null) {
				objective.getScoreboard().resetScores(p.getDisplayName());
				continue;
			}
			objective.getScore(p.getDisplayName()).setScore(BuildLineManager.getFails(p));
			p.setScoreboard(FastBridge.SCOREBOARD);
		}
		for (Player p : Bukkit.getOnlinePlayers())
			p.setScoreboard(FastBridge.SCOREBOARD);
	}
}
