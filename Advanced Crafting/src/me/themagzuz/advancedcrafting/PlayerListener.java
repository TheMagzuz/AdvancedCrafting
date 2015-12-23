package me.themagzuz.advancedcrafting;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener{

	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	private Inventory inv = pl.getRecInv();
	
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
			ItemStack clicked = e.getCurrentItem();
			Player clicker = (Player) e.getWhoClicked();
			if(clicked.equals(pl.getNextPageItem())){
				ACPlayer.getACPlayer(clicker.getUniqueId()).nextPage();
			} else if (clicked.equals(pl.getPrevPageItem())){
				ACPlayer.getACPlayer(clicker.getUniqueId()).prevPage();
			}
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e){
		new ACPlayer(e.getPlayer().getUniqueId());
	}
	
}
