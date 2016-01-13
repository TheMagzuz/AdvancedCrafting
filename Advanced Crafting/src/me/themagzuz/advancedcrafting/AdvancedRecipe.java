package me.themagzuz.advancedcrafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedRecipe implements ConfigurationSerializable{

	private String name;
	
	private ItemStack icon;
	
	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	private List<ItemStack> ing;
	
	private Map<String, Object> map;
	
	private List<ItemStack> res;
	
	private int id;
	
	public AdvancedRecipe(String nm, Material ico, boolean glow, List<ItemStack> ingredients, List<ItemStack> results){
		name = nm;
		icon = new ItemStack(ico);
		ItemMeta meta = icon.getItemMeta();
		ing = ingredients;
		res = results;
		meta.setDisplayName(name);
		if (glow){
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		icon.setItemMeta(meta);
		AddItem(icon);
		
		if (pl.getInDebugMode())
		pl.getLogger().info("Created an advanced recipe with the name " + name);
		
		pl.getRecipes().add(this);
		
	}
	
	
	
	public ItemStack getItem(){
		return icon;
	}
	private void AddItem(ItemStack item){
		for (Inventory inv : pl.getPages()){
			if (inv.firstEmpty() != -1){
		
				if (pl.getInDebugMode())
				pl.getLogger().info("Added the item to an inventory");
			
				inv.addItem(item);
				return;
				}
			}
			
			
		}
	
	
	
	public List<ItemStack> getIngs(){
		return ing;
	}
	public List<ItemStack> getResults(){
		return res;
	}



	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> serialize() {


		
		
		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String path = "" + this.name;
		
		map.put(path + ".Name", this.name);
		map.put(path + ".Item.ID", this.getItem().getTypeId());
		map.put(path + ".Item.Data", 0);
		map.put(path + ".Item.Name", this.name);
		for (int i = 0; i < this.getIngs().size(); i++){
			map.put(path + ".Ingredients." + i + ".ID", this.getIngs().get(i).getTypeId());
			map.put(path + ".Ingredients." + i + ".Name", this.getIngs().get(i).getItemMeta().getDisplayName());
			map.put(path + ".Ingredients." + i + ".Count", this.getIngs().get(i).getAmount());
		}
		
		this.map = map;
		
		return map;
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public AdvancedRecipe deserialize(Map<String, Object> in){
		Object temp;
		temp = in.get("");
		
		AdvancedRecipe rec = new AdvancedRecipe("Test1", Material.STONE, true, new ArrayList<ItemStack>(), new ArrayList<ItemStack>());
		
		return rec;
	}
	
	}
	

