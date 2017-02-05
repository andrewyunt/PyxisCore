package com.andrewyunt.pyxiscore.player;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
	
	private Map<Player, PyxisPlayer> players = new HashMap<Player, PyxisPlayer>();
	
	public void addPlayer(Player player) {
		
		players.put(player, new PyxisPlayer());
	}
	
	public PyxisPlayer getPlayer(Player player) {
		
		return players.get(player);
	}
}