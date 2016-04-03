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

	/**
	 * The plugin instance
	 */
	
	private AdvancedCrafting pl = AdvancedCrafting.getPl();
	
	
	/**
	 * The page that the player is currently on<br>
	 * -1 if interface is not open.<br>
	 * Uses standard array indexing (Starts at 0)<br>
	 */
	private int page = -1;
	
	/**
	 * The UUID of the player that this represents
	 */
	
	private UUID id;
	
	/**
	 * The player that this represents
	 */
	
	private Player player;
	
	/**
	 * A list of all the players on the server
	 */
	
	private static List<ACPlayer> players = new ArrayList<ACPlayer>();
	
	/**
	 * The recipe that this player is currently viewing<br>
	 * null if none
	 */
	
	private AdvancedRecipe selectedRecipe;
	
	/**
	 * 
	 * @param p The UUID of the player that an instance is being created for
	 */
	
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
	
	/**
	 * 
	 * @return <b>True</b> if the recipe selection GUI is currently open, otherwise <b>false</b> 
	 */
	
	public boolean interfaceOpen(){
		return page != -1;
	}
	
	/**
	 * 
	 * @return The page that the player is currently on.<br>
	 * <b>-1</b> if the recipe selection GUI is not open
	 */
	
	public int getPage(){
		return page;
	}
	
	/**
	 * Redirects the player to the next page of recipes
	 */
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
	/**
	 * Redirects the player to the previous page
	 */
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
	/**
	 * 
	 * @return The UUID of the represented player
	 */
	public UUID getID(){
		return id;
	}
	
	/**
	 * 
	 * @param p The player to get the instance from
	 * @return The ACPlayer instance of {@code p}
	 */
	public static ACPlayer getACPlayer(UUID p){
		for (ACPlayer sel : players){
			if (sel.getID().equals(p)){
				return sel;
			}
		}
		return null;
	}
	/**
	 * 
	 * @return A list of all ACPlayer instances
	 */
	public static List<ACPlayer> getPlayers(){
		return players;
	}
	/**
	 * 
	 * @return The player that this instance represents
	 */
	public Player getPlayer(){
		return player;
	}
	/**
	 * 
	 * @param val The page to redirect the player to
	 */
	public void SetPage(int val){
		page = val;
	}
	/**
	 * @return A string in the following format: page:playerName:UUID
	 */
	public String toString(){
		String out = (page + ":" + player.getName() +":" + id.toString());
		return out;
	}
	
	/**
	 * Changes the recipe that the player currently has selected
	 * @param rec The recipe to select
	 */
	public void SetSelectedRecipe(AdvancedRecipe rec){
		selectedRecipe = rec;
	}
	
	/**
	 * 
	 * @param rec The recipe to check
	 * @return <b>True</b> if the player has the required materials to craft the given recipe.
	 * Otherwise <b>false</b>
	 */
	public boolean canCraft(AdvancedRecipe rec){
		for (ItemStack is : rec.getIngs()){
			if (!(player.getInventory().containsAtLeast(is, is.getAmount()))) return false;
			else continue;
		}
		return true;
	}
	/**
	 * 
	 * @return The recipe that the player currently has selected
	 */
	public AdvancedRecipe getSelectedRecipe(){
		return selectedRecipe;
	}
}
