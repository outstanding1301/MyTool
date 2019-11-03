package com.yeomryo.mytool.ability;

import org.bukkit.event.Event;

import com.yeomryo.mytool.tool.Tool;

public class Ability {
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	private Tool tool;
	private int level;
	private int toolLevel;
	private AbilityType type;
	
	public Ability(Tool t) {
		tool = t;
	}
	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public int getLevel() {
		return level;
	}
	public int getToolLevel() {
		return toolLevel;
	}
	public void setToolLevel(int toolLevel) {
		this.toolLevel = toolLevel;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public AbilityType getType() {
		return type;
	}
	public void setType(AbilityType type) {
		this.type = type;
	}
	
	public void _A_(Event e){}
}
