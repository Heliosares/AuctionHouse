/*     */ package com.spawnchunk.auctionhouse.config;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.events.DropUnclaimedEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ListingCleanupEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ServerTickEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.SortOrder;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.text.NumberFormat;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.ConsoleCommandSender;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.Damageable;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Config
/*     */ {
/*     */   private FileConfiguration fc;
/*     */   private static final int config_version = 33;
/*     */   public static boolean debug = false;
/*     */   public static boolean log_ansi = true;
/*  48 */   public static String locale = "en_us";
/*  49 */   public static String decimal_format = "";
/*     */   public static boolean strict = false;
/*  51 */   public static String economy = "vault";
/*     */   public static boolean chat_hook = false;
/*  53 */   public static int updateTicks = 0;
/*     */   
/*  55 */   public static double auction_listing_price = 0.0D;
/*  56 */   public static double auction_listing_rate = 0.0D;
/*  57 */   public static long auction_listing_cooldown = 0L;
/*  58 */   public static long auction_listing_duration = 0L;
/*     */   public static boolean show_seconds = false;
/*  60 */   public static double auction_sales_tax = 0.0D;
/*  61 */   public static double auction_max_sales_tax = 0.0D;
/*  62 */   public static long auction_expired_duration = 0L;
/*  63 */   public static long auction_unclaimed_duration = 0L;
/*  64 */   public static long auction_cleanup_duration = 0L;
/*  65 */   public static long auction_sold_duration = 0L;
/*     */   public static boolean auction_prevent_creative = false;
/*     */   public static boolean auction_prevent_spectator = false;
/*     */   public static boolean auction_prevent_filled_containers = false;
/*  69 */   public static double auction_min_sell_price = 0.0D;
/*  70 */   public static double auction_max_sell_price = 0.0D;
/*     */   public static boolean auction_allow_damaged_items = false;
/*  72 */   public static int auction_default_max_listings = 0;
/*  73 */   public static SortOrder auction_sort_order = SortOrder.CHRONO_OLDEST;
/*  74 */   public static String click_sound = "";
/*  75 */   public static String fail_sound = "";
/*  76 */   public static String drop_sound = "";
/*  77 */   public static String sold_sound = "";
/*  78 */   public static String exit_button = "minecraft:iron_door";
/*  79 */   public static String back_button = "minecraft:iron_door";
/*  80 */   public static String previous_button = "minecraft:paper";
/*  81 */   public static String sort_listings_button = "minecraft:sunflower";
/*  82 */   public static String next_button = "minecraft:paper";
/*  83 */   public static String info_button = "minecraft:book";
/*  84 */   public static String howto_button = "minecraft:emerald";
/*  85 */   public static String return_all_button = "minecraft:flower_pot";
/*  86 */   public static String player_listings_button = "minecraft:diamond";
/*  87 */   public static String expired_listings_button = "minecraft:poisonous_potato";
/*  88 */   public static String sold_items_button = "minecraft:gold_ingot";
/*  89 */   public static String clear_button = "minecraft:barrier";
/*  90 */   public static String confirm_button = "minecraft:lime_stained_glass_pane";
/*  91 */   public static String cancel_button = "minecraft:red_stained_glass_pane";
/*  92 */   public static String sign_trigger = "";
/*  93 */   public static List<String> block_name_triggers = new ArrayList<>();
/*  94 */   public static List<String> entity_name_triggers = new ArrayList<>();
/*  95 */   public static String exit_command = "";
/*     */   public static boolean log_listed = false;
/*     */   public static boolean log_sold = false;
/*     */   public static boolean log_cancelled = false;
/*     */   public static boolean log_returned = false;
/*     */   public static boolean log_dropped = false;
/*     */   public static boolean log_purged = false;
/* 102 */   public static List<String> disabled_worlds = new ArrayList<>();
/*     */   public static boolean announce_chat_listings = false;
/*     */   public static boolean announce_chat_purchases = false;
/*     */   public static boolean announce_action_bar_listings = false;
/*     */   public static boolean announce_action_bar_purchases = false;
/*     */   public static boolean announce_discord_listings = false;
/*     */   public static boolean announce_discord_purchases = false;
/* 109 */   public static String discord_channel = "";
/*     */   public static boolean per_world_listings = false;
/*     */   public static boolean group_worlds = false;
/*     */   public static boolean replace_item_uuids = false;
/*     */   public static boolean replace_player_names = false;
/*     */   public static boolean drop_at_feet = false;
/* 115 */   public static long unclaimed_check_duration = 0L;
/*     */   public static boolean unclaimed_check_on_world_change = false;
/* 117 */   public static Material item_wildcard = Material.STRUCTURE_VOID;
/* 118 */   public static Map<String, ItemStack> restricted_items = new LinkedHashMap<>();
/* 119 */   public static Map<String, Boolean> wildcard_name = new HashMap<>();
/* 120 */   public static Map<String, Boolean> wildcard_item = new HashMap<>();
/* 121 */   public static Map<String, Boolean> wildcard_lore = new HashMap<>();
/* 122 */   public static Map<String, Boolean> wildcard_enchantments = new HashMap<>();
/* 123 */   public static Map<String, Boolean> wildcard_damage = new HashMap<>();
/* 124 */   public static Map<String, Boolean> wildcard_unbreakable = new HashMap<>();
/* 125 */   public static Map<String, Boolean> wildcard_custommodeldata = new HashMap<>();
/* 126 */   public static Map<String, Boolean> wildcard_persistentdata = new HashMap<>();
/* 127 */   public static TreeMap<String, String> translations = new TreeMap<>();
/* 128 */   public static TreeMap<String, String> translations_fallback = new TreeMap<>();
/*     */   
/*     */   public static boolean spawner_info = false;
/*     */   public static boolean integer_price = false;
/*     */   public static boolean show_repair_cost = false;
/*     */   public static boolean spam_check = false;
/* 134 */   public static NumberFormat numberFormat = null;
/* 135 */   public static long auction_future_duration = 4611686018427387903L;
/*     */   
/*     */   public Config() {
/* 138 */     this.fc = AuctionHouse.plugin.getConfig();
/* 139 */     parseConfig();
/*     */   }
/*     */   
/*     */   private long getDuration(String duration) {
/* 143 */     long millis = 0L;
/* 144 */     if (duration != null && !duration.isEmpty()) {
/* 145 */       String input = "P".concat(duration.replace("d", "DT")).toUpperCase();
/* 146 */       Duration d = Duration.parse(input);
/* 147 */       millis = d.toMillis();
/*     */     } 
/* 149 */     return millis;
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseConfig() {
/* 154 */     int version = this.fc.contains("configVersion") ? this.fc.getInt("configVersion") : 0;
/* 155 */     if (version < 33) {
/* 156 */       upgradeConfig(version);
/*     */     }
/*     */ 
/*     */     
/* 160 */     if (this.fc.contains("debug")) {
/* 161 */       debug = this.fc.getBoolean("debug");
/* 162 */       if (debug) AuctionHouse.logger.info("debug = true"); 
/*     */     } 
/* 164 */     if (this.fc.contains("ansi")) {
/* 165 */       log_ansi = this.fc.getBoolean("ansi");
/* 166 */       if (debug) AuctionHouse.logger.info(String.format("log_ansi = %s", new Object[] { log_ansi ? "true" : "false" })); 
/*     */     } 
/* 168 */     if (this.fc.contains("locale")) {
/* 169 */       locale = this.fc.getString("locale", "en_us");
/* 170 */       if (debug) AuctionHouse.logger.info(String.format("locale = %s", new Object[] { locale })); 
/*     */     } 
/* 172 */     locale = AuctionHouse.locales.containsKey(locale) ? locale : "en_us";
/* 173 */     AuctionHouse.logger.info(String.format("Using %s locale", new Object[] { locale }));
/* 174 */     numberFormat = getNumberFormat(locale);
/* 175 */     if (this.fc.contains("decimal_format")) {
/* 176 */       decimal_format = this.fc.getString("decimal_format");
/* 177 */       if (decimal_format != null) {
/* 178 */         if (debug) AuctionHouse.logger.info(String.format("decimal_format = %s", new Object[] { decimal_format })); 
/* 179 */         applyDecimalFormat(decimal_format);
/*     */       } 
/*     */     } 
/* 182 */     if (this.fc.contains("strict")) {
/* 183 */       strict = this.fc.getBoolean("strict");
/* 184 */       if (debug) AuctionHouse.logger.info(String.format("strict = %s", new Object[] { strict ? "true" : "false" })); 
/*     */     } 
/* 186 */     if (this.fc.contains("economy")) {
/* 187 */       economy = this.fc.getString("economy");
/* 188 */       if (debug) AuctionHouse.logger.info(String.format("economy = %s", new Object[] { economy })); 
/*     */     } 
/* 190 */     if (this.fc.contains("chatHook")) {
/* 191 */       chat_hook = this.fc.getBoolean("chatHook");
/* 192 */       if (debug) AuctionHouse.logger.info(String.format("chatHook = %s", new Object[] { chat_hook ? "true" : "false" })); 
/*     */     } 
/* 194 */     if (this.fc.contains("updateTicks")) {
/* 195 */       updateTicks = this.fc.getInt("updateTicks");
/* 196 */       if (debug) AuctionHouse.logger.info(String.format("updateTicks = %d", new Object[] { Integer.valueOf(updateTicks) })); 
/*     */     } 
/* 198 */     updateTicks = (updateTicks < 1) ? 1 : Math.min(updateTicks, 100);
/*     */ 
/*     */     
/* 201 */     if (this.fc.contains("auction")) {
/* 202 */       ConfigurationSection cs = this.fc.getConfigurationSection("auction");
/* 203 */       if (cs != null) {
/* 204 */         if (cs.contains("listingPrice")) {
/* 205 */           auction_listing_price = cs.getDouble("listingPrice");
/* 206 */           if (debug) AuctionHouse.logger.info(String.format("auction.listingPrice = %.2f", new Object[] { Double.valueOf(auction_listing_price) })); 
/*     */         } 
/* 208 */         if (cs.contains("listingRate")) {
/* 209 */           auction_listing_rate = cs.getDouble("listingRate");
/* 210 */           auction_listing_rate = (auction_listing_rate < 0.0D) ? 0.0D : ((auction_listing_rate > 100.0D) ? 100.0D : auction_listing_rate);
/* 211 */           if (debug) AuctionHouse.logger.info(String.format("auction.listingRate = %.2f", new Object[] { Double.valueOf(auction_listing_rate) })); 
/*     */         } 
/* 213 */         if (cs.contains("listingCooldown")) {
/* 214 */           String duration = cs.getString("listingCooldown");
/* 215 */           auction_listing_cooldown = getDuration(duration);
/* 216 */           if (debug) AuctionHouse.logger.info(String.format("auction.listingCooldown = %s", new Object[] { duration })); 
/*     */         } 
/* 218 */         if (cs.contains("listingDuration")) {
/* 219 */           String duration = cs.getString("listingDuration");
/* 220 */           auction_listing_duration = getDuration(duration);
/* 221 */           if (debug) AuctionHouse.logger.info(String.format("auction.listingDuration = %s", new Object[] { duration })); 
/*     */         } 
/* 223 */         if (cs.contains("showSeconds")) {
/* 224 */           show_seconds = cs.getBoolean("showSeconds");
/* 225 */           if (debug) AuctionHouse.logger.info(String.format("auction.showSeconds = %s", new Object[] { show_seconds ? "true" : "false" })); 
/*     */         } 
/* 227 */         if (cs.contains("salesTax")) {
/* 228 */           auction_sales_tax = cs.getDouble("salesTax");
/* 229 */           auction_sales_tax = (auction_sales_tax < 0.0D) ? 0.0D : ((auction_sales_tax > 100.0D) ? 100.0D : auction_sales_tax);
/* 230 */           if (debug) AuctionHouse.logger.info(String.format("auction.salesTax = %.2f", new Object[] { Double.valueOf(auction_sales_tax) })); 
/*     */         } 
/* 232 */         if (cs.contains("maxSalesTax")) {
/* 233 */           auction_max_sales_tax = cs.getDouble("maxSalesTax");
/* 234 */           if (debug) AuctionHouse.logger.info(String.format("auction.maxSalesTax = %.2f", new Object[] { Double.valueOf(auction_max_sales_tax) })); 
/*     */         } 
/* 236 */         if (cs.contains("expiredDuration")) {
/* 237 */           String duration = cs.getString("expiredDuration");
/* 238 */           auction_expired_duration = getDuration(duration);
/* 239 */           if (debug) AuctionHouse.logger.info(String.format("auction.expiredDuration = %s", new Object[] { duration })); 
/*     */         } 
/* 241 */         if (cs.contains("unclaimedDuration")) {
/* 242 */           String duration = cs.getString("unclaimedDuration");
/* 243 */           auction_unclaimed_duration = getDuration(duration);
/* 244 */           if (debug) AuctionHouse.logger.info(String.format("auction.unclaimedDuration = %s", new Object[] { duration })); 
/*     */         } 
/* 246 */         if (cs.contains("cleanupDuration")) {
/* 247 */           String duration = cs.getString("cleanupDuration");
/* 248 */           auction_cleanup_duration = getDuration(duration);
/* 249 */           if (debug) AuctionHouse.logger.info(String.format("auction.cleanupDuration = %s", new Object[] { duration })); 
/*     */         } 
/* 251 */         if (cs.contains("soldDuration")) {
/* 252 */           String duration = cs.getString("soldDuration");
/* 253 */           auction_sold_duration = getDuration(duration);
/* 254 */           if (debug) AuctionHouse.logger.info(String.format("auction.soldDuration = %s", new Object[] { duration })); 
/*     */         } 
/* 256 */         if (cs.contains("preventCreative")) {
/* 257 */           auction_prevent_creative = cs.getBoolean("preventCreative");
/* 258 */           if (debug) AuctionHouse.logger.info(String.format("auction.preventCreative = %s", new Object[] { auction_prevent_creative ? "true" : "false" })); 
/*     */         } 
/* 260 */         if (cs.contains("preventSpectator")) {
/* 261 */           auction_prevent_spectator = cs.getBoolean("preventSpectator");
/* 262 */           if (debug) AuctionHouse.logger.info(String.format("auction.preventSpectator = %s", new Object[] { auction_prevent_spectator ? "true" : "false" })); 
/*     */         } 
/* 264 */         if (cs.contains("preventFilledContainers")) {
/* 265 */           auction_prevent_filled_containers = cs.getBoolean("preventFilledContainers");
/* 266 */           if (debug) AuctionHouse.logger.info(String.format("auction.preventFilledContainers = %s", new Object[] { auction_prevent_filled_containers ? "true" : "false" })); 
/*     */         } 
/* 268 */         if (cs.contains("minSellPrice")) {
/* 269 */           auction_min_sell_price = cs.getDouble("minSellPrice");
/* 270 */           if (debug) AuctionHouse.logger.info(String.format("auction.minSellPrice = %.2f", new Object[] { Double.valueOf(auction_min_sell_price) })); 
/*     */         } 
/* 272 */         if (cs.contains("maxSellPrice")) {
/* 273 */           auction_max_sell_price = cs.getDouble("maxSellPrice");
/* 274 */           if (debug) AuctionHouse.logger.info(String.format("auction.maxSellPrice = %.2f", new Object[] { Double.valueOf(auction_max_sell_price) })); 
/*     */         } 
/* 276 */         if (cs.contains("allowDamagedItems")) {
/* 277 */           auction_allow_damaged_items = cs.getBoolean("allowDamagedItems");
/* 278 */           if (debug) AuctionHouse.logger.info(String.format("auction.allowDamagedItems = %s", new Object[] { auction_allow_damaged_items ? "true" : "false" })); 
/*     */         } 
/* 280 */         if (cs.contains("defaultMaxListings")) {
/* 281 */           auction_default_max_listings = cs.getInt("defaultMaxListings");
/* 282 */           if (debug) AuctionHouse.logger.info(String.format("auction.defaultMaxListings = %d", new Object[] { Integer.valueOf(auction_default_max_listings) })); 
/*     */         } 
/* 284 */         if (cs.contains("sortOrder")) {
/* 285 */           String sortOrder = cs.getString("sortOrder");
/* 286 */           if (sortOrder == null) sortOrder = "oldest"; 
/* 287 */           switch (sortOrder.toLowerCase()) {
/*     */             case "lowest_price":
/* 289 */               auction_sort_order = SortOrder.PRICE_LOWEST;
/*     */               break;
/*     */             case "highest_price":
/* 292 */               auction_sort_order = SortOrder.PRICE_HIGHEST;
/*     */               break;
/*     */             case "newest":
/* 295 */               auction_sort_order = SortOrder.CHRONO_NEWEST;
/*     */               break;
/*     */             
/*     */             default:
/* 299 */               auction_sort_order = SortOrder.CHRONO_OLDEST;
/*     */               break;
/*     */           } 
/* 302 */           if (debug) AuctionHouse.logger.info(String.format("auction.sortOrder = %s", new Object[] { auction_sort_order.toString() })); 
/*     */         } 
/* 304 */         if (cs.contains("discord_channel")) {
/* 305 */           discord_channel = cs.getString("discord_channel");
/* 306 */           if (debug) AuctionHouse.logger.info(String.format("auction.discord_channel = %s", new Object[] { discord_channel })); 
/*     */         } 
/* 308 */         if (cs.contains("multiworld")) {
/* 309 */           per_world_listings = cs.getBoolean("multiworld");
/* 310 */           if (debug) AuctionHouse.logger.info(String.format("auction.multiworld = %s", new Object[] { per_world_listings ? "true" : "false" })); 
/*     */         } 
/* 312 */         if (cs.contains("groupWorlds")) {
/* 313 */           group_worlds = cs.getBoolean("groupWorlds");
/* 314 */           if (debug) AuctionHouse.logger.info(String.format("auction.groupWorlds = %s", new Object[] { group_worlds ? "true" : "false" })); 
/*     */         } 
/* 316 */         if (cs.contains("replaceUUIDs")) {
/* 317 */           replace_item_uuids = cs.getBoolean("replaceUUIDs");
/* 318 */           if (debug) AuctionHouse.logger.info(String.format("auction.replaceUUIDs = %s", new Object[] { replace_item_uuids ? "true" : "false" })); 
/*     */         } 
/* 320 */         if (cs.contains("replacePlayerNames")) {
/* 321 */           replace_player_names = cs.getBoolean("replacePlayerNames");
/* 322 */           if (debug) AuctionHouse.logger.info(String.format("auction.replacePlayerNames = %s", new Object[] { replace_player_names ? "true" : "false" })); 
/*     */         } 
/* 324 */         if (cs.contains("dropAtFeet")) {
/* 325 */           drop_at_feet = cs.getBoolean("dropAtFeet");
/* 326 */           if (debug) AuctionHouse.logger.info(String.format("auction.dropAtFeet = %s", new Object[] { drop_at_feet ? "true" : "false" })); 
/*     */         } 
/* 328 */         if (cs.contains("unclaimedCheckDuration")) {
/* 329 */           String duration = cs.getString("unclaimedCheckDuration");
/* 330 */           unclaimed_check_duration = getDuration(duration);
/* 331 */           if (debug) AuctionHouse.logger.info(String.format("auction.unclaimedCheckDuration = %s", new Object[] { duration })); 
/*     */         } 
/* 333 */         if (cs.contains("unclaimedCheckOnWorldChange")) {
/* 334 */           unclaimed_check_on_world_change = cs.getBoolean("unclaimedCheckOnWorldChange");
/* 335 */           if (debug) AuctionHouse.logger.info(String.format("auction.unclaimedCheckOnWorldChange = %s", new Object[] { unclaimed_check_on_world_change ? "true" : "false" })); 
/*     */         } 
/* 337 */         if (cs.contains("spawnerInfo")) {
/* 338 */           spawner_info = cs.getBoolean("spawnerInfo");
/* 339 */           if (debug) AuctionHouse.logger.info(String.format("auction.spawnerInfo = %s", new Object[] { spawner_info ? "true" : "false" })); 
/*     */         } 
/* 341 */         if (cs.contains("integerPrice")) {
/* 342 */           integer_price = cs.getBoolean("integerPrice");
/* 343 */           if (debug) AuctionHouse.logger.info(String.format("auction.integerPrice = %s", new Object[] { integer_price ? "true" : "false" })); 
/*     */         } 
/* 345 */         if (cs.contains("showRepairCost")) {
/* 346 */           show_repair_cost = cs.getBoolean("showRepairCost");
/* 347 */           if (debug) AuctionHouse.logger.info(String.format("auction.showRepairCost = %s", new Object[] { show_repair_cost ? "true" : "false" }));
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 353 */     if (this.fc.contains("announce")) {
/* 354 */       ConfigurationSection cs1 = this.fc.getConfigurationSection("announce");
/* 355 */       if (cs1 != null) {
/* 356 */         if (cs1.contains("chat.listings")) {
/* 357 */           announce_chat_listings = cs1.getBoolean("chat.listings");
/* 358 */           if (debug) AuctionHouse.logger.info(String.format("announce.chat.listings = %s", new Object[] { announce_chat_listings ? "true" : "false" })); 
/*     */         } 
/* 360 */         if (cs1.contains("chat.purchases")) {
/* 361 */           announce_chat_purchases = cs1.getBoolean("chat.purchases");
/* 362 */           if (debug) AuctionHouse.logger.info(String.format("announce.chat.purchases = %s", new Object[] { announce_chat_purchases ? "true" : "false" })); 
/*     */         } 
/* 364 */         if (cs1.contains("action_bar.listings")) {
/* 365 */           announce_action_bar_listings = cs1.getBoolean("action_bar.listings");
/* 366 */           if (debug) AuctionHouse.logger.info(String.format("announce.action_bar.listings = %s", new Object[] { announce_action_bar_listings ? "true" : "false" })); 
/*     */         } 
/* 368 */         if (cs1.contains("action_bar.purchases")) {
/* 369 */           announce_action_bar_purchases = cs1.getBoolean("action_bar.purchases");
/* 370 */           if (debug) AuctionHouse.logger.info(String.format("announce.action_bar.purchases = %s", new Object[] { announce_action_bar_purchases ? "true" : "false" })); 
/*     */         } 
/* 372 */         if (cs1.contains("discord.listings")) {
/* 373 */           announce_discord_listings = cs1.getBoolean("discord.listings");
/* 374 */           if (debug) AuctionHouse.logger.info(String.format("announce.discord.listings = %s", new Object[] { announce_discord_listings ? "true" : "false" })); 
/*     */         } 
/* 376 */         if (cs1.contains("discord.purchases")) {
/* 377 */           announce_discord_purchases = cs1.getBoolean("discord.purchases");
/* 378 */           if (debug) AuctionHouse.logger.info(String.format("announce.discord.purchases = %s", new Object[] { announce_discord_purchases ? "true" : "false" }));
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 384 */     if (this.fc.contains("restricted_items")) {
/* 385 */       ConfigurationSection cs2 = this.fc.getConfigurationSection("restricted_items");
/* 386 */       if (cs2 != null) {
/* 387 */         Set<String> keys = cs2.getKeys(false);
/* 388 */         for (String key : keys) {
/* 389 */           if (cs2.isConfigurationSection(key)) {
/* 390 */             loadRestrictedItem(cs2.getConfigurationSection(key));
/*     */           }
/*     */         } 
/*     */       } 
/* 394 */       logRestrictedItemsList();
/*     */     } 
/*     */ 
/*     */     
/* 398 */     if (this.fc.contains("sounds")) {
/* 399 */       ConfigurationSection cs3 = this.fc.getConfigurationSection("sounds");
/* 400 */       if (cs3 != null) {
/* 401 */         if (cs3.contains("click")) {
/* 402 */           click_sound = cs3.getString("click");
/* 403 */           if (debug) AuctionHouse.logger.info(String.format("sounds.click = %s", new Object[] { click_sound })); 
/*     */         } 
/* 405 */         if (cs3.contains("fail")) {
/* 406 */           fail_sound = cs3.getString("fail");
/* 407 */           if (debug) AuctionHouse.logger.info(String.format("sounds.fail = %s", new Object[] { fail_sound })); 
/*     */         } 
/* 409 */         if (cs3.contains("drop")) {
/* 410 */           drop_sound = cs3.getString("drop");
/* 411 */           if (debug) AuctionHouse.logger.info(String.format("sounds.drop = %s", new Object[] { drop_sound })); 
/*     */         } 
/* 413 */         if (cs3.contains("sold")) {
/* 414 */           sold_sound = cs3.getString("sold");
/* 415 */           if (debug) AuctionHouse.logger.info(String.format("sounds.sold = %s", new Object[] { sold_sound }));
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 421 */     if (this.fc.contains("buttons")) {
/* 422 */       ConfigurationSection cs4 = this.fc.getConfigurationSection("buttons");
/* 423 */       if (cs4 != null) {
/* 424 */         if (cs4.contains("exit")) {
/* 425 */           exit_button = cs4.getString("exit");
/* 426 */           if (debug) AuctionHouse.logger.info(String.format("buttons.exit = %s", new Object[] { exit_button })); 
/*     */         } 
/* 428 */         if (cs4.contains("back")) {
/* 429 */           back_button = cs4.getString("back");
/* 430 */           if (debug) AuctionHouse.logger.info(String.format("buttons.back = %s", new Object[] { back_button })); 
/*     */         } 
/* 432 */         if (cs4.contains("previous")) {
/* 433 */           previous_button = cs4.getString("previous");
/* 434 */           if (debug) AuctionHouse.logger.info(String.format("buttons.previous = %s", new Object[] { previous_button })); 
/*     */         } 
/* 436 */         if (cs4.contains("sort_listings")) {
/* 437 */           sort_listings_button = cs4.getString("sort_listings");
/* 438 */           if (debug) AuctionHouse.logger.info(String.format("buttons.sort_listings = %s", new Object[] { sort_listings_button })); 
/*     */         } 
/* 440 */         if (cs4.contains("next")) {
/* 441 */           next_button = cs4.getString("next");
/* 442 */           if (debug) AuctionHouse.logger.info(String.format("buttons.next = %s", new Object[] { next_button })); 
/*     */         } 
/* 444 */         if (cs4.contains("info")) {
/* 445 */           info_button = cs4.getString("info");
/* 446 */           if (debug) AuctionHouse.logger.info(String.format("buttons.info = %s", new Object[] { info_button })); 
/*     */         } 
/* 448 */         if (cs4.contains("howto")) {
/* 449 */           howto_button = cs4.getString("howto");
/* 450 */           if (debug) AuctionHouse.logger.info(String.format("buttons.howto = %s", new Object[] { howto_button })); 
/*     */         } 
/* 452 */         if (cs4.contains("return_all")) {
/* 453 */           return_all_button = cs4.getString("return_all");
/* 454 */           if (debug) AuctionHouse.logger.info(String.format("buttons.return_all = %s", new Object[] { return_all_button })); 
/*     */         } 
/* 456 */         if (cs4.contains("player_listings")) {
/* 457 */           player_listings_button = cs4.getString("player_listings");
/* 458 */           if (debug) AuctionHouse.logger.info(String.format("buttons.player_listings = %s", new Object[] { player_listings_button })); 
/*     */         } 
/* 460 */         if (cs4.contains("expired_listings")) {
/* 461 */           expired_listings_button = cs4.getString("expired_listings");
/* 462 */           if (debug) AuctionHouse.logger.info(String.format("buttons.expired_listings = %s", new Object[] { expired_listings_button })); 
/*     */         } 
/* 464 */         if (cs4.contains("sold_items")) {
/* 465 */           sold_items_button = cs4.getString("sold_items");
/* 466 */           if (debug) AuctionHouse.logger.info(String.format("buttons.sold_items = %s", new Object[] { sold_items_button })); 
/*     */         } 
/* 468 */         if (cs4.contains("clear")) {
/* 469 */           clear_button = cs4.getString("clear");
/* 470 */           if (debug) AuctionHouse.logger.info(String.format("buttons.clear = %s", new Object[] { clear_button })); 
/*     */         } 
/* 472 */         if (cs4.contains("confirm")) {
/* 473 */           confirm_button = cs4.getString("confirm");
/* 474 */           if (debug) AuctionHouse.logger.info(String.format("buttons.confirm = %s", new Object[] { confirm_button })); 
/*     */         } 
/* 476 */         if (cs4.contains("cancel")) {
/* 477 */           cancel_button = cs4.getString("cancel");
/* 478 */           if (debug) AuctionHouse.logger.info(String.format("buttons.cancel = %s", new Object[] { cancel_button }));
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 484 */     if (this.fc.contains("triggers")) {
/* 485 */       ConfigurationSection cs5 = this.fc.getConfigurationSection("triggers");
/* 486 */       if (cs5 != null) {
/* 487 */         if (cs5.contains("sign")) {
/* 488 */           String trigger = cs5.getString("sign");
/* 489 */           sign_trigger = (trigger != null) ? MessageUtil.sectionSymbol(trigger) : "";
/* 490 */           if (debug) AuctionHouse.logger.info(String.format("triggers.sign = %s", new Object[] { sign_trigger })); 
/*     */         } 
/* 492 */         if (cs5.contains("block_name")) {
/* 493 */           if (cs5.isString("block_name")) {
/* 494 */             block_name_triggers.add(cs5.getString("block_name"));
/* 495 */           } else if (cs5.isList("block_name")) {
/* 496 */             block_name_triggers = cs5.getStringList("block_name");
/*     */           } 
/* 498 */           if (debug) AuctionHouse.logger.info(String.format("triggers.block_name = %s", new Object[] { block_name_triggers })); 
/*     */         } 
/* 500 */         if (cs5.contains("entity_name")) {
/* 501 */           if (cs5.isString("entity_name")) {
/* 502 */             String trigger = cs5.getString("entity_name");
/* 503 */             if (trigger != null) entity_name_triggers.add(MessageUtil.sectionSymbol(trigger)); 
/* 504 */           } else if (cs5.isList("entity_name")) {
/* 505 */             entity_name_triggers = cs5.getStringList("entity_name");
/* 506 */             entity_name_triggers.replaceAll(MessageUtil::sectionSymbol);
/*     */           } 
/* 508 */           if (debug) AuctionHouse.logger.info(String.format("triggers.entity_name = %s", new Object[] { entity_name_triggers }));
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 514 */     if (this.fc.contains("commands.exit")) {
/* 515 */       exit_command = this.fc.getString("commands.exit", "");
/* 516 */       if (debug) AuctionHouse.logger.info(String.format("commands.exit = %s", new Object[] { exit_command }));
/*     */     
/*     */     } 
/*     */     
/* 520 */     if (this.fc.contains("log")) {
/* 521 */       ConfigurationSection cs6 = this.fc.getConfigurationSection("log");
/* 522 */       if (cs6 != null) {
/* 523 */         if (cs6.contains("listed")) {
/* 524 */           log_listed = cs6.getBoolean("listed");
/* 525 */           if (debug) AuctionHouse.logger.info(String.format("log.listed = %s", new Object[] { log_listed ? "true" : "false" })); 
/*     */         } 
/* 527 */         if (cs6.contains("sold")) {
/* 528 */           log_sold = cs6.getBoolean("sold");
/* 529 */           if (debug) AuctionHouse.logger.info(String.format("log.sold = %s", new Object[] { log_sold ? "true" : "false" })); 
/*     */         } 
/* 531 */         if (cs6.contains("cancelled")) {
/* 532 */           log_cancelled = cs6.getBoolean("cancelled");
/* 533 */           if (debug) AuctionHouse.logger.info(String.format("log.cancelled = %s", new Object[] { log_cancelled ? "true" : "false" })); 
/*     */         } 
/* 535 */         if (cs6.contains("returned")) {
/* 536 */           log_returned = cs6.getBoolean("returned");
/* 537 */           if (debug) AuctionHouse.logger.info(String.format("log.returned = %s", new Object[] { log_returned ? "true" : "false" })); 
/*     */         } 
/* 539 */         if (cs6.contains("dropped")) {
/* 540 */           log_dropped = cs6.getBoolean("dropped");
/* 541 */           if (debug) AuctionHouse.logger.info(String.format("log.dropped = %s", new Object[] { log_dropped ? "true" : "false" })); 
/*     */         } 
/* 543 */         if (cs6.contains("purged")) {
/* 544 */           log_purged = cs6.getBoolean("purged");
/* 545 */           if (debug) AuctionHouse.logger.info(String.format("log.purged = %s", new Object[] { log_purged ? "true" : "false" }));
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 551 */     if (this.fc.contains("anticheat")) {
/* 552 */       ConfigurationSection cs7 = this.fc.getConfigurationSection("anticheat");
/* 553 */       if (cs7 != null && 
/* 554 */         cs7.contains("spam_check")) {
/* 555 */         spam_check = cs7.getBoolean("spam_check");
/* 556 */         if (debug) AuctionHouse.logger.info(String.format("anticheat.spam_check = %s", new Object[] { spam_check ? "true" : "false" }));
/*     */       
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 562 */     if (this.fc.contains("disabled-worlds")) {
/* 563 */       disabled_worlds = this.fc.getStringList("disabled-worlds");
/* 564 */       if (debug && !disabled_worlds.isEmpty()) {
/* 565 */         AuctionHouse.logger.info("disabled-worlds = [");
/* 566 */         for (String w : disabled_worlds) {
/* 567 */           AuctionHouse.logger.info(String.format("  %s", new Object[] { w }));
/*     */         } 
/* 569 */         AuctionHouse.logger.info("]");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void upgradeConfig(int version) {
/* 577 */     if (version > 0) MessageUtil.logWarning("Upgrading config file to the latest version");
/*     */     
/* 579 */     this.fc.options().copyDefaults(true);
/*     */ 
/*     */     
/* 582 */     if (this.fc.contains("blacklist")) {
/* 583 */       ConfigurationSection cs = this.fc.getConfigurationSection("blacklist");
/* 584 */       if (cs != null) {
/* 585 */         this.fc.createSection("restricted_items", cs.getValues(true));
/*     */         
/* 587 */         this.fc.set("blacklist", null);
/*     */       } 
/*     */     } 
/* 590 */     if (this.fc.contains("auction")) {
/* 591 */       ConfigurationSection cs1 = this.fc.getConfigurationSection("auctions");
/* 592 */       if (cs1 != null) {
/*     */         
/* 594 */         if (cs1.contains("announce")) cs1.set("announce", null);
/*     */         
/* 596 */         if (cs1.contains("action_bar")) cs1.set("action_bar", null);
/*     */         
/* 598 */         if (cs1.contains("discord")) cs1.set("discord", null);
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 603 */     this.fc.set("configVersion", Integer.valueOf(33));
/* 604 */     AuctionHouse.plugin.saveConfig();
/*     */   }
/*     */   
/*     */   public void reloadConfig() {
/* 608 */     if (debug) AuctionHouse.logger.info(LocaleStorage.translate("message.config.reload", locale)); 
/* 609 */     AuctionHouse.plugin.reloadConfig();
/* 610 */     this.fc = AuctionHouse.plugin.getConfig();
/*     */     
/* 612 */     restricted_items.clear();
/* 613 */     translations.clear();
/* 614 */     translations_fallback.clear();
/* 615 */     disabled_worlds.clear();
/* 616 */     numberFormat = null;
/* 617 */     LocaleStorage.reloadLocales();
/* 618 */     parseConfig();
/* 619 */     Auctions.RebuildAllMenus();
/*     */ 
/*     */     
/* 622 */     Server server = AuctionHouse.plugin.getServer();
/* 623 */     ConsoleCommandSender consoleCommandSender = server.getConsoleSender();
/* 624 */     BukkitScheduler scheduler = server.getScheduler();
/*     */ 
/*     */     
/* 627 */     scheduler.cancelTask(AuctionHouse.tickEventId);
/* 628 */     AuctionHouse.tickEventId = scheduler.scheduleSyncRepeatingTask((Plugin)AuctionHouse.plugin, () -> { ServerTickEvent event = new ServerTickEvent(sender, server); Bukkit.getPluginManager().callEvent((Event)event); }0L, updateTicks);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 634 */     scheduler.cancelTask(AuctionHouse.dropEventId);
/* 635 */     long dropTicks = Math.max(1200L, unclaimed_check_duration / 50L);
/* 636 */     AuctionHouse.dropEventId = scheduler.scheduleSyncRepeatingTask((Plugin)AuctionHouse.plugin, () -> { DropUnclaimedEvent event = new DropUnclaimedEvent(sender, server); Bukkit.getPluginManager().callEvent((Event)event); }0L, dropTicks);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 642 */     scheduler.cancelTask(AuctionHouse.cleanupEventId);
/* 643 */     long cleanupTicks = Math.max(1200L, auction_cleanup_duration / 50L);
/* 644 */     AuctionHouse.cleanupEventId = scheduler.scheduleSyncRepeatingTask((Plugin)AuctionHouse.plugin, () -> { ListingCleanupEvent event = new ListingCleanupEvent(sender, server); Bukkit.getPluginManager().callEvent((Event)event); }0L, cleanupTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadRestrictedItem(ConfigurationSection cs) {
/* 653 */     if (cs == null)
/* 654 */       return;  String section = cs.getName();
/*     */     
/* 656 */     Set<String> keys = cs.getKeys(false);
/*     */     
/* 658 */     Material material = item_wildcard;
/* 659 */     if (keys.contains("item") && cs.isString("item")) {
/*     */       
/* 661 */       String itemName = cs.getString("item");
/* 662 */       if (itemName != null) {
/* 663 */         Material match = Material.matchMaterial(itemName);
/* 664 */         if (match != null) {
/* 665 */           material = match;
/* 666 */           wildcard_item.put(section, Boolean.valueOf((material == item_wildcard)));
/*     */         } else {
/* 668 */           AuctionHouse.logger.info(String.format("Invalid Item Material \"%s\" in restricted items", new Object[] { itemName }));
/* 669 */           wildcard_item.put(section, Boolean.valueOf(true));
/*     */         } 
/*     */       } else {
/* 672 */         wildcard_item.put(section, Boolean.valueOf(true));
/*     */       } 
/*     */     } else {
/* 675 */       wildcard_item.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 678 */     String displayName = null;
/* 679 */     if (keys.contains("name") && cs.isString("name")) {
/* 680 */       displayName = cs.getString("name");
/* 681 */       wildcard_name.put(section, Boolean.valueOf(false));
/*     */     } else {
/* 683 */       wildcard_name.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 686 */     List<String> loreList = new ArrayList<>();
/* 687 */     if (keys.contains("lore")) {
/* 688 */       wildcard_lore.put(section, Boolean.valueOf(false));
/* 689 */       if (cs.isList("lore")) {
/* 690 */         loreList = cs.getStringList("lore");
/*     */       }
/* 692 */       if (cs.isString("lore")) {
/* 693 */         String line = cs.getString("lore");
/* 694 */         if (line != null && !line.isEmpty()) loreList.add(line); 
/*     */       } 
/*     */     } else {
/* 697 */       wildcard_lore.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 700 */     Map<Enchantment, Integer> enchantments = new HashMap<>();
/* 701 */     if (cs.contains("enchantments")) {
/* 702 */       wildcard_enchantments.put(section, Boolean.valueOf(false));
/* 703 */       List<Map<?, ?>> enchants = cs.getMapList("enchantments");
/* 704 */       for (Map<?, ?> map : enchants) {
/* 705 */         Enchantment ench = null;
/* 706 */         int level = 0;
/* 707 */         for (Object k : map.keySet()) {
/* 708 */           if (k instanceof String) {
/* 709 */             String key = (String)k;
/* 710 */             if (key.equalsIgnoreCase("ench")) {
/* 711 */               Object v = map.get(key);
/* 712 */               if (v instanceof String) {
/* 713 */                 String e = (String)v;
/* 714 */                 if (e.startsWith("minecraft:")) {
/*     */                   
/* 716 */                   NamespacedKey ns = NamespacedKey.minecraft(e.replace("minecraft:", ""));
/* 717 */                   ench = Enchantment.getByKey(ns); continue;
/*     */                 } 
/* 719 */                 ench = Enchantment.getByName(e);
/*     */               }  continue;
/*     */             } 
/* 722 */             if (key.equalsIgnoreCase("level")) {
/* 723 */               Object v = map.get(key);
/* 724 */               if (v instanceof Integer) {
/* 725 */                 level = ((Integer)v).intValue();
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 730 */         if (ench != null) {
/* 731 */           enchantments.put(ench, Integer.valueOf(level));
/*     */         }
/*     */       } 
/*     */     } else {
/* 735 */       wildcard_enchantments.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 738 */     int damage = 0;
/* 739 */     if (keys.contains("damage")) {
/* 740 */       wildcard_damage.put(section, Boolean.valueOf(false));
/*     */       try {
/* 742 */         damage = cs.getInt("damage");
/* 743 */       } catch (NumberFormatException numberFormatException) {}
/*     */     } else {
/*     */       
/* 746 */       wildcard_damage.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 749 */     boolean unbreakable = false;
/* 750 */     if (keys.contains("unbreakable")) {
/* 751 */       wildcard_unbreakable.put(section, Boolean.valueOf(false));
/* 752 */       unbreakable = cs.getBoolean("unbreakable");
/*     */     } else {
/* 754 */       wildcard_unbreakable.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 757 */     int custom_model_data = 0;
/* 758 */     if (AuctionHouse.mcVersion >= 1141) {
/* 759 */       if (keys.contains("custom_model_data")) {
/* 760 */         wildcard_custommodeldata.put(section, Boolean.valueOf(false));
/*     */         try {
/* 762 */           custom_model_data = cs.getInt("custom_model_data");
/* 763 */         } catch (NumberFormatException numberFormatException) {}
/*     */       } else {
/*     */         
/* 766 */         wildcard_custommodeldata.put(section, Boolean.valueOf(true));
/*     */       } 
/*     */     } else {
/* 769 */       wildcard_custommodeldata.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 772 */     Map<String, Object> persistentData = new HashMap<>();
/* 773 */     if (AuctionHouse.mcVersion >= 1132 && keys.contains("persistent_data")) {
/* 774 */       if (cs.isConfigurationSection("persistent_data")) {
/* 775 */         ConfigurationSection cs_pdk = cs.getConfigurationSection("persistent_data");
/* 776 */         if (cs_pdk != null) {
/*     */           
/* 778 */           Set<String> pdk_keys = cs_pdk.getKeys(false);
/* 779 */           for (String key : pdk_keys) {
/* 780 */             Object obj = cs_pdk.get(key);
/* 781 */             if (obj != null) {
/* 782 */               if (obj instanceof String) {
/* 783 */                 persistentData.put(key, obj); continue;
/* 784 */               }  if (obj instanceof Boolean) {
/* 785 */                 persistentData.put(key, obj); continue;
/* 786 */               }  if (obj instanceof Byte) {
/* 787 */                 persistentData.put(key, obj); continue;
/* 788 */               }  if (obj instanceof Short) {
/* 789 */                 persistentData.put(key, obj); continue;
/* 790 */               }  if (obj instanceof Integer) {
/* 791 */                 persistentData.put(key, obj); continue;
/* 792 */               }  if (obj instanceof Long) {
/* 793 */                 persistentData.put(key, obj); continue;
/* 794 */               }  if (obj instanceof Float) {
/* 795 */                 persistentData.put(key, obj); continue;
/* 796 */               }  if (obj instanceof Double) {
/* 797 */                 persistentData.put(key, obj);
/*     */               }
/*     */             } 
/*     */           } 
/* 801 */           if (!persistentData.isEmpty()) wildcard_persistentdata.put(section, Boolean.valueOf(false)); 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 805 */       wildcard_persistentdata.put(section, Boolean.valueOf(true));
/*     */     } 
/*     */ 
/*     */     
/* 809 */     ItemStack item = new ItemStack(material);
/* 810 */     ItemMeta meta = item.getItemMeta();
/* 811 */     boolean changed = false;
/* 812 */     if (meta != null) {
/* 813 */       if (displayName != null) {
/* 814 */         meta.setDisplayName(MessageUtil.sectionSymbol(displayName).replaceAll("\\\\u00[aA]7", "§"));
/* 815 */         changed = true;
/*     */       } 
/* 817 */       if (!loreList.isEmpty()) {
/* 818 */         List<String> lore = new ArrayList<>();
/* 819 */         for (String l : loreList) {
/* 820 */           if (l != null) {
/* 821 */             String line = l.replaceAll("\\\\u00[aA]7", "§");
/* 822 */             lore.add(MessageUtil.sectionSymbol(line));
/*     */           } 
/*     */         } 
/* 825 */         meta.setLore(lore);
/* 826 */         changed = true;
/*     */       } 
/* 828 */       if (!enchantments.isEmpty()) {
/* 829 */         for (Enchantment ench : enchantments.keySet()) {
/* 830 */           meta.addEnchant(ench, Math.max(0, ((Integer)enchantments.get(ench)).intValue()), true);
/*     */         }
/* 832 */         changed = true;
/*     */       } 
/* 834 */       if (meta instanceof Damageable && damage > 0) {
/* 835 */         ((Damageable)meta).setDamage(damage);
/* 836 */         changed = true;
/*     */       } 
/* 838 */       if (unbreakable) {
/* 839 */         meta.setUnbreakable(true);
/* 840 */         changed = true;
/*     */       } 
/* 842 */       if (changed) item.setItemMeta(meta); 
/* 843 */       if (AuctionHouse.mcVersion >= 1141 && 
/* 844 */         custom_model_data > 0) {
/* 845 */         item = AuctionHouse.nms.setCustomModelData(item, custom_model_data);
/*     */       }
/*     */       
/* 848 */       if (AuctionHouse.mcVersion >= 1132 && 
/* 849 */         !persistentData.isEmpty()) {
/* 850 */         for (String key : persistentData.keySet()) {
/* 851 */           Object value = persistentData.get(key);
/* 852 */           item = AuctionHouse.nms.setPersistentDataKey(item, key, value);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 858 */     restricted_items.put(section, item);
/*     */   }
/*     */ 
/*     */   
/*     */   private void logRestrictedItemsList() {
/* 863 */     if (debug) {
/* 864 */       for (String section : restricted_items.keySet()) {
/* 865 */         ItemStack item = restricted_items.get(section);
/*     */         
/* 867 */         AuctionHouse.logger.info(String.format("restricted_items.%s = ItemStack {", new Object[] { section }));
/* 868 */         AuctionHouse.logger.info(String.format("  Material = %s", new Object[] { (item.getType() == item_wildcard) ? "WILDCARD" : item.getType().getKey() }));
/* 869 */         ItemMeta itemMeta = item.getItemMeta();
/* 870 */         if (itemMeta != null) {
/* 871 */           if (itemMeta.hasDisplayName()) AuctionHouse.logger.info(String.format("  Name = %s", new Object[] { itemMeta.getDisplayName() })); 
/* 872 */           List<String> lore = itemMeta.getLore();
/* 873 */           if (lore != null && !lore.isEmpty()) {
/* 874 */             AuctionHouse.logger.info("  Lore = [");
/* 875 */             for (String line : lore) { AuctionHouse.logger.info(String.format("    %s,", new Object[] { line })); }
/* 876 */              AuctionHouse.logger.info("  ]");
/*     */           } 
/* 878 */           if (itemMeta.hasEnchants()) {
/* 879 */             Map<Enchantment, Integer> enchants = itemMeta.getEnchants();
/* 880 */             AuctionHouse.logger.info("  Enchantments = [");
/* 881 */             for (Enchantment ench : enchants.keySet()) {
/* 882 */               int level = ((Integer)enchants.get(ench)).intValue();
/* 883 */               AuctionHouse.logger.info(String.format("    {%s, %d},", new Object[] { ench.getKey().getKey(), Integer.valueOf(level) }));
/*     */             } 
/* 885 */             AuctionHouse.logger.info("  ]");
/*     */           } 
/* 887 */           if (itemMeta instanceof Damageable) {
/* 888 */             int durability = ((Damageable)item.getItemMeta()).getDamage();
/* 889 */             if (durability > 0) AuctionHouse.logger.info(String.format("  Damage = %d", new Object[] { Integer.valueOf(durability) })); 
/*     */           } 
/* 891 */           if (itemMeta.isUnbreakable()) {
/* 892 */             AuctionHouse.logger.info("  Unbreakable = true");
/*     */           }
/* 894 */           if (AuctionHouse.mcVersion >= 1141) {
/* 895 */             int customModelData = AuctionHouse.nms.getCustomModelData(item);
/* 896 */             if (customModelData > 0) {
/* 897 */               AuctionHouse.logger.info(String.format("  CustomModelData = %d", new Object[] { Integer.valueOf(customModelData) }));
/*     */             }
/*     */           } 
/* 900 */           if (AuctionHouse.mcVersion >= 1132) {
/* 901 */             Map<String, Object> persistentData = AuctionHouse.nms.getPersistentData(item);
/* 902 */             if (!persistentData.isEmpty()) {
/* 903 */               AuctionHouse.logger.info("  PersistentData:");
/* 904 */               for (String key : persistentData.keySet()) {
/* 905 */                 Object value = persistentData.get(key);
/* 906 */                 String class_name = value.getClass().getCanonicalName().replace("java.lang.", "");
/* 907 */                 AuctionHouse.logger.info(String.format("    %s = (%s) %s", new Object[] { key, class_name, value.toString() }));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 912 */         AuctionHouse.logger.info("}");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberFormat getNumberFormat(@Nullable String locale) {
/* 920 */     Locale loc = Locale.US;
/* 921 */     if (locale != null && !locale.isEmpty() && locale.contains("_")) {
/* 922 */       String language = locale.split("_")[0];
/* 923 */       String country = locale.split("_")[1];
/* 924 */       loc = new Locale(language, country);
/*     */     } 
/* 926 */     NumberFormat nf = NumberFormat.getInstance(loc);
/* 927 */     return nf;
/*     */   }
/*     */   
/*     */   public void applyDecimalFormat(String decimal_format) {
/* 931 */     if (!decimal_format.isEmpty() && 
/* 932 */       numberFormat instanceof DecimalFormat) {
/* 933 */       ((DecimalFormat)numberFormat).applyPattern(decimal_format);
/* 934 */       DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat)numberFormat).getDecimalFormatSymbols();
/*     */       
/* 936 */       if (decimalFormatSymbols.getGroupingSeparator() == '@') numberFormat.setGroupingUsed(false); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\config\Config.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */