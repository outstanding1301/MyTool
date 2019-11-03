package com.yeomryo.mytool.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.yeomryo.mytool.tool.Tool;

public class Atkspeed extends Ability{
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public Atkspeed(Tool t) {
		super(t);
		setType(AbilityType.ATTACKSPEED);
	}
	public int getAtkspeed(){ // entity한테 해야함
		return 20-getLevel() > 0 ? 20-getLevel() : 0;
	}
	
	public void use(LivingEntity t){
		t.setNoDamageTicks(getAtkspeed());
	}
	
	@Override
	public void _A_(Event e2){
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)e2;
		if(e.getEntity() instanceof LivingEntity)
		use((LivingEntity)e.getEntity());
	}
}
