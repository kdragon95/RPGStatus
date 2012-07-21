package kr.co.minecraftforum.kdragon.rpgstatus;


import java.util.logging.Logger;

import kr.co.minecraftforum.kdragon.rpgstatus.configuration.ConfigManager;
import kr.co.minecraftforum.kdragon.rpgstatus.listeners.PlayerListeners;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Background;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.EXP;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Health;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.HeroesClass;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Level;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Mana;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.heroes.Heroes;

public class RPGStatus extends JavaPlugin {
	
	Logger log = Bukkit.getLogger();
	
	public static Heroes heroes;
	
	public RPGStatus() {};
	
	public static RPGStatus plugin;
	
	public static RPGStatus getPlugin() {
		if(plugin == null)
			plugin = new RPGStatus();
		return plugin;
	}
	
	@Override
	public void onEnable() {
		log.info(getName() + " enabled.");
		ConfigManager.configEnable(this);
		registerAll();
		isdependsExist();
	}
	
	@Override
	public void onDisable() {
		log.info(getName() + " disabled.");
	}
	
	//리스너, pre login cache 등록.
	private void registerAll() {
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(this), this);
		Health.addPreLoginCache(this);
		Mana.addPreLoginCache(this);
		Level.addPreLoginCache(this);
		EXP.addPreLoginCache(this);
		HeroesClass.addPreLoginCache(this);
		Background.addPreLoginCache(this);
	}
	
	//필수 플러그인 spout, Heroes가 있는지 확인.
	private void isdependsExist() {
		Plugin spout = Bukkit.getPluginManager().getPlugin("Spout");
		Plugin heroes = Bukkit.getPluginManager().getPlugin("Heroes");
		if(spout != null || heroes != null) {
			RPGStatus.heroes = (Heroes) heroes;
		}
		if(spout == null || heroes == null) {
			log.severe("[RPGStatus] This plugin needs 'Spout' and 'Heroes'. Check your Plugin folder.");
			log.severe("[RPGStatus] disabling..");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
	}

}
