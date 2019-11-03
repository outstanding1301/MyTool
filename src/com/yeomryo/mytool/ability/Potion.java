package com.yeomryo.mytool.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.yeomryo.mytool.tool.Tool;

public class Potion extends Ability{
	
	private PotionEffectType effect;
	
	public Potion(Tool t, PotionEffectType effect) {
		super(t);
		this.effect = effect;
		setType(AbilityType.POTION);
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public void use(Player p){
		p.removePotionEffect(effect);
		p.addPotionEffect(new PotionEffect(effect, 40, getLevel()-1));
	}
	
	public PotionEffectType getEffect() {
		return effect;
	}

	public void setEffect(PotionEffectType effect) {
		this.effect = effect;
	}

	
	public int getId() {
		return effect.getId();
	}

	@Override
	public void _A_(Event e){
		
	}
}
