package me.themagzuz.advancedcrafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;
import org.bukkit.enchantments.Enchantment;


public class AdvancedCrafting extends JavaPlugin{
	
	//NOTICE: EVERYTHING SHOULD BE OBJECT BASED, AVOID STATICS, USE PUBLIC METHODS, AND SAVE KITTENS!!!
	
	private AdvancedCrafting _pl;
	
	private Inventory inv = Bukkit.createInventory(null, 27, "Crafting");
	
	private Inventory recList = Bukkit.createInventory(null, 54, "Recipes");
	
	private Permission perms = null;
	
	private Permission admin = new Permission("AdvancedCrafting.admin");
	
	private ItemStack seperator;
	
	/* PAGE ITEMS ETC. */
	
	// These are initialized in the InitItems function
	private ItemStack nextPageItem;
	
	private ItemStack prevPageItem;
	
	private ItemStack pageDisplay;
	
	private boolean debugMode;
	
	/**
	 * NEVER use this. This is only for use in the static getter function
	 */
	private static AdvancedCrafting pl;
	
	private List<Inventory> pages = new ArrayList<Inventory>();
	
	private List<AdvancedRecipe> recipes = new ArrayList<AdvancedRecipe>();
	
	@Override
	public void onEnable(){
		_pl = this;
		pl = _pl;
		new PlayerListener(this);
		InitItems();
		InitializeInventory();
		InitPages();
		for (Player p : Bukkit.getOnlinePlayers()){
			new ACPlayer(p.getUniqueId());
			p.closeInventory();
			p.updateInventory();
		}
		List<ItemStack> rec = Arrays.asList(new ItemStack(Material.DIRT, 2)/*, new ItemStack(Material.COBBLESTONE), new ItemStack(Material.DIAMOND), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.GOLD_INGOT)*/);
		List<ItemStack> out = Arrays.asList(new ItemStack(Material.APPLE));
		new AdvancedRecipe("Test", Material.APPLE, true, rec, out);
		FileConfiguration config = pl.getConfig();
		if (!config.isSet("DebugMode")){
			config.set("DebugMode", false);
		}
		pl.saveConfig();
		pl.reloadConfig();
		debugMode = config.getBoolean("DebugMode");
		
		
	}
	

	
	@Override
	public void onDisable(){
		getPl().getLogger().info("Disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){			
	if (sender instanceof Player){
		Player p = (Player) sender;
	if (cmd.getName().equalsIgnoreCase("cadmin")){
		if (p.hasPermission(admin)){	
		if (args.length >= 1){

			if (args[0].equalsIgnoreCase("getplayers")){
				for (ACPlayer pl : ACPlayer.getPlayers()){
					p.sendMessage(pl.getPlayer().getName());
				} 
				}else if (args[0].equalsIgnoreCase("getpagecount")){
					p.sendMessage(pages.size() + "");
				
			} else if (args[0].equalsIgnoreCase("getpage")){
				p.sendMessage(ACPlayer.getACPlayer(p.getUniqueId()).getPage() + "");
			} else if (args[0].equalsIgnoreCase("item")){
				p.getInventory().addItem(StringToItemStack("1:0:10"));
				p.sendMessage("Gave you an item");
				p.sendMessage("�cThis is a debug command. If you did not recive an item, please contact TheMagzuz on bukkit.org");
			}
			
			} else {
				p.sendMessage("�b----------�aAdvanced Crafting�b----------");
				p.sendMessage(String.format("�aMade by:�b %s", pl.getDescription().getAuthors().toString().replaceAll("\\[", "").replaceAll("\\]", "")));
				p.sendMessage(String.format("�aVersion: �b%s", pl.getDescription().getVersion()));
			}
		} else p.sendMessage("�cYou do not have permission to do that!");
	}
	
			 else if (cmd.getName().equalsIgnoreCase("craft")){

			if(ACPlayer.getACPlayer(p.getUniqueId()) != null){
				ACPlayer.getACPlayer(p.getUniqueId()).nextPage();
			} else {
				p.sendMessage("�cYou do not appear to have an Advanced Crafting Player instance!");
				pl.getLogger().severe(String.format("Player %s, with UUID %s, does not appear to have an Advanced Player Instance", p.getName(), p.getUniqueId()));
			}
			
			
		}
	} else{
	
		if (args.length >=1){
			if (cmd.getName().equalsIgnoreCase("cadmin")){
			if (args[0].equalsIgnoreCase("printplayers")){
				for (ACPlayer pla : ACPlayer.getPlayers()){
					pl.getLogger().info(pla.toString());
				}
			}
			}
		}
	}
	
		
	
		
		return true;
	}
	
	public static AdvancedCrafting getPl(){
		return pl;
	}
	
	public Inventory getInv(){
		return inv;
	}
	
	public void InitializeInventory(){
		
	}
	public Inventory getRecInv(){
		return recList;
	}
	
	private void InitPages(){
		
		//TODO: Add configurable pages
		pages.add(recList);
		pages.add(recList);
		pages.add(recList);
		
	}
	
	private void InitItems(){
		nextPageItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
		ItemMeta meta = nextPageItem.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Go to the next page");
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.WHITE + (ChatColor.BOLD + "Next Page"));
		nextPageItem.setItemMeta(meta);
		
		prevPageItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
		ItemMeta meta2 = prevPageItem.getItemMeta();
		List<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.DARK_PURPLE + "Go to the previous page");
		meta2.setLore(lore2);
		meta2.setDisplayName(ChatColor.WHITE + (ChatColor.BOLD + "Previous Page"));
		prevPageItem.setItemMeta(meta2);
		
		pageDisplay = new ItemStack(Material.GOLD_BLOCK, 1);
		ItemMeta meta3 = pageDisplay.getItemMeta();
		meta3.setDisplayName("Page ");
		meta3.addEnchant(Enchantment.LURE, 1, true);
		
		seperator = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
		ItemMeta meta4 = seperator.getItemMeta();
		meta4.setDisplayName(" ");
		seperator.setItemMeta(meta4);
	}

	public ItemStack getNextPageItem(){
		return nextPageItem;
	}
	
	public List<Inventory> getPages(){
		return pages;
	}
	
	public ItemStack getPrevPageItem(){
		return prevPageItem;
	}
	
	public void SetPage(int set){
		ItemMeta meta = pageDisplay.getItemMeta();
		meta.setDisplayName("�6�lPage " + set);
		pageDisplay.setItemMeta(meta);
	}
	
	public ItemStack getPageDisplay(){
		return pageDisplay;
	}
	
	public boolean getInDebugMode(){
		return debugMode;
	}
	
	/*
	 * SYNTAX: ITEMID:DATAVALUE:COUNT
	 * Note that the item id is numeral.
	 */
	
	@SuppressWarnings("deprecation")
	public ItemStack StringToItemStack(String str){
		int item;
		int damage;
		int size;
		try{
			item = Integer.parseInt(str.split(":")[0]);
			damage = Integer.parseInt(str.split(":")[1]);
			size = Integer.parseInt(str.split(":")[2]);
			
			
		} catch(Exception e){
			pl.getLogger().severe(String.format("String with value \"%s\" was passed but not recognized as an item. The syntax is \"ID:DATA:COUNT\", all values should be numbers", str));
			pl.getLogger().severe("This might also be an error on the plugins side. If you belive that your syntax is correct, please send your config.yml for this plugin to TheMagzuz on bukkit.org");
			pl.getLogger().severe("Plugin disabled");
			pl.getServer().getPluginManager().disablePlugin(this);
			return null;
		}
		
		ItemStack is;
		
		is = new ItemStack(Material.getMaterial(item), size, (byte) damage);
		if (pl.getInDebugMode()){
			pl.getLogger().info("ID = " + item);
			pl.getLogger().info("DATA = " + damage);
			pl.getLogger().info("SIZE = " + size);
		}
		return is;
	}
	
	public Inventory getRecListTemplate(){
		return recList;
	}
	
	public List<AdvancedRecipe> getRecipes(){
		return recipes;
	}
	//4, 13, 22
	public void OpenRecipe(AdvancedRecipe rec, Player p){
		inv.clear();
		int ingSlot = 0;
		for (int i = 0; i<inv.getSize(); i++){
			if (i != 4 && i != 13 && i != 22){
				if (i < rec.getIngs().size())
					inv.setItem(ingSlot, rec.getIngs().get(i));
				else break;
			} else {
				if (i < rec.getIngs().size()){
				ingSlot += 5;
				inv.setItem(ingSlot, rec.getIngs().get(i));
				} else break;
			}
			ingSlot++;
		}
			
		

		inv.setItem(4, seperator);
		inv.setItem(13, seperator);
		inv.setItem(22, seperator);
		
		int resSlot = 5;
		for (int i = 0; i<inv.getSize(); i++){
			if (i != 9 && i != 18 ){
				if (i < rec.getResults().size()){
					inv.setItem(resSlot, rec.getResults().get(i));
				} else break;
			} else {
				if (i <= rec.getResults().size()){
					resSlot += 5;
					inv.setItem(resSlot, rec.getIngs().get(i));
				} else break;
			}
			resSlot++;
		}
		
		ACPlayer.getACPlayer(p.getUniqueId()).SetSelectedRecipe(rec);
		
		p.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	public void RemoveItems(Player p, AdvancedRecipe rec){
		Inventory inv = p.getInventory();
		List<ItemStack> ings = rec.getIngs(); 
		
		for (ItemStack is : ings){
			int count = is.getAmount();
			//p.sendMessage(count + "");
			for (ItemStack i : inv){
				try{
					if (i.getItemMeta().equals(is.getItemMeta()) && i.getType().equals(is.getType())){
						if (i.getAmount() >= count){
							//p.sendMessage(i.getAmount() + "count 1");
							i.setAmount(i.getAmount() - count);
							//p.sendMessage(i.getAmount() + " count 2");
							count = 0;
						} else {
							count -= i.getAmount();
							i.setAmount(0);
						}
					}
					if (count <= 0){
						//p.sendMessage(count + "");
						p.updateInventory();
						return;
					}
				} catch (NullPointerException e){
					
				}
			}
		}
		
	}
	
}
