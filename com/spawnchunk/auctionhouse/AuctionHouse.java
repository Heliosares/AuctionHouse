/*     */ package com.spawnchunk.auctionhouse;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.commands.AHCommand;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.DropUnclaimedEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ListingCleanupEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ServerTickEvent;
/*     */ import com.spawnchunk.auctionhouse.listeners.AuctionListener;
/*     */ import com.spawnchunk.auctionhouse.listeners.CleanupListener;
/*     */ import com.spawnchunk.auctionhouse.listeners.DiscordSRVListener;
/*     */ import com.spawnchunk.auctionhouse.listeners.MenuListener;
/*     */ import com.spawnchunk.auctionhouse.listeners.PlayerListener;
/*     */ import com.spawnchunk.auctionhouse.menus.MenuManager;
/*     */ import com.spawnchunk.auctionhouse.modules.Listings;
/*     */ import com.spawnchunk.auctionhouse.nms.NMS;
/*     */ import com.spawnchunk.auctionhouse.nms.v1_19_R1;
/*     */ import com.spawnchunk.auctionhouse.placeholders.Expansion;
/*     */ import com.spawnchunk.auctionhouse.placeholders.MVdWExpansion;
/*     */ import com.spawnchunk.auctionhouse.storage.DatabaseStorage;
/*     */ import com.spawnchunk.auctionhouse.storage.ImportDBFile;
/*     */ import com.spawnchunk.auctionhouse.storage.ImportDatFile;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.WorldUtil;
/*     */ import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.TreeMap;
/*     */ import java.util.UUID;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import me.arcaniax.hdb.api.HeadDatabaseAPI;
/*     */ import net.milkbowl.vault.chat.Chat;
/*     */ import net.milkbowl.vault.economy.Economy;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.ConsoleCommandSender;
/*     */ import org.bukkit.command.PluginCommand;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.RegisteredServiceProvider;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public final class AuctionHouse extends JavaPlugin implements Listener {
/*     */   public static AuctionHouse plugin;
/*     */   public static PluginManager pm;
/*  55 */   public static Chat chat = null;
/*  56 */   public static Economy econ = null;
/*  57 */   public static HeadDatabaseAPI hdb = null;
/*  58 */   public static TokenEnchantAPI te = null;
/*     */   
/*     */   public static boolean discord = false;
/*  61 */   public static Connection conn = null;
/*     */   
/*  63 */   public static DiscordSRVListener discordSRVListener = new DiscordSRVListener();
/*     */   
/*     */   public static NMS nms;
/*     */   
/*     */   public static Config config;
/*     */   
/*     */   public static Logger logger;
/*     */   public static String version;
/*     */   public static int mcVersion;
/*     */   public static String servername;
/*     */   public static String levelname;
/*  74 */   public static Listings listings = new Listings();
/*  75 */   public static final MenuManager menuManager = new MenuManager();
/*     */   
/*  77 */   public static Map<UUID, Long> playerCooldowns = new HashMap<>();
/*     */   
/*     */   public static int tickEventId;
/*     */   
/*     */   public static int dropEventId;
/*     */   public static int cleanupEventId;
/*  83 */   public static final Map<String, TreeMap<String, String>> locales = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  93 */     plugin = this;
/*  94 */     logger = plugin.getLogger();
/*     */ 
/*     */     
/*  97 */     LocaleStorage.loadLocales();
/*     */ 
/*     */     
/*     */     try {
/* 101 */       Class.forName("org.sqlite.JDBC");
/* 102 */     } catch (ClassNotFoundException e) {
/* 103 */       MessageUtil.logSevere("Could not load SQLite JDBC driver!");
/* 104 */       MessageUtil.logSevere("Plugin will be disabled!");
/* 105 */       getServer().getPluginManager().disablePlugin((Plugin)plugin);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 110 */     conn = DatabaseStorage.getConnection();
/* 111 */     if (conn == null) {
/* 112 */       MessageUtil.logSevere("Could not create SQLite database!");
/* 113 */       MessageUtil.logSevere("Plugin will be disabled!");
/* 114 */       getServer().getPluginManager().disablePlugin((Plugin)plugin);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 119 */     String packageName = plugin.getServer().getClass().getPackage().getName();
/* 120 */     version = packageName.substring(packageName.lastIndexOf('.') + 1);
/*     */     try {
/* 122 */       mcVersion = Integer.parseInt(version.replaceAll("[^0-9]", ""));
/* 123 */     } catch (NumberFormatException numberFormatException) {}
/* 124 */     logger.info(String.format("Using NMS version %s", new Object[] { version }));
/* 125 */     switch (version) {
/*     */       case "v1_19_R1":
/* 127 */         nms = (NMS)new v1_19_R1();
/*     */         break;
/*     */       default:
/* 130 */         MessageUtil.logSevere("Error! This plugin only supports Spigot version 1.19!");
/* 131 */         MessageUtil.logSevere("Plugin will be disabled!");
/* 132 */         getServer().getPluginManager().disablePlugin((Plugin)plugin);
/*     */         return;
/*     */     } 
/*     */     
/* 136 */     levelname = WorldUtil.getMainWorld();
/*     */ 
/*     */     
/* 139 */     onServerFullyLoaded();
/*     */ 
/*     */     
/* 142 */     if (setupHDB()) {
/* 143 */       logger.log(Level.INFO, "Enabled HeadDatabase support");
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (setupDiscordSRV()) {
/* 148 */       logger.log(Level.INFO, "Enabled DiscordSRV support");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 153 */     config = new Config();
/*     */     
/* 155 */     servername = LocaleStorage.translate("server.name", Config.locale);
/*     */ 
/*     */     
/* 158 */     if (!Config.chat_hook) {
/* 159 */       chat = null;
/* 160 */       MessageUtil.logWarning("Meta-based auction limits will be disabled");
/*     */     } 
/*     */ 
/*     */     
/* 164 */     DatabaseStorage.readAllListings(conn);
/*     */ 
/*     */     
/* 167 */     ImportDatFile.importData();
/*     */     
/* 169 */     ImportDBFile.importData();
/*     */ 
/*     */     
/* 172 */     Server server = getServer();
/* 173 */     ConsoleCommandSender consoleCommandSender = server.getConsoleSender();
/* 174 */     BukkitScheduler scheduler = server.getScheduler();
/*     */     
/* 176 */     PluginManager pm = Bukkit.getPluginManager();
/* 177 */     ServerTickEvent serverTickEvent = new ServerTickEvent((CommandSender)consoleCommandSender, server);
/* 178 */     tickEventId = scheduler.scheduleSyncRepeatingTask((Plugin)this, () -> pm.callEvent((Event)serverTickEvent), 0L, Config.updateTicks);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     long dropTicks = Math.max(1200L, Config.unclaimed_check_duration / 50L);
/* 184 */     DropUnclaimedEvent dropUnclaimedEvent = new DropUnclaimedEvent((CommandSender)consoleCommandSender, server);
/* 185 */     dropEventId = scheduler.scheduleSyncRepeatingTask((Plugin)this, () -> pm.callEvent((Event)dropUnclaimedEvent), 0L, dropTicks);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     long cleanupTicks = Math.max(1200L, Config.auction_cleanup_duration / 50L);
/* 191 */     if (Config.debug) logger.info(String.format("cleanupTicks = %s", new Object[] { Long.valueOf(cleanupTicks) })); 
/* 192 */     ListingCleanupEvent listingCleanupEvent = new ListingCleanupEvent((CommandSender)consoleCommandSender, server);
/* 193 */     cleanupEventId = scheduler.scheduleSyncRepeatingTask((Plugin)this, () -> pm.callEvent((Event)listingCleanupEvent), 0L, cleanupTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 201 */     menuManager.closeAllMenus();
/*     */ 
/*     */     
/* 204 */     logger.info("Saving auctions data");
/*     */ 
/*     */     
/* 207 */     if (conn != null) {
/*     */       try {
/* 209 */         conn.close();
/* 210 */       } catch (SQLException e) {
/* 211 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean setupEconomy() {
/* 217 */     RegisteredServiceProvider<Economy> rsp_econ = getServer().getServicesManager().getRegistration(Economy.class);
/* 218 */     if (rsp_econ != null) {
/* 219 */       logger.info(String.format("Registered Service Provider %s for Vault's Economy API", new Object[] { rsp_econ.getPlugin().getName() }));
/* 220 */       econ = (Economy)rsp_econ.getProvider();
/*     */     } 
/* 222 */     return (econ != null);
/*     */   }
/*     */   
/*     */   private boolean setupChat() {
/* 226 */     RegisteredServiceProvider<Chat> rsp_chat = getServer().getServicesManager().getRegistration(Chat.class);
/* 227 */     if (rsp_chat != null) {
/* 228 */       logger.info(String.format("Registered Service Provider %s for Vault's Chat API", new Object[] { rsp_chat.getPlugin().getName() }));
/* 229 */       chat = (Chat)rsp_chat.getProvider();
/*     */     } 
/* 231 */     return (chat != null);
/*     */   }
/*     */   
/*     */   private boolean setupHDB() {
/* 235 */     hdb = getServer().getPluginManager().isPluginEnabled("HeadDatabase") ? new HeadDatabaseAPI() : null;
/* 236 */     return (hdb != null);
/*     */   }
/*     */   
/*     */   private boolean setupDiscordSRV() {
/* 240 */     discord = getServer().getPluginManager().isPluginEnabled("DiscordSRV");
/* 241 */     return discord;
/*     */   }
/*     */   
/*     */   public void onServerFullyLoaded() {
/* 245 */     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, () -> {
/*     */           pm = getServer().getPluginManager();
/*     */           if (Config.economy.equalsIgnoreCase("vault")) {
/*     */             if (pm.getPlugin("Vault") == null) {
/*     */               MessageUtil.logSevere("Error! Required Vault plugin was not found!");
/*     */               MessageUtil.logSevere("Plugin will be disabled!");
/*     */               pm.disablePlugin((Plugin)plugin);
/*     */               return;
/*     */             } 
/*     */             if (!setupEconomy()) {
/*     */               MessageUtil.logSevere("Error! No plugin supporting Vault's Economy API was found!");
/*     */               MessageUtil.logSevere("Plugin will be disabled!");
/*     */               pm.disablePlugin((Plugin)plugin);
/*     */               return;
/*     */             } 
/*     */             if (!setupChat()) {
/*     */               MessageUtil.logWarning("Warning! No plugin supporting Vault's Chat API was found!");
/*     */               MessageUtil.logWarning("Meta-based auction limits are disabled.");
/*     */             } 
/*     */           } else if (Config.economy.equalsIgnoreCase("tokenenchant")) {
/*     */             if (pm.getPlugin("TokenEnchant") != null) {
/*     */               te = TokenEnchantAPI.getInstance();
/*     */               if (te != null) {
/*     */                 logger.info("Enabled TokenEnchant API support");
/*     */               }
/*     */             } else {
/*     */               logger.warning("Error! Required TokenEnchant plugin was not found!");
/*     */               logger.warning("Plugin will be disabled!");
/*     */               pm.disablePlugin((Plugin)plugin);
/*     */               return;
/*     */             } 
/*     */             if (pm.getPlugin("Vault") == null && !setupChat()) {
/*     */               MessageUtil.logWarning("Warning! No plugin supporting Vault's Chat API was found!");
/*     */               MessageUtil.logWarning("Meta-based auction limits are disabled.");
/*     */             } 
/*     */           } else {
/*     */             MessageUtil.logSevere("Error! Economy plugin was not found!");
/*     */             MessageUtil.logSevere("Plugin will be disabled!");
/*     */             pm.disablePlugin((Plugin)plugin);
/*     */             return;
/*     */           } 
/*     */           Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, this::registerExpansion);
/*     */           Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, this::registerMVdW);
/*     */           Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, this::registerListeners);
/*     */           Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, this::registerCommands);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerListeners() {
/* 313 */     getServer().getPluginManager().registerEvents((Listener)new MenuListener(), (Plugin)plugin);
/* 314 */     getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)plugin);
/* 315 */     getServer().getPluginManager().registerEvents((Listener)new CleanupListener(), (Plugin)plugin);
/* 316 */     getServer().getPluginManager().registerEvents((Listener)new AuctionListener(), (Plugin)plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerCommands() {
/* 321 */     ((PluginCommand)Objects.<PluginCommand>requireNonNull(plugin.getCommand("ah"))).setExecutor((CommandExecutor)new AHCommand());
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerExpansion() {
/* 326 */     if (setupPlaceholderAPI()) {
/* 327 */       (new Expansion(this)).register();
/* 328 */       logger.info("Registered PlaceholderAPI placeholders");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerMVdW() {
/* 334 */     if (setupMVdWPlaceholderAPI()) {
/* 335 */       (new MVdWExpansion(this)).register();
/* 336 */       logger.info("Registered MVdWPlaceholderAPI placeholders");
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean setupPlaceholderAPI() {
/* 341 */     Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
/* 342 */     if (papi != null && papi.isEnabled()) {
/* 343 */       logger.info("Found PlaceholderAPI plugin");
/* 344 */       return true;
/*     */     } 
/* 346 */     return false;
/*     */   }
/*     */   
/*     */   private boolean setupMVdWPlaceholderAPI() {
/* 350 */     Plugin mvdw = Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI");
/* 351 */     if (mvdw != null && mvdw.isEnabled()) {
/* 352 */       logger.info("Found MVdWPlaceholderAPI plugin");
/* 353 */       return true;
/*     */     } 
/* 355 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\AuctionHouse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */