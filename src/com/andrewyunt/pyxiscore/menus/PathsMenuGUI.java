package com.andrewyunt.pyxiscore.menus;

import com.andrewyunt.pyxiscore.PyxisCore;
import com.andrewyunt.pyxiscore.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PathsMenuGUI {

    private Inventory inv;

    private Player player;

    public PathsMenuGUI(Player player) {

        this.player = player;

        inv = Bukkit.createInventory(null, PyxisCore.plugin.config.getInt("paths_menu_size"), ChatColor.BLACK + "Choose Your Paths");

        ConfigurationSection section = PyxisCore.plugin.config.getConfigurationSection("Paths");

        for(String path : section.getKeys(false)) {
            ItemStack is = new ItemStack(Material.getMaterial(section.getConfigurationSection(path).getString("icon.material")), 1,
                    (short) section.getConfigurationSection(path).getInt("icon.data"));

            ItemMeta meta = is.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getConfigurationSection(path).getString("icon.name")));
            meta.setLore(Utils.colorizeStringList(section.getConfigurationSection(path).getStringList("icon.lore")));

            is.setItemMeta(meta);

            is = Utils.addGlow(is); // Item will not glow unless it has a meta

            inv.setItem(section.getConfigurationSection(path).getInt("icon.slot") - 1, is);
        }
    }

    public void open() {

        player.openInventory(inv);
    }

    public void close() {

        player.closeInventory();
    }

    public void selectedPath(String pathName) {

        if(PyxisCore.perms.playerInGroup(player, pathName)) {
            player.sendMessage(ChatColor.GRAY + "You currently own that path and cannot purchase it again.");
            return;
        }

        String requiredPathName = PyxisCore.plugin.config.getString("Paths." + pathName + ".required_group");

        if(!(requiredPathName == null) && !(requiredPathName.equals("")))
            if(!(PyxisCore.perms.playerInGroup(player, requiredPathName))) {
                player.sendMessage(ChatColor.GRAY + "You must unlock the " + ChatColor.BLUE + requiredPathName + ChatColor.GRAY + " path before purchasing this path.");
                return;
            }

        if(PyxisCore.economy.getBalance(player) < PyxisCore.plugin.config.getDouble("Paths." + pathName + ".price")) {
            player.sendMessage(ChatColor.GRAY + "You do not have enough PYX to afford that path.");
            return;
        }

        PyxisCore.perms.playerAddGroup(player, pathName);
        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Pyxis" + ChatColor.GRAY + "Paths" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "You have successfully purchased the " +
                ChatColor.BLUE + pathName + ChatColor.GRAY + " path.");
    }
}