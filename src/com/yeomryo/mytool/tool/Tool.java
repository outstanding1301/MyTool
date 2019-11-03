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
import java.util.logging.Level;

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

public class Tool {
	private ItemStack item;
	private String name;
	private int maxLevel;
	private int maxExp;
	private ToolMode mode;
	private LinkedList<BreakData> BREAK = new LinkedList<>();
	private HashMap<Integer, LinkedList<Ability>> ability = new HashMap<Integer, LinkedList<Ability>>();
	private List<String> loreFormat = new ArrayList<>();
	private LinkedList<PlayerTool> playerTools = new LinkedList<>();
	
	public Tool(String name) {
		this(name, 100, ToolMode.KILL);
	}
	
	public Tool(String name, int max, ToolMode mode) {
		this.name = name;
		this.maxLevel = max;
		this.maxExp = 100;
		this.mode = mode;
		loreFormat = new ArrayList<>();
		loreFormat.add("&9[도구 레벨] &6+Min &f/ &c+Max");
		loreFormat.add("&9[경험치] &6+ExpMin &f/ &c+ExpMax");
		loreFormat.add("&9[경험치] +Line");
		loreFormat.add("&9[마이툴순위] &f+Top위");
	}
	public PlayerTool createPlayerTool(String player){
		PlayerTool pt = new PlayerTool(name, maxLevel, mode);
		pt.setPlayer(player);
		pt.setAbility(ability);
		pt.setBREAK(BREAK);
		pt.setLevel(1);
		pt.setLoreFormat(loreFormat);
		pt.setMode(mode);
		pt.setParent(this);
		pt.setItem(item);
		playerTools.add(pt);
		return pt;
	}
	
	public PlayerTool getPlayerTool(String player){
		for(PlayerTool p : playerTools){
			if(p.getPlayer().equalsIgnoreCase(player)){
				return p;
			}
		}
		return null;
	}
	
	public static boolean isSame(ItemStack is, ItemStack is2){
		if(is != null){
			if(is.getType() == is2.getType()){
				if(is2.getItemMeta().hasDisplayName() && is.getItemMeta().hasDisplayName()){
					if(is2.getItemMeta().getDisplayName().equals(is.getItemMeta().getDisplayName())){
						if(is2.getItemMeta().hasLore() && is.getItemMeta().hasLore()){
							if(is2.getItemMeta().getLore().get(is2.getItemMeta().getLore().size()-1).subSequence(0, 5)
									.equals(is.getItemMeta().getLore().get(is.getItemMeta().getLore().size()-1).subSequence(0, 5))){

								return true;
							}
						}
					}
				}else if(!is.getItemMeta().hasDisplayName() && !is.getItemMeta().hasDisplayName()){
					if(is2.getItemMeta().hasLore() && is.getItemMeta().hasLore()){
						if(is2.getItemMeta().getLore().get(is2.getItemMeta().getLore().size()-1).subSequence(0, 5)
								.equals(is.getItemMeta().getLore().get(is.getItemMeta().getLore().size()-1).subSequence(0, 5))){

							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public void refresh(){
		for(PlayerTool pt : playerTools){
			pt.refresh();
		}
	}
	public LinkedList<PlayerTool> getPlayerTools() {
		return playerTools;
	}

	public void setPlayerTools(LinkedList<PlayerTool> playerTools) {
		this.playerTools = playerTools;
	}

	public List<String> getLoreFormat(){
		return loreFormat;
	}
	
	public void setLoreFormat(List<String> l){
		this.loreFormat = l;
	}
	
	public void remove(){
		for(LinkedList<Ability> al : ability.values()){
			for(Ability a : al){
				if (a.getType() == AbilityType.HEALTH)
					((Health) a).clear();
				if (a.getType() == AbilityType.MOVESPEED)
					((Health) a).clear();
			}
		}
		new File(MyTool.dataFoler,"\\MyTools\\"+name+".yml").delete();
		MyTool.tools.remove(this);
	}
	
	public static  Tool getTool(String name){
		for(Tool mt : MyTool.tools){
			if(mt.getName().equalsIgnoreCase(name))
				return mt;
		}
		return null;
	}
	
	public int getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}
	
	public void save(){
		YamlConfiguration yc = YConfig.getToolforsave(ChatColor.stripColor(name)+".yml");
		yc.set("이름", name);
		yc.set("최대레벨", maxLevel);
		yc.set("최대경험치", maxExp);
		yc.set("아이템", item);
		yc.set("모드", mode.toString());
		if(mode == ToolMode.BREAK){
			yc.set("브레이크.사이즈", BREAK.size());
			for(BreakData bd : BREAK){
				yc.set("브레이크."+BREAK.indexOf(bd)+".ID", bd.getCode_BREAK());
				yc.set("브레이크."+BREAK.indexOf(bd)+".확률", bd.getChance_BREAK());
				yc.set("브레이크."+BREAK.indexOf(bd)+".경험치", bd.getExp_BREAK());
			}
		}
		LinkedList<Integer> ks = new LinkedList<>(ability.keySet());
		Collections.sort(ks);
		yc.set("능력.키", ks);
		for(int i : ability.keySet()){
			LinkedList<Ability> al = ability.get(i);
			yc.set("능력."+i+".키사이즈", al.size());
			for(Ability a : al){
				yc.set("능력."+i+"."+al.indexOf(a)+".종류", a.getType().toString());
				if(a.getType() == AbilityType.ARMOUR){
					Armour q = (Armour)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
				if(a.getType() == AbilityType.ATTACKSPEED){
					Atkspeed q = (Atkspeed)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
				if(a.getType() == AbilityType.COMMAND){
					Command q = (Command)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".명령어", q.getCommand());
					yc.set("능력."+i+"."+al.indexOf(a)+".확률", q.getChance());
				}
				if(a.getType() == AbilityType.DRAIN){
					Drain q = (Drain)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
				if(a.getType() == AbilityType.HEALTH){
					Health q = (Health)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
				if(a.getType() == AbilityType.MOVESPEED){
					Movspeed q = (Movspeed)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
				if(a.getType() == AbilityType.POTION){
					Potion q = (Potion)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".Id", q.getId());
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
				if(a.getType() == AbilityType.REGENERATION){
					Regen q = (Regen)a;
					yc.set("능력."+i+"."+al.indexOf(a)+".강도", q.getLevel());
				}
			}
		}
		List<String> players = new ArrayList<>();
		for(PlayerTool pt : playerTools){
			players.add(pt.getPlayer());
			yc.set("플레이어."+pt.getPlayer()+".레벨", pt.getLevel());
			yc.set("플레이어."+pt.getPlayer()+".경험치", pt.getExp());
		}
		yc.set("플레이어목록", players);
		yc.set("lore_format", loreFormat);
		try {
			yc.save(new File(MyTool.dataFoler,"\\MyTools\\"+name+".yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Tool load(File f){
		YamlConfiguration yc = YConfig.getTool(f.getName());
		String name = yc.getString("이름");
		int maxLevel = yc.getInt("최대레벨");
		int maxExp = yc.getInt("최대경험치");
		ItemStack item = yc.getItemStack("아이템");
		ToolMode mode = ToolMode.valueOf(yc.getString("모드"));
		LinkedList<BreakData> bdl = new LinkedList<>();
		if(mode == ToolMode.BREAK){
			int size = yc.getInt("브레이크.사이즈");
			for(int i=0;i<size;i++){
				int id = yc.getInt("브레이크."+i+".ID");
				double chance = yc.getDouble("브레이크."+i+".확률");
				int exp2 = yc.getInt("브레이크."+i+".경험치");
				BreakData bd = new BreakData(id, chance, exp2);
				bdl.add(bd);
			}
		}
		Tool t = new Tool(name, maxLevel, mode);
		t.setMaxExp(maxExp);
		t.setItem(item);
		t.setBREAK(bdl);

		HashMap<Integer, LinkedList<Ability>> ability = new HashMap<Integer, LinkedList<Ability>>();
		
		for(int i : yc.getIntegerList("능력.키")){
			int size2 = yc.getInt("능력."+i+".키사이즈");
			LinkedList<Ability> al = new LinkedList<>();
			for(int j = 0;j<size2;j++){
				AbilityType type = AbilityType.valueOf(yc.getString("능력."+i+"."+j+".종류"));
				Ability a = null;
				if(type == AbilityType.ARMOUR){
					a = new Armour(t);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.ARMOUR);
				}
				if(type == AbilityType.ATTACKSPEED){
					a = new Atkspeed(t);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.ATTACKSPEED);
				}
				if(type == AbilityType.COMMAND){
					String command = yc.getString("능력."+i+"."+j+".명령어");
					double chance = yc.getDouble("능력."+i+"."+j+".확률");
					a = new Command(t, command, chance);
					a.setType(AbilityType.COMMAND);
				}
				if(type == AbilityType.DRAIN){
					a = new Drain(t);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.DRAIN);
				}
				if(type == AbilityType.HEALTH){
					a = new Health(t);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.HEALTH);
				}
				if(type == AbilityType.MOVESPEED){
					a = new Movspeed(t);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.MOVESPEED);
				}
				if(type == AbilityType.POTION){
					PotionEffectType effect = PotionEffectType.getById(yc.getInt("능력."+i+"."+j+".Id"));
					a = new Potion(t, effect);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.POTION);
				}
				if(type == AbilityType.REGENERATION){
					a = new Regen(t);
					a.setLevel(yc.getInt("능력."+i+"."+j+".강도"));
					a.setType(AbilityType.REGENERATION);
				}
				a.setToolLevel(i);
				if(a!=null)
				al.add(a);
			}
			ability.put(i, al);
		}
		
		t.setAbility(ability);
		t.setLoreFormat(yc.getStringList("lore_format"));
		List<String> players = yc.getStringList("플레이어목록");
		if(players != null)
		for(String s : players){
			int lev = yc.getInt("플레이어."+s+".레벨");
			int exp = yc.getInt("플레이어."+s+".경험치");
			PlayerTool pt = t.createPlayerTool(s);
			pt.setLevel(lev);
			pt.setExp(exp);
		}
		return t;
	}
	
	public LinkedList<PlayerTool> getRank() {
		LinkedList<PlayerTool> tl = new LinkedList<>();
		HashMap<PlayerTool, Double> pti = new HashMap<>();
		for (PlayerTool t : getPlayerTools()) {
			pti.put(t, t.getLevel() + ((double) t.getExp() / getMaxExp()));
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
	
	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public ToolMode getMode() {
		return mode;
	}
	public void setMode(ToolMode mode) {
		this.mode = mode;
	}
	public LinkedList<BreakData> getBREAK() {
		return BREAK;
	}
	public void setBREAK(LinkedList<BreakData> bREAK) {
		BREAK = bREAK;
	}

	public HashMap<Integer, LinkedList<Ability>> getAbility() {
		return ability;
	}

	public void setAbility(HashMap<Integer, LinkedList<Ability>> ability) {
		this.ability = ability;
	}
	
	public void addAbility(int level, Ability ability){
		LinkedList<Ability> al = this.ability.getOrDefault(level, new LinkedList<>());
		LinkedList<Ability> alc = (LinkedList<Ability>)al.clone();
		for(Ability a : alc)
			if(a.getType() == ability.getType())
				al.remove(a);
		al.add(ability);
		this.ability.put(level, al);
	}
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
}
