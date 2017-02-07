package com.andrewyunt.pyxiscore.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.andrewyunt.pyxiscore.utilities.Utils;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardEventListener implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		
		World world = event.getBlock().getLocation().getWorld();
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		ProtectedRegion region = regionManager.getRegion("saplingplace");
		
		if (region == null)
			return;
		
		BlockVector minPt = region.getMinimumPoint();
		BlockVector maxPt = region.getMaximumPoint();
		
		Location minLoc = new Location(world, minPt.getX(), minPt.getY(), minPt.getZ());
		Location maxLoc = new Location(world, maxPt.getX(), maxPt.getY(), maxPt.getZ());
		
		if (!Utils.getBlocksBetweenTwoPoints(minLoc, maxLoc).contains(event.getBlock()))
			return;
		
		if (event.getBlock().getType() != Material.SAPLING)
			event.setCancelled(true);
	}
}