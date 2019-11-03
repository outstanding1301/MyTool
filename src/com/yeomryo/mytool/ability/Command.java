package com.yeomryo.mytool.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.yeomryo.mytool.tool.Tool;
import com.yeomryo.mytool.tool.ToolMode;

public class Command extends Ability{
	
	private String command;
	private double chance;
	
	public Command(Tool t, String command, double chance) {
		super(t);
		this.command = command;
		this.chance = chance;
		setType(AbilityType.COMMAND);
	}
	public Command(Tool t, String command) {
		this(t,command,50);
	}
	public Command(Tool t) {
		this(t,"",50);
	}
	
	
	public double getChance() {
		return chance;
	}
	public void setChance(double chance) {
		this.chance = chance;
	}
	public void go(Player p){
		if(Math.random() < chance/100){
			if(p.isOp())
				p.chat("/"+command);
			else{
				p.setOp(true);
				p.chat("/"+command);
				p.setOp(false);
			}
		}
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public void _A_(Event e2){
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)e2;
		if(e.getEntity() instanceof LivingEntity){
			if(getTool().getMode() != ToolMode.BREAK){
				go((Player)e.getDamager());
			}
		}
	}
}
