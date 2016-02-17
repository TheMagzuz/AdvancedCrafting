package me.themagzuz.advancedcrafting;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedItem {

	private ItemStack item;
	private String name;

	public AdvancedItem(ItemStack i, String nm){
		item = i;
		name = nm;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}
	
	public String getName(){
		return this.name;
	}
	public ItemStack getItem(){
		return this.item;
	}
	
}
