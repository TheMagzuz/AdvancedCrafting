package me.themagzuz.advancedcrafting.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.themagzuz.advancedcrafting.AdvancedCrafting;

public class ACPlayer {

	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	
	/*
	 * -1 if interface is not open.
	 * Uses standard array rules (Starts at 0)
	 */
	private int page = -1;
	
	private UUID id;
	
	private Player player;
	
	private static List<ACPlayer> players = new ArrayList<ACPlayer>();
	
	private AdvancedRecipe selectedRecipe;
	
	public ACPlayer(UUID p){
		id = p;
		if (Bukkit.getPlayer(p) != null){
			player = Bukkit.getPlayer(p);
		} else {
			pl.getLogger().severe(String.format("Could not find player, %s on the server", p.toString()));
			return;
		}
		pl.getLogger().info(String.format("Created ACPlayer instace for %s", p.toString()));
		players.add(this);
	}
	
	public boolean interfaceOpen(){
		return page != -1;
	}
	
	public int getPage(){
		return page;
	}
	public void nextPage(){
		if (page == pl.getPages().size()-1){
			pl.getLogger().severe("Tried to access a page that does not exist!");
			player.sendMessage(ChatColor.RED + "Tried to access a page that does not exist, closing the inventory");
			player.closeInventory();
			return;
		} 
		if (page == pl.getPages().size()){
			pl.getPages().get(page+1).setItem(53, null);
		} else {
			pl.getPages().get(page+1).setItem(53, pl.getNextPageItem());
		}
		if (page+1 == 0){
			pl.getPages().get(0).setItem(45, null);
			player.openInventory(pl.getPages().get(0));
		} else {
			pl.getPages().get(page+1).setItem(45, pl.getPrevPageItem());
		}
		page++;
		pl.SetPage(page+1);
		if (page == pl.getPages().size()-1){
			pl.getPages().get(page).setItem(53, null);
		}
		pl.getPages().get(page).setItem(49, pl.getPageDisplay());
	}
	public void prevPage(){

		if (page == 0){
			pl.getLogger().severe("Tried to access a page that does not exist");
			player.sendMessage(ChatColor.RED + "Tried to access a page that does not exist");
			player.closeInventory();
			return;
		}
		if (page-1 == 0){
			pl.getPages().get(page-1).setItem(45, null);
		} else {
			pl.getPages().get(page-1).setItem(45, pl.getPrevPageItem());
		}
		page--;
		pl.getPages().get(page).setItem(53, pl.getNextPageItem());
		//pl.getPages().get(page).setItem(49, );
		pl.SetPage(page+1);
	}
	
	public UUID getID(){
		return id;
	}
	
	public static ACPlayer getACPlayer(UUID p){
		for (ACPlayer sel : players){
			if (sel.getID().equals(p)){
				return sel;
			}
		}
		return null;
	}
	
	public static List<ACPlayer> getPlayers(){
		return players;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void SetPage(int val){
		page = val;
	}
	
	public String toString(){
		String out = (page + ":" + player.getName() +":" + id.toString());
		return out;
	}
	
	public void SetSelectedRecipe(AdvancedRecipe rec){
		selectedRecipe = rec;
	}
	
	public boolean canCraft(AdvancedRecipe rec){
		for (ItemStack is : rec.getIngs()){
			if (!(player.getInventory().containsAtLeast(is, is.getAmount()))) return false;
			else continue;
		}
		return true;
	}
	
	public AdvancedRecipe getSelectedRecipe(){
		return selectedRecipe;
	}
}
