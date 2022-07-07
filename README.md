# bukkit-IronSaver
A simple bukkit plugin for saving and backing up worlds. 一个简易的Bukkit保存/备份插件

Commands - 指令:

  /ironsave save //save worlds - 保存世界
  
  /ironsave backup //backup worlds to defult folder. - 备份世界到默认文件夹
  
  /ironsave backup [path] //backup worlds to [path]. - 备份世界到[path]
  
  config.yml:
  
  ```
auto-save: #Auto save - 自动保存

  enable: true #Enable the function. - 是否启用
  
  timer(s): 1200 #The delay bettween saves. - 保存时间间隔
  
  save-when-unload: true #Auto save when the plugin is unloaded. - 是否在插件被卸载时保存
  
auto-backup: #Auto backup - 自动备份

  enable: true #Enable the function. - 是否启用
  
  timer(s): 1200 #Delay bettween backups. - 备份时间间隔
  
  backup-when-unload: true #Auto backup when the plugin is unloaded. - 是否在插件被卸载时备份
  
backup: #Backup - 备份

  path: 'backups' #Default backup path. - 默认备份路径

default-language: 'zh_cn' #Default language - 默认语言

date-time-zone: 'Asia/Shanghai' #Time Zone - 时区
  ```
