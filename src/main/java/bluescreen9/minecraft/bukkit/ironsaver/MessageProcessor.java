package bluescreen9.minecraft.bukkit.ironsaver;

import org.bukkit.ChatColor;

public class MessageProcessor implements bluescreen9.minecraft.bukkit.lang.MessageProcessor{
					public static MessageProcessor processor = new MessageProcessor();
	@Override
	public String process(String original) {
		return ChatColor.translateAlternateColorCodes('&', original);
	}
}
