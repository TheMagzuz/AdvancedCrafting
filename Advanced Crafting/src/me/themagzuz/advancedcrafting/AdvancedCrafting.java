package me.themagzuz.advancedcrafting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedCrafting extends JavaPlugin{
	
	//NOTICE: EVERYTHING SHOULD BE OBJECT BASED, AVOID STATICS, USE PUBLIC METHODS, AND SAVE KITTENS!!!
	
	private AdvancedCrafting _pl;
	
	private Inventory inv = Bukkit.createInventory(null, 27, "Crafting");
	
	private Inventory recList = Bukkit.createInventory(null, 54, "Recipes");
	
	private Permission perms = null;
	
	private Permission admin = new Permission("AdvancedCrafting.admin");
	
	
	
	/* PAGE ITEMS ETC. */
	
	// These are initialized in the InitItems function
	private ItemStack nextPageItem;
	
	private ItemStack prevPageItem;
	
	
	/**
	 * NEVER use this. This is only for use in the static getter function
	 */
	private static AdvancedCrafting pl;
	
	private List<Inventory> pages = new ArrayList<Inventory>();
	
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
		}
		
	}
	

	
	@Override
	public void onDisable(){
		getPl().getLogger().info("Disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){			
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
				
			}
			
			} else {
				p.sendMessage("§b----------§aAdvanced Crafting§b----------");
				p.sendMessage(String.format("§aMade by:§b %s", pl.getDescription().getAuthors().toString().replaceAll("\\[", "").replaceAll("\\]", "")));
				p.sendMessage(String.format("§aVersion: §b%s", pl.getDescription().getVersion()));
			}
		} else p.sendMessage("§cYou do not have permission to do that!");
	}
	
			 else if (cmd.getName().equalsIgnoreCase("craft")){

			if(ACPlayer.getACPlayer(p.getUniqueId()) != null){
				ACPlayer.getACPlayer(p.getUniqueId()).nextPage();
			} else {
				p.sendMessage("§cYou do not appear to have an Advanced Crafting Player instance!");
				pl.getLogger().severe(String.format("Player %s, with UUID %s, does not appear to have an Advanced Player Instance", p.getName(), p.getUniqueId()));
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
	
	
}
