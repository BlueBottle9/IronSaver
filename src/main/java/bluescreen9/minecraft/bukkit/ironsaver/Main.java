package bluescreen9.minecraft.bukkit.ironsaver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import bluescreen9.minecraft.bukkit.lang.Lang;

public class Main extends JavaPlugin{
	
	protected static Plugin IronSaver;
	protected static Lang Language;
	protected static FileConfiguration Config;
				@Override
				public void onEnable() {
					saveDefaultConfig();
					reloadConfig();
					Config = getConfig();
					IronSaver = Main.getPlugin(Main.class);
					Language = new Lang(IronSaver);
					Language.copyDeafultLangFile();
					Language.loadLanguages();
					Language.setDefaultLang(Config.getString("default-language"));
					getCommand("ironsave").setExecutor(new IronSaverCommand());
					getCommand("ironsave").setTabCompleter(new IronSaverCommand());
					
					if (Config.getBoolean("auto-save.enable")) {
						new BukkitRunnable() {
							@Override
							public void run() {
								MessageUtil.broadcast("broadcast.saving");
								WorldUtil.saveAll();
							}
						}.runTaskTimer(IronSaver, Config.getLong("auto-save.timer(s)") * 20, Config.getLong("auto-save.timer(s)") * 20);
					}
					
					if (Config.getBoolean("auto-backup.enable")) {
						new BukkitRunnable() {
							@Override
							public void run() {
								MessageUtil.broadcast("broadcast.backup");
								WorldUtil.backupAll();
							}
						}.runTaskTimerAsynchronously(IronSaver, Config.getLong("auto-backup.timer(s)") * 20, Config.getLong("auto-backup.timer(s)") * 20);
					}
				}
				
				@Override
				public void onDisable() {
						
				}
}
