package me.themagzuz.advancedcrafting.listeners;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.themagzuz.advancedcrafting.AdvancedCrafting;
import me.themagzuz.advancedcrafting.datatypes.ACPlayer;
import me.themagzuz.advancedcrafting.datatypes.AdvancedRecipe;

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
			} else {
				
				for (AdvancedRecipe rec : pl.getRecipes()){
					if (rec.getItem().equals(clicked)){
						pl.OpenRecipe(rec, clicker);
					}
				}
				
			}
			
			e.setCancelled(true);
		} else if (e.getInventory().equals(pl.getInv())){
			Player clicker = (Player) e.getWhoClicked();
			if (ACPlayer.getACPlayer(clicker.getUniqueId()).canCraft(ACPlayer.getACPlayer(clicker.getUniqueId()).getSelectedRecipe())){
				int count = 0;
				ItemStack[] inventory = clicker.getInventory().getContents();
				for (int i = 0; i < inventory.length; i++){
					try{
					if (inventory[i].equals(null)){
						count++;
					}
					} catch (NullPointerException er){
						count++;
					}
				}
				if (count >= ACPlayer.getACPlayer(clicker.getUniqueId()).getSelectedRecipe().getResults().size()){
					for (ItemStack is : ACPlayer.getACPlayer(clicker.getUniqueId()).getSelectedRecipe().getResults()){
						clicker.getInventory().addItem(is);
						
					}

					pl.RemoveItems(clicker, ACPlayer.getACPlayer(clicker.getUniqueId()).getSelectedRecipe());
					
				} else{
					clicker.sendMessage("§cYou do not have enough inventory space to do that!");
				}
			} else {
				clicker.sendMessage("§cYou do not have the required materials to craft that!");
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e){
	}
	
	@EventHandler
	public void OnInvClose(InventoryCloseEvent e){
		if (e.getInventory().equals(pl.getRecInv())){
			ACPlayer.getACPlayer(e.getPlayer().getUniqueId()).SetPage(-1);
		}
	}
	
	@EventHandler
	public void OnPlayerLeave(PlayerQuitEvent e){
		ACPlayer.getPlayers().remove(ACPlayer.getACPlayer(e.getPlayer().getUniqueId()));
		
	}
	
}
