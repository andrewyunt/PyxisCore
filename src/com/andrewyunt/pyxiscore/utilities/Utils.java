package com.andrewyunt.pyxiscore.utilities;

import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    private static Random random;

    public static List<String> colorizeStringList(List<String> input) {

        List<String> colorized = input.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());

        return colorized;
    }

    public static ItemStack addGlow(ItemStack is) {

        net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);

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
        if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    /**
     * Code from method java.util.Collections.swap();
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}