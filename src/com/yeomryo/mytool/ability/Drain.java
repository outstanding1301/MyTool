package com.yeomryo.mytool.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.yeomryo.mytool.tool.Tool;

public class Drain extends Ability{

	public Drain(Tool t) {
		super(t);
		setType(AbilityType.DRAIN);
	}
	public void drain(Player p, LivingEntity t){
		if(t.getNoDamageTicks() == 0){
			if(p.getHealth()+getLevel() <= p.getMaxHealth()){
				p.setHealth(p.getHealth()+getLevel());
			}else{
				p.setHealth(p.getMaxHealth());
			}
		}
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	@Override
	public void _A_(Event e2){
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)e2;
		if(e.getEntity() instanceof LivingEntity){
			drain((Player)e.getDamager(),(LivingEntity)e.getEntity());
		}
	}
}
