package com.yeomryo.mytool.ability;

public enum AbilityType {
	HEALTH,
	ARMOUR,
	ATTACKSPEED,
	DRAIN,
	MOVESPEED,
	POTION,
	REGENERATION,
	COMMAND;
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
}
