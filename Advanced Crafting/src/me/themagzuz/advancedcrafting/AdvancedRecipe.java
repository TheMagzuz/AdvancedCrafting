package me.themagzuz.advancedcrafting;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedRecipe {

	private String name;
	
	private ItemStack icon;
	
	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	private List<ItemStack> ing;
	
	private List<ItemStack> res;
	
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
	
	}
	

