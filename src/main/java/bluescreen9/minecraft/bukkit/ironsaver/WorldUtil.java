package bluescreen9.minecraft.bukkit.ironsaver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
					MessageUtil.broadcast("broadcast.save.complete");
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
								zip(worldFolder, dest);
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
								zip(worldFolder, dest);
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
							MessageUtil.broadcast("broadcast.backup.complete");
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
							MessageUtil.broadcast("broadcast.backup.complete");
						}
					}.runTaskAsynchronously(Main.IronSaver);
				}
				
				public static void zip(File src, File dest) {
					int byte_size = 2048;
					 try {
			              ZipOutputStream ziper = new ZipOutputStream(new FileOutputStream(dest));
			              if (src.isFile()) {
			                BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
			                ZipEntry fileEntry = new ZipEntry(src.getName());
			                ziper.putNextEntry(fileEntry);
			                int size = in.available();
			                int byteSize = 0;
			                if (size / byte_size < 1) {
			                  byteSize = 1;
			                } else {
			                  byteSize = byte_size;
			                } 
			                byte[] data = new byte[byteSize];
			                int blockNum = size / byteSize;
			                int Left = 0;
			                Left = size % byteSize;
			                for (long i = 0L; i <= blockNum; i++) {
			                  if (i < blockNum) {
			                    in.read(data);
			                  } else {
			                    data = new byte[Left];
			                    in.read(data);
			                  } 
			                  ziper.write(data);
			                  ziper.flush();
			                } 
			                ziper.closeEntry();
			                ziper.close();
			                in.close();
			              } else {
			                ArrayList<ZipEntry> Entrys = (ArrayList<ZipEntry>)fileToZipEntrys(src);
			                for (ZipEntry z : Entrys) {
			                  if (!z.getName().endsWith("/")) {
			                    BufferedInputStream readerZ = new BufferedInputStream(new FileInputStream(new File(src.getParentFile(), z.getName())));
			                    ziper.putNextEntry(z);
			                    int size = readerZ.available();
			                    int ByteSize = 0;
			                    if (size / byte_size < 1) {
			                      ByteSize = 1;
			                    } else {
			                      ByteSize = byte_size;
			                    } 
			                    byte[] data = new byte[ByteSize];
			                    int BlockNum = size / ByteSize;
			                    int Left = 0;
			                    Left = size % ByteSize;
			                    for (long i = 0L; i <= BlockNum; i++) {
			                      if (i < BlockNum) {
			                        readerZ.read(data);
			                      } else {
			                        data = new byte[Left];
			                        readerZ.read(data);
			                      } 
			                      ziper.write(data);
			                      ziper.flush();
			                    } 
			                    readerZ.close();
			                    ziper.closeEntry();
			                    continue;
			                  } 
			                  ziper.putNextEntry(z);
			                  ziper.closeEntry();
			                } 
			                ziper.close();
			              } 
			            } catch (Exception e) {
			              e.printStackTrace();
			            } 
				  }
				
				 public static List<ZipEntry> fileToZipEntrys(File file) {
					    ArrayList<ZipEntry> entryList = new ArrayList<>();
					    if (file.isFile()) {
					      entryList.add(new ZipEntry(file.getName()));
					    } else {
					      File[] fileList = file.listFiles();
					      if (fileList != null) {
					        byte b;
					        int i;
					        File[] arrayOfFile;
					        for (i = (arrayOfFile = fileList).length, b = 0; b < i; ) {
					          File f = arrayOfFile[b];
					          entryList.addAll(fileToZipEntrys(f, String.valueOf(file.getName()) + "/"));
					          b++;
					        } 
					      } else {
					        entryList.add(new ZipEntry(String.valueOf(file.getName()) + "/"));
					      } 
					    } 
					    return entryList;
					  }
				 
				 private static List<ZipEntry> fileToZipEntrys(File file, String parent) {
					    ArrayList<ZipEntry> entryList = new ArrayList<>();
					    if (file.isFile()) {
					      entryList.add(new ZipEntry(String.valueOf(parent) + file.getName()));
					    } else {
					      entryList.add(new ZipEntry(String.valueOf(parent) + file.getName() + "/"));
					      File[] fileList = file.listFiles();
					      byte b;
					      int i;
					      File[] arrayOfFile1;
					      for (i = (arrayOfFile1 = fileList).length, b = 0; b < i; ) {
					        File f = arrayOfFile1[b];
					        entryList.addAll(fileToZipEntrys(f, String.valueOf(parent) + file.getName() + "/"));
					        b++;
					      } 
					    } 
					    return entryList;
					  }
}
