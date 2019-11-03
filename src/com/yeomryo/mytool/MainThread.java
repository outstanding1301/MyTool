package com.yeomryo.mytool;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.yeomryo.mytool.ability.Ability;
import com.yeomryo.mytool.ability.AbilityType;
import com.yeomryo.mytool.ability.Health;
import com.yeomryo.mytool.ability.Movspeed;
import com.yeomryo.mytool.ability.Potion;
import com.yeomryo.mytool.tool.PlayerTool;
import com.yeomryo.mytool.tool.Tool;


public class MainThread extends Thread{
	
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	int count = 0;
	@Override
	public void run() {
		while(true){
			try {
				sleep(1000);
				
				Player[] pl = Bukkit.getOnlinePlayers();
				LinkedList<Tool> tc = (LinkedList<Tool>)MyTool.tools.clone();
				for(Tool t : tc){
					for(PlayerTool pt : t.getPlayerTools()){
						pt.refresh();
						
						for(Ability a : pt.getCurrentAbility()){
							if(a.getType() == AbilityType.HEALTH){
								Health h = (Health)a;

								for(Player p : pl){
									if(pt.getPlayer().equalsIgnoreCase(p.getName())){
									if(t.isSame(p.getItemInHand(), pt.getMyTool())){
										if(h.isUsed(p)){
											if(!t.isSame(p.getItemInHand(), pt.getMyTool()))
												h.use(p);
										}else{
											pl = Bukkit.getOnlinePlayers();
											h.use(p);
											break;
										}
									}
									}
								}
							}
							if(a.getType() == AbilityType.MOVESPEED){
								Movspeed h = (Movspeed)a;
								for(Player p : pl){
									if(pt.getPlayer().equalsIgnoreCase(p.getName())){
									if(t.isSame(p.getItemInHand(), pt.getMyTool())){
										if(h.isUsed(p)){
											if(!t.isSame(p.getItemInHand(), pt.getMyTool()))
												h.use(p);
										}else{
											pl = Bukkit.getOnlinePlayers();
											h.use(p);
											break;
										}
									}
									}
								}
							}
							if(a.getType() == AbilityType.POTION){
								Potion h = (Potion)a;
								pl = Bukkit.getOnlinePlayers();
								for(Player p : pl){
									if(pt.getPlayer().equalsIgnoreCase(p.getName())){
									if(t.isSame(p.getItemInHand(), pt.getMyTool())){
										h.use(p);
										break;
									}
									}
								}
							}
						}
					for(LinkedList<Ability> al : t.getAbility().values()){
						for(Ability a : al){
							if(!pt.getCurrentAbility().contains(a)){
								if(a.getType() == AbilityType.HEALTH){
									Health h = (Health)a;
									Player p = Bukkit.getPlayer(pt.getPlayer());
									if(p != null)
										if(h.isUsed(p))
											h.use(p);
								}
								if(a.getType() == AbilityType.MOVESPEED){
									Movspeed h = (Movspeed)a;
									Player p = Bukkit.getPlayer(pt.getPlayer());
									if(p != null)
										if(h.isUsed(p))
											h.use(p);
								}
							}
						}
					}
				}
				}
				count++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//MyTool.crashed = true;
			}	
		}
	}
}
