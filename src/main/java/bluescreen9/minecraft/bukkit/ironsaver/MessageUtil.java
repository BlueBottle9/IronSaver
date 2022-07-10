package bluescreen9.minecraft.bukkit.ironsaver;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MessageUtil {
				public static void sendMessage(Player player,String key) {
						String message = Main.Language.get(player, key);
						player.sendMessage( ChatColor.translateAlternateColorCodes('&', message));
				}
				
				public static void sendMessage(CommandSender target,String key) {
					if (target instanceof Player) {
						sendMessage((Player)target, key);
						return;
					}
					
					String message = Main.Language.get(Main.Language.getDefaultLang(), key);
					target.sendMessage( ChatColor.translateAlternateColorCodes('&', message));
				}
				
				public static void broadcast(String key) {
						for (Player p:Main.IronSaver.getServer().getOnlinePlayers()) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(p, key)));
						}
				}
}
