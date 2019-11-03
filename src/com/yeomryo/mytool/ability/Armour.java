package com.yeomryo.mytool.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import com.yeomryo.mytool.tool.Tool;

public class Armour extends Ability{
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public Armour(Tool t) {
		super(t);
		setType(AbilityType.ARMOUR);
	}
	public int getArmour(){
		return getLevel()*1;
	}
	
	@Override
	public void _A_(Event e2){
		EntityDamageEvent e = (EntityDamageEvent)e2;
		e.setDamage(e.getDamage() - getArmour() > 0 ? e.getDamage() - getArmour() : 1);
	}
}
