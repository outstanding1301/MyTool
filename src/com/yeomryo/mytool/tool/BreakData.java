package com.yeomryo.mytool.tool;

public class BreakData {
	private int code_BREAK;
	private double chance_BREAK;
	private int exp_BREAK;
	public BreakData(int id, double chance, int exp) {
		code_BREAK = id;
		chance_BREAK = chance;
		exp_BREAK = exp;
	}
	public int getCode_BREAK() {
		return code_BREAK;
	}
	public void setCode_BREAK(int code_BREAK) {
		this.code_BREAK = code_BREAK;
	}
	public double getChance_BREAK() {
		return chance_BREAK;
	}
	public void setChance_BREAK(double chance_BREAK) {
		this.chance_BREAK = chance_BREAK;
	}
	public int getExp_BREAK() {
		return exp_BREAK;
	}
	public void setExp_BREAK(int exp_BREAK) {
		this.exp_BREAK = exp_BREAK;
	}
	
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
}
