package com.andrewyunt.pyxiscore.listeners;

import com.andrewyunt.pyxiscore.PyxisCore;
import com.andrewyunt.pyxiscore.player.PyxisPlayer;
import com.andrewyunt.pyxiscore.utilities.Utils;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

        if(inv == null)
            return;

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

        Block block = event.getBlock();

        Player player = event.getPlayer();

        Material type = player.getInventory().getItemInMainHand().getType();

        if(type == Material.DIAMOND_AXE || type == Material.DIAMOND_PICKAXE) {

            if(!(PyxisCore.perms.playerInGroup(player, "Craftsman"))) {
                player.sendMessage(ChatColor.RED + "You must unlock the Craftsman path to use a diamond axe or diamond pickaxe.");
                event.setCancelled(true);
            }

            return;

        } else if(type == Material.DIAMOND_SPADE || type == Material.DIAMOND_HOE) {

            if(!(PyxisCore.perms.playerInGroup(player, "Harvester"))) {
                player.sendMessage(ChatColor.RED + "You must unlock the Harvester path to use a diamond hoe or shovel.");
                event.setCancelled(true);
            }

        } else if(type == Material.DIAMOND_SWORD) {

            if(!(PyxisCore.perms.playerInGroup(player, "Ninja"))) {
                player.sendMessage(ChatColor.RED + "You must unlock the Ninja path to use a diamond sword.");
                event.setCancelled(true);
            }
        }

        if(!(PyxisCore.perms.playerInGroup(event.getPlayer(), "Harvester")))
            return;

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

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {

        if(event.getNewArmorPiece().getType() == Material.DIAMOND_HELMET || event.getNewArmorPiece().getType() == Material.DIAMOND_CHESTPLATE ||
                event.getNewArmorPiece().getType() == Material.DIAMOND_LEGGINGS || event.getNewArmorPiece().getType() == Material.DIAMOND_BOOTS) {

            Player player = event.getPlayer();

            if(!(PyxisCore.perms.playerInGroup(player, "Knight"))) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You must unlock the Knight path to equip diamond armor.");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ItemStack item = event.getItem();

        if(item == null)
            return;

        Material type = item.getType();

        if(type == Material.DIAMOND_HOE) {
            if(!(PyxisCore.perms.playerInGroup(player, "Harvester")))
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    player.sendMessage(ChatColor.RED + "You must unlock the Harvester path to use a diamond hoe.");
                    event.setCancelled(true);
                }
            return;
        }

        if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;

        if(!(type == Material.PAPER))
            return;

        if(!(item.hasItemMeta()))
            return;

        String displayName = item.getItemMeta().getDisplayName();

        PyxisPlayer pp = PyxisCore.plugin.playerManagerInstance.getPlayer(player);

        if(displayName.equals(ChatColor.GOLD + "Lightning Spell")) {

            int lightningCooldown = pp.getLightningCooldown();

            if(lightningCooldown > 0) {
                player.sendMessage(String.format(ChatColor.RED + "You must wait %s seconds before using the lightning spell again.", lightningCooldown));
                return;
            }

            pp.setLightningCooldown(25);

        } else if(displayName.equals(ChatColor.RED + "Fireball Spell")) {

            int fireballCooldown = pp.getFireballCooldown();

            if(fireballCooldown > 0) {
                player.sendMessage(String.format(ChatColor.RED + "You must wait %s seconds before using the fireball spell again.", fireballCooldown));
                return;
            }



            pp.setFireballCooldown(10);

        } else if(displayName.equals(ChatColor.AQUA + "Ice Lance Spell")) {

            int icelanceCooldown = pp.getIcelanceCooldown();

            if(icelanceCooldown > 0) {
                player.sendMessage(String.format(ChatColor.RED + "You must wait %s seconds before using that spell again.", icelanceCooldown));
                return;
            }

            pp.setIcelanceCooldown(15);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();

        if(!(damager instanceof Player))
            return;

        Player player = (Player) damager;

        Material type = player.getInventory().getItemInMainHand().getType();

        if(type == Material.DIAMOND_SWORD && !(PyxisCore.perms.playerInGroup(player, "Ninja"))) {
            player.sendMessage(ChatColor.RED + "You must unlock the Ninja path to attack using a diamond sword.");
            event.setCancelled(true);
        }

        Entity entity = event.getEntity();

        if(!(entity instanceof Player))
            return;

        player = (Player) entity;

        if(PyxisCore.perms.playerInGroup(player, "Rogue")) {
            double random = Math.random();

            if(random <= 0.1) {
                damager.sendMessage(ChatColor.RED + "Your attack was dodged by " + player.getName());
                player.sendMessage(ChatColor.GREEN + "You dodged the attack by " + damager.getName());
                event.setCancelled(true);
            }
        } else if(PyxisCore.perms.playerInGroup(player, "Ninja")) {
            double random = Math.random();

            if(random <= 0.15) {
                ((Player) damager).sendMessage(ChatColor.RED + "Your attack was dodged by " + player.getName());
                player.sendMessage(ChatColor.GREEN + "You dodged the attack by " + damager.getName());
                event.setCancelled(true);

                event.setCancelled(true);
            }
        }
    }
}