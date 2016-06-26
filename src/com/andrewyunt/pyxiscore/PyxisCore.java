package com.andrewyunt.pyxiscore;

import com.andrewyunt.pyxiscore.listeners.PlayerEventListener;
import com.andrewyunt.pyxiscore.listeners.TownyEventListener;
import com.andrewyunt.pyxiscore.menus.PathsMenuGUI;
import com.andrewyunt.pyxiscore.player.PlayerManager;
import com.andrewyunt.pyxiscore.player.PyxisPlayer;
import com.codingforcookies.armorequip.ArmorListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PyxisCore extends JavaPlugin {
	
	public static PyxisCore plugin;
	
	public static Economy economy = null;
	public static Permission perms = null;
	public PlayerManager playerManagerInstance = new PlayerManager();
	public FileConfiguration config = getConfig();
	private Logger logger = getLogger();
	public Server server = getServer();
	public PluginManager pm = server.getPluginManager();

	@Override
	public void onEnable() {
		
		plugin = this;
		
		logger.info("Enabling PyxisCore... Please wait.");

		setupEconomy();
		setupPermissions();

		saveDefaultConfig();

		// Register events for Borlea ArmorEquipEvent
		pm.registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);

		// Register plugin specific events
		pm.registerEvents(new PlayerEventListener(), this);
		pm.registerEvents(new TownyEventListener(), this);

		server.addRecipe(Recipes.getMageSpellRecipe());
		server.addRecipe(Recipes.getAcolyteSpellRecipeCoal());
		server.addRecipe(Recipes.getAcolyteSpellRecipeCharcoal());
		server.addRecipe(Recipes.getWizardSpellRecipe());
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
}