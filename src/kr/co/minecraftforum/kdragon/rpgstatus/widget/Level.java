package kr.co.minecraftforum.kdragon.rpgstatus.widget;

import java.util.HashMap;
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

public class Level {
	
	private static Map<String, GenericTexture> playerWidgets = new HashMap<String, GenericTexture>();
	private static Map<String, GenericTexture> playerWidgets1 = new HashMap<String, GenericTexture>();
	private static Map<String, GenericTexture> playerWidgets2 = new HashMap<String, GenericTexture>();
	private static Map<String, GenericTexture> playerWidgets3 = new HashMap<String, GenericTexture>();
	
	public static RPGStatus plugin;
	public static ConfigurationSection level;
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
	private static boolean isUseRepresentativeImage;
	private static String rimage_priority;
	private static String rimage_anchor;
	private static boolean rimage_size_lock;
	private static double rimage_width_rate;
	private static double rimage_height_rate;
	private static double rimage_shiftX_rate;
	private static double rimage_shiftY_rate;
	private static String rimage_url;
	
	static {
		isEnabled = ConfigManager.getLevelConfig().getBoolean("enabled");
		if(isEnabled) {
			plugin = new RPGStatus();
			level = ConfigManager.getLevelConfig();
			priority = level.getString("priority");
			anchor = level.getString("anchor");
			size_lock = level.getBoolean("size_lock");
			width_rate = level.getDouble("width_rate");
			height_rate = level.getDouble("height_rate");
			shiftX_rate = level.getDouble("shiftX_rate");
			shiftY_rate = level.getDouble("shiftY_rate");
			common_url = level.getString("common_url");
			detail_url = new String[10];
			isUseRepresentativeImage = level.getBoolean("use_representative_image");
			if(isUseRepresentativeImage) {
				rimage_priority = level.getString("rimage_priority");
				rimage_anchor = level.getString("rimage_anchor");
				rimage_size_lock = level.getBoolean("rimage_size_lock");
				rimage_width_rate = level.getDouble("rimage_width_rate");
				rimage_height_rate = level.getDouble("rimage_height_rate");
				rimage_shiftX_rate = level.getDouble("rimage_shiftX_rate");
				rimage_shiftY_rate = level.getDouble("rimage_shiftY_rate");
				rimage_url = level.getString("rimage_url");
			}
			setTextureURL();
		}
	}
	
	//레벨 위젯 이미지를 preLoginCache에 추가(로그인 하기 전에 다운로드 하도록 함).
	public static void addPreLoginCache(Plugin plugin) {
		if(isEnabled) {
			for(int i = 0 ; i < 10 ; i++) {
				SpoutManager.getFileManager().addToPreLoginCache(plugin, common_url + detail_url[i]);
			}
			SpoutManager.getFileManager().addToPreLoginCache(plugin, common_url + rimage_url);
		}
	}
	
	//0~9까지 url 지정.
	public static void setTextureURL() {
		for(int i = 0 ; i < 10 ; i++) {
			detail_url[i] = level.getConfigurationSection("details").getString("n" + i);
		}
	}
	
	//유저 처음 접속 시 레벨 위젯 생성 메서드.
	public void create(Plugin plugin, SpoutPlayer player, int level) {
		if(isEnabled) {
			String name = player.getName();
			
			GenericTexture texture1 = new GenericTexture();
			GenericTexture texture2 = new GenericTexture();
			GenericTexture texture3 = new GenericTexture();
			
			int levelLength = 1;
			
			if((0 <= level) && (level <= 9))
				levelLength = 1;
			if((10 <= level) && (level <= 99)) {
				levelLength = 2;
			}
			if((100 <= level) && (level <= 999)) {
				levelLength = 3;
			}
			
			int hundreds_place = (int) (level/100);
			int tens_place = 0;
			int units_place = 0;
			
			if(hundreds_place != 0)
				tens_place = (int) (level%(hundreds_place*100))/10;
			if(hundreds_place == 0)
				tens_place = (int) (level/10);
			if(tens_place != 0)
				units_place = (int) (level%(hundreds_place*100 + tens_place*10));
			if(tens_place == 0)
				units_place = level;
			
			int screenWidth = player.getMainScreen().getWidth();
			int screenHeight = player.getMainScreen().getHeight();
			
			int width = (int) width_rate*screenWidth/100;
			int height = (int) height_rate*screenHeight/100;
			int shiftX = (int) shiftX_rate*screenWidth/100;
			int shiftY = (int) shiftY_rate*screenHeight/100;
			
			texture1.setWidth(width).setHeight(height);
			texture2.setWidth(width).setHeight(height);
			texture3.setWidth(width).setHeight(height);
			
			if(priority.equalsIgnoreCase("highest")) {
				texture1.setPriority(RenderPriority.Highest);
				texture2.setPriority(RenderPriority.Highest);
				texture3.setPriority(RenderPriority.Highest);
			}
			if(priority.equalsIgnoreCase("high")) {
				texture1.setPriority(RenderPriority.High);
				texture2.setPriority(RenderPriority.High);
				texture3.setPriority(RenderPriority.High);
			}
			if(priority.equalsIgnoreCase("normal")) {
				texture1.setPriority(RenderPriority.Normal);
				texture2.setPriority(RenderPriority.Normal);
				texture3.setPriority(RenderPriority.Normal);
			}
			if(priority.equalsIgnoreCase("low")) {
				texture1.setPriority(RenderPriority.Low);
				texture2.setPriority(RenderPriority.Low);
				texture3.setPriority(RenderPriority.Low);
			}
			if(priority.equalsIgnoreCase("lowest")) {
				texture1.setPriority(RenderPriority.Lowest);
				texture2.setPriority(RenderPriority.Lowest);
				texture3.setPriority(RenderPriority.Lowest);
			}
			
			if(size_lock) {
				if(anchor.equalsIgnoreCase("top_left")) {
					texture1.setAnchor(WidgetAnchor.TOP_LEFT);
					texture2.setAnchor(WidgetAnchor.TOP_LEFT);
					texture3.setAnchor(WidgetAnchor.TOP_LEFT);
				}
				if(anchor.equalsIgnoreCase("top_center")) {
					texture1.setAnchor(WidgetAnchor.TOP_CENTER).shiftXPos(-(width/2));
					texture2.setAnchor(WidgetAnchor.TOP_CENTER).shiftXPos(-(width/2));
					texture3.setAnchor(WidgetAnchor.TOP_CENTER).shiftXPos(-(width/2));
				}
				if(anchor.equalsIgnoreCase("top_right")) {
					texture1.setAnchor(WidgetAnchor.TOP_RIGHT).shiftXPos(-width);
					texture2.setAnchor(WidgetAnchor.TOP_RIGHT).shiftXPos(-width);
					texture3.setAnchor(WidgetAnchor.TOP_RIGHT).shiftXPos(-width);
				}
				if(anchor.equalsIgnoreCase("center_left")) {
					texture1.setAnchor(WidgetAnchor.CENTER_LEFT).shiftYPos(-(height/2));
					texture2.setAnchor(WidgetAnchor.CENTER_LEFT).shiftYPos(-(height/2));
					texture3.setAnchor(WidgetAnchor.CENTER_LEFT).shiftYPos(-(height/2));
				}
				if(anchor.equalsIgnoreCase("center_center")) {
					texture1.setAnchor(WidgetAnchor.CENTER_CENTER).shiftXPos(-(width/2)).shiftYPos(-(height/2));
					texture2.setAnchor(WidgetAnchor.CENTER_CENTER).shiftXPos(-(width/2)).shiftYPos(-(height/2));
					texture3.setAnchor(WidgetAnchor.CENTER_CENTER).shiftXPos(-(width/2)).shiftYPos(-(height/2));
				}
				if(anchor.equalsIgnoreCase("center_right")) {
					texture1.setAnchor(WidgetAnchor.CENTER_RIGHT).shiftXPos(-width).shiftYPos(-(height/2));
					texture2.setAnchor(WidgetAnchor.CENTER_RIGHT).shiftXPos(-width).shiftYPos(-(height/2));
					texture3.setAnchor(WidgetAnchor.CENTER_RIGHT).shiftXPos(-width).shiftYPos(-(height/2));
				}
				if(anchor.equalsIgnoreCase("bottom_left")) {
					texture1.setAnchor(WidgetAnchor.BOTTOM_LEFT).shiftYPos(-height);
					texture2.setAnchor(WidgetAnchor.BOTTOM_LEFT).shiftYPos(-height);
					texture3.setAnchor(WidgetAnchor.BOTTOM_LEFT).shiftYPos(-height);
				}
				if(anchor.equalsIgnoreCase("bottom_center")) {
					texture1.setAnchor(WidgetAnchor.BOTTOM_CENTER).shiftXPos(-(width/2)).shiftYPos(-height);
					texture2.setAnchor(WidgetAnchor.BOTTOM_CENTER).shiftXPos(-(width/2)).shiftYPos(-height);
					texture3.setAnchor(WidgetAnchor.BOTTOM_CENTER).shiftXPos(-(width/2)).shiftYPos(-height);
				}
				if(anchor.equalsIgnoreCase("bottom_right")) {
					texture1.setAnchor(WidgetAnchor.BOTTOM_RIGHT).shiftXPos(-width).shiftYPos(-height);
					texture2.setAnchor(WidgetAnchor.BOTTOM_RIGHT).shiftXPos(-width).shiftYPos(-height);
					texture3.setAnchor(WidgetAnchor.BOTTOM_RIGHT).shiftXPos(-width).shiftYPos(-height);
				}
			}
			
			if(!size_lock) {
				if(anchor.equalsIgnoreCase("top_left")) {
					texture1.setX(0).setY(0);
					texture2.setX(0).setY(0);
					texture3.setX(0).setY(0);
				}
				if(anchor.equalsIgnoreCase("top_center")) {
					texture1.setX((screenWidth/2) - (width/2)).setY(0);
					texture2.setX((screenWidth/2) - (width/2)).setY(0);
					texture3.setX((screenWidth/2) - (width/2)).setY(0);
				}
				if(anchor.equalsIgnoreCase("top_right")) {
					texture1.setX(screenWidth - width).setY(0);
					texture2.setX(screenWidth - width).setY(0);
					texture3.setX(screenWidth - width).setY(0);
				}
				if(anchor.equalsIgnoreCase("center_left")) {
					texture1.setX(0).setY(screenHeight - (height/2));
					texture2.setX(0).setY(screenHeight - (height/2));
					texture3.setX(0).setY(screenHeight - (height/2));
				}
				if(anchor.equalsIgnoreCase("center_center")) {
					texture1.setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
					texture2.setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
					texture3.setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
				}
				if(anchor.equalsIgnoreCase("center_right")) {
					texture1.setX(screenWidth - width).setY(screenHeight - (height/2));
					texture2.setX(screenWidth - width).setY(screenHeight - (height/2));
					texture3.setX(screenWidth - width).setY(screenHeight - (height/2));
				}
				if(anchor.equalsIgnoreCase("bottom_left")) {
					texture1.setX(0).setY(screenHeight - height);
					texture2.setX(0).setY(screenHeight - height);
					texture3.setX(0).setY(screenHeight - height);
				}
				if(anchor.equalsIgnoreCase("bottom_center")) {
					texture1.setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
					texture2.setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
					texture3.setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
				}
				if(anchor.equalsIgnoreCase("bottom_right")) {
					texture1.setX(screenWidth - width).setY(screenHeight - height);
					texture2.setX(screenWidth - width).setY(screenHeight - height);
					texture3.setX(screenWidth - width).setY(screenHeight - height);
				}
			}
			
			switch(levelLength) {
				case 1:
					texture3.shiftXPos(shiftX).shiftYPos(shiftY);
					break;
				case 2:
					texture2.shiftXPos(shiftX).shiftYPos(shiftY);
					texture3.shiftXPos(shiftX + width).shiftYPos(shiftY);
					break;
				case 3:
					texture1.shiftXPos(shiftX).shiftYPos(shiftY);
					texture2.shiftXPos(shiftX + width).shiftYPos(shiftY);
					texture3.shiftXPos(shiftX + width + width).shiftYPos(shiftY);
					break;
			}
			
			texture1.setUrl(common_url + detail_url[hundreds_place]);
			texture2.setUrl(common_url + detail_url[tens_place]);
			texture3.setUrl(common_url + detail_url[units_place]);
			
			playerWidgets1.put(name, texture1);
			playerWidgets2.put(name, texture2);
			playerWidgets3.put(name, texture3);
			
			switch(levelLength) {
				case 1:
					player.getMainScreen().attachWidget(plugin, playerWidgets3.get(name));
					break;
				case 2:
					player.getMainScreen().attachWidget(plugin, playerWidgets2.get(name));
					player.getMainScreen().attachWidget(plugin, playerWidgets3.get(name));
					break;
				case 3:
					player.getMainScreen().attachWidget(plugin, playerWidgets1.get(name));
					player.getMainScreen().attachWidget(plugin, playerWidgets2.get(name));
					player.getMainScreen().attachWidget(plugin, playerWidgets3.get(name));
					break;
			}
			
			if(isUseRepresentativeImage) {
				GenericTexture texture = new GenericTexture();
				
				int rimage_width = (int) rimage_width_rate*screenWidth/100;
				int rimage_height = (int) rimage_height_rate*screenHeight/100;
				int rimage_shiftX = (int) rimage_shiftX_rate*screenWidth/100;
				int rimage_shiftY = (int) rimage_shiftY_rate*screenHeight/100;
				
				texture.setWidth(rimage_width).setHeight(rimage_height);
				
				if(rimage_priority.equalsIgnoreCase("highest")) {
					texture.setPriority(RenderPriority.Highest);
				}
				if(rimage_priority.equalsIgnoreCase("high")) {
					texture.setPriority(RenderPriority.High);
				}
				if(rimage_priority.equalsIgnoreCase("normal")) {
					texture.setPriority(RenderPriority.Normal);
				}
				if(rimage_priority.equalsIgnoreCase("low")) {
					texture.setPriority(RenderPriority.Low);
				}
				if(rimage_priority.equalsIgnoreCase("lowest")) {
					texture.setPriority(RenderPriority.Lowest);
				}
				
				if(rimage_size_lock) {
					if(rimage_anchor.equalsIgnoreCase("top_left"))
						texture.setAnchor(WidgetAnchor.TOP_LEFT);
					if(rimage_anchor.equalsIgnoreCase("top_center"))
						texture.setAnchor(WidgetAnchor.TOP_CENTER).shiftXPos(-(rimage_width/2));
					if(rimage_anchor.equalsIgnoreCase("top_right"))
						texture.setAnchor(WidgetAnchor.TOP_RIGHT).shiftXPos(-rimage_width);
					if(rimage_anchor.equalsIgnoreCase("center_left"))
						texture.setAnchor(WidgetAnchor.CENTER_LEFT).shiftYPos(-(rimage_height/2));
					if(rimage_anchor.equalsIgnoreCase("center_center"))
						texture.setAnchor(WidgetAnchor.CENTER_CENTER).shiftXPos(-(rimage_width/2)).shiftYPos(-(rimage_height/2));
					if(rimage_anchor.equalsIgnoreCase("center_right"))
						texture.setAnchor(WidgetAnchor.CENTER_RIGHT).shiftXPos(-rimage_width).shiftYPos(-(rimage_height/2));
					if(rimage_anchor.equalsIgnoreCase("bottom_left"))
						texture.setAnchor(WidgetAnchor.BOTTOM_LEFT).shiftYPos(-rimage_height);
					if(rimage_anchor.equalsIgnoreCase("bottom_center"))
						texture.setAnchor(WidgetAnchor.BOTTOM_CENTER).shiftXPos(-(rimage_width/2)).shiftYPos(-rimage_height);
					if(rimage_anchor.equalsIgnoreCase("bottom_right"))
						texture.setAnchor(WidgetAnchor.BOTTOM_RIGHT).shiftXPos(-rimage_width).shiftYPos(-rimage_height);
				}
				
				if(!rimage_size_lock) {
					if(anchor.equalsIgnoreCase("top_left"))
						texture.setX(0).setY(0);
					if(anchor.equalsIgnoreCase("top_center"))
						texture.setX((screenWidth/2) - (rimage_width/2)).setY(0);
					if(anchor.equalsIgnoreCase("top_right"))
						texture.setX(screenWidth - rimage_width).setY(0);
					if(anchor.equalsIgnoreCase("center_left"))
						texture.setX(0).setY(screenHeight - (rimage_height/2));
					if(anchor.equalsIgnoreCase("center_center"))
						texture.setX((screenWidth/2) - (rimage_width/2)).setY(screenHeight - (rimage_height/2));
					if(anchor.equalsIgnoreCase("center_right"))
						texture.setX(screenWidth - rimage_width).setY(screenHeight - (rimage_height/2));
					if(anchor.equalsIgnoreCase("bottom_left"))
						texture.setX(0).setY(screenHeight - rimage_height);
					if(anchor.equalsIgnoreCase("bottom_center"))
						texture.setX((screenWidth/2) - (rimage_width/2)).setY(screenHeight - rimage_height);
					if(anchor.equalsIgnoreCase("bottom_right"))
						texture.setX(screenWidth - rimage_width).setY(screenHeight - rimage_height);
				}
				
				texture.shiftXPos(rimage_shiftX).shiftYPos(rimage_shiftY);
				texture.setUrl(common_url + rimage_url);
				
				playerWidgets.put(name, texture);
				player.getMainScreen().attachWidget(plugin, playerWidgets.get(name));
			}
		}
	}
	
	//유저 레벨 변경 시 체력 위젯 업데이트 메서드.
	public void change(Plugin plugin, SpoutPlayer player, int level) {
		String name = player.getName();
		if(playerWidgets1.containsKey(name)) {
			playerWidgets1.get(name).setX(0).setY(0);
			playerWidgets2.get(name).setX(0).setY(0);
			playerWidgets3.get(name).setX(0).setY(0);
			
			int screenWidth = player.getMainScreen().getWidth();
			int screenHeight = player.getMainScreen().getHeight();
			
			int width = (int) width_rate*screenWidth/100;
			int height = (int) height_rate*screenHeight/100;
			int shiftX = (int) shiftX_rate*screenWidth/100;
			int shiftY = (int) shiftY_rate*screenHeight/100;
			
			int levelLength = 1;
			
			if((0 <= level) && (level <= 9))
				levelLength = 1;
			if((10 <= level) && (level <= 99)) {
				levelLength = 2;
			}
			if((100 <= level) && (level <= 999)) {
				levelLength = 3;
			}
			
			int hundreds_place = (int) (level/100);
			int tens_place = 0;
			int units_place = 0;
			
			if(hundreds_place != 0)
				tens_place = (int) (level%(hundreds_place*100))/10;
			if(hundreds_place == 0)
				tens_place = (int) (level/10);
			if(tens_place != 0)
				units_place = (int) (level%(hundreds_place*100 + tens_place*10));
			if(tens_place == 0)
				units_place = level;
			
			if(size_lock) {
				if(anchor.equalsIgnoreCase("top_center")) {
					playerWidgets1.get(name).shiftXPos(-(width/2));
					playerWidgets2.get(name).shiftXPos(-(width/2));
					playerWidgets3.get(name).shiftXPos(-(width/2));
				}
				if(anchor.equalsIgnoreCase("top_right")) {
					playerWidgets1.get(name).shiftXPos(-width);
					playerWidgets2.get(name).shiftXPos(-width);
					playerWidgets3.get(name).shiftXPos(-width);
				}
				if(anchor.equalsIgnoreCase("center_left")) {
					playerWidgets1.get(name).shiftYPos(-(height/2));
					playerWidgets2.get(name).shiftYPos(-(height/2));
					playerWidgets3.get(name).shiftYPos(-(height/2));
				}
				if(anchor.equalsIgnoreCase("center_center")) {
					playerWidgets1.get(name).shiftXPos(-(width/2)).shiftYPos(-(height/2));
					playerWidgets2.get(name).shiftXPos(-(width/2)).shiftYPos(-(height/2));
					playerWidgets3.get(name).shiftXPos(-(width/2)).shiftYPos(-(height/2));
				}
				if(anchor.equalsIgnoreCase("center_right")) {
					playerWidgets1.get(name).shiftXPos(-width).shiftYPos(-(height/2));
					playerWidgets2.get(name).shiftXPos(-width).shiftYPos(-(height/2));
					playerWidgets3.get(name).shiftXPos(-width).shiftYPos(-(height/2));
				}
				if(anchor.equalsIgnoreCase("bottom_left")) {
					playerWidgets1.get(name).shiftYPos(-height);
					playerWidgets2.get(name).shiftYPos(-height);
					playerWidgets3.get(name).shiftYPos(-height);
				}
				if(anchor.equalsIgnoreCase("bottom_center")) {
					playerWidgets1.get(name).shiftXPos(-(width/2)).shiftYPos(-height);
					playerWidgets2.get(name).shiftXPos(-(width/2)).shiftYPos(-height);
					playerWidgets3.get(name).shiftXPos(-(width/2)).shiftYPos(-height);
				}
				if(anchor.equalsIgnoreCase("bottom_right")) {
					playerWidgets1.get(name).shiftXPos(-width).shiftYPos(-height);
					playerWidgets2.get(name).shiftXPos(-width).shiftYPos(-height);
					playerWidgets3.get(name).shiftXPos(-width).shiftYPos(-height);
				}
			}
			
			if(!size_lock) {
				if(anchor.equalsIgnoreCase("top_left")) {
					playerWidgets1.get(name).setX(0).setY(0);
					playerWidgets2.get(name).setX(0).setY(0);
					playerWidgets3.get(name).setX(0).setY(0);
				}
				if(anchor.equalsIgnoreCase("top_center")) {
					playerWidgets1.get(name).setX((screenWidth/2) - (width/2)).setY(0);
					playerWidgets2.get(name).setX((screenWidth/2) - (width/2)).setY(0);
					playerWidgets3.get(name).setX((screenWidth/2) - (width/2)).setY(0);
				}
				if(anchor.equalsIgnoreCase("top_right")) {
					playerWidgets1.get(name).setX(screenWidth - width).setY(0);
					playerWidgets2.get(name).setX(screenWidth - width).setY(0);
					playerWidgets3.get(name).setX(screenWidth - width).setY(0);
				}
				if(anchor.equalsIgnoreCase("center_left")) {
					playerWidgets1.get(name).setX(0).setY(screenHeight - (height/2));
					playerWidgets2.get(name).setX(0).setY(screenHeight - (height/2));
					playerWidgets3.get(name).setX(0).setY(screenHeight - (height/2));
				}
				if(anchor.equalsIgnoreCase("center_center")) {
					playerWidgets1.get(name).setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
					playerWidgets2.get(name).setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
					playerWidgets3.get(name).setX((screenWidth/2) - (width/2)).setY(screenHeight - (height/2));
				}
				if(anchor.equalsIgnoreCase("center_right")) {
					playerWidgets1.get(name).setX(screenWidth - width).setY(screenHeight - (height/2));
					playerWidgets2.get(name).setX(screenWidth - width).setY(screenHeight - (height/2));
					playerWidgets3.get(name).setX(screenWidth - width).setY(screenHeight - (height/2));
				}
				if(anchor.equalsIgnoreCase("bottom_left")) {
					playerWidgets1.get(name).setX(0).setY(screenHeight - height);
					playerWidgets2.get(name).setX(0).setY(screenHeight - height);
					playerWidgets3.get(name).setX(0).setY(screenHeight - height);
				}
				if(anchor.equalsIgnoreCase("bottom_center")) {
					playerWidgets1.get(name).setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
					playerWidgets2.get(name).setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
					playerWidgets3.get(name).setX((screenWidth/2) - (width/2)).setY(screenHeight - height);
				}
				if(anchor.equalsIgnoreCase("bottom_right")) {
					playerWidgets1.get(name).setX(screenWidth - width).setY(screenHeight - height);
					playerWidgets2.get(name).setX(screenWidth - width).setY(screenHeight - height);
					playerWidgets3.get(name).setX(screenWidth - width).setY(screenHeight - height);
				}
			}
			
			playerWidgets1.get(name).setUrl(common_url + detail_url[hundreds_place]);
			playerWidgets2.get(name).setUrl(common_url + detail_url[tens_place]);
			playerWidgets3.get(name).setUrl(common_url + detail_url[units_place]);
			
			switch(levelLength) {
				case 1:
					playerWidgets3.get(name).shiftXPos(shiftX).shiftYPos(shiftY);
					player.getMainScreen().attachWidget(plugin, playerWidgets3.get(name));
					break;
				case 2:
					playerWidgets2.get(name).shiftXPos(shiftX).shiftYPos(shiftY);
					playerWidgets3.get(name).shiftXPos(shiftX + width).shiftYPos(shiftY);
					player.getMainScreen().attachWidget(plugin, playerWidgets2.get(name));
					player.getMainScreen().attachWidget(plugin, playerWidgets3.get(name));
					break;
				case 3:
					playerWidgets1.get(name).shiftXPos(shiftX).shiftYPos(shiftY);
					playerWidgets2.get(name).shiftXPos(shiftX + width).shiftYPos(shiftY);
					playerWidgets3.get(name).shiftXPos(shiftX + width + width).shiftYPos(shiftY);
					player.getMainScreen().attachWidget(plugin, playerWidgets1.get(name));
					player.getMainScreen().attachWidget(plugin, playerWidgets2.get(name));
					player.getMainScreen().attachWidget(plugin, playerWidgets3.get(name));
					break;
			}
		}
	}
	
	//유저 widget 제거 이벤트
	public void remove(String player) {
		if(playerWidgets1.containsKey(player)) {
			playerWidgets1.remove(player);
			playerWidgets2.remove(player);
			playerWidgets3.remove(player);
		}
	}
	
}
