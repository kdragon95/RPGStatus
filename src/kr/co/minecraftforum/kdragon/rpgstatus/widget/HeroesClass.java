package kr.co.minecraftforum.kdragon.rpgstatus.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.minecraftforum.kdragon.rpgstatus.RPGStatus;
import kr.co.minecraftforum.kdragon.rpgstatus.configuration.ConfigManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class HeroesClass {
	
	private static Map<String, GenericTexture> playerWidgets = new HashMap<String, GenericTexture>();
	private static Map<String, String> classImageURLs = new HashMap<String, String>();
	
	public static RPGStatus plugin;
	public static ConfigurationSection heroesClass;
	public static String common_url;
	public static String[] detail_url;
	
	private static boolean isEnabled;
	private static String priority;
	private static String anchor;
	private static boolean size_lock;
	private static double width_rate;
	private static double height_rate;
	private static double shiftX_rate;
	private static double shiftY_rate;
	
	static {
		isEnabled = ConfigManager.getHeroesClassConfig().getBoolean("enabled");
		if(isEnabled) {
			plugin = new RPGStatus();
			heroesClass = ConfigManager.getHeroesClassConfig();
			priority = heroesClass.getString("priority");
			anchor = heroesClass.getString("anchor");
			size_lock = heroesClass.getBoolean("size_lock");
			width_rate = heroesClass.getDouble("width_rate");
			height_rate = heroesClass.getDouble("height_rate");
			shiftX_rate = heroesClass.getDouble("shiftX_rate");
			shiftY_rate = heroesClass.getDouble("shiftY_rate");
			common_url = heroesClass.getString("common_url");
			setTextureURL();
		}
	}
	
	//히어로 클래스 위젯 이미지를 preLoginCache에 추가(로그인 하기 전에 다운로드 하도록 함).
	public static void addPreLoginCache(Plugin plugin) {
		if(isEnabled) {
			List<String> bookedClasses = ConfigManager.getHeroesClassConfig().getConfigurationSection("details").getStringList("classlist");
			for(int i = 0 ; i < bookedClasses.size() ; i++) {
				String detail = ConfigManager.getHeroesClassConfig().getConfigurationSection("details").getString(bookedClasses.get(i));
				if(detail != null)
					SpoutManager.getFileManager().addToPreLoginCache(plugin, common_url + detail);
			}
		}
	}
	
	//클래스 이미지 url 지정.
	public static void setTextureURL() {
		List<String> bookedClasses = ConfigManager.getHeroesClassConfig().getConfigurationSection("details").getStringList("classlist");
		for(int i = 0 ; i < bookedClasses.size() ; i++) {
			String detail = ConfigManager.getHeroesClassConfig().getConfigurationSection("details").getString(bookedClasses.get(i));
			if(detail != null)
				classImageURLs.put(bookedClasses.get(i), common_url + detail);
		}
	}
	
	//유저 처음 접속 시 히어로 클래스 위젯 생성 메서드.
	public void create(Plugin plugin, SpoutPlayer player, String className) {
		if(isEnabled) {
			if(classImageURLs.containsKey(className)) {
				String name = player.getName();
				
				GenericTexture texture = new GenericTexture();
				
				int screenWidth = player.getMainScreen().getWidth();
				int screenHeight = player.getMainScreen().getHeight();
				
				int width = (int) width_rate*screenWidth/100;
				int height = (int) height_rate*screenHeight/100;
				int shiftX = (int) shiftX_rate*screenWidth/100;
				int shiftY = (int) shiftY_rate*screenHeight/100;
				
				if(priority.equalsIgnoreCase("highest"))
					texture.setPriority(RenderPriority.Highest);
				if(priority.equalsIgnoreCase("high"))
					texture.setPriority(RenderPriority.High);
				if(priority.equalsIgnoreCase("normal"))
					texture.setPriority(RenderPriority.Normal);
				if(priority.equalsIgnoreCase("low"))
					texture.setPriority(RenderPriority.Low);
				if(priority.equalsIgnoreCase("lowest"))
					texture.setPriority(RenderPriority.Lowest);
				
				texture.setWidth(width).setHeight(height);
				
				if(size_lock) {
					if(anchor.equalsIgnoreCase("top_left"))
						texture.setAnchor(WidgetAnchor.TOP_LEFT);
					if(anchor.equalsIgnoreCase("top_center"))
						texture.setAnchor(WidgetAnchor.TOP_CENTER).shiftXPos(-(width/2));
					if(anchor.equalsIgnoreCase("top_right"))
						texture.setAnchor(WidgetAnchor.TOP_RIGHT).shiftXPos(-width);
					if(anchor.equalsIgnoreCase("center_left"))
						texture.setAnchor(WidgetAnchor.CENTER_LEFT).shiftYPos(-(height/2));
					if(anchor.equalsIgnoreCase("center_center"))
						texture.setAnchor(WidgetAnchor.CENTER_CENTER).shiftXPos(-(width/2)).shiftYPos(-(height/2));
					if(anchor.equalsIgnoreCase("center_right"))
						texture.setAnchor(WidgetAnchor.CENTER_RIGHT).shiftXPos(-width).shiftYPos(-(height/2));
					if(anchor.equalsIgnoreCase("bottom_left"))
						texture.setAnchor(WidgetAnchor.BOTTOM_LEFT).shiftYPos(-height);
					if(anchor.equalsIgnoreCase("bottom_center"))
						texture.setAnchor(WidgetAnchor.BOTTOM_CENTER).shiftXPos(-(width/2)).shiftYPos(-height);
					if(anchor.equalsIgnoreCase("bottom_right"))
						texture.setAnchor(WidgetAnchor.BOTTOM_RIGHT).shiftXPos(-width).shiftYPos(-height);
				}
				
				if(!size_lock) {
					if(anchor.equalsIgnoreCase("top_left"))
						texture.setX(0).setY(0);
					if(anchor.equalsIgnoreCase("top_center"))
						texture.setX((screenWidth/2) - (width/2)).setY(0);
					if(anchor.equalsIgnoreCase("top_right"))
						texture.setX(screenWidth - width).setY(0);
					if(anchor.equalsIgnoreCase("center_left"))
						texture.setX(0).setY(screenHeight - (height/2));
					if(anchor.equalsIgnoreCase("center_center"))
						texture.setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
					if(anchor.equalsIgnoreCase("center_right"))
						texture.setX(screenWidth - width).setY(screenHeight - (height/2));
					if(anchor.equalsIgnoreCase("bottom_left"))
						texture.setX(0).setY(screenHeight - height);
					if(anchor.equalsIgnoreCase("bottom_center"))
						texture.setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
					if(anchor.equalsIgnoreCase("bottom_right"))
						texture.setX(screenWidth - width).setY(screenHeight - height);
				}
				
				texture.shiftXPos(shiftX).shiftYPos(shiftY);
				texture.setUrl(classImageURLs.get(className));
				
				playerWidgets.put(name, texture);
				player.getMainScreen().attachWidget(plugin, playerWidgets.get(name));
			}
		}
	}
	
	//유저 히어로 클래스 변경 시 체력 위젯 업데이트 메서드.
	public void change(Plugin plugin, SpoutPlayer player, String className) {
		String name = player.getName();
		if(playerWidgets.containsKey(name)) {
			playerWidgets.get(name).setUrl(classImageURLs.get(className));
			
			player.getMainScreen().updateWidget(playerWidgets.get(name));
		}
	}
	
	//유저 widget 제거 이벤트
	public void remove(String player) {
		if(playerWidgets.containsKey(player))
			playerWidgets.remove(player);
	}
	
}
