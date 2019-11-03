package com.yeomryo.mytool.tool;

import java.io.File;
import java.io.IOException;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import com.yeomryo.mytool.MyTool;
import com.yeomryo.mytool.YConfig;
import com.yeomryo.mytool.ability.Ability;
import com.yeomryo.mytool.ability.AbilityType;
import com.yeomryo.mytool.ability.Armour;
import com.yeomryo.mytool.ability.Atkspeed;
import com.yeomryo.mytool.ability.Command;
import com.yeomryo.mytool.ability.Drain;
import com.yeomryo.mytool.ability.Health;
import com.yeomryo.mytool.ability.Movspeed;
import com.yeomryo.mytool.ability.Potion;
import com.yeomryo.mytool.ability.Regen;

public class PlayerTool extends Tool{
	private String player;
	private ItemStack itemTool;
	private Tool parent;
	private int level;
	private int exp;

	public PlayerTool(String name) {
		this(name, 100, ToolMode.KILL);
	}

	public PlayerTool(String name, int max, ToolMode mode) {
		super(name, max, mode);
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public List<String> getLore() {
		List<String> lore = new ArrayList<>();
		if (getParent().getItem().hasItemMeta())
			if (getParent().getItem().getItemMeta().hasLore())
				lore = new ArrayList<>(getParent().getItem().getItemMeta().getLore());
		for (String line : getParent().getLoreFormat()) {
			lore.add(line.replaceAll("&", "§").replaceAll("\\+Min", "" + getLevel())
					.replaceAll("\\+Max", "" + getParent().getMaxLevel()).replaceAll("\\+ExpMin", "" + getExp())
					.replaceAll("\\+ExpMax", "" + (getLevel() < getParent().getMaxLevel() ? getParent().getMaxExp() : ""))
					.replaceAll("\\+Line", getExpLine())
					.replaceAll("\\+Top", "" + getRanking()));
		}
		return lore;
	}

	public String getExpLine() {
		String s = "";
		if(getLevel() < getParent().getMaxLevel()){
			for (int i = 0; i < ((double) getExp() / getParent().getMaxExp()) * MyTool.GAGE_AMOUNT; i++) {
				s += "§" + MyTool.GAGE_C1 + MyTool.GAGE_CHR;
			}
			for (int i = 0; i < MyTool.GAGE_AMOUNT - ((double) getExp() / getParent().getMaxExp()) * MyTool.GAGE_AMOUNT; i++) {
				s += "§" + MyTool.GAGE_C2 + MyTool.GAGE_CHR;
			}
		}else{
			for (int i = 0; i < MyTool.GAGE_AMOUNT; i++) {
				s += "§" + MyTool.GAGE_C1 + MyTool.GAGE_CHR;
			}
		}
		return s;
	}

	public void remove() {
		for (LinkedList<Ability> al : getParent().getAbility().values()) {
			for (Ability a : al) {
				if (a.getType() == AbilityType.HEALTH)
						((Health) a).clear();
				if (a.getType() == AbilityType.MOVESPEED)
					((Health) a).clear();
				}
		}
		new File(MyTool.dataFoler, "\\MyTools\\" + getParent().getName() + ".yml").delete();
		MyTool.tools.remove(this);
	}

	public static PlayerTool getTool(String name, String player) {
		for (Tool mt : MyTool.tools) {
			if (mt.getName().equalsIgnoreCase(name)){
				for(PlayerTool pt : mt.getPlayerTools()){
					if(pt.getPlayer().equalsIgnoreCase(player)){
						return pt;
					}
				}
			}
		}
		return null;
	}

	public Tool getParent() {
		return parent;
	}

	public void setParent(Tool parent) {
		this.parent = parent;
	}

	public int getRanking() {
		int r = getRank().indexOf(this);
		return r + 1;
	}

	public ItemStack getMyTool() {
		return itemTool;
	}

	public void refresh() {
		Player[] pl = Bukkit.getOnlinePlayers();
		if (itemTool == null)
			itemTool = getParent().getItem().clone();
		ItemMeta im = itemTool.getItemMeta();
		im.setLore(getLore());
		for (Player p : pl) {
			for (int i = 0; i < p.getInventory().getSize(); i++) {
				if (p.getInventory().getItem(i) != null){
					if(getPlayer().equalsIgnoreCase(p.getName())){
						if (isSame(itemTool, p.getInventory().getItem(i))) {
							if(getParent().getPlayerTool(p.getName()) == null)
								getParent().createPlayerTool(p.getName());
							p.getInventory().getItem(i).setItemMeta(im);
						}
					}
				}
			}/*
			if (p.getOpenInventory() != null) {
				if (p.getOpenInventory().getTopInventory() != null) {
					for (int i = 0; i < p.getOpenInventory().getTopInventory().getSize(); i++) {
						if (p.getOpenInventory().getTopInventory().getItem(i) != null) {
							if (isSame(itemTool, p.getOpenInventory().getTopInventory().getItem(i))) {
								p.getOpenInventory().getTopInventory().getItem(i).setItemMeta(im);
							}
						}
					}
				}
			}*/
		}
		itemTool.setItemMeta(im);
	}
/*
	public void getParent().save(); {
		YamlConfiguration yc = YConfig.getPlayerToolforSave(ChatColor.stripColor(name), getPlayer()+".yml");
		yc.set("주인", player);
		yc.set("레벨", level);
		yc.set("경험치", exp);
		try {
			yc.save(new File(MyTool.dataFoler, "\\MyTools\\" + name + "\\"+getPlayer()+".yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static PlayerTool load(String nam, String playe) {
		YamlConfiguration yc = YConfig.getPlayerTool(nam, playe);
		
		String player = yc.getString("주인");
		int level = yc.getInt("레벨");
		int exp = yc.getInt("경험치");
		Tool t = Tool.getTool(nam);
		PlayerTool pt = t.createPlayerTool(player);
		pt.setLevel(level);
		pt.setExp(exp);
		pt.setPlayer(player);
		return pt;
	}
*/
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void addExp(int exp) {
		if (level < getParent().getMaxLevel()) {
			this.exp += exp;
			int i = this.exp / getParent().getMaxExp();
			addLevel(i);
			this.exp = this.exp % getParent().getMaxExp();
		}else{
			this.exp += exp;
		}
		refresh();
		getParent().save();
	}

	public void addLevel(int level) {
		if (level < getParent().getMaxLevel()) {
			this.level += level;
			for (int j = 0; j < level; j++) {
				msg("§6마이툴 레벨업! 현재 레벨 : §f" + this.level + " Lv");
			}
		}
		refresh();
		getParent().save();
	}

	public void msg(String s) {
		Player[] pl = Bukkit.getOnlinePlayers();
		for (Player p : pl) {
			if (isSame(p.getItemInHand(), getMyTool())) {
				p.sendMessage("§6[ §fMyTool §6]§f " + s);
				break;
			}
		}
	}

	void a() {
		new Thread(() -> {
			int i = 0;
		}).start();
	}

	public LinkedList<PlayerTool> getRank() {
		LinkedList<PlayerTool> tl = new LinkedList<>();
		HashMap<PlayerTool, Double> pti = new HashMap<>();
		for (PlayerTool t : getParent().getPlayerTools()) {
			pti.put(t, t.getLevel() + ((double) t.getExp() / getParent().getMaxExp()));
		}
		List<Double> intlist = new ArrayList<>(pti.values());
		Collections.sort(intlist, Collections.reverseOrder());
		for (double i : intlist) {
			for (PlayerTool t : pti.keySet()) {
				if (pti.get(t) == i) {
					if (!tl.contains(t)) {
						tl.add(t);
					}
				}
			}
		}
		return tl;
	}

	public LinkedList<Ability> getCurrentAbility() {
		LinkedList<Ability> al = new LinkedList<>();
		LinkedList<Integer> keys = new LinkedList<>(getParent().getAbility().keySet());
		Collections.sort(keys);
		for (int j : keys) {
			if (j <= level) {
				for (Ability a : getParent().getAbility().get(j)) { // 더 높은레벨
					LinkedList<Ability> alc = (LinkedList<Ability>) al.clone();
					boolean z = false;
					for (Ability a2 : alc) { // 낮은레벨
						if (a.getType() == a2.getType()) {
							z = true;
							if (a.getToolLevel() > a2.getToolLevel()) {
								al.remove(a2);
								al.add(a);
							}
						}
					}
					if (!z)
						al.add(a);
				}
			}
		}
		return al;
	}
}
