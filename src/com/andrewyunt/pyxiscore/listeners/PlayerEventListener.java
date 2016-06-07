package com.andrewyunt.pyxiscore.listeners;

import com.andrewyunt.pyxiscore.PyxisCore;
import com.andrewyunt.pyxiscore.utilities.Utils;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEventListener implements Listener {

    /**
     * Method is used for checking if a player is in the nether and preventing
     * them from placing spawners if they do not have the permission node required.
     * @param event BlockPlaceEvent
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        Block block = event.getBlock();

        if(block.getType().equals(Material.MOB_SPAWNER)) {
            if (!player.getWorld().getEnvironment().equals(Environment.NETHER))
                return;

            if (player.hasPermission("netherspawner.place"))
                return;

            event.setCancelled(true); // Cancel the block place event
            player.sendMessage(ChatColor.RED + "You are not allowed to place spawners in the Nether.");
        } else if(block.getType() == Material.CROPS || block.getType() == Material.CARROT_STICK || block.getType() == Material.POTATO) {
            if(!(PyxisCore.perms.playerInGroup(player, "Agrarian")))
                return;

            int[] array = new int[100];

            for(int i = 0; i < 25; ++i)
                array[i] = 1;

            for(int i = 25; i < 50; ++i)
                array[i] = 2;

            for(int i = 50; i < 75; ++i)
                array[i] = 3;

            for(int i = 75; i < 100; ++i)
                array[i] = 4;

            Utils.shuffle(array);

            if(array[50] == 1)
                block.setData(CropState.RIPE.getData());
        }
    }

    /**
     * Method is executed when a player clicks in their inventory.
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        Inventory inv = event.getClickedInventory();

        if(inv.getType() == InventoryType.BREWING) {
            if (!(PyxisCore.perms.playerInGroup(player, "Rogue"))) {
                player.sendMessage(ChatColor.RED + "You must unlock the Rogue path to gain access to alchemy.");
                event.setCancelled(true);
            }

            return;
        }

        if(!(ChatColor.stripColor(inv.getName()).equalsIgnoreCase("Choose Your Paths")))
            return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        ItemMeta meta = item.getItemMeta();

        if(item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
            player.closeInventory();
            return;
        }

        ConfigurationSection section = PyxisCore.plugin.config.getConfigurationSection("Paths");

        for(String pathName : section.getKeys(false))
            if (ChatColor.translateAlternateColorCodes('&', section.getConfigurationSection(pathName).getString("icon.name")).equals(meta.getDisplayName())) {
                PyxisCore.plugin.playerManagerInstance.getPlayer(player).getPathsMenu().selectedPath(pathName);
                break;
            }

        player.closeInventory();
    }

    /**
     * Method is executed when a player joins the server and enters the player
     * into the players HashMap in PlayerManager.
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        PyxisCore.plugin.playerManagerInstance.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {

        Material itemType = event.getRecipe().getResult().getType();

        for (HumanEntity he : event.getViewers()) {
            if (he instanceof Player) {
                if ((itemType == Material.DIAMOND_PICKAXE || itemType == Material.DIAMOND_AXE || itemType == Material.DIAMOND_HOE || itemType == Material.DIAMOND_SPADE || itemType == Material.DIAMOND_SWORD
                        || itemType == Material.DIAMOND_HELMET || itemType == Material.DIAMOND_CHESTPLATE || itemType == Material.DIAMOND_LEGGINGS || itemType == Material.DIAMOND_BOOTS)
                        && !(PyxisCore.perms.playerInGroup((Player) he, "Craftsman"))) {
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    he.sendMessage(ChatColor.RED + "You must unlock the Craftsman path before crafting this item.");
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if(event.isCancelled())
            return;

        Entity entity = event.getEntity();

        if(entity instanceof Player)
            if(((Player) entity).isSprinting())
                if (!(PyxisCore.perms.playerInGroup((Player) entity, "Thief")))
                    event.setFoodLevel(event.getFoodLevel() - 1);
            else
                event.setFoodLevel(event.getFoodLevel() - 1);
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {

        Player player = event.getPlayer();

        if(!(PyxisCore.perms.playerInGroup(event.getPlayer(), "Thief")))
            return;

        if(player.isSprinting() == false)
            player.setWalkSpeed(0.3F);
        else
            player.setWalkSpeed(0.2F);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if(!(PyxisCore.perms.playerInGroup(event.getPlayer(), "Harvester")))
            return;

        Block block = event.getBlock();

        if(block.getType() == Material.CROPS) {
            if(block.getData() == CropState.RIPE.getData()) {
                ItemStack is = new ItemStack(Material.WHEAT);

                Location loc = block.getLocation();

                loc.getWorld().dropItemNaturally(loc, is);
            }
        } else if(block.getType() == Material.CARROT_STICK || block.getType() == Material.POTATO) {
            if(block.getData() == CropState.RIPE.getData()) {
                int rand = (int) Math.round(2 + (Math.random() * (3 - 2)));

                ItemStack is;

                if(block.getType() == Material.CARROT_STICK)
                    is = new ItemStack(Material.CARROT_ITEM, rand);
                else
                    is = new ItemStack(Material.POTATO_ITEM);

                Location loc = block.getLocation();

                loc.getWorld().dropItemNaturally(loc, is);
            }
        } else if(block.getType() == Material.BEETROOT_BLOCK || block.getType() == Material.MELON_BLOCK) {
            int rand = (int) Math.round(2 + (Math.random() * (5 - 2)));

            ItemStack is;

            if (block.getType() == Material.BEETROOT_BLOCK)
                is = new ItemStack(Material.BEETROOT, rand);
            else
                is = new ItemStack(Material.MELON, rand);

            Location loc = block.getLocation();

            loc.getWorld().dropItemNaturally(loc, is);
        }
    }
}