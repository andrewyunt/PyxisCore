package com.andrewyunt.pyxiscore;

import com.andrewyunt.pyxiscore.listeners.PlayerEventListener;
import com.andrewyunt.pyxiscore.listeners.TownyEventListener;
import com.andrewyunt.pyxiscore.menus.PathsMenuGUI;
import com.andrewyunt.pyxiscore.player.PlayerManager;
import com.andrewyunt.pyxiscore.player.PyxisPlayer;
import com.codingforcookies.armorequip.ArmorListener;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PyxisCore extends JavaPlugin {

	public static Economy economy = null;
	public static Permission perms = null;

	private static PyxisCore instance;

	private PlayerManager playerManagerInstance = new PlayerManager();
	private Logger logger = getLogger();
	private Server server = getServer();
	private PluginManager pm = server.getPluginManager();

	@Override
	public void onEnable() {
		
		instance = this;
		
		logger.info("Enabling PyxisCore... Please wait.");

		setupEconomy();
		setupPermissions();

		saveDefaultConfig();

		// Register events for Borlea ArmorEquipEvent
		pm.registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);

		// Register plugin specific events
		pm.registerEvents(new PlayerEventListener(), this);
		pm.registerEvents(new TownyEventListener(), this);

		server.addRecipe(Recipes.getIceLanceSpellRecipe());
		server.addRecipe(Recipes.getLightningSpellRecipe());
		server.addRecipe(Recipes.getFireballSpellRecipe());
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
	
	@Override
	public void onDisable() {
		
		logger.info("Disabling PyxisCore... Please wait.");
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
		} else if(cmd.getName().equalsIgnoreCase("bankdialog")) {
			new BankConversation().beginConversation(sender);
		}
		return true;
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

	public static PyxisCore getInstance() {

		return instance;
	}
}