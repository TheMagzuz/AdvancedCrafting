package me.themagzuz.advancedcrafting.datatypes;

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

import me.themagzuz.advancedcrafting.AdvancedCrafting;

public class AdvancedRecipe implements ConfigurationSerializable {

	/**
	 * The name of the recipe
	 */

	private String name;

	/**
	 * The item to display in the menu
	 */

	private ItemStack icon;

	/**
	 * The plugin
	 */

	private AdvancedCrafting pl = AdvancedCrafting.getPl();

	/**
	 * The ingredients of the recipe
	 */

	private List<AdvancedItem> ing;

	/**
	 * I honestly don't remember :/
	 */

	private Map<String, Object> map;

	/**
	 * The results of the recipe
	 */

	private List<AdvancedItem> res;

	/**
	 * The results and the ingredients of the recipe in form of an itemstack
	 */

	private List<ItemStack> ingItemStack, resItemStack;

	/**
	 * The id of the recipe <br>
	 * This should NEVER be changed
	 */

	private int id;

	/**
	 * Whether or not the recipe icon has an enchantment glow
	 */

	private boolean glow;

	/**
	 * The plugin instance For use in static methods
	 */

	private static AdvancedCrafting _pl = AdvancedCrafting.getPl();

	/**
	 * 
	 * @param nm
	 *            The name of the recipe
	 * @param ico
	 *            The icon of the recipe
	 * @param glw
	 *            Does the icon have an enchantment glow?
	 * @param ingredients
	 *            The ingredients of the recipe
	 * @param results
	 *            The output of the recipe
	 */

	public AdvancedRecipe(String nm, Material ico, boolean glw, ArrayList<AdvancedItem> ingredients,
			ArrayList<AdvancedItem> results) {

		name = nm;
		icon = new ItemStack(ico);
		ItemMeta meta = icon.getItemMeta();
		ing = ingredients;
		res = results;
		glow = glw;
		meta.setDisplayName(name);
		if (glw) {
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}

		for (AdvancedItem i : ingredients) {
			ingItemStack.add(i.getItem());
		}
		for (AdvancedItem i : results) {
			resItemStack.add(i.getItem());
		}

		icon.setItemMeta(meta);
		AddItem(icon);

		if (pl.getInDebugMode())
			pl.getLogger().info("Created an advanced recipe with the name " + name);

		pl.getRecipes().add(this);

	}

	/**
	 * 
	 * @return The item of the recipe
	 */

	public ItemStack getItem() {
		return icon;
	}

	/**
	 * Adds an item to the recipe
	 * 
	 * @param item
	 *            The Item to add to the recipe
	 */

	private void AddItem(ItemStack item) {
		for (Inventory inv : pl.getPages()) {
			if (inv.firstEmpty() != -1) {

				if (pl.getInDebugMode())
					pl.getLogger().info("Added the item to an inventory");

				inv.addItem(item);
				return;
			}
		}

	}

	/**
	 * Adds an item to the recipe
	 * 
	 * @param item
	 *            The Item to add to the recipe
	 */
	private void AddItem(AdvancedItem item) {
		for (Inventory inv : pl.getPages()) {
			if (inv.firstEmpty() != -1) {

				if (pl.getInDebugMode())
					pl.getLogger().info("Added the item to an inventory");

				inv.addItem(item.getItem());
				return;
			}
		}
	}

	/**
	 * 
	 * @return The list of ingredients in form of a list of itemstacks
	 */

	public List<ItemStack> getIngs() {
		return ingItemStack;
	}

	/**
	 * 
	 * @return The list of results in form of a list of itemstacks
	 */

	public List<ItemStack> getResults() {
		return resItemStack;
	}

	/**
	 * 
	 * @return The list of ingredients in form of a list of advanced items
	 */

	public List<AdvancedItem> getAIngs() {
		return this.ing;
	}

	/**
	 * 
	 * @return The list of results in form of a list of advanced items
	 */

	public List<AdvancedItem> getAResults() {
		return this.res;
	}

	/**
	 * 
	 * @return The name of the recipe
	 */

	public String getName() {
		return name;
	}

	
	
	/**
	 * @return The recipe in form of a map<br>
	 * Used for storing in YAML
	 */
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
		for (int i = 0; i < this.getIngs().size(); i++) {
			/*
			 * map.put(path + ".Ingredients." + i + ".ID",
			 * this.getIngs().get(i).getTypeId()); map.put(path +
			 * ".Ingredients." + i + ".Name",
			 * this.getIngs().get(i).getItemMeta().getDisplayName());
			 * map.put(path + ".Ingredients." + i + ".Count",
			 * this.getIngs().get(i).getAmount());
			 */
			String value = i + "";
			map.put(path + ".Ingredients." + value, this.getResults().get(i).serialize());
			if (!map.containsKey(path + ".Ingredients." + value + ".damage")) {
				map.put(path + ".Ingredients." + value + ".damage", 0);
			}
			if (!map.containsKey(path + ".Ingredients." + value + ".amount")) {
				map.put(path + ".Ingredients." + value + ".amount", 1);
			}
			if (this.getAIngs().get(i).getName() != null) {
				map.put(path + ".Ingredients." + value + ".Name", this.getAIngs().get(i).getName());
			} else {
				map.put(path + ".Ingredients." + value + ".Name",
						this.getAIngs().get(i).getItem().getType().toString());
			}
		}
		for (int i = 0; i < this.getResults().size(); i++) {
			/*
			 * map.put(path + ".Ingredients." + i + ".ID",
			 * this.getIngs().get(i).getTypeId()); map.put(path +
			 * ".Ingredients." + i + ".Name",
			 * this.getIngs().get(i).getItemMeta().getDisplayName());
			 * map.put(path + ".Ingredients." + i + ".Count",
			 * this.getIngs().get(i).getAmount());
			 */
			String value = i + "";
			if (pl.getInDebugMode()) {
				pl.getLogger().info("Serializing recipe with name \"" + this.getName() + "\" loop number " + i);
			}
			map.put(path + ".Results." + value, this.getIngs().get(i).serialize());
			if (!map.containsKey(path + ".Results." + value + ".damage")) {
				map.put(path + ".Results." + value + ".damage", 0);
			}
			if (!map.containsKey(path + ".Results." + value + ".amount")) {
				map.put(path + ".Results." + value + ".amount", 1);
			}
			if (this.getAResults().get(i).getName() != null) {
				map.put(path + ".Results." + value + ".Name", this.getAResults().get(i).getName());
			} else {
				map.put(path + ".Results." + value + ".Name", this.getAResults().get(i).getItem().getType().toString());
			}
		}
		map.put(path + ".Glow", this.glow);

		this.map = map;

		pl.getLogger().info("Serialization finished for recipe " + name);

		return map;
	}
	/**
	 * 
	 * @param in The map to deserialize
	 * @return An AdvancedRecipe based on the map that was given<br>
	 * Returns null and throws error if map is not valid
	 */
	@SuppressWarnings({ "unused", "deprecation", "rawtypes", "unchecked" })
	public static AdvancedRecipe deserialize(Map<String, Object> in) {
		Object temp;
		temp = in.keySet();

		String name = (String) in.get("Name");

		ReadStage stage = ReadStage.NONE;

		Iterator i = in.keySet().iterator();

		_pl.getLogger().info("Name: " + name);

		List<ItemStack> ingredients = new ArrayList<ItemStack>();
		List<ItemStack> results = new ArrayList<ItemStack>();

		/*
		 * while (i.hasNext()){ temp = i.next(); /*if (temp ==
		 * in.keySet().toArray()[0]){ _pl.getLogger().info(
		 * "The 1st element is: " + temp.toString()); } if
		 * (!StringUtils.containsIgnoreCase(in.get(temp.toString()).toString(),
		 * "MemorySection")){
		 * //_pl.getLogger().info(in.get(temp.toString()).toString()); if
		 * (temp.toString().equalsIgnoreCase("Name")){ name =
		 * in.get(temp.toString()).toString(); } } else if
		 * (StringUtils.containsIgnoreCase(in.get(temp.toString()).toString(),
		 * "Ingredients") || stage == ReadStage.INGREDIENTS){ stage =
		 * ReadStage.INGREDIENTS; ConfigurationSection sec =
		 * _pl.getRecipesCfg().getConfigurationSection(name + ".Ingredients");
		 * int loops = 0; for (Object ar : sec.getKeys(false).toArray()){ String
		 * str = "";
		 * 
		 * 
		 * str = in.get("Ingredients." + ar.toString()).toString(); if
		 * (StringUtils.containsIgnoreCase(str, "MemorySection")){ // We're
		 * reading an item String item = ""; item = item +
		 * (in.get("Ingredients." + ar.toString() + ".type")) + ":";
		 * ConfigurationSection itemSec = (ConfigurationSection)
		 * in.get("Ingredients." + ar.toString()); Map<String, Object> map =
		 * itemSec.getValues(false);
		 * ingredients.add(ItemStack.deserialize(map)); int loops2 = 0; for
		 * (Player p : _pl.getServer().getOnlinePlayers()){
		 * p.getInventory().addItem(ItemStack.deserialize(map));
		 * p.sendMessage(loops2 + ""); } _pl.getLogger().info(
		 * "Looped over object: " + ar.toString() + " on loop number " + loops);
		 * _pl.getLogger().info("The item returned was: " +
		 * ItemStack.deserialize(map).toString()); loops++;
		 * //_pl.getLogger().info(item); }
		 * 
		 * } stage = ReadStage.NONE; }
		 * 
		 * }
		 */

		ConfigurationSection ingSec = _pl.getRecipesCfg().getConfigurationSection(name + ".Ingredients");
		List<ItemStack> ingList = new ArrayList<ItemStack>();
		for (Object o : ingSec.getKeys(false)) {
			int secId = Integer.parseInt(o.toString());
			_pl.getLogger().info("Parsing section with id " + secId);
			// for (Object ob : )
		}

		AdvancedRecipe rec = new AdvancedRecipe("Test1", Material.STONE, true, new ArrayList<AdvancedItem>(),
				new ArrayList<AdvancedItem>());

		return rec;
	}

}
