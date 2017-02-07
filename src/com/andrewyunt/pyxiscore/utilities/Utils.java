package com.andrewyunt.pyxiscore.utilities;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

	private static Random random;
	public static List<String> colorizeStringList(List<String> input) {
		
		List<String> colorized = input.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line))
				.collect(Collectors.toList());
		
		return colorized;
	}
	
	public static ItemStack addGlow(ItemStack is) {
		
		net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbt = nmsItem.getTag() == null ? new NBTTagCompound() : nmsItem.getTag();
		NBTTagList ench = new NBTTagList();

		nbt.set("ench", ench);
		nmsItem.setTag(nbt);
		
		return CraftItemStack.asBukkitCopy(nmsItem);
	}
	
	/**
	 * Code from method java.util.Collections.shuffle();
	 */
	public static void shuffle(int[] array) {
		
		if (random == null)
			random = new Random();
		
		int count = array.length;
		
		for (int i = count; i > 1; i--)
			swap(array, i - 1, random.nextInt(i));
	}
	
	/**
	 * Code from method java.util.Collections.swap();
	 */
	private static void swap(int[] array, int i, int j) {
		
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public static ItemStack removeAttributes(ItemStack i) {
		
		if (i == null || i.getType() == Material.BOOK_AND_QUILL)
			return i;
		
		ItemStack item = i.clone();
		net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag;
		
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		} else
			tag = nmsStack.getTag();
		
		NBTTagList am = new NBTTagList();
		
		tag.set("AttributeModifiers", am);
		nmsStack.setTag(tag);
		
		return CraftItemStack.asBukkitCopy(nmsStack);
	}
	
	/**
	 * @author Quackster
	 */
	public static Set<Block> getBlocksBetweenTwoPoints(Location loc1, Location loc2) {

		Set<Block> blocks = new HashSet<Block>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for (int x = bottomBlockX; x <= topBlockX; x++)
			for (int z = bottomBlockZ; z <= topBlockZ; z++)
				for (int y = bottomBlockY; y <= topBlockY; y++) {
					Block block = loc1.getWorld().getBlockAt(x, y, z);

					blocks.add(block);
				}

		return blocks;
	}
}