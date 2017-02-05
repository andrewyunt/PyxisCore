package com.andrewyunt.pyxiscore.listeners;

import com.andrewyunt.pyxiscore.PyxisCore;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.TownRankAddEvent;
import com.palmergames.bukkit.towny.war.flagwar.events.CellAttackEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyEventListener implements Listener {
	
	@EventHandler
	public void onTownRankAdd(TownRankAddEvent event) {
		
		if (!(event.getRankName().equals("assistant")))
			return;
		
		Player targetPlayer = PyxisCore.getInstance().getServer().getPlayer(event.getResident().getName());
		Player senderPlayer = PyxisCore.getInstance().getServer().getPlayer(event.getSenderResident().getName());
		
		if (!(PyxisCore.perms.playerInGroup(targetPlayer, "Politician"))) {
			TownyMessaging.sendErrorMsg(senderPlayer, "The resident " + targetPlayer.getName()
					+ " does not have the politician path and may not hold the town assistant rank.");
			return;
		}
	}

	@EventHandler
	public void onCellAttackEvent(CellAttackEvent event) {
		
		Player player = event.getPlayer();
		
		if (PyxisCore.perms.playerInGroup(player, "Warrior"))
			return;
		
		event.setCancelled(true);
		TownyMessaging.sendErrorMsg(player, "You must have the Warrior path to place a war flag in an enemy town.");
	}
}