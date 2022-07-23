package bluescreen9.minecraft.bukkit.ironsaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitRunnable;

public class IronSaverCommand implements TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		ArrayList<String> completes = new ArrayList<String>();
		if (!sender.isOp()) {
			return completes;
		}
		if (args.length == 1) {
			completes.add("save");
			completes.add("backup");
			return completes;
		}
		return completes;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp()) {
			Main.Language.sendMessage(sender, "command.nopermission",MessageProcessor.processor);
			return true;
		}
		
		
		if (args.length == 0) {
			Main.Language.sendMessage(sender, "command.wrongusage",MessageProcessor.processor);
			return true;
		}
		
		if ("save".equals(args[0]) && args.length != 1) {
			Main.Language.sendMessage(sender, "command.wrongusage",MessageProcessor.processor);
			return true;
		}
		
		if ("save".equals(args[0])) {
			new BukkitRunnable() {
				@Override
				public void run() {
					WorldUtil.saveAll();
				}
			}.runTask(Main.IronSaver);
			Main.Language.sendMessage(sender, "command.saving",MessageProcessor.processor);
			Main.Language.broadcastMessage(Main.IronSaver.getServer(),"broadcast.saving",MessageProcessor.processor);
			return true;
		}
		
		if (args.length == 1) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Main.Language.sendMessage(sender, "command.backup",MessageProcessor.processor);
					Main.Language.broadcastMessage(Main.IronSaver.getServer(),"broadcast.backup",MessageProcessor.processor);
					WorldUtil.backupAll();
				}
			}.runTask(Main.IronSaver);
			return true;
		}
		
		StringBuilder builder = new StringBuilder();
		for (int i = 1;i < args.length;i++) {
			builder.append(args[i]);
		}
		
		File folder = new File(builder.toString());
		if (!folder.exists()) {
			Main.Language.sendMessage(sender, "command.path.notexsit",MessageProcessor.processor);
			return true;
		}
		
		if (!folder.isDirectory()) {
			Main.Language.sendMessage(sender, "command.path.notdirectory",MessageProcessor.processor);
			return true;
		}
		Main.Language.sendMessage(sender, "command.backup",MessageProcessor.processor);
		Main.Language.broadcastMessage(Main.IronSaver.getServer(),"broadcast.backup",MessageProcessor.processor);
		new BukkitRunnable() {
			@Override
			public void run() {
				WorldUtil.backupAll(folder.getAbsolutePath());
			}
		}.runTask(Main.IronSaver);
		return true;
	}

}
