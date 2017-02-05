package com.andrewyunt.pyxiscore.player;

import com.andrewyunt.pyxiscore.menus.PathsMenuGUI;

public class PyxisPlayer {
	
	private PathsMenuGUI pathsMenuInstance;
	private int lightningCooldown = 0, fireballCooldown = 0, icelanceCooldown = 0;
	private boolean isFrozen = false;
	
	public PathsMenuGUI getPathsMenu() {
		
		return pathsMenuInstance;
	}
	
	public void setPathsMenu(PathsMenuGUI pathsMenuInstance) {
		
		this.pathsMenuInstance = pathsMenuInstance;
	}
	
	public int getIcelanceCooldown() {
		
		return icelanceCooldown;
	}
	
	public int getFireballCooldown() {
		
		return fireballCooldown;
	}
	
	public int getLightningCooldown() {
		
		return lightningCooldown;
	}
	
	public void setIcelanceCooldown(int icelanceCooldown) {
		
		this.icelanceCooldown = icelanceCooldown;
	}
	
	public void setFireballCooldown(int fireballCooldown) {

		this.fireballCooldown = fireballCooldown;
	}
	
	public void setLightningCooldown(int lightningCooldown) {
		
		this.lightningCooldown = lightningCooldown;
	}
	
	public boolean isFrozen() {
		
		return isFrozen;
	}
	
	public void setFrozen(boolean isFrozen) {
		
		this.isFrozen = isFrozen;
	}
}