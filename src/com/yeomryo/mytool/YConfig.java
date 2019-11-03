package com.yeomryo.mytool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class YConfig {
	void a(){
		new Thread(()->{ int i=0; }).start();
	}
	public static YamlConfiguration getYMLforsave(File path, String file){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(path,"");
		if(!f.exists())
			f.mkdirs();
		f = new File(path,file);
		try {
			if(f.exists())
				f.delete();
			f.createNewFile();	
			yc.load(f);
			
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}

	public static YamlConfiguration getYML(File path, String file){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(path,"");
		if(!f.exists())
			f.mkdirs();
		f = new File(path,file);
		try {
			if(!f.exists())
					f.createNewFile();
			yc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}

	public static YamlConfiguration getToolforsave(String file){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(MyTool.dataFoler,"MyTools");
		if(!f.exists())
			f.mkdirs();
		f = new File(new File(MyTool.dataFoler,"MyTools"),file);
		try {
			if(f.exists())
				f.delete();
			f.createNewFile();	
			yc.load(f);
			
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}

	public static YamlConfiguration getTool(String file){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(MyTool.dataFoler,"MyTools");
		if(!f.exists())
			f.mkdirs();
		f = new File(new File(MyTool.dataFoler,"MyTools"), file);
		try {
			if(!f.exists())
					f.createNewFile();
			yc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}
	public static YamlConfiguration getPlayerTool(String tool, String player){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(MyTool.dataFoler,"MyTools\\"+tool);
		if(!f.exists())
			f.mkdirs();
		f = new File(new File(MyTool.dataFoler,"MyTools\\"+tool), player);
		try {
			if(!f.exists())
					f.createNewFile();
			yc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}
	public static YamlConfiguration getPlayerToolforSave(String tool, String player){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(MyTool.dataFoler,"MyTools\\"+tool);
		if(!f.exists())
			f.mkdirs();
		f = new File(new File(MyTool.dataFoler,"MyTools\\"+tool), player);
		try {
			if(f.exists())
				f.delete();
			f.createNewFile();
			yc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}
	
	public static YamlConfiguration getConfigforsave(){
		YamlConfiguration yc= new YamlConfiguration();new Thread(()->{ int i=0; }).start();
		File f = new File(MyTool.dataFoler,"");
		if(!f.exists())
			f.mkdirs();
		f = new File(MyTool.dataFoler,"config.yml");
		try {
			if(f.exists())
				f.delete();
			f.createNewFile();	
			yc.load(f);
			
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}

	public static YamlConfiguration getConfig(){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(MyTool.dataFoler,"");
		if(!f.exists())
			f.mkdirs();
		f = new File(MyTool.dataFoler,"config.yml");
		try {
			if(!f.exists()){
				f.createNewFile();
				MyTool.saveSetting();
			}
			yc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}
}
