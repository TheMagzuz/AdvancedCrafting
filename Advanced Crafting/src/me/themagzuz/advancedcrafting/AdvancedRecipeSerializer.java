package me.themagzuz.advancedcrafting;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class AdvancedRecipeSerializer implements ConfigurationSerializable{

	
	public AdvancedRecipeSerializer(){
		
	}
	
	
	@Override
	public Map<String, Object> serialize() {
		return null;
	}
	
	public AdvancedRecipeSerializer deserialize(Map<String, Object> map){
		return this;
	}

}
