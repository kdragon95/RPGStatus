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
	
	//ȯ�� ���� �ε�.
	public static void configEnable(RPGStatus plugin) {
		try {
			configIdentifier(plugin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		customConfigFile = new File(plugin.getDataFolder(), "config.yml");
		customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	}
	
	//ȯ�漳������ ���� Ȯ��. ���� �� ����.
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
	
	//ȯ�漳������ �ʱ�ȭ.
	public static void configInitialize(RPGStatus plugin) throws IOException {
		customConfig = YamlConfiguration.loadConfiguration(plugin.getResource("config.yml"));
		customConfig.save(customConfigFile);
	}
	
	//�⺻ Widget ���� ��ȯ
	public static ConfigurationSection getBasicWidgetConfig() {
		return customConfig.getConfigurationSection("Basic_Widgets");
	}
	
	//ü�� ���� ��ȯ
	public static ConfigurationSection getHealthConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Health");
	}
	
	//���� ���� ��ȯ
	public static ConfigurationSection getManaConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Mana");
	}
	
	//���� ���� ��ȯ
	public static ConfigurationSection getLevelConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Level");
	}
	
	//����ġ ���� ��ȯ
	public static ConfigurationSection getEXPConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("EXP");
	}
	
	//����� Ŭ���� ���� ��ȯ
	public static ConfigurationSection getHeroesClassConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("HeroClass");
	}
	
	//��׶��� ���� ��ȯ
	public static ConfigurationSection getBackgroundConfig() {
		return customConfig.getConfigurationSection("Widgets").getConfigurationSection("Background");
	}
	
}
