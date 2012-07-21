package kr.co.minecraftforum.kdragon.rpgstatus.listeners;

import kr.co.minecraftforum.kdragon.rpgstatus.RPGStatus;
import kr.co.minecraftforum.kdragon.rpgstatus.configuration.ConfigManager;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Background;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.BasicHUD;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.EXP;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Health;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.HeroesClass;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Level;
import kr.co.minecraftforum.kdragon.rpgstatus.widget.Mana;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;
import com.herocraftonline.heroes.api.events.HeroRegainManaEvent;
import com.herocraftonline.heroes.api.events.SkillCompleteEvent;
import com.herocraftonline.heroes.characters.Hero;

public class PlayerListeners implements Listener {
	
	private RPGStatus plugin;
	private Health h;
	private Mana m;
	private Level l;
	private EXP ex;
	private HeroesClass hc;
	private Background bg;
	private BasicHUD bh;
	
	private ConfigurationSection basicWidgetConfig;
	private boolean isBEvisible;
	
	public PlayerListeners(RPGStatus plugin) {
		this.plugin = plugin;
		h = new Health();;
		m = new Mana();
		l = new Level();
		ex = new EXP();
		hc = new HeroesClass();
		bg = new Background();
		bh = new BasicHUD();
		basicWidgetConfig = ConfigManager.getBasicWidgetConfig();
		isBEvisible = basicWidgetConfig.getConfigurationSection("EXPbar").getBoolean("visible");
	}
	
	//플레이어 접속 이벤트(위젯 생성).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void spoutCraftEnableEvent(SpoutCraftEnableEvent e) {
		
		if(e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			SpoutPlayer player = (SpoutPlayer) e.getPlayer();
			Player p = player.getPlayer();
			Hero hero = RPGStatus.heroes.getCharacterManager().getHero(p);
			
			int maxHealth = hero.getMaxHealth();
			int health = hero.getHealth();
			int maxMana = hero.getMaxMana();
			int mana = hero.getMana();
			int level = hero.getLevel();
			float exp = p.getExp();
			String className = hero.getHeroClass().getName();
			
			bh.initializeHealthBar(player);
			bh.initializeEXPBar(player);
			bh.initializeHungerBar(player);
			bh.initializeArmorBar(player);
			bh.initializeBubbleBar(player);
			
			h.create(plugin, player, maxHealth, health);
			m.create(plugin, player, maxMana, mana);
			l.create(plugin, player, level);
			ex.create(plugin, player, exp);
			hc.create(plugin, player, className);
			bg.create(plugin, player);
			
		}
		
	}
	
	//플레이어 체력 회복 이벤트(체력 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void regainHealthEvent(EntityRegainHealthEvent e) {
		
		int eid = e.getEntity().getEntityId();
		final Player p = playerEID(eid);
		if(p != null) {
			final Hero hero = RPGStatus.heroes.getCharacterManager().getHero(p);
			final SpoutPlayer player = (SpoutPlayer) p;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					int maxHealth = hero.getMaxHealth();
					int health = hero.getHealth();
					
					h.change(plugin, player, maxHealth, health);
				}
				
			}, 1L);
		}
	}
	
	//플레이어 데미지 이벤트(체력, 마나 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void damageEvent(EntityDamageEvent e) {
		
		int eid = e.getEntity().getEntityId();
		Player p = playerEID(eid);
		if(p != null) {
			final Hero hero = RPGStatus.heroes.getCharacterManager().getHero(p);
			final SpoutPlayer player = (SpoutPlayer) p;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					int maxHealth = hero.getMaxHealth();
					int health = hero.getHealth();
					int maxMana = hero.getMaxMana();
					int mana = hero.getMana();
					
					h.change(plugin, player, maxHealth, health);
					m.change(plugin, player, maxMana, mana);
				}
				
			}, 1L);
		}
	}
	
	//플레이어 스킬 사용 이벤트(체력, 마나 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void skillCompleteEvent(SkillCompleteEvent e) {
		Player p = e.getHero().getPlayer();
		SpoutPlayer player = SpoutManager.getPlayer(p);
		
		int maxHealth = e.getHero().getMaxHealth();
		int health = e.getHero().getHealth();
		int maxMana = e.getHero().getMaxMana();
		int mana = e.getHero().getMana();
		
		h.change(plugin, player, maxHealth, health);
		m.change(plugin, player, maxMana, mana);
	}
	
	//플레이어 마나 회복 이벤트(마나 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void regainManaEvent(HeroRegainManaEvent e) {
		final Hero hero = e.getHero();
		Player p = e.getHero().getPlayer();
		final SpoutPlayer player = SpoutManager.getPlayer(p);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				int maxMana = hero.getMaxMana();
				int mana = hero.getMana();
				
				m.change(plugin, player, maxMana, mana);
			}
		}, 0L);
	}
	
	//플레이어 레벨 변경 이벤트(레벨 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void levelChangeEvent(HeroChangeLevelEvent e) {
		Hero hero = e.getHero();
		Player p = hero.getPlayer();
		SpoutPlayer player = SpoutManager.getPlayer(p);
		int level = hero.getLevel();
		
		l.change(plugin, player, level);
	}
	
	//플레이어 경험치 변경 이벤트(경험치 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void experienceChangeEvent(ExperienceChangeEvent e) {
		final Player p = e.getHero().getPlayer();
		final SpoutPlayer player = SpoutManager.getPlayer(p);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				float exp = p.getExp();
				
				ex.change(plugin, player, exp);
			}
		}, 1L);
		
	}
	
	//플레이어 히어로 클래스 변경 이벤트(히어로 클래스 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void classChangeEvent(ClassChangeEvent e) {
		Player p = e.getHero().getPlayer();
		final SpoutPlayer player = SpoutManager.getPlayer(p);
		final Hero hero = e.getHero();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				String className = hero.getHeroClass().getName();
				int maxHealth = hero.getMaxHealth();
				int health = hero.getHealth();
				int maxMana = hero.getMaxMana();
				int mana = hero.getMana();
				int level = hero.getLevel();
				
				h.change(plugin, player, maxHealth, health);
				m.change(plugin, player, maxMana, mana);
				l.change(plugin, player, level);
				hc.change(plugin, player, className);
			}
		}, 1L);
	}
	
	//플레이어 죽을 시 이벤트(위젯 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void deathEvent(PlayerDeathEvent e) {
		
		int eid = e.getEntity().getEntityId();
		Player p = playerEID(eid);
		if(p != null) {
			Hero hero = RPGStatus.heroes.getCharacterManager().getHero(p);
			SpoutPlayer player = (SpoutPlayer) p;
			
			int maxHealth = hero.getMaxHealth();
			int health = hero.getHealth();
			int maxMana = hero.getMaxMana();
			int mana = hero.getMana();
			
			h.change(plugin, player, maxHealth, health);
			m.change(plugin, player, maxMana, mana);
		}
	}
	
	//플레이어 리스폰 시 이벤트(위젯 업데이트).
	@EventHandler (priority = EventPriority.HIGHEST)
	public void respawnEvent(PlayerRespawnEvent e) {
		
		Player p = e.getPlayer();
		final Hero hero = RPGStatus.heroes.getCharacterManager().getHero(p);
		final SpoutPlayer player = (SpoutPlayer) p;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				int maxHealth = hero.getMaxHealth();
				int health = hero.getHealth();
				int maxMana = hero.getMaxMana();
				int mana = hero.getMana();
				
				h.change(plugin, player, maxHealth, health);
				m.change(plugin, player, maxMana, mana);
			}
		}, 1L);
	}
	
	//게임모드 변경 이벤트
	@EventHandler (priority = EventPriority.HIGHEST)
	public void playerGamemodeChangeEvent(PlayerGameModeChangeEvent e) {
		final Player p = e.getPlayer();
		final SpoutPlayer player = (SpoutPlayer) e.getPlayer();
		final Hero hero = RPGStatus.heroes.getCharacterManager().getHero(p);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				int maxHealth = hero.getMaxHealth();
				int health = hero.getHealth();
				int maxMana = hero.getMaxMana();
				int mana = hero.getMana();
				int level = hero.getLevel();
				float exp = p.getExp();
				String className = hero.getHeroClass().getName();
				
				if(p.getGameMode() == GameMode.SURVIVAL) {
					
					bh.updateHealthBar(player);
					bh.updateEXPBar(player);
					bh.updateHungerBar(player);
					bh.updateArmorBar(player);
					bh.updateBubbleBar(player);
					
					h.create(plugin, player, maxHealth, health);
					m.create(plugin, player, maxMana, mana);
					l.create(plugin, player, level);
					ex.create(plugin, player, exp);
					hc.create(plugin, player, className);
					bg.create(plugin, player);
				}
				
				if(p.getGameMode() == GameMode.CREATIVE) {
					player.getMainScreen().removeWidgets(plugin);
					h.remove(player.getName());
					m.remove(player.getName());
					l.remove(player.getName());
					ex.remove(player.getName());
					hc.remove(player.getName());
					bg.remove(player.getName());
				}
			}
		}, 1L);
	}
	
	//플에이어 접속 종료 이벤트
	@EventHandler (priority = EventPriority.HIGHEST)
	public void playerQuitEvent(PlayerQuitEvent e) {
		String player = e.getPlayer().getName();
		
		h.remove(player);
		m.remove(player);
		l.remove(player);
		ex.remove(player);
		hc.remove(player);
		bg.remove(player);
	}

	//플레이어 Entity ID 확인 메서드. 이벤트 방생 Entity의 Entity ID와 일치하는 플레이어를 반환.
	public Player playerEID(int eid) {
		
		int i = 0;
		Player[] players = Bukkit.getOnlinePlayers();
		Player p = null;
		for(i = 0 ; i < players.length ; i++) {
			if(players[i].getEntityId() == eid) {
				p = players[i];
			}
		}
		return p;
	}
	
}
