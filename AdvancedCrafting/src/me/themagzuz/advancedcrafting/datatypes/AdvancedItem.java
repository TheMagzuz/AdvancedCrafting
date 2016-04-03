package me.themagzuz.advancedcrafting.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedItem implements ConfigurationSerializable{

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

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> out = new HashMap<String, Object>();
		
		out.put("Item", item.serialize());
		out.put("Name", name);
		
		return out;
	}
	
	
	public static AdvancedItem deserialize(Map<String, Object> m){
		
		ItemStack i = ItemStack.deserialize((Map<String, Object>) m.get("Item"));
		
		String name = (String) m.get("Name");
		
		AdvancedItem out = new AdvancedItem(i, name);
		
		return out;
		
	}
}
