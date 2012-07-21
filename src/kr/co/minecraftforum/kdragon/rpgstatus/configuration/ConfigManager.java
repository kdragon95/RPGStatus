package kr.co.minecraftforum.kdragon.rpgstatus.configuration;

import java.io.File;
import java.io.IOException;

import kr.co.minecraftforum.kdragon.rpgstatus.RPGStatus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	public static File customConfigFile;
	public static FileConfiguration customConfig;
	
	//환경 설정 로드.
	public static void configEnable(RPGStatus plugin) {
		try {
			configIdentifier(plugin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		customConfigFile = new File(plugin.getDataFolder(), "config.yml");
		customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	}
	
	//환경설정파일 유무 확인. 없을 시 생성.
	public static void configIdentifier(RPGStatus plugin) throws IOException {
		customConfigFile = new File(plugin.getDataFolder(), "config.yml");
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
			customConfigFile.createNewFile();
			configInitialize(plugin);
			Bukkit.getLogger().warning("[RPGStatus] 'config.yml' does not exist in data folder. Default config file created.");
		}
		if(plugin.getDataFolder().exists()) {
			if(customConfigFile == null) {
				customConfigFile.createNewFile();
				configInitialize(plugin);
				Bukkit.getLogger().warning("[RPGStatus] 'config.yml' does not exist in data folder. Default config file created.");
			}
		}
	}
	
	//환경설정파일 초기화.
	public static void configInitialize(RPGStatus plugin) throws IOException {
		customConfig = YamlConfiguration.loadConfiguration(plugin.getResource("config.yml"));
		customConfig.save(customConfigFile);
	}
	
	//기본 Widget 설정 반환
	public static ConfigurationSection getBasicWidgetConfig() {
		return customConfig.getConfigurationSection("Basic_Widgets");
	}
	
	//체력 설정 반환
	public static ConfigurationSection getHealthConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Health");
	}
	
	//마나 설정 반환
	public static ConfigurationSection getManaConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Mana");
	}
	
	//레벨 설정 반환
	public static ConfigurationSection getLevelConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Level");
	}
	
	//경험치 설정 반환
	public static ConfigurationSection getEXPConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("EXP");
	}
	
	//히어로 클래스 설정 반환
	public static ConfigurationSection getHeroesClassConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("HeroClass");
	}
	
	//백그라운드 설정 반환
	public static ConfigurationSection getBackgroundConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Background");
	}
	
}
