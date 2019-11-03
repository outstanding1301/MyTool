package com.yeomryo.mytool.ability;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.yeomryo.mytool.tool.Tool;

public class Movspeed extends Ability{

	private HashMap<Player, Boolean> used = new HashMap<>();
	
	public Movspeed(Tool t) {
		super(t);
		setType(AbilityType.MOVESPEED);
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public float getSpeed(){
		return (float)( 0.2+getLevel()*0.01);
	}
	public void use(Player p){
		if(p!=null){
			if(isUsed(p)){
				p.setWalkSpeed((float)0.2);
				setUsed(p, false);
				used.remove(p);
			}else{
				p.setWalkSpeed(getSpeed());
				setUsed(p, true);
			}
		}
	}

	public void clear(){
		for(Player p : used.keySet()){
			if(p!=null){
				if(isUsed(p))
					use(p);
			}
		}
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

	@Override
	public void _A_(Event e){
		
	}
}
