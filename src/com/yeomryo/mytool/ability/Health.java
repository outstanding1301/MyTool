package com.yeomryo.mytool.ability;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.yeomryo.mytool.tool.Tool;

public class Health extends Ability{
	
	private HashMap<Player, Boolean> used = new HashMap<>();
	
	public Health(Tool t) {
		super(t);
		setType(AbilityType.HEALTH);
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public boolean containsPlayer(Player p) {
		return used.containsKey(p);
	}

	public void setPlayer(Player player) {
		used.put(player, false);
	}

	public boolean isUsed(Player player) {
		used.putIfAbsent(player, false);
		return used.get(player);
	}

	public void setUsed(Player player, boolean used) {
		this.used.put(player, used);
	}
	
	public void clear(){
		for(Player p : used.keySet()){
			if(p!=null){
				if(isUsed(p))
					use(p);
			}
		}
	}

	public void use(Player p){
		if(p!=null){
			if(isUsed(p)){
				if(p.getHealth() > p.getMaxHealth()-getLevel())
					p.setHealth(p.getMaxHealth()-getLevel());
				p.setMaxHealth(p.getMaxHealth()-getLevel());
				setUsed(p, false);
				used.remove(p);
			}else{
				p.setMaxHealth(p.getMaxHealth()+getLevel());
				setUsed(p, true);
			}
		}
	}
	
	@Override
	public void _A_(Event e){
		
	}
}
