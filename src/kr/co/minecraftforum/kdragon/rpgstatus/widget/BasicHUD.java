package kr.co.minecraftforum.kdragon.rpgstatus.widget;

import kr.co.minecraftforum.kdragon.rpgstatus.configuration.ConfigManager;

import org.bukkit.configuration.ConfigurationSection;
import org.getspout.spoutapi.player.SpoutPlayer;

public class BasicHUD {
	
	public static ConfigurationSection basic;
	
	public static boolean healthbar_visible;
	public static double healthbar_shiftX_rate;
	public static double healthbar_shiftY_rate;
	public static boolean expbar_visible;
	public static double expbar_shiftX_rate;
	public static double expbar_shiftY_rate;
	public static boolean hungerbar_visible;
	public static double hungerbar_shiftX_rate;
	public static double hungerbar_shiftY_rate;
	public static boolean armorbar_visible;
	public static double armorbar_shiftX_rate;
	public static double armorbar_shiftY_rate;
	public static boolean bubblebar_visible;
	public static double bubblebar_shiftX_rate;
	public static double bubblebar_shiftY_rate;
	
	static {
		basic = ConfigManager.getBasicWidgetConfig();
		healthbar_visible = basic.getConfigurationSection("Healthbar").getBoolean("visible");
		healthbar_shiftX_rate = basic.getConfigurationSection("Healthbar").getDouble("shiftX_rate");
		healthbar_shiftY_rate = basic.getConfigurationSection("Healthbar").getDouble("shiftY_rate");
		expbar_visible = basic.getConfigurationSection("EXPbar").getBoolean("visible");
		expbar_shiftX_rate = basic.getConfigurationSection("EXPbar").getDouble("shiftX_rate");
		expbar_shiftY_rate = basic.getConfigurationSection("EXPbar").getDouble("shiftY_rate");
		hungerbar_visible = basic.getConfigurationSection("Hungerbar").getBoolean("visible");
		hungerbar_shiftX_rate = basic.getConfigurationSection("Hungerbar").getDouble("shiftX_rate");
		hungerbar_shiftY_rate = basic.getConfigurationSection("Hungerbar").getDouble("shiftY_rate");
		armorbar_visible = basic.getConfigurationSection("Armorbar").getBoolean("visible");
		armorbar_shiftX_rate = basic.getConfigurationSection("Armorbar").getDouble("shiftX_rate");
		armorbar_shiftY_rate = basic.getConfigurationSection("Armorbar").getDouble("shiftY_rate");
		bubblebar_visible = basic.getConfigurationSection("Bubblebar").getBoolean("visible");
		bubblebar_shiftX_rate = basic.getConfigurationSection("Bubblebar").getDouble("shiftX_rate");
		bubblebar_shiftY_rate = basic.getConfigurationSection("Bubblebar").getDouble("shiftY_rate");
	}
	
	//기본 체력바 초기설정.
	public void initializeHealthBar(SpoutPlayer player) {
		
		int screenWidth = player.getMainScreen().getWidth();
		int screenHeight = player.getMainScreen().getHeight();
		int shiftX = (int) healthbar_shiftX_rate*screenWidth/100;
		int shiftY = (int) healthbar_shiftY_rate*screenHeight/100;
		
		if(healthbar_visible)
			player.getMainScreen().getHealthBar().shiftXPos(-shiftX).shiftYPos(shiftY);
		if(!healthbar_visible)
			player.getMainScreen().getHealthBar().setVisible(false);
	}
	
	//기본 경험치바 초기설정.
	public void initializeEXPBar(SpoutPlayer player) {
		
		int screenWidth = player.getMainScreen().getWidth();
		int screenHeight = player.getMainScreen().getHeight();
		int shiftX = (int) expbar_shiftX_rate*screenWidth/100;
		int shiftY = (int) expbar_shiftY_rate*screenHeight/100;
		
		if(expbar_visible)
			player.getMainScreen().getExpBar().shiftXPos(-shiftX).shiftYPos(shiftY);
		if(!expbar_visible)
			player.getMainScreen().getExpBar().setVisible(false);
	}
	
	//기본 배고픔바 초기설정.
	public void initializeHungerBar(SpoutPlayer player) {
		
		int screenWidth = player.getMainScreen().getWidth();
		int screenHeight = player.getMainScreen().getHeight();
		int shiftX = (int) hungerbar_shiftX_rate*screenWidth/100;
		int shiftY = (int) hungerbar_shiftY_rate*screenHeight/100;
		
		if(hungerbar_visible)
			player.getMainScreen().getHungerBar().shiftXPos(-shiftX).shiftYPos(shiftY);
		if(!hungerbar_visible)
			player.getMainScreen().getHungerBar().setVisible(false);
	}
	
	//기본 방어구 내구도바 초기설정.
	public void initializeArmorBar(SpoutPlayer player) {
		
		int screenWidth = player.getMainScreen().getWidth();
		int screenHeight = player.getMainScreen().getHeight();
		int shiftX = (int) hungerbar_shiftX_rate*screenWidth/100;
		int shiftY = (int) hungerbar_shiftY_rate*screenHeight/100;
		
		if(armorbar_visible)
			player.getMainScreen().getArmorBar().shiftXPos(-shiftX).shiftYPos(shiftY);
		if(!armorbar_visible)
			player.getMainScreen().getArmorBar().setVisible(false);
	}
	
	//기본 산소바 초기설정.
	public void initializeBubbleBar(SpoutPlayer player) {
		
		int screenWidth = player.getMainScreen().getWidth();
		int screenHeight = player.getMainScreen().getHeight();
		int shiftX = (int) hungerbar_shiftX_rate*screenWidth/100;
		int shiftY = (int) hungerbar_shiftY_rate*screenHeight/100;
		
		if(bubblebar_visible)
			player.getMainScreen().getBubbleBar().shiftXPos(-shiftX).shiftYPos(shiftY);
		if(!bubblebar_visible)
			player.getMainScreen().getBubbleBar().setVisible(false);
	}
	
	//기본 체력바 업데이트.
	public void updateHealthBar(SpoutPlayer player) {
		if(healthbar_visible)	
			player.getMainScreen().getHealthBar().setVisible(true);
		if(!healthbar_visible)
			player.getMainScreen().getHealthBar().setVisible(false);
	}
	
	//기본 경험치바 업데이트.
	public void updateEXPBar(SpoutPlayer player) {
		if(expbar_visible) {			
			player.getMainScreen().getExpBar().setVisible(true);
		}
		if(!expbar_visible) {
			player.getMainScreen().getExpBar().setVisible(false);
		}
	}
	
	//기본 배고픔 바 업데이트.
	public void updateHungerBar(SpoutPlayer player) {
		if(hungerbar_visible)
			player.getMainScreen().getHungerBar().setVisible(true);
		if(!hungerbar_visible)
			player.getMainScreen().getHungerBar().setVisible(false);
	}
	
	//기본 방어구 내구도 바 업데이트.
	public void updateArmorBar(SpoutPlayer player) {
		if(armorbar_visible)
			player.getMainScreen().getArmorBar().setVisible(true);
		if(!armorbar_visible)
			player.getMainScreen().getArmorBar().setVisible(false);
	}
	
	//기본 산소바 업데이트.
	public void updateBubbleBar(SpoutPlayer player) {
		if(bubblebar_visible)
			player.getMainScreen().getBubbleBar().setVisible(true);
		if(!bubblebar_visible)
			player.getMainScreen().getBubbleBar().setVisible(false);
	}
	
}
