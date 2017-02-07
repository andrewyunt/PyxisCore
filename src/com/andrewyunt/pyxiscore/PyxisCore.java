package com.andrewyunt.pyxiscore;

import com.andrewyunt.pyxiscore.listeners.PlayerEventListener;
import com.andrewyunt.pyxiscore.listeners.TownyEventListener;
import com.andrewyunt.pyxiscore.listeners.WorldGuardEventListener;
import com.andrewyunt.pyxiscore.menus.PathsMenuGUI;
import com.andrewyunt.pyxiscore.player.PlayerManager;
import com.andrewyunt.pyxiscore.player.PyxisPlayer;
import com.codingforcookies.armorequip.ArmorListener;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PyxisCore extends JavaPlugin {

	private static PyxisCore instance;
	public static Economy economy = null;
	public static Permission perms = null;

	private PlayerManager playerManagerInstance = new PlayerManager();

	@Override
	public void onEnable() {
		
		instance = this;
		
		// Set up Vault
		setupEconomy();
		setupPermissions();
		
		// Save default configuration
		saveDefaultConfig();
		
		// Register events for Borlea ArmorEquipEvent
		getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
		
		// Register plugin specific events
		getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
		getServer().getPluginManager().registerEvents(new TownyEventListener(), this);
		getServer().getPluginManager().registerEvents(new WorldGuardEventListener(), this);
		
		getServer().addRecipe(Recipes.getIceLanceSpellRecipe());
		getServer().addRecipe(Recipes.getLightningSpellRecipe());
		getServer().addRecipe(Recipes.getFireballSpellRecipe());
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("Disabling PyxisCore... Please wait.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Sorry, that command may only be executed by players.");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("paths")) {
			PyxisPlayer player = playerManagerInstance.getPlayer((Player) sender);
			
			player.setPathsMenu(new PathsMenuGUI((Player) sender));
			player.getPathsMenu().open();
		} else if(cmd.getName().equalsIgnoreCase("bankdialog"))
			new BankConversation().beginConversation(sender);
			
		return true;
	}
	
	public static PyxisCore getInstance() {
		
		return instance;
	}
	
	public PlayerManager getPlayerManagerInstance() {

		return playerManagerInstance;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		
		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
			return null; // Maybe you want throw an exception instead
		
		return (WorldGuardPlugin) plugin;
	}
	
	private boolean setupEconomy() {
		
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		
		if(economyProvider != null)
			economy = economyProvider.getProvider();
		
		return (economy != null);
	}

	private boolean setupPermissions() {
		
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		
		return perms != null;
	}
}