package com.andrewyunt.pyxiscore;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipes {
	
	public static ShapelessRecipe getLightningSpellRecipe() {
		
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta meta = is.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "Lightning Spell");
		is.setItemMeta(meta);
		
		ShapelessRecipe recipe = new ShapelessRecipe(is);
		
		recipe.addIngredient(Material.IRON_FENCE);
		recipe.addIngredient(Material.REDSTONE_BLOCK);
		
		return recipe;
	}
	
	public static ShapelessRecipe getFireballSpellRecipe() {
		
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta meta = is.getItemMeta();
		
		meta.setDisplayName(ChatColor.RED + "Fireball Spell");
		is.setItemMeta(meta);
		
		ShapelessRecipe recipe = new ShapelessRecipe(is);
		
		recipe.addIngredient(Material.FIREBALL);
		recipe.addIngredient(Material.SULPHUR);
		
		return recipe;
	}
	
	public static ShapelessRecipe getIceLanceSpellRecipe() {
		
		ItemStack is = new ItemStack(Material.IRON_SWORD);
		ItemMeta meta = is.getItemMeta();
		
		meta.setDisplayName(ChatColor.AQUA + "Ice Lance");
		is.setItemMeta(meta);
		
		ShapelessRecipe recipe = new ShapelessRecipe(is);
		
		recipe.addIngredient(Material.IRON_SWORD);
		recipe.addIngredient(Material.ICE);
		
		return recipe;
	}
}