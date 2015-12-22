package me.themagzuz.advancedcrafting;

import java.awt.List;

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
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedCrafting extends JavaPlugin{
	
	//NOTICE: EVERYTHING SHOULD BE OBJECT BASED, AVOID STATICS, USE PUBLIC METHODS, AND SAVE KITTENS!!!
	
	private AdvancedCrafting _pl;
	
	private Inventory inv = Bukkit.createInventory(null, 27, "Crafting");
	
	private Inventory recList = Bukkit.createInventory(null, 54, "Recipes");
	
	/* PAGE ITEMS ETC. */
	private ItemStack nextPageItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
	
	
	/**
	 * NEVER use this. This is only for use in the static getter function
	 */
	public static AdvancedCrafting pl;
	
	
	@Override
	public void onEnable(){
		_pl = this;
		pl = _pl;
		new PlayerListener(this);
		InitItems();
		InitializeInventory();
		
	}
	
	@Override
	public void onDisable(){
		getPl().getLogger().info("Disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	
		if (args.length >= 1){
			
			//TODO: Add commands
			
		} else if (cmd.getName().equalsIgnoreCase("craft")){
			Player p = (Player) sender;
			p.openInventory(inv);
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
	
	private void InitItems(){
		ItemMeta meta = nextPageItem.getItemMeta();
		List lore = new List();
		lore.add(ChatColor.DARK_PURPLE + "Go to the next page");
		meta.setLore((java.util.List<String>) lore);
		meta.setDisplayName(ChatColor.WHITE + (ChatColor.BOLD + "Next Page"));
		nextPageItem.setItemMeta(meta);
	}

	public ItemStack getNextPageItem(){
		return nextPageItem;
	}
	
	
}
