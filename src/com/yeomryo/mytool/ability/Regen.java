package com.yeomryo.mytool.ability;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.yeomryo.mytool.tool.Tool;

public class Regen extends Ability{

	public Regen(Tool t) {
		super(t);
		setType(AbilityType.REGENERATION);
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	@Override
	public void _A_(Event e2){
		EntityRegainHealthEvent e = (EntityRegainHealthEvent)e2;
		e.setAmount(e.getAmount()+getLevel());
	}
}
