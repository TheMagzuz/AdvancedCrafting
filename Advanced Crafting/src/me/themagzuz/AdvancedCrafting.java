package me.themagzuz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedCrafting extends JavaPlugin{
	
	@Override
	public void onEnable(){
		
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	
		if (args.length >= 1){
			
			//TODO: Add commands
			
		}
		
		return true;
	}
	
}
