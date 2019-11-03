package com.yeomryo.mytool;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.yeomryo.mytool.ability.Ability;
import com.yeomryo.mytool.ability.AbilityType;
import com.yeomryo.mytool.ability.Command;
import com.yeomryo.mytool.ability.Health;
import com.yeomryo.mytool.ability.Movspeed;
import com.yeomryo.mytool.tool.BreakData;
import com.yeomryo.mytool.tool.PlayerTool;
import com.yeomryo.mytool.tool.Tool;
import com.yeomryo.mytool.tool.ToolMode;

public class EventManager implements Listener{
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player)e.getDamager();
			if(p.getItemInHand() != null){
				for(Tool t : MyTool.tools){
					for(PlayerTool pt : t.getPlayerTools()){
						if(pt.getPlayer().equalsIgnoreCase(p.getName())){
						if(t.isSame(pt.getMyTool(), p.getItemInHand())){
							for(Ability a : pt.getCurrentAbility()){
								if(a.getType() == AbilityType.ATTACKSPEED || a.getType() == AbilityType.DRAIN || a.getType() == AbilityType.COMMAND){
									a._A_(e);
									return;
								}
							}
						}
						}
					}
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamageEvent(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(p.getItemInHand() != null){
				for(Tool t : MyTool.tools){
					for(PlayerTool pt : t.getPlayerTools()){
						if(pt.getPlayer().equalsIgnoreCase(p.getName())){
						if(t.isSame(pt.getMyTool(), p.getItemInHand())){
							for(Ability a : pt.getCurrentAbility()){
								if(a.getType() == AbilityType.ARMOUR){
									a._A_(e);
									return;
								}
							}
						}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onEntityRegainHealthEvent(EntityRegainHealthEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(p.getItemInHand() != null){
				for(Tool t : MyTool.tools){
					for(PlayerTool pt : t.getPlayerTools()){
						if(pt.getPlayer().equalsIgnoreCase(p.getName())){
						if(t.isSame(pt.getMyTool(), p.getItemInHand())){
							for(Ability a : pt.getCurrentAbility()){
								if(a.getType() == AbilityType.REGENERATION){
									a._A_(e);
									return;
								}
							}
						}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onout(PlayerQuitEvent e){
		Player p = e.getPlayer();
		for(Tool t : MyTool.tools){
			for(PlayerTool pt : t.getPlayerTools()){
				if(pt.getPlayer().equalsIgnoreCase(p.getName())){
				for(Ability a : pt.getCurrentAbility()){
					if(a.getType() == AbilityType.HEALTH){
						Health h = (Health)a;
						if(h.isUsed(p)){
							h.use(p);
							return;
						}
					}if(a.getType() == AbilityType.MOVESPEED){
						Movspeed h = (Movspeed)a;
						if(h.isUsed(p)){
							h.use(p);
							return;
						}
					}
				}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onBlockBreakEvent(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(p.getItemInHand() != null){
			for(Tool t : MyTool.tools){
				for(PlayerTool pt : t.getPlayerTools()){
					if(pt.getPlayer().equalsIgnoreCase(p.getName())){
					if(t.isSame(pt.getMyTool(), p.getItemInHand())){
						if(t.getMode() == ToolMode.BREAK){
							for(BreakData bd : t.getBREAK()){
								if(bd.getCode_BREAK() == e.getBlock().getTypeId()){
									if(Math.random() < bd.getChance_BREAK()/100){
										pt.addExp(bd.getExp_BREAK());
										for(Ability a : pt.getCurrentAbility()){
											if(a.getType() == AbilityType.COMMAND){
												((Command)a).go(p);
												return;
											}
										}
									}
								}
							}
						}
						}
					}
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDeathEvent(EntityDeathEvent e) {
		Player p = e.getEntity().getKiller();
		if(p!=null){
			if(p.getItemInHand() != null){
				for(Tool t : MyTool.tools){
					for(PlayerTool pt : t.getPlayerTools()){
						if(pt.getPlayer().equalsIgnoreCase(p.getName())){
						if(t.isSame(pt.getMyTool(), p.getItemInHand())){
							if(t.getMode() == ToolMode.ENTITY_KILL){
								pt.addExp(1);
								return;
							}
						}
					}
					}
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity().getKiller();
		if(p!=null){
			if(p.getItemInHand() != null){
				for(Tool t : MyTool.tools){
					for(PlayerTool pt : t.getPlayerTools()){
						if(pt.getPlayer().equalsIgnoreCase(p.getName())){
						if(t.isSame(pt.getMyTool(), p.getItemInHand())){
							if(t.getMode() == ToolMode.KILL){
								pt.addExp(1);
								return;
							}
						}
					}
					}
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(p!=null){
			if(p.getItemInHand() != null){
				for(Tool t : MyTool.tools){
					for(PlayerTool pt : t.getPlayerTools()){
						if(pt.getPlayer().equalsIgnoreCase(p.getName())){
						if(t.isSame(pt.getMyTool(), p.getItemInHand())){
							e.setCancelled(true);
							return;
						}
					}
					}
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onshiftright(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
			if(p!=null){
				if(p.getItemInHand() != null){
					for(Tool t : MyTool.tools){
						for(PlayerTool pt : t.getPlayerTools()){
							if(pt.getPlayer().equalsIgnoreCase(p.getName())){
							if(t.isSame(pt.getMyTool(), p.getItemInHand())){
								LinkedList<PlayerTool> tl = t.getRank();
								p.sendMessage(MyTool.pf+"§b마이툴 랭킹");
								for(PlayerTool z : tl){
									p.sendMessage(MyTool.pf+"§6"+(tl.indexOf(z)+1)+"§f위 - "+z.getPlayer()+"(Lv.§a"+z.getLevel()+"§f, Exp. §e"+z.getExp()+"§f)");
								}
								return;
							}
						}
					}
					}
				}
			}
		}
	}
}
