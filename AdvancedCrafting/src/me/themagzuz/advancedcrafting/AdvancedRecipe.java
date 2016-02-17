package me.themagzuz.advancedcrafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedRecipe implements ConfigurationSerializable{

	private String name;
	
	private ItemStack icon;
	
	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	private List<AdvancedItem> ing;
	
	private Map<String, Object> map;
	
	private List<AdvancedItem> res;
	
	private int id;
	
	private boolean glow;
	
	private static AdvancedCrafting _pl = AdvancedCrafting.getPl();
	
	public AdvancedRecipe(String nm, Material ico, boolean glw, ArrayList<AdvancedItem> ingredients, ArrayList<AdvancedItem> results){
		
		name = nm;
		icon = new ItemStack(ico);
		ItemMeta meta = icon.getItemMeta();
		ing = ingredients;
		res = results;
		glow = glw;
		meta.setDisplayName(name);
		if (glw){
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
	
	public String getName(){
		return name;
	}



	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> serialize() {
		
		pl.getLogger().info("Starting serialization on recipe " + name);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String path = "";
		
		map.put(path + ".Name", this.name);
		map.put(path + ".Item.ID", this.getItem().getTypeId());
		map.put(path + ".Item.Data", 0);
		map.put(path + ".Item.Name", this.name);
		for (int i = 0; i < this.getIngs().size(); i++){
			/*
			map.put(path + ".Ingredients." + i + ".ID", this.getIngs().get(i).getTypeId());
			map.put(path + ".Ingredients." + i + ".Name", this.getIngs().get(i).getItemMeta().getDisplayName());
			map.put(path + ".Ingredients." + i + ".Count", this.getIngs().get(i).getAmount());
			*/
			String value = i + "";
			map.put(path + ".Ingredients." + value, this.getIngs().get(i).serialize());
			if (!map.containsKey(path + ".Ingredients." + value + ".damage")){
				map.put(path + ".Ingredients." + value + ".damage", 0);
			}
			if (!map.containsKey(path + ".Ingredients." + value + ".amount")){
				map.put(path + ".Ingredients." + value + ".amount", 1);
			}
			if (this.getIngs().get(i).getItemMeta().getDisplayName() != null){
				map.put(path + ".Ingredients." + value + ".Name", this.getIngs().get(i).getItemMeta().getDisplayName());
			} else {
				map.put(path + ".Ingredients." + value + ".Name", this.getIngs().get(i).getType().toString());
			}
		}
		for (int i = 0; i < this.getResults().size(); i++){
			/*
			map.put(path + ".Ingredients." + i + ".ID", this.getIngs().get(i).getTypeId());
			map.put(path + ".Ingredients." + i + ".Name", this.getIngs().get(i).getItemMeta().getDisplayName());
			map.put(path + ".Ingredients." + i + ".Count", this.getIngs().get(i).getAmount());
			*/
			String value = i + "";
			if (pl.getInDebugMode()){
				pl.getLogger().info("Serializing recipe with name \"" + this.getName() + "\" loop number " + i);
			}
			map.put(path + ".Results." + value, this.getIngs().get(i).serialize());
			if (!map.containsKey(path + ".Results." + value + ".damage")){
				map.put(path + ".Results." + value + ".damage", 0);
			}
			if (!map.containsKey(path + ".Results." + value + ".amount")){
				map.put(path + ".Results." + value + ".amount", 1);
			}
			if (this.getIngs().get(i).getItemMeta().getDisplayName() != null){
				map.put(path + ".Results." + value + ".Name", this.getIngs().get(i).getItemMeta().getDisplayName());
			} else {
				map.put(path + ".Results." + value + ".Name", this.getIngs().get(i).getType().toString());
			}
		}
		map.put(path + ".Glow", this.glow);
		
		this.map = map;
		
		pl.getLogger().info("Serialization finished for recipe " + name);
		
		return map;
	}
	
	@SuppressWarnings({ "unused" ,"deprecation", "rawtypes", "unchecked" })
	public static AdvancedRecipe deserialize(Map<String, Object> in){
		Object temp;
		temp = in.keySet();

		String name = (String) in.get("Name");
		
		int stage = ReadStage.NONE;
		
		Iterator i = in.keySet().iterator();
		
		_pl.getLogger().info("Name: " + name);
		
		List<ItemStack> ingredients = new ArrayList<ItemStack>();
		List<ItemStack> results = new ArrayList<ItemStack>();
		
		/*while (i.hasNext()){
			temp = i.next();
			/*if (temp == in.keySet().toArray()[0]){
				_pl.getLogger().info("The 1st element is: " + temp.toString());
			}
			if (!StringUtils.containsIgnoreCase(in.get(temp.toString()).toString(), "MemorySection")){
				//_pl.getLogger().info(in.get(temp.toString()).toString());
				if (temp.toString().equalsIgnoreCase("Name")){
					name = in.get(temp.toString()).toString();
				}
			} else if (StringUtils.containsIgnoreCase(in.get(temp.toString()).toString(), "Ingredients") || stage == ReadStage.INGREDIENTS){
				stage = ReadStage.INGREDIENTS;
				ConfigurationSection sec = _pl.getRecipesCfg().getConfigurationSection(name + ".Ingredients");
				int loops = 0;
				for (Object ar : sec.getKeys(false).toArray()){
					String str = "";

					
					str = in.get("Ingredients." + ar.toString()).toString();
					if (StringUtils.containsIgnoreCase(str, "MemorySection")){
						// We're reading an item
						String item = "";
						item = item + (in.get("Ingredients." + ar.toString() + ".type")) + ":";
						ConfigurationSection itemSec = (ConfigurationSection) in.get("Ingredients." + ar.toString());
						Map<String, Object> map = itemSec.getValues(false);
						ingredients.add(ItemStack.deserialize(map));
						int loops2 = 0;
						for (Player p : _pl.getServer().getOnlinePlayers()){
							p.getInventory().addItem(ItemStack.deserialize(map));
							p.sendMessage(loops2 + "");
						}
						_pl.getLogger().info("Looped over object: " + ar.toString() + " on loop number " + loops);
						_pl.getLogger().info("The item returned was: " + ItemStack.deserialize(map).toString());
						loops++;
						//_pl.getLogger().info(item);
					}
				
				}
				stage = ReadStage.NONE;
			}

		}*/
		
		ConfigurationSection ingSec = _pl.getRecipesCfg().getConfigurationSection(name + ".Ingredients");
		List<ItemStack> ingList = new ArrayList<ItemStack>();
		for (Object o : ingSec.getKeys(false)){
			int secId = Integer.parseInt(o.toString());
			_pl.getLogger().info("Parsing section with id " + secId);
			//for (Object ob : )
		}
		
		AdvancedRecipe rec = new AdvancedRecipe("Test1", Material.STONE, true, new ArrayList<ItemStack>(), new ArrayList<ItemStack>());
		
		return rec;
	}
	
	}
	

