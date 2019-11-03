package com.yeomryo.mytool.tool;

public enum ToolMode {
	KILL,
	ENTITY_KILL,
	BREAK;
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
}
