package com.andrewyunt.pyxiscore;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipes {

    public static ShapelessRecipe getMageSpellRecipe() {

        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta meta = is.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "Lightning Spell");
        is.setItemMeta(meta);

        ShapelessRecipe recipe = new ShapelessRecipe(is);

        recipe.addIngredient(Material.PAPER);
        recipe.addIngredient(Material.REDSTONE);

        return recipe;
    }

    public static ShapelessRecipe getAcolyteSpellRecipe() {

        ItemStack is = new ItemStack(Material.FIREBALL);
        ItemMeta meta = is.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Fireball Spell");
        is.setItemMeta(meta);

        ShapelessRecipe recipe = new ShapelessRecipe(is);

        recipe.addIngredient(Material.PAPER);
        recipe.addIngredient(Material.COAL);

        return recipe;
    }

    public static ShapelessRecipe getWizardSpellRecipe() {

        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta meta = is.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Ice Lance Spell");
        is.setItemMeta(meta);

        ShapelessRecipe recipe = new ShapelessRecipe(is);

        recipe.addIngredient(Material.PAPER);
        recipe.addIngredient(Material.ICE);

        return recipe;
    }
}