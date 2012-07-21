package kr.co.minecraftforum.kdragon.rpgstatus.widget;

import java.util.HashMap;
import java.util.Map;

import kr.co.minecraftforum.kdragon.rpgstatus.RPGStatus;
import kr.co.minecraftforum.kdragon.rpgstatus.configuration.ConfigManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class EXP {
	
	private static Map<String, GenericTexture> playerWidgets = new HashMap<String, GenericTexture>();
	private static Map<String, GenericLabel> playerLabels = new HashMap<String, GenericLabel>();
	
	private static Color textColor;
	
	public static RPGStatus plugin;
	public static ConfigurationSection exp;
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
	private static boolean isUseValueText;
	private static String text_priority;
	private static String text_anchor;
	private static float text_scale;
	private static String text_color;
	private static double text_shiftX_rate;
	private static double text_shiftY_rate;
	
	static {
		isEnabled = ConfigManager.getEXPConfig().getBoolean("enabled");
		if(isEnabled) {
			plugin = new RPGStatus();
			exp = ConfigManager.getEXPConfig();
			priority = exp.getString("priority");
			anchor = exp.getString("anchor");
			size_lock = exp.getBoolean("size_lock");
			width_rate = exp.getDouble("width_rate");
			height_rate = exp.getDouble("height_rate");
			shiftX_rate = exp.getDouble("shiftX_rate");
			shiftY_rate = exp.getDouble("shiftY_rate");
			common_url = exp.getString("common_url");
			detail_url = new String[101];
			if(isUseValueText) {
				text_priority = exp.getString("text_priority");
				text_anchor = exp.getString("text_anchor");
				text_scale = (float) exp.getDouble("text_scale");
				text_color = exp.getString("text_color");
				text_shiftX_rate = exp.getDouble("text_shiftX_rate");
				text_shiftY_rate = exp.getDouble("text_shiftY_rate");
				textColor = new Color(text_color);
			}
			setTextureURL();
		}
	}
	
	//경험치 위젯 이미지를 preLoginCache에 추가(로그인 하기 전에 다운로드 하도록 함).
	public static void addPreLoginCache(Plugin plugin) {
		if(isEnabled) {
			int i = 0;
			for(i = 0 ; i <= 100 ; i++) {
				if(i == 0) {
					SpoutManager.getFileManager().addToPreLoginCache(plugin, common_url + detail_url[i]);
				}
				if(i != 0) {
					if(detail_url[i] == detail_url[i-1]) {}
					if(detail_url[i] != detail_url[i-1]) {
						SpoutManager.getFileManager().addToPreLoginCache(plugin, common_url + detail_url[i]);
					}
				}
			}
		}
	}
	
	//0~100%까지 1퍼센트 단위로 텍스쳐 url 지정.
	public static void setTextureURL() {
		int i = 0;
		for(i = 100 ; i >= 0 ; i--) {
			String detail = exp.getConfigurationSection("details").getString(i + "%");
			if(detail != null) {
				detail_url[i] = detail;
			}
			if(detail == null) {
				searchDown(i, 0);
			}
		}
	}
	
	//아래로 내려가며 url이 존재하는 값 검색.
	public static void searchDown(int i, int repeat) {
		int j = i - 1;
		String detail = exp.getConfigurationSection("details").getString(j + "%");
		if(detail != null) {
			detail_url[i + repeat] = detail;
		}
		if(detail == null) {
			searchDown(j, repeat + 1);
		}
	}
	
	//유저 처음 접속 시 경험치 위젯 생성 메서드.
	public void create(Plugin plugin, SpoutPlayer player, float exp) {
		if(isEnabled) {
			String name = player.getName();
			
			GenericTexture texture = new GenericTexture();
			
			
			int ePercent = (int) (exp*100);
			
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
			texture.setUrl(common_url + detail_url[ePercent]);
			
			playerWidgets.put(name, texture);
			player.getMainScreen().attachWidget(plugin, playerWidgets.get(name));
			
			if(isUseValueText) {
				GenericLabel label = new GenericLabel();
				label.setText(exp + "%").setTextColor(textColor).setScale(text_scale);
				
				int text_shiftX = (int) text_shiftX_rate*screenWidth/100;
				int text_shiftY = (int) text_shiftY_rate*screenHeight/100;
				
				if(text_priority.equalsIgnoreCase("highest"))
					label.setPriority(RenderPriority.Highest);
				if(text_priority.equalsIgnoreCase("high"))
					label.setPriority(RenderPriority.High);
				if(text_priority.equalsIgnoreCase("normal"))
					label.setPriority(RenderPriority.Normal);
				if(text_priority.equalsIgnoreCase("low"))
					label.setPriority(RenderPriority.Low);
				if(text_priority.equalsIgnoreCase("lowest"))
					label.setPriority(RenderPriority.Lowest);
				
				if(text_anchor.equalsIgnoreCase("top_left"))
					label.setAnchor(WidgetAnchor.TOP_LEFT);
				if(text_anchor.equalsIgnoreCase("top_center"))
					label.setAnchor(WidgetAnchor.TOP_CENTER).shiftXPos(-(width/2));
				if(text_anchor.equalsIgnoreCase("top_right"))
					label.setAnchor(WidgetAnchor.TOP_RIGHT).shiftXPos(-width);
				if(text_anchor.equalsIgnoreCase("center_left"))
					label.setAnchor(WidgetAnchor.CENTER_LEFT).shiftYPos(-(height/2));
				if(text_anchor.equalsIgnoreCase("center_center"))
					label.setAnchor(WidgetAnchor.CENTER_CENTER).shiftXPos(-(width/2)).shiftYPos(-(height/2));
				if(text_anchor.equalsIgnoreCase("center_right"))
					label.setAnchor(WidgetAnchor.CENTER_RIGHT).shiftXPos(-width).shiftYPos(-(height/2));
				if(text_anchor.equalsIgnoreCase("bottom_left"))
					label.setAnchor(WidgetAnchor.BOTTOM_LEFT).shiftYPos(-height);
				if(text_anchor.equalsIgnoreCase("bottom_center"))
					label.setAnchor(WidgetAnchor.BOTTOM_CENTER).shiftXPos(-(width/2)).shiftYPos(-height);
				if(text_anchor.equalsIgnoreCase("bottom_right"))
					label.setAnchor(WidgetAnchor.BOTTOM_RIGHT).shiftXPos(-width).shiftYPos(-height);
				
				label.shiftXPos(text_shiftX).shiftYPos(text_shiftY);
				
				playerLabels.put(name, label);
				player.getMainScreen().attachWidget(plugin, playerLabels.get(name));
			}
		}
	}
	
	//유저 경험치 변경 시 경험치 위젯 업데이트 메서드.
	public void change(Plugin plugin, SpoutPlayer player, float exp) {
		String name = player.getName();
		if(playerWidgets.containsKey(name)) {
			int ePercent = (int) (exp*100);
			
			playerWidgets.get(name).setUrl(common_url + detail_url[ePercent]);
			
			player.getMainScreen().updateWidget(playerWidgets.get(name));
		}
		if(playerLabels.containsKey(name)) {
			playerLabels.get(name).setText(exp + "%");
			
			player.getMainScreen().updateWidget(playerLabels.get(name));
		}
	}
	
	//유저 widget 제거 이벤트
	public void remove(String player) {
		if(playerWidgets.containsKey(player))
			playerWidgets.remove(player);
	}
	
}
