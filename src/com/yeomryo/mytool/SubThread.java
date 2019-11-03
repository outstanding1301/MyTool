package com.yeomryo.mytool;

import org.bukkit.Bukkit;

public class SubThread extends Thread{
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	@Override
	public void run() {
		while(true){
			try {
				sleep(1000);
				if(MyTool.crashed){
					Bukkit.broadcastMessage("씨발좆같네");
					MyTool.crashed = false;
					MyTool.mainthread = new MainThread();
					MyTool.mainthread.start();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
