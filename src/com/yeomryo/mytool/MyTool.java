package com.yeomryo.mytool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedInsertTimestamp;
import com.yeomryo.mytool.ability.Ability;
import com.yeomryo.mytool.ability.AbilityType;
import com.yeomryo.mytool.ability.Armour;
import com.yeomryo.mytool.ability.Atkspeed;
import com.yeomryo.mytool.ability.Drain;
import com.yeomryo.mytool.ability.Health;
import com.yeomryo.mytool.ability.Movspeed;
import com.yeomryo.mytool.ability.Potion;
import com.yeomryo.mytool.ability.Regen;
import com.yeomryo.mytool.tool.BreakData;
import com.yeomryo.mytool.tool.PlayerTool;
import com.yeomryo.mytool.tool.Tool;
import com.yeomryo.mytool.tool.ToolMode;

public class MyTool extends JavaPlugin {
	
	public static LinkedList<Tool> tools = new LinkedList<>();
	public static MainThread mainthread = new MainThread();
	public static SubThread subthread = new SubThread();
	public static boolean crashed = false;
	public static File dataFoler;
	
	public static int GAGE_AMOUNT = 10;
	public static String GAGE_CHR = "-";
	public static String GAGE_C1 = "b";
	public static String GAGE_C2 = "7";
	
	public static String pf = "§6[ §fMyTool §6]§f";
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public void loadSetting(){
		YamlConfiguration yc = YConfig.getConfig();
		GAGE_AMOUNT = yc.getInt("게이지 갯수");
		GAGE_CHR = yc.getString("게이지 문양");
		GAGE_C1 = yc.getString("게이지 현재색상");
		GAGE_C2 = yc.getString("게이지 최대색상");
	}
	
	public static void saveSetting(){
		YamlConfiguration yc = YConfig.getConfigforsave();
		yc.set("게이지 갯수", GAGE_AMOUNT);
		yc.set("게이지 문양", GAGE_CHR);
		yc.set("게이지 현재색상", GAGE_C1);
		yc.set("게이지 최대색상", GAGE_C2);
		try {
			yc.save(new File(dataFoler, "config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEnable() {
		dataFoler = getDataFolder();
		
		loadSetting();
		loadToolFiles();
		
		getServer().getPluginManager().registerEvents(new EventManager(), this);
		
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println(" ");
		System.out.println("[ MyTool ] 제작 : 염료 (yeomryo@naver.com)");
		System.out.println("[ MyTool ] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("[ MyTool ] 플러그인이 활성화 되었습니다.");
		System.out.println("[ MyTool ] 본 플러그인의 저작권은 모두 염료에게 있습니다.");
		System.out.println(" ");
		System.out.println("[ MyTool ] 툴 정보를 불러왔습니다.");
		System.out.println("[ MyTool ] 등록된 툴 : "+tools.size()+"개");
		System.out.println(" ");
		System.out.println("[ MyTool ] 설정파일을 불러왔습니다.");
		System.out.println("[ MyTool ] 대부분의 게임 세부사항은 모두 config.yml에서 설정합니다.");
		System.out.println(" ");
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		
		mainthread = new MainThread();
		subthread = new SubThread();
		mainthread.start();
		subthread.start();
	}
	
	@Override
	public void onDisable() {
		mainthread.stop();
		mainthread.destroy();
		subthread.stop();
		subthread.destroy();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println(" ");
		System.out.println("[ MyTool ] 제작 : 염료 (yeomryo@naver.com)");
		System.out.println("[ MyTool ] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("[ MyTool ] 플러그인이 비활성화 되었습니다.");
		System.out.println("[ MyTool ] 본 플러그인의 저작권은 모두 염료에게 있습니다.");
		System.out.println(" ");
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		for(Tool t : MyTool.tools){
			for(PlayerTool pt : t.getPlayerTools()){
				for(Ability a : pt.getCurrentAbility()){
					if(a.getType() == AbilityType.HEALTH){
						Health h = (Health)a;
						h.clear();
					}
				}
			}
		}
	}
	
	public void loadToolFiles(){
		File dir = new File(getDataFolder(), "MyTools");
		if(!dir.exists())
			dir.mkdirs();
		for(File f : dir.listFiles()){
			if(!f.isDirectory())
			tools.add(Tool.load(f));
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("마이툴")){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player)sender;
			if(!p.isOp()){
				p.sendMessage(pf+"§c관리자 전용 명령어입니다.");
				return true;
			}
			if(args.length == 0){
				p.sendMessage(pf+"/마이툴 §6생성 [이름]§f - 손에 든 아이템을 [이름] 툴로 설정합니다.");
				p.sendMessage(pf+"/마이툴 §6삭제 [이름]§f - [이름] 툴을 삭제합니다.");
				p.sendMessage(pf+"/마이툴 §6목록§f - 툴 목록을 봅니다.");
				p.sendMessage(pf+"/마이툴 §6최대레벨 [이름] [레벨]§f - [이름] 툴의 최대레벨을 설정합니다. 기본값 = 100");
				p.sendMessage(pf+"/마이툴 §6모드 [이름] [킬/엔티티킬/브레이크]§f - [이름] 툴의 모드를 해당모드로 변경합니다.");
				p.sendMessage(pf+"/마이툴 §6설정 [이름] [아이템코드] [확률] [경험치]§f - 브레이크의 모드툴에만 적용됩니다. (여러개 가능)");
				p.sendMessage(pf+"/마이툴 §6지급 [이름] [플레이어]§f - [플레이어] 에게 [이름] 툴 지급");
				p.sendMessage(pf+"/마이툴 §6경험치설정 [이름] [경험치량]§f - [이름] 툴의 경험치 할당량을 설정합니다.");
				p.sendMessage(pf+"/마이툴 §6능력 [이름] [능력]... §f- [이름] 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("생성")){
				if(args.length != 2){
					p.sendMessage(pf+"/마이툴 §6생성 [이름]§f - 손에 든 아이템을 [이름] 툴로 설정합니다.");
					return true;
				}
				if(p.getItemInHand() == null){
					p.sendMessage(pf+"§c손에 아이템을 들고 다시 시도해주세요.");
					return true;
				}
				if(p.getItemInHand().getType() == Material.AIR){
					p.sendMessage(pf+"§c손에 아이템을 들고 다시 시도해주세요.");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t!=null){
					p.sendMessage(pf+"§c이미 존재하는 이름입니다.");
					return true;
				}
				t = new Tool(name);
				t.setItem(p.getItemInHand().clone());
				t.refresh();
				tools.add(t);
				t.save();
				p.sendMessage(pf+"§a툴이 생성되었습니다.");
				return true;
			}

			if(args[0].equalsIgnoreCase("삭제")){
				if(args.length != 2){
					p.sendMessage(pf+"/마이툴 §6삭제 [이름]§f - [이름] 툴을 삭제합니다.");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}
				t.remove();
				Player[] pl = Bukkit.getOnlinePlayers();
				for(PlayerTool pt : t.getPlayerTools()){
					for(Player v : pl){
						v.getInventory().removeItem(pt.getMyTool());
					}
				}
				p.sendMessage(pf+"§c툴이 삭제되었습니다.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("목록")){
				p.sendMessage(pf+"§a생성된 툴 목록");
				for(Tool t : tools){
					p.sendMessage(pf+t.getName()+"  Mode : "+t.getMode().toString());
				}
				return true;
			}

			if(args[0].equalsIgnoreCase("최대레벨")){
				if(args.length != 3){
					p.sendMessage(pf+"/마이툴 §6최대레벨 [이름] [레벨]§f - [이름] 툴의 최대레벨을 설정합니다. 기본값 = 100");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}
				try{
					int ar = Integer.parseInt(args[2]);
					t.setMaxLevel(ar);
					t.refresh();
					t.save();
					p.sendMessage(pf+"§a설정되었습니다.");
					return true;
				}catch(NumberFormatException e){
					p.sendMessage(pf+"/마이툴§6 최대레벨 [이름] [레벨] §f- [이름] 툴의 최대레벨을 설정합니다. 기본값 = 100");
					return true;
				}
			}

			if(args[0].equalsIgnoreCase("모드")){
				if(args.length != 3){
					p.sendMessage(pf+"/마이툴 §6모드 [이름] [킬/엔티티킬/브레이크]§f - [이름] 툴의 모드를 해당모드로 변경합니다.");
					p.sendMessage(pf+"킬 = §a플레이어를 죽일시 경험치 1 상승");
					p.sendMessage(pf+"엔티티킬 = §a플레이어를 포함한 엔티티를 죽일시 경험치 1 상승");
					p.sendMessage(pf+"브레이크 = §a설정한 블럭을 부술시 n% 확률로 경험치 n 상승");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}
				String mode = args[2];
				ToolMode tm=null;
				if(mode.equalsIgnoreCase("킬")){
					tm=ToolMode.KILL;
				}
				if(mode.equalsIgnoreCase("엔티티킬")){
					tm=ToolMode.ENTITY_KILL;
				}
				if(mode.equalsIgnoreCase("브레이크")){
					tm=ToolMode.BREAK;
				}
				if(tm == null){
					p.sendMessage(pf+"/마이툴§6 모드 [이름] [킬/엔티티킬/브레이크] §f- [이름] 툴의 모드를 해당모드로 변경합니다.");
					p.sendMessage(pf+"킬 = §a플레이어를 죽일시 경험치 1 상승");
					p.sendMessage(pf+"엔티티킬 = §a플레이어를 포함한 엔티티를 죽일시 경험치 1 상승");
					p.sendMessage(pf+"브레이크 = §a설정한 블럭을 부술시 n% 확률로 경험치 n 상승");
					return true;
				}
				
				t.setMode(tm);
				t.refresh();
				t.save();
				p.sendMessage(pf+"§a설정되었습니다.");
				return true;
			}
			if(args[0].equalsIgnoreCase("지급")){
				if(args.length != 3){
					p.sendMessage(pf+"/마이툴 §6지급 [이름] [플레이어]§f - [플레이어] 에게 [이름] 툴 지급");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}
				String pname = args[2];
				Player z = Bukkit.getPlayer(pname);
				if(z == null){
					p.sendMessage(pf+"§c존재하지 않는 플레이어입니다.");
					return true;
				}
				PlayerTool pt = null;
				if(t.getPlayerTool(z.getName()) == null)
					pt = t.createPlayerTool(z.getName());
				else pt = t.getPlayerTool(z.getName());
				pt.refresh();
				z.getInventory().addItem(pt.getMyTool());
				p.sendMessage(pf+"§a지급되었습니다.");
				t.save();
				return true;
			}
			
			if(args[0].equalsIgnoreCase("경험치설정")){
				if(args.length != 3){
					p.sendMessage(pf+"/마이툴 §6경험치설정 [이름] [경험치량] §f- [이름] 툴의 경험치 할당량을 설정합니다.");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}
				try{
					int ar = Integer.parseInt(args[2]);
					t.setMaxExp(ar);
					t.refresh();
					t.save();
					p.sendMessage(pf+"§a설정되었습니다.");
					return true;
				}catch(NumberFormatException e){
					p.sendMessage(pf+"/마이툴 §6경험치설정 [이름] [경험치량] §f- [이름] 툴의 경험치 할당량을 설정합니다.");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("능력")){
				if(args.length <= 2){
					p.sendMessage(pf+"/마이툴§6 능력 [이름] [능력]...§f - [이름] 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
					p.sendMessage(pf+"§e- 능력 - ");
					p.sendMessage(pf+"체력 = §a1당 반칸증가");
					p.sendMessage(pf+"방어력 =§a 1당 반칸증가");
					p.sendMessage(pf+"회복량 = §a1당 반칸증가");
					p.sendMessage(pf+"라이프스틸 =§a 1당 반칸스틸");
					p.sendMessage(pf+"포션 = §a1당 1레벨");
					p.sendMessage(pf+"이동속도 =§a 1당 1% 증가");
					p.sendMessage(pf+"공격속도 = §a1당 1% 증가");
					p.sendMessage(pf+"명령어 = §a명령어 사용");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}

				try{
					String ab = args[2];
					
					if(ab.equalsIgnoreCase("체력")){
						if(args.length != 5){
							p.sendMessage(pf+"/마이툴§6 능력 "+name+" 체력 [레벨] [강도]§f - "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int tense = Integer.parseInt(args[4]);
						Health h = new Health(t);
						h.setLevel(tense);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("방어력")){
						if(args.length != 5){
							p.sendMessage(pf+"/마이툴 §6능력 "+name+" 방어력 [레벨] [강도] §f- "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int tense = Integer.parseInt(args[4]);
						Armour h = new Armour(t);
						h.setLevel(tense);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("회복량")){
						if(args.length != 5){
							p.sendMessage(pf+"/마이툴 §6능력 "+name+" 회복량 [레벨] [강도] §f- "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int tense = Integer.parseInt(args[4]);
						Regen h = new Regen(t);
						h.setLevel(tense);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("라이프스틸")){
						if(args.length != 5){
							p.sendMessage(pf+"/마이툴 §6능력 "+name+" 라이프스틸 [레벨] [강도] §f- "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int tense = Integer.parseInt(args[4]);
						Drain h = new Drain(t);
						h.setLevel(tense);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("포션")){
						if(args.length != 6){
							p.sendMessage(pf+"/마이툴 §6능력 "+name+" 포션 [레벨] [포션코드] [강도] §f- "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							p.sendMessage(pf+"§e포션코드 : 1-신속  2-구속  3-성급함  4-피로  5-힘  6-즉시회복  7-즉시피해");
							p.sendMessage(pf+"§e포션코드 : 8-점프강화  9-멀미  10-재생  11-저항  12-화염저항  13-수중호흡");
							p.sendMessage(pf+"§e포션코드 : 14-투명화  15-실명  16-야간투시  17-허기  18-나약함  19-독  20-위더");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int id = Integer.parseInt(args[4]);
						int tense = Integer.parseInt(args[5]);
						PotionEffectType pe = PotionEffectType.getById(id);
						if(pe == null){
							p.sendMessage(pf+"§c존재하지 않는 포션코드입니다.");
							return true;
						}
						Potion h = new Potion(t,pe);
						h.setToolLevel(level);
						h.setLevel(tense);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("이동속도")){
						if(args.length != 5){
							p.sendMessage(pf+"/마이툴§6 능력 "+name+" 이동속도 [레벨] [강도] §f- "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int tense = Integer.parseInt(args[4]);
						Movspeed h = new Movspeed(t);
						h.setLevel(tense);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("공격속도")){
						if(args.length != 5){
							p.sendMessage(pf+"/마이툴§6 능력 "+name+" 공격속도 [레벨] [강도]§f - "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						int tense = Integer.parseInt(args[4]);
						Atkspeed h = new Atkspeed(t);
						h.setLevel(tense);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}else if(ab.equalsIgnoreCase("명령어")){
						if(args.length < 5){
							p.sendMessage(pf+"/마이툴§6 능력 "+name+" 명령어 [레벨] [확률%] [명령어] §f- "+name+" 툴이 해당 레벨이 되었을때 적용시킬 능력을 설정합니다.");
							return true;
						}
						int level = Integer.parseInt(args[3]);
						double chance = Double.parseDouble(args[4]);
						String cmd = "";
						for(int i=5;i<args.length;i++){
							cmd+=args[i]+" ";
						}
						com.yeomryo.mytool.ability.Command h = new com.yeomryo.mytool.ability.Command(t, cmd, chance);
						h.setToolLevel(level);
						t.addAbility(level, h);
						p.sendMessage(pf+"§a설정되었습니다.");
					}
					t.save();
					return true;
				}catch(NumberFormatException e){
					p.sendMessage(pf+"§c숫자를 입력해주세요.");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("설정")){
				if(args.length != 5){
					p.sendMessage(pf+"/마이툴 §6설정 [이름] [아이템코드] [확률] [경험치] §f- 브레이크의 모드툴에만 적용됩니다. (여러개 가능)");
					return true;
				}
				String name = args[1];
				Tool t = Tool.getTool(name);
				if(t==null){
					p.sendMessage(pf+"§c존재하지 않는 이름입니다.");
					return true;
				}
				ToolMode tm = t.getMode();
				if(tm != ToolMode.BREAK){
					p.sendMessage(pf+"§c브레이크 모드 툴만 적용됩니다.");
					return true;
				}
				try{
					int id = Integer.parseInt(args[2]);
					double chance = Double.parseDouble(args[3]);
					int exp = Integer.parseInt(args[4]);
					BreakData bd = new BreakData(id,chance,exp);
					t.getBREAK().add(bd);
					t.save();
					p.sendMessage(pf+"§a설정되었습니다.");
				}catch(NumberFormatException e){
					
				}
				return true;
			}
		}
		return false;
	}
	
}
