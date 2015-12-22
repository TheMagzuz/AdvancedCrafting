package me.themagzuz.advancedcrafting;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener{

	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	private Inventory inv = pl.getInv();
	
	public PlayerListener(AdvancedCrafting plugin){
		plugin.getServer().getPluginManager().registerEvents(this, pl);
		Initialized();
		
	}
	
	private void Initialized(){
		pl.getLogger().info("The player listener has been initialized");
	}
	
	@EventHandler
	public void OnInvClick(InventoryClickEvent e){
		if (e.getInventory().equals(inv)){
			e.getWhoClicked().sendMessage("You clicked in the crafting GUI!");
			e.setCancelled(true);
		}
	}
	
}
