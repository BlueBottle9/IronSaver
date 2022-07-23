package bluescreen9.minecraft.bukkit.ironsaver;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldUtil {
				public static void save(World world) {
					new BukkitRunnable() {
						@Override
						public void run() {
							world.save();
						}
					}.runTask(Main.IronSaver);
				}
				
				
				public static void saveAll() {
					Main.IronSaver.getServer().savePlayers();
					for (World world:Main.IronSaver.getServer().getWorlds()) {
						save(world);
					}
					Main.Language.broadcastMessage(Main.IronSaver.getServer(),"broadcast.save.complete",MessageProcessor.processor);
				}
				
				public static void backup(World world,Date date) {
					save(world);
					try {
						File worldFolder = world.getWorldFolder();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
						dateFormat.setTimeZone(TimeZone.getTimeZone(Main.Config.getString("date-time-zone")));
						File dest = new File(Main.Config.getString("backup.path") + "/" + dateFormat.format(date),world.getName()  + ".zip");
						dest.getParentFile().mkdirs();
						dest.createNewFile();
								File locker = new File(worldFolder,"session.lock");
								if (locker.exists()) {
									locker.delete();
								}
								ArrayList<File> files = new ArrayList<File>();
								files.add(worldFolder);
								zip(files, dest);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				public static void backup(World world,Date date,String path) {
					save(world);
					try {
						File worldFolder = world.getWorldFolder();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
						dateFormat.setTimeZone(TimeZone.getTimeZone(Main.Config.getString("date-time-zone")));
						File dest = new File(path + "/" + dateFormat.format(date),world.getName() + ".zip");
						dest.getParentFile().mkdirs();
						dest.createNewFile();
								File locker = new File(worldFolder,"session.lock");
								if (locker.exists()) {
									locker.delete();
								}
								ArrayList<File> files = new ArrayList<File>();
								files.add(worldFolder);
								zip(files, dest);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				public static void backupAll() {
					List<World> worlds = Main.IronSaver.getServer().getWorlds();
					Date date = new Date();
					new BukkitRunnable() {
						@Override
						public void run() {
							for (World world:worlds){
								backup(world,date);
							}
							Main.Language.broadcastMessage(Main.IronSaver.getServer(),"broadcast.backup.complete",MessageProcessor.processor);
						}
					}.runTaskAsynchronously(Main.IronSaver);
				}
				
				public static void backupAll(String path) {
					List<World> worlds = Main.IronSaver.getServer().getWorlds();
					Date date = new Date();
					new BukkitRunnable() {
						@Override
						public void run() {
							for (World world:worlds){
								backup(world,date,path);
							}
							Main.Language.broadcastMessage(Main.IronSaver.getServer(),"broadcast.backup.complete",MessageProcessor.processor);
						}
					}.runTaskAsynchronously(Main.IronSaver);
				}
				
				public static void zip(List<File> sources,File dest) {
					try {
							if (!dest.exists()) {
								dest.createNewFile();
							}
							
							ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dest));
							
							ArrayList<ZipEntry> entry = new ArrayList<ZipEntry>();
							entry.addAll(getZipEntry(sources));
							
							for (ZipEntry ze:entry) {
									out.putNextEntry(ze);
								if (ze.isDirectory()) {
									continue;
								}
								File f = new File(sources.get(0).getParentFile().getAbsolutePath() + "/" + ze.getName());
								Files.copy(f.toPath(), out);
							}
							out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}

			public static List<File> listFile(File file) {
			ArrayList<File> files = new ArrayList<File>();
			if (file.isFile()) {
				files.add(file);
				return files;
			}
			for (File f:file.listFiles()) {
				if (f.isFile()) {
					files.add(file);
					continue;
				}
				files.add(f);
				files.addAll(listFile(f));
			}
			return files;
			}

			public static List<ZipEntry> getZipEntry(List<File> file) {
						ArrayList<ZipEntry> entry = new ArrayList<ZipEntry>();
						try {
							ArrayList<File> files = new ArrayList<File>();
							files.addAll(file);
							for (File f:files) {
								if (f.isFile()) {
									entry.add(new ZipEntry(f.getName()));
									continue;
								}
								entry.addAll(getZipEntry(f.listFiles(),f.getName() + "/"));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
			return entry;
			}

			public static List<ZipEntry> getZipEntry(File[] file,String parent){
					ArrayList<ZipEntry> entry = new ArrayList<ZipEntry>();
					try {
							for (File f:file) {
								if (f.isFile()) {
									entry.add(new ZipEntry(parent + f.getName()));
									continue;
								}
								if (f.listFiles() == null) {
									entry.add(new ZipEntry(parent + f.getName()));
									continue;
								}
								if (f.listFiles().length == 0) {
									entry.add(new ZipEntry(parent + f.getName()));
									continue;
								}
								entry.addAll(getZipEntry(f.listFiles(), parent + f.getName() + "/"));
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
			return entry;
			}
}
