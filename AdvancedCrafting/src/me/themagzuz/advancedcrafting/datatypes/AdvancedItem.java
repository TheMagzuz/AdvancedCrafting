package me.themagzuz.advancedcrafting.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedItem implements ConfigurationSerializable {

	/**
	 * The item that this represents
	 */
	private ItemStack item;
	
	/**
	 * The name of the item<br>
	 * Also used as the name for the itemstack<br>
	 * Standard Minecraft color syntax is used
	 */
	private String name;

	/**
	 * 
	 * @param i The itemstack that this object represents
	 * @param nm The name of the item<br>
	 * Also names the itemstack
	 */
	public AdvancedItem(ItemStack i, String nm){
		item = i;
		name = nm;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}
	/**
	 * 
	 * @return The name of the item
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * 
	 * @return The item that this represents
	 */
	public ItemStack getItem(){
		return this.item;
	}

	/**
	 * @return A map based on the object that this was called on
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> out = new HashMap<String, Object>();
		
		out.put("Item", item.serialize());
		out.put("Name", name);
		
		return out;
	}
	
	/**
	 * 
	 * @param m The map to deserialize
	 * @return An Advanced Item based on the map given<br>
	 * Returns null and throws an exception if map is invalid
	 */
	public static AdvancedItem deserialize(Map<String, Object> m){
		
		ItemStack i = ItemStack.deserialize((Map<String, Object>) m.get("Item"));
		
		String name = (String) m.get("Name");
		
		AdvancedItem out = new AdvancedItem(i, name);
		
		return out;
		
	}
}
