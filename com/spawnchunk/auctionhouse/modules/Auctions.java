/*      */ package com.spawnchunk.auctionhouse.modules;
/*      */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*      */ import com.spawnchunk.auctionhouse.config.Config;
/*      */ import com.spawnchunk.auctionhouse.events.AuctionItemEvent;
/*      */ import com.spawnchunk.auctionhouse.events.ItemAction;
/*      */ import com.spawnchunk.auctionhouse.events.ListItemEvent;
/*      */ import com.spawnchunk.auctionhouse.menus.ActiveListingsMenu;
/*      */ import com.spawnchunk.auctionhouse.menus.ConfirmListingMenu;
/*      */ import com.spawnchunk.auctionhouse.menus.ExpiredListingsMenu;
/*      */ import com.spawnchunk.auctionhouse.menus.PlayerListingsMenu;
/*      */ import com.spawnchunk.auctionhouse.menus.PurchaseItemMenu;
/*      */ import com.spawnchunk.auctionhouse.menus.SoldItemsMenu;
/*      */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*      */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.GameMode;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.OfflinePlayer;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.Event;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.inventory.PlayerInventory;
/*      */ import org.bukkit.inventory.meta.ItemMeta;
/*      */ import org.bukkit.permissions.PermissionAttachmentInfo;
/*      */ import org.bukkit.plugin.Plugin;
/*      */ import org.bukkit.scheduler.BukkitRunnable;
/*      */ 
/*      */ public class Auctions {
/*   37 */   private static final Map<UUID, ActiveListingsMenu> activeListingsMenus = new HashMap<>();
/*   38 */   private static final Map<UUID, PlayerListingsMenu> playerListingsMenus = new HashMap<>();
/*   39 */   private static final Map<UUID, ExpiredListingsMenu> expiredListingsMenus = new HashMap<>();
/*   40 */   private static final Map<UUID, SoldItemsMenu> soldItemsMenus = new HashMap<>();
/*   41 */   private static final Map<UUID, PurchaseItemMenu> purchaseItemMenus = new HashMap<>();
/*   42 */   private static final Map<UUID, ConfirmListingMenu> confirmListingMenus = new HashMap<>();
/*      */   
/*   44 */   private static int activeListingsCount = 0;
/*   45 */   private static final Map<UUID, Integer> playerListingsCount = new HashMap<>();
/*   46 */   private static final Map<UUID, Integer> expiredListingsCount = new HashMap<>();
/*   47 */   private static final Map<UUID, Integer> soldItemsCount = new HashMap<>();
/*      */   
/*   49 */   public static Set<UUID> lock = new HashSet<>();
/*      */   
/*      */   public static String filter;
/*      */   public static boolean menu_mode;
/*      */   
/*      */   public static void initializeMenus() {
/*   55 */     activeListingsMenus.clear();
/*   56 */     activeListingsCount = 0;
/*   57 */     playerListingsMenus.clear();
/*   58 */     playerListingsCount.clear();
/*   59 */     expiredListingsMenus.clear();
/*   60 */     expiredListingsCount.clear();
/*   61 */     soldItemsMenus.clear();
/*   62 */     soldItemsCount.clear();
/*   63 */     purchaseItemMenus.clear();
/*   64 */     confirmListingMenus.clear();
/*      */   }
/*      */   
/*      */   public static void openActiveListingsMenu(Player player) {
/*   68 */     openActiveListingsMenu(player, 0);
/*      */   }
/*      */   
/*      */   public static void openActiveListingsMenu(final Player player, final int page) {
/*      */     final ActiveListingsMenu menu;
/*   73 */     UUID uuid = player.getUniqueId();
/*      */ 
/*      */     
/*   76 */     if (activeListingsMenus.containsKey(uuid) && activeListingsMenus.get(uuid) != null) {
/*   77 */       menu = activeListingsMenus.get(uuid);
/*      */     } else {
/*   79 */       menu = new ActiveListingsMenu(player, menu_mode);
/*   80 */       activeListingsMenus.put(uuid, menu);
/*      */     } 
/*   82 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*   85 */           menu.show(player, Auctions.menu_mode, Auctions.filter, page);
/*      */         }
/*   87 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*      */   }
/*      */   
/*      */   public static void rebuildActiveListingsMenus() {
/*   91 */     for (UUID uuid : activeListingsMenus.keySet()) {
/*   92 */       ActiveListingsMenu menu = activeListingsMenus.get(uuid);
/*   93 */       if (menu != null) menu.initialize(); 
/*      */     } 
/*      */   }
/*      */   public static void openPlayerListingsMenu(final Player player) {
/*      */     final PlayerListingsMenu menu;
/*   98 */     final int page = 0;
/*      */     
/*  100 */     UUID uuid = player.getUniqueId();
/*      */ 
/*      */     
/*  103 */     if (playerListingsMenus.containsKey(uuid) && playerListingsMenus.get(uuid) != null) {
/*  104 */       menu = playerListingsMenus.get(uuid);
/*      */     } else {
/*  106 */       menu = new PlayerListingsMenu(player);
/*  107 */       playerListingsMenus.put(uuid, menu);
/*      */     } 
/*  109 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  112 */           menu.show(player, page);
/*      */         }
/*  114 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*      */   }
/*      */   
/*      */   public static void rebuildPlayerListingsMenus() {
/*  118 */     for (UUID uuid : playerListingsMenus.keySet()) {
/*  119 */       PlayerListingsMenu menu = playerListingsMenus.get(uuid);
/*  120 */       if (menu != null) menu.initialize(); 
/*      */     } 
/*      */   }
/*      */   public static void openExpiredListingsMenu(final Player player) {
/*      */     final ExpiredListingsMenu menu;
/*  125 */     final int page = 0;
/*      */     
/*  127 */     UUID uuid = player.getUniqueId();
/*      */ 
/*      */     
/*  130 */     if (expiredListingsMenus.containsKey(uuid) && expiredListingsMenus.get(uuid) != null) {
/*  131 */       menu = expiredListingsMenus.get(uuid);
/*      */     } else {
/*  133 */       menu = new ExpiredListingsMenu(player);
/*  134 */       expiredListingsMenus.put(uuid, menu);
/*      */     } 
/*  136 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  139 */           menu.show(player, page);
/*      */         }
/*  141 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*      */   }
/*      */   
/*      */   public static void rebuildExpiredListingsMenus() {
/*  145 */     for (UUID uuid : expiredListingsMenus.keySet()) {
/*  146 */       ExpiredListingsMenu menu = expiredListingsMenus.get(uuid);
/*  147 */       if (menu != null) menu.initialize(); 
/*      */     } 
/*      */   }
/*      */   public static void openSoldItemsMenu(final Player player) {
/*      */     final SoldItemsMenu menu;
/*  152 */     final int page = 0;
/*      */     
/*  154 */     UUID uuid = player.getUniqueId();
/*      */ 
/*      */     
/*  157 */     if (soldItemsMenus.containsKey(uuid) && soldItemsMenus.get(uuid) != null) {
/*  158 */       menu = soldItemsMenus.get(uuid);
/*      */     } else {
/*  160 */       menu = new SoldItemsMenu(player);
/*  161 */       soldItemsMenus.put(uuid, menu);
/*      */     } 
/*  163 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  166 */           menu.show(player, page);
/*      */         }
/*  168 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*      */   }
/*      */   
/*      */   public static void rebuildSoldItemsMenus() {
/*  172 */     for (UUID uuid : soldItemsMenus.keySet()) {
/*  173 */       SoldItemsMenu menu = soldItemsMenus.get(uuid);
/*  174 */       if (menu != null) menu.initialize(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void openPurchaseItemMenu(final Player player, final Long timestamp, final int return_page) {
/*      */     final PurchaseItemMenu menu;
/*  180 */     UUID uuid = player.getUniqueId();
/*      */ 
/*      */     
/*  183 */     if (purchaseItemMenus.containsKey(uuid) && purchaseItemMenus.get(uuid) != null) {
/*  184 */       menu = purchaseItemMenus.get(uuid);
/*      */     } else {
/*  186 */       menu = new PurchaseItemMenu(player);
/*  187 */       purchaseItemMenus.put(uuid, menu);
/*      */     } 
/*  189 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  192 */           menu.show(player, timestamp.longValue(), return_page);
/*      */         }
/*  194 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*      */   }
/*      */   
/*      */   public static void rebuildPurchaseItemMenus() {
/*  198 */     for (UUID uuid : purchaseItemMenus.keySet()) {
/*  199 */       PurchaseItemMenu menu = purchaseItemMenus.get(uuid);
/*  200 */       if (menu != null) menu.initialize(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void openConfirmListingMenu(final Player player, final ItemStack item, float price, double listing_fee, ListingType type) {
/*  205 */     if (Config.debug) AuctionHouse.logger.info("openConfirmListingMenu");
/*      */     
/*  207 */     UUID uuid = player.getUniqueId();
/*  208 */     final ConfirmListingMenu menu = new ConfirmListingMenu(player, price, listing_fee, type);
/*      */ 
/*      */     
/*  211 */     if (confirmListingMenus.containsKey(uuid)) {
/*  212 */       removeConfirmListingsMenu(player);
/*      */     }
/*  214 */     confirmListingMenus.put(uuid, menu);
/*  215 */     (new BukkitRunnable()
/*      */       {
/*      */         public void run() {
/*  218 */           menu.show(player, item);
/*      */         }
/*  220 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*      */   }
/*      */   
/*      */   public static void removeConfirmListingsMenu(Player player) {
/*  224 */     UUID uuid = player.getUniqueId();
/*  225 */     ConfirmListingMenu menu = confirmListingMenus.get(uuid);
/*  226 */     if (menu != null) {
/*  227 */       MenuClickEvent.getHandlerList().unregister((Listener)menu);
/*  228 */       MenuCloseEvent.getHandlerList().unregister((Listener)menu);
/*      */     } 
/*  230 */     confirmListingMenus.remove(uuid);
/*      */   }
/*      */   
/*      */   public static void RebuildAllMenus() {
/*  234 */     rebuildActiveListingsMenus();
/*  235 */     rebuildPlayerListingsMenus();
/*  236 */     rebuildExpiredListingsMenus();
/*  237 */     rebuildSoldItemsMenus();
/*  238 */     rebuildPurchaseItemMenus();
/*      */   }
/*      */   
/*      */   private static boolean similarType(ItemStack item, ItemStack match) {
/*  242 */     Material comparisonType = item.getType();
/*  243 */     return (match.getType() == Config.item_wildcard || comparisonType == match.getType());
/*      */   }
/*      */   
/*      */   private static boolean similarName(ItemStack item, ItemStack match) {
/*  247 */     ItemMeta m = match.getItemMeta();
/*  248 */     if (m == null) return true; 
/*  249 */     if (!m.hasDisplayName()) return true; 
/*  250 */     ItemMeta i = item.getItemMeta();
/*  251 */     String name = (i != null) ? (i.hasDisplayName() ? i.getDisplayName() : "") : "";
/*  252 */     String pattern = m.getDisplayName();
/*  253 */     if (pattern.startsWith("regex:")) {
/*  254 */       pattern = pattern.replace("regex:", "");
/*  255 */       return name.matches(pattern);
/*  256 */     }  if (pattern.startsWith("fuzzy:")) {
/*  257 */       pattern = pattern.replace("fuzzy:", "");
/*  258 */       return name.contains(pattern);
/*      */     } 
/*  260 */     return name.equals(pattern);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean similarLore(ItemStack item, ItemStack match) {
/*  265 */     ItemMeta m = match.getItemMeta();
/*  266 */     ItemMeta i = item.getItemMeta();
/*  267 */     if (m == null && i == null) return true; 
/*  268 */     if (m == null) return false; 
/*  269 */     if (i == null) return false; 
/*  270 */     List<String> iLore = i.hasLore() ? i.getLore() : new ArrayList<>();
/*  271 */     List<String> mLore = m.hasLore() ? m.getLore() : new ArrayList<>();
/*  272 */     if (iLore == null) return (mLore == null); 
/*  273 */     if (mLore == null) return true; 
/*  274 */     boolean similar = true;
/*  275 */     for (String pattern : mLore) {
/*  276 */       boolean hit = false;
/*  277 */       if (pattern.startsWith("regex:")) {
/*  278 */         pattern = pattern.replace("regex:", "");
/*  279 */         for (String line : iLore) {
/*  280 */           if (line.matches(pattern)) {
/*  281 */             hit = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*  285 */       } else if (pattern.startsWith("fuzzy:")) {
/*  286 */         pattern = pattern.replace("fuzzy:", "");
/*  287 */         for (String line : iLore) {
/*  288 */           if (line.contains(pattern)) {
/*  289 */             hit = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } else {
/*  294 */         for (String line : iLore) {
/*  295 */           if (line.equals(pattern)) {
/*  296 */             hit = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*  301 */       if (!hit) {
/*  302 */         similar = false;
/*      */         break;
/*      */       } 
/*      */     } 
/*  306 */     return similar;
/*      */   }
/*      */   
/*      */   private static boolean similarEnchants(ItemStack item, ItemStack match) {
/*  310 */     ItemMeta m = match.getItemMeta();
/*  311 */     ItemMeta i = item.getItemMeta();
/*  312 */     if (m == null && i == null) return true; 
/*  313 */     if (m == null) return false; 
/*  314 */     if (i == null) return false; 
/*  315 */     return (i.hasEnchants() && m.hasEnchants()) ? i.getEnchants().equals(m.getEnchants()) : (!m.hasEnchants());
/*      */   }
/*      */   
/*      */   private static boolean similarDamage(ItemStack item, ItemStack match) {
/*  319 */     ItemMeta matchMeta = match.getItemMeta();
/*  320 */     ItemMeta itemMeta = item.getItemMeta();
/*  321 */     if (matchMeta == null && itemMeta == null) return true; 
/*  322 */     if (itemMeta instanceof Damageable && matchMeta instanceof Damageable) {
/*  323 */       int itemDamage = ((Damageable)itemMeta).getDamage();
/*  324 */       int matchDamage = ((Damageable)matchMeta).getDamage();
/*  325 */       return (itemDamage == matchDamage);
/*  326 */     }  return (!(itemMeta instanceof Damageable) && !(matchMeta instanceof Damageable));
/*      */   }
/*      */   
/*      */   private static boolean similarUnbreakable(ItemStack item, ItemStack match) {
/*  330 */     boolean itemIsUnbreakable = (item.getItemMeta() != null && item.getItemMeta().isUnbreakable());
/*  331 */     boolean matchIsUnbreakable = (match.getItemMeta() != null && match.getItemMeta().isUnbreakable());
/*  332 */     return (itemIsUnbreakable == matchIsUnbreakable);
/*      */   }
/*      */   
/*      */   private static boolean similarCustomModelData(ItemStack item, ItemStack match) {
/*  336 */     if (AuctionHouse.mcVersion < 1141) return true; 
/*  337 */     int itemCustomModelData = AuctionHouse.nms.getCustomModelData(item);
/*  338 */     int matchCustomModelData = AuctionHouse.nms.getCustomModelData(match);
/*  339 */     return (itemCustomModelData == matchCustomModelData);
/*      */   }
/*      */   
/*      */   private static boolean similarPersistentData(ItemStack item, ItemStack match) {
/*  343 */     if (AuctionHouse.mcVersion < 1141) return true; 
/*  344 */     Map<String, Object> itemPersistentData = AuctionHouse.nms.getPersistentData(item);
/*  345 */     Map<String, Object> matchPersistentData = AuctionHouse.nms.getPersistentData(match);
/*  346 */     if (matchPersistentData.isEmpty()) return true; 
/*  347 */     for (String key : matchPersistentData.keySet()) {
/*  348 */       Object value = matchPersistentData.get(key);
/*  349 */       if (itemPersistentData.containsKey(key)) {
/*  350 */         Object value2 = itemPersistentData.get(key);
/*  351 */         if (!Objects.equals(value, value2)) return false;  continue;
/*      */       } 
/*  353 */       return false;
/*      */     } 
/*      */     
/*  356 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean isSimilar(ItemStack item, ItemStack match, String section) {
/*  360 */     if (match == null) return false; 
/*  361 */     if (item == match) return true;
/*      */     
/*  363 */     boolean f = (((Boolean)Config.wildcard_item.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarType(item, match));
/*  364 */     boolean f1 = (((Boolean)Config.wildcard_name.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarName(item, match));
/*  365 */     boolean f2 = (((Boolean)Config.wildcard_lore.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarLore(item, match));
/*  366 */     boolean f3 = (((Boolean)Config.wildcard_enchantments.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarEnchants(item, match));
/*  367 */     boolean f4 = (((Boolean)Config.wildcard_damage.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarDamage(item, match));
/*  368 */     boolean f5 = (((Boolean)Config.wildcard_unbreakable.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarUnbreakable(item, match));
/*  369 */     boolean f6 = (((Boolean)Config.wildcard_custommodeldata.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarCustomModelData(item, match));
/*  370 */     boolean f7 = (((Boolean)Config.wildcard_persistentdata.getOrDefault(section, Boolean.valueOf(false))).booleanValue() || similarPersistentData(item, match));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  383 */     boolean result = (f && f1 && f2 && f3 && f4 && f5 && f6 && f7);
/*  384 */     if (Config.debug && result) AuctionHouse.logger.info(String.format("Blocked restricted item matching restricted_items.%s", new Object[] { section })); 
/*  385 */     return result;
/*      */   }
/*      */   
/*      */   public static void clearAllData(CommandSender sender) {
/*  389 */     AuctionHouse.listings.clear();
/*  390 */     Server server = AuctionHouse.plugin.getServer();
/*  391 */     for (Player player : server.getOnlinePlayers()) {
/*  392 */       updateCounts(player);
/*      */     }
/*  394 */     sender.sendMessage("All auction data cleared!");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void createTestData(CommandSender sender, int entries) {
/*  399 */     OfflinePlayer[] players = AuctionHouse.plugin.getServer().getOfflinePlayers();
/*  400 */     int player_count = players.length;
/*  401 */     if (player_count < 1) {
/*  402 */       sender.sendMessage("No offline players available to create test data!");
/*      */       return;
/*      */     } 
/*  405 */     Random random = new Random();
/*  406 */     Material[] materials = Material.values();
/*  407 */     int count = materials.length;
/*  408 */     int i = 0;
/*  409 */     while (i < entries) {
/*  410 */       int index = random.nextInt(count);
/*  411 */       Material material = materials[index];
/*  412 */       if (material != null && material != Material.AIR && material.isItem()) {
/*  413 */         int max_stacksize = material.getMaxStackSize();
/*  414 */         int amount = random.nextInt(max_stacksize) + 1;
/*  415 */         ItemStack item = new ItemStack(material, amount);
/*  416 */         int price = random.nextInt(1000) + 1;
/*  417 */         int r = random.nextInt(player_count);
/*  418 */         OfflinePlayer seller = players[r];
/*  419 */         String world = ((World)Bukkit.getServer().getWorlds().get(0)).getName();
/*  420 */         if (seller.isOnline()) world = ((Player)seller).getWorld().getName(); 
/*  421 */         final Listing listing = new Listing(world, seller.getUniqueId().toString(), null, null, price, 0.0F, 0.0F, ListingType.PLAYER_LISTING, item);
/*      */         
/*  423 */         (new BukkitRunnable()
/*      */           {
/*      */             public void run() {
/*  426 */               AuctionHouse.listings.newListing(listing);
/*      */             }
/*  428 */           }).runTaskLater((Plugin)AuctionHouse.plugin, (i + 1));
/*  429 */         if (seller.isOnline()) updateCounts(seller.getPlayer()); 
/*  430 */         i++;
/*      */       } 
/*      */     } 
/*  433 */     sender.sendMessage(String.format("Creating %s test data entries in the background.", new Object[] { Integer.valueOf(entries) }));
/*      */   }
/*      */   
/*      */   public static void sellItemInHand(Player player, float price, ListingType type) {
/*  437 */     sellItemInHand(player, price, null, type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sellItemInHand(Player player, float price, @Nullable Integer count, ListingType type) {
/*  442 */     boolean bypass = (player.isOp() && type.isServer());
/*  443 */     if (player.getGameMode().equals(GameMode.CREATIVE) && Config.auction_prevent_creative) {
/*  444 */       MessageUtil.sendMessage(player, "warning.sell.creative", Config.locale);
/*      */       return;
/*      */     } 
/*  447 */     if (player.getGameMode().equals(GameMode.SPECTATOR) && Config.auction_prevent_spectator) {
/*  448 */       MessageUtil.sendMessage(player, "warning.sell.spectator", Config.locale);
/*      */       
/*      */       return;
/*      */     } 
/*  452 */     PlayerInventory inventory = player.getInventory();
/*  453 */     ItemStack item = inventory.getItemInMainHand();
/*  454 */     if (item.getType().equals(Material.AIR) || item.getType().equals(Material.CAVE_AIR) || item.getType().equals(Material.VOID_AIR)) {
/*  455 */       MessageUtil.sendMessage(player, "warning.sell.no_item", Config.locale);
/*      */       
/*      */       return;
/*      */     } 
/*  459 */     if (!bypass) {
/*  460 */       for (String section : Config.restricted_items.keySet()) {
/*  461 */         ItemStack prohibited = (ItemStack)Config.restricted_items.get(section);
/*  462 */         if (isSimilar(item, prohibited, section)) {
/*  463 */           MessageUtil.sendMessage(player, "warning.sell.restricted_item", Config.locale);
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     }
/*  468 */     if (Config.auction_prevent_filled_containers && ItemUtil.isFilledContainer(item) && !bypass) {
/*  469 */       MessageUtil.sendMessage(player, "warning.sell.filled_container", Config.locale);
/*      */       return;
/*      */     } 
/*  472 */     if (!Config.auction_allow_damaged_items && ItemUtil.hasDamage(item) && !bypass) {
/*  473 */       MessageUtil.sendMessage(player, "warning.sell.damaged_item", Config.locale);
/*      */       return;
/*      */     } 
/*  476 */     if (price > Config.auction_max_sell_price && !bypass) {
/*  477 */       MessageUtil.sendMessage(player, "warning.sell.max_price", Config.locale, new Object[] { Double.valueOf(Config.auction_max_sell_price) });
/*      */       return;
/*      */     } 
/*  480 */     if (price < Config.auction_min_sell_price && !bypass) {
/*  481 */       MessageUtil.sendMessage(player, "warning.sell.min_price", Config.locale, new Object[] { Double.valueOf(Config.auction_min_sell_price) });
/*      */       return;
/*      */     } 
/*  484 */     if (price < 0.0F) {
/*  485 */       MessageUtil.sendMessage(player, "warning.sell.negative_price", Config.locale);
/*      */       
/*      */       return;
/*      */     } 
/*  489 */     if (count != null && (
/*  490 */       count.intValue() <= 0 || count.intValue() > item.getAmount())) {
/*  491 */       MessageUtil.sendMessage(player, "warning.sell.invalid_amount", Config.locale);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  496 */     updateCounts(player);
/*  497 */     int listings = getPlayerListingsCount((OfflinePlayer)player);
/*  498 */     int max_listings = getMaxListings(player);
/*  499 */     if (max_listings == 0 && !player.isOp()) {
/*  500 */       MessageUtil.sendMessage(player, "warning.sell.unavailable", Config.locale);
/*      */       return;
/*      */     } 
/*  503 */     if (listings >= max_listings && !bypass) {
/*  504 */       MessageUtil.sendMessage(player, "warning.sell.max_listings", Config.locale, new Object[] { Integer.valueOf(max_listings) });
/*      */       return;
/*      */     } 
/*  507 */     UUID uuid = player.getUniqueId();
/*  508 */     if (AuctionHouse.playerCooldowns.containsKey(uuid) && !bypass) {
/*  509 */       long lastListing = ((Long)AuctionHouse.playerCooldowns.get(uuid)).longValue();
/*  510 */       long nextListing = lastListing + Config.auction_listing_cooldown;
/*  511 */       if (TimeUtil.now() < nextListing) {
/*  512 */         long remaining = nextListing - TimeUtil.now();
/*  513 */         MessageUtil.sendMessage(player, "warning.listing.cooldown", Config.locale, new Object[] { TimeUtil.duration(remaining, true) });
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  518 */     double listing_fee = bypass ? 0.0D : (price * Config.auction_listing_rate / 100.0D + Config.auction_listing_price);
/*  519 */     OfflinePlayer seller = AuctionHouse.plugin.getServer().getOfflinePlayer(player.getUniqueId());
/*  520 */     String world = player.getWorld().getName();
/*  521 */     if (listing_fee > 0.0D) {
/*  522 */       double balance = Economy.getBalance(seller, world);
/*  523 */       if (listing_fee > balance) {
/*  524 */         MessageUtil.sendMessage(player, "warning.sell.insufficient_funds", Config.locale);
/*  525 */         if (Config.debug) AuctionHouse.logger.info(String.format("listing_fee = %.2f", new Object[] { Double.valueOf(listing_fee) })); 
/*  526 */         if (Config.debug) AuctionHouse.logger.info(String.format("balance = %.2f", new Object[] { Double.valueOf(balance) }));
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  532 */     lock.add(player.getUniqueId());
/*      */ 
/*      */     
/*  535 */     if (count == null || item.getAmount() == count.intValue()) {
/*  536 */       inventory.setItemInMainHand(new ItemStack(Material.AIR, 1));
/*      */     }
/*  538 */     else if (item.getAmount() > count.intValue()) {
/*  539 */       ItemStack is = inventory.getItemInMainHand().clone();
/*  540 */       is.setAmount(is.getAmount() - count.intValue());
/*  541 */       inventory.setItemInMainHand(is);
/*  542 */       item.setAmount(count.intValue());
/*      */     } else {
/*  544 */       MessageUtil.sendMessage(player, "warning.sell.invalid_amount", Config.locale);
/*  545 */       lock.remove(player.getUniqueId());
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  550 */     if (listing_fee > 0.0D) {
/*      */       
/*  552 */       openConfirmListingMenu(player, item, price, listing_fee, type);
/*      */     } else {
/*  554 */       completeListing(player, item, price, listing_fee, type, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void completeListing(Player player, ItemStack item, float price, double listing_fee, ListingType type, boolean cancelled) {
/*  561 */     boolean bypass = (player.isOp() && type.isServer());
/*  562 */     Listing listing = null;
/*  563 */     String world = player.getWorld().getName();
/*  564 */     String seller_uuid = player.getUniqueId().toString();
/*  565 */     String seller_name = type.isServer() ? AuctionHouse.servername : player.getName();
/*      */ 
/*      */     
/*  568 */     if (listing_fee > 0.0D) {
/*  569 */       double balance = Economy.getBalance((OfflinePlayer)player, world);
/*  570 */       if (listing_fee > balance) {
/*  571 */         MessageUtil.sendMessage(player, "warning.sell.insufficient_funds", Config.locale);
/*  572 */         if (Config.debug) AuctionHouse.logger.info(String.format("listing_fee = %.2f", new Object[] { Double.valueOf(listing_fee) })); 
/*  573 */         if (Config.debug) AuctionHouse.logger.info(String.format("balance = %.2f", new Object[] { Double.valueOf(balance) })); 
/*  574 */         cancelled = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  579 */     ListItemEvent event = new ListItemEvent(world, seller_uuid, price, type, item);
/*  580 */     Bukkit.getPluginManager().callEvent((Event)event);
/*  581 */     if (event.isCancelled()) {
/*  582 */       cancelled = true;
/*      */     }
/*      */     
/*  585 */     if (!cancelled) {
/*      */       
/*  587 */       float reserve = 0.0F;
/*      */       
/*  589 */       listing = new Listing(world, seller_uuid, null, null, price, reserve, 0.0F, type, item);
/*      */       
/*  591 */       if (!AuctionHouse.listings.newListing(listing)) {
/*  592 */         MessageUtil.sendMessage(player, "warning.sell.unknown", Config.locale);
/*  593 */         cancelled = true;
/*      */       }
/*      */       else {
/*      */         
/*  597 */         if (!bypass) {
/*  598 */           Economy.withdrawPlayer((OfflinePlayer)player, world, listing_fee);
/*  599 */           double seller_balance = Economy.getBalance((OfflinePlayer)player, world);
/*  600 */           if (listing_fee > 0.0D) {
/*  601 */             MessageUtil.sendMessage(player, "message.sell.fee", Config.locale, new Object[] { Double.valueOf(listing_fee) });
/*  602 */             MessageUtil.sendMessage(player, "message.sell.seller_balance", Config.locale, new Object[] { Double.valueOf(seller_balance) });
/*      */           } 
/*      */         } 
/*      */         
/*  606 */         UUID uuid = player.getUniqueId();
/*  607 */         AuctionHouse.playerCooldowns.put(uuid, Long.valueOf(TimeUtil.now()));
/*      */         
/*  609 */         updateCounts(player);
/*      */       } 
/*      */     } 
/*      */     
/*  613 */     if (cancelled) {
/*      */ 
/*      */       
/*  616 */       Map<Integer, ItemStack> remaining = player.getInventory().addItem(new ItemStack[] { item });
/*  617 */       for (Integer index : remaining.keySet()) {
/*  618 */         player.getWorld().dropItem(player.getLocation(), remaining.get(index));
/*      */       }
/*      */     } else {
/*  621 */       int amount = item.getAmount();
/*  622 */       String translatable_name = ItemUtil.getTranslatableName(item);
/*  623 */       String item_name = MessageUtil.sectionSymbol(ItemUtil.getTranslatableName(item));
/*  624 */       MessageUtil.sendMessage(player, "message.sell.listed_item", Config.locale, new Object[] { Integer.valueOf(amount), item_name, Float.valueOf(price) });
/*  625 */       for (Player p : AuctionHouse.plugin.getServer().getOnlinePlayers()) {
/*  626 */         if (p != player) {
/*      */           
/*  628 */           if (Config.announce_chat_listings) MessageUtil.sendMessage(listing.getWorld(), p, "message.sell.player.listed_item", Config.locale, new Object[] { seller_name, Integer.valueOf(amount), item_name, Float.valueOf(price) });
/*      */           
/*  630 */           if (Config.announce_action_bar_listings) MessageUtil.sendActionBar(listing.getWorld(), p, Long.valueOf(0L), "message.sell.player.listed_item", Config.locale, new Object[] { seller_name, Integer.valueOf(amount), item_name, Float.valueOf(price) });
/*      */         
/*      */         } 
/*      */       } 
/*  634 */       if (Config.announce_discord_listings) {
/*  635 */         MessageUtil.discordMessage("discord.sell.player.listed_item", Config.locale, new Object[] { seller_name, Integer.valueOf(amount), MessageUtil.nocolor(item_name), Float.valueOf(price) });
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  641 */       AuctionItemEvent event1 = new AuctionItemEvent(ItemAction.ITEM_LISTED, listing.getWorld(), seller_uuid, listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/*  642 */       Bukkit.getPluginManager().callEvent((Event)event1);
/*      */     } 
/*      */     
/*  645 */     lock.remove(player.getUniqueId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getMaxListings(Player player) {
/*  651 */     int max = Config.auction_default_max_listings;
/*  652 */     if (Config.debug) AuctionHouse.logger.info(String.format("Found auction.defaultMaxListings: %d in config.yml", new Object[] { Integer.valueOf(max) }));
/*      */ 
/*      */     
/*  655 */     Set<PermissionAttachmentInfo> effective = player.getEffectivePermissions();
/*  656 */     for (PermissionAttachmentInfo info : effective) {
/*  657 */       String perm = info.getPermission();
/*  658 */       if (perm.toLowerCase().startsWith("auctionhouse.auctions.")) {
/*  659 */         String value = perm.replaceAll("\\D+", "");
/*      */         try {
/*  661 */           int max_perm = Integer.parseInt(value);
/*  662 */           if (Config.debug)
/*  663 */             AuctionHouse.logger.info(String.format("Found effective permission %s (either player or group)", new Object[] { perm })); 
/*  664 */           max = Math.max(max, max_perm);
/*  665 */         } catch (NumberFormatException ignored) {
/*  666 */           if (Config.debug) {
/*  667 */             AuctionHouse.logger.info(String.format("Could not parse number value from auctionhouse.auctions.# permission for %s", new Object[] { player.getName() }));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  673 */     if (AuctionHouse.chat != null && AuctionHouse.chat.isEnabled()) {
/*      */       
/*  675 */       String[] groups = AuctionHouse.chat.getPlayerGroups(player);
/*  676 */       if (groups != null)
/*      */       {
/*  678 */         for (String group : groups) {
/*  679 */           int max_group_meta = AuctionHouse.chat.getGroupInfoInteger(player.getWorld(), group, "auctions", -1);
/*  680 */           if (Config.debug && max_group_meta >= 0)
/*  681 */             AuctionHouse.logger.info(String.format("Found group meta value auction = %d", new Object[] { Integer.valueOf(max_group_meta) })); 
/*  682 */           if (max_group_meta >= 0) max = Math.max(max, max_group_meta);
/*      */         
/*      */         } 
/*      */       }
/*      */       
/*  687 */       int max_meta = AuctionHouse.chat.getPlayerInfoInteger(player, "auctions", -1);
/*  688 */       if (Config.debug && max_meta >= 0) AuctionHouse.logger.info(String.format("Found player meta value auction = %d", new Object[] { Integer.valueOf(max_meta) }));
/*      */       
/*  690 */       if (max_meta >= 0) max = Math.max(max, max_meta);
/*      */     
/*      */     } 
/*  693 */     if (Config.debug) AuctionHouse.logger.info(String.format("Maximum auctions set to %d", new Object[] { Integer.valueOf(max) })); 
/*  694 */     return max;
/*      */   }
/*      */   
/*      */   public static void updateCounts(Player player) {
/*  698 */     AuctionHouse.listings.updateCounts(player);
/*      */   }
/*      */   
/*      */   public static int getActiveListingsCount() {
/*  702 */     return activeListingsCount;
/*      */   }
/*      */   
/*      */   public static void setActiveListingsCount(int count) {
/*  706 */     activeListingsCount = count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getPlayerListingsCount(OfflinePlayer op) {
/*  714 */     UUID uuid = op.getUniqueId();
/*  715 */     return ((Integer)playerListingsCount.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/*      */   }
/*      */   
/*      */   public static void setPlayerListingsCount(UUID uuid, int count) {
/*  719 */     playerListingsCount.put(uuid, Integer.valueOf(count));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getExpiredListingsCount(OfflinePlayer op) {
/*  729 */     UUID uuid = op.getUniqueId();
/*  730 */     return ((Integer)expiredListingsCount.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/*      */   }
/*      */   
/*      */   public static void setExpiredListingsCount(UUID uuid, int count) {
/*  734 */     expiredListingsCount.put(uuid, Integer.valueOf(count));
/*      */   }
/*      */   
/*      */   public static int getExpiredListingsCount() {
/*  738 */     return AuctionHouse.listings.getExpiredListings().count();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getSoldItemsCount(OfflinePlayer op) {
/*  748 */     UUID uuid = op.getUniqueId();
/*  749 */     return ((Integer)soldItemsCount.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/*      */   }
/*      */   
/*      */   public static void setSoldItemsCount(UUID uuid, int count) {
/*  753 */     soldItemsCount.put(uuid, Integer.valueOf(count));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static OfflinePlayer getSeller(Long timestamp) {
/*  763 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  764 */     return (listing != null) ? listing.getSeller() : null;
/*      */   }
/*      */   
/*      */   public static OfflinePlayer getBuyer(Long timestamp) {
/*  768 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  769 */     return (listing != null) ? listing.getBuyer() : null;
/*      */   }
/*      */   
/*      */   public static float getPrice(Long timestamp) {
/*  773 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  774 */     return (listing != null) ? listing.getPrice() : 0.0F;
/*      */   }
/*      */   
/*      */   public static float getReserve(Long timestamp) {
/*  778 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  779 */     return (listing != null) ? listing.getReserve() : 0.0F;
/*      */   }
/*      */   
/*      */   public static float getBid(Long timestamp) {
/*  783 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  784 */     return (listing != null) ? listing.getBid() : 0.0F;
/*      */   }
/*      */   
/*      */   public static ListingType getType(Long timestamp) {
/*  788 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  789 */     return (listing != null) ? listing.getType() : ListingType.PLAYER_LISTING;
/*      */   }
/*      */   
/*      */   public static ItemStack getItem(Long timestamp) {
/*  793 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  794 */     return (listing != null) ? listing.getItem() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean canGiveItem(Player player, ItemStack item) {
/*  800 */     if (Config.drop_at_feet) return true;
/*      */     
/*  802 */     PlayerInventory pi = player.getInventory();
/*  803 */     if (pi.firstEmpty() != -1) return true;
/*      */     
/*  805 */     if (pi.containsAtLeast(item, 1)) {
/*  806 */       int free = 0;
/*  807 */       for (ItemStack i : pi.getStorageContents()) {
/*  808 */         if (i.isSimilar(item)) free += i.getMaxStackSize() - i.getAmount(); 
/*      */       } 
/*  810 */       return (free >= item.getAmount());
/*      */     } 
/*  812 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean purchaseItem(Player player, Long timestamp) {
/*  816 */     if (player.getGameMode().equals(GameMode.CREATIVE) && Config.auction_prevent_creative) {
/*  817 */       MessageUtil.sendMessage(player, "warning.purchase.creative", Config.locale);
/*  818 */       return false;
/*      */     } 
/*  820 */     if (player.getGameMode().equals(GameMode.SPECTATOR) && Config.auction_prevent_spectator) {
/*  821 */       MessageUtil.sendMessage(player, "warning.purchase.spectator", Config.locale);
/*  822 */       return false;
/*      */     } 
/*      */     
/*  825 */     OfflinePlayer buyer = AuctionHouse.plugin.getServer().getOfflinePlayer(player.getUniqueId());
/*  826 */     String world = player.getWorld().getName();
/*  827 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/*  828 */     if (listing == null) {
/*  829 */       MessageUtil.sendMessage(player, "warning.listing.expired", Config.locale);
/*  830 */       return false;
/*      */     } 
/*  832 */     float price = listing.getPrice();
/*  833 */     OfflinePlayer seller = listing.getSeller();
/*  834 */     int amount = listing.getItem().getAmount();
/*  835 */     ListingType type = listing.getType();
/*  836 */     if (seller == null) return false; 
/*  837 */     if (player.getUniqueId().equals(seller.getUniqueId()) && !type.isServer()) {
/*  838 */       MessageUtil.sendMessage(player, "warning.purchase.own_item", Config.locale);
/*  839 */       return false;
/*      */     } 
/*  841 */     double balance = Economy.getBalance(buyer, world);
/*  842 */     if (price > balance) {
/*  843 */       MessageUtil.sendMessage(player, "warning.purchase.insufficient_funds", Config.locale);
/*  844 */       return false;
/*      */     } 
/*      */     
/*  847 */     if (!canGiveItem(player, listing.getItem().clone())) {
/*  848 */       MessageUtil.sendMessage(player, "warning.player.no_inventory", Config.locale);
/*  849 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  854 */     PurchaseItemEvent event = new PurchaseItemEvent(listing.getWorld(), listing.getSeller_UUID(), buyer.getUniqueId().toString(), listing.getPrice(), listing.getItem());
/*  855 */     Bukkit.getPluginManager().callEvent((Event)event);
/*  856 */     if (event.isCancelled()) return false;
/*      */ 
/*      */     
/*  859 */     double tax = price * Config.auction_sales_tax / 100.0D;
/*  860 */     tax = Math.min(tax, Config.auction_max_sales_tax);
/*  861 */     double revenue = price - tax;
/*  862 */     boolean withdrawSuccess = Economy.withdrawPlayer(buyer, world, price);
/*  863 */     if (withdrawSuccess) {
/*  864 */       boolean depositSuccess = true;
/*  865 */       if (!type.isServer())
/*      */       {
/*  867 */         depositSuccess = Economy.depositPlayer(seller, world, revenue);
/*      */       }
/*  869 */       if (depositSuccess) {
/*  870 */         if (AuctionHouse.listings.soldListing(timestamp.longValue(), buyer)) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  875 */           ItemStack is = listing.getItem().clone();
/*      */           
/*  877 */           if (Config.replace_item_uuids) {
/*  878 */             String nbt = AuctionHouse.nms.getNBTString(is);
/*  879 */             if (nbt != null) {
/*  880 */               if (Config.debug) AuctionHouse.logger.info(String.format("nbt = %s", new Object[] { nbt })); 
/*  881 */               String seller_uuid = seller.getUniqueId().toString();
/*  882 */               if (Config.debug) AuctionHouse.logger.info(String.format("seller_uuid = %s", new Object[] { seller_uuid })); 
/*  883 */               String buyer_uuid = buyer.getUniqueId().toString();
/*  884 */               if (Config.debug) AuctionHouse.logger.info(String.format("buyer_uuid = %s", new Object[] { buyer_uuid })); 
/*  885 */               nbt = nbt.replace(seller_uuid, buyer_uuid);
/*  886 */               if (Config.debug) AuctionHouse.logger.info(String.format("nbt = %s", new Object[] { nbt })); 
/*  887 */               is = AuctionHouse.nms.setNBTString(is, nbt);
/*      */             } 
/*      */           } 
/*  890 */           if (Config.replace_player_names && 
/*  891 */             is != null) {
/*  892 */             String nbt = AuctionHouse.nms.getNBTString(is);
/*  893 */             if (nbt != null) {
/*  894 */               if (Config.debug) AuctionHouse.logger.info(String.format("nbt = %s", new Object[] { nbt })); 
/*  895 */               String str1 = seller.getName();
/*  896 */               String str2 = buyer.getName();
/*  897 */               if (str1 != null && str2 != null) {
/*  898 */                 if (Config.debug) AuctionHouse.logger.info(String.format("seller_name = %s", new Object[] { str1 })); 
/*  899 */                 if (Config.debug) AuctionHouse.logger.info(String.format("buyer_name = %s", new Object[] { str2 })); 
/*  900 */                 nbt = nbt.replace(str1, str2);
/*  901 */                 if (Config.debug) AuctionHouse.logger.info(String.format("nbt = %s", new Object[] { nbt })); 
/*  902 */                 is = AuctionHouse.nms.setNBTString(is, nbt);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  910 */           if (is != null) {
/*  911 */             if (Config.drop_at_feet) {
/*  912 */               player.getWorld().dropItem(player.getLocation(), is);
/*      */             } else {
/*      */               
/*  915 */               Map<Integer, ItemStack> remaining = player.getInventory().addItem(new ItemStack[] { is });
/*  916 */               for (Integer index : remaining.keySet()) {
/*  917 */                 player.getWorld().dropItem(player.getLocation(), remaining.get(index));
/*      */               }
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*  923 */           updateCounts(player);
/*      */ 
/*      */ 
/*      */           
/*  927 */           String item_name = MessageUtil.sectionSymbol(ItemUtil.getTranslatableName(is));
/*  928 */           MessageUtil.sendMessage(player, "message.purchase.success", Config.locale, new Object[] { Integer.valueOf(amount), item_name, Float.valueOf(price) });
/*  929 */           String buyer_name = buyer.getName();
/*  930 */           String seller_name = type.isServer() ? AuctionHouse.servername : seller.getName();
/*  931 */           double buyer_balance = Economy.getBalance(buyer, world);
/*  932 */           MessageUtil.sendMessage(player, "message.purchase.buyer_balance", Config.locale, new Object[] { Double.valueOf(buyer_balance) });
/*      */ 
/*      */           
/*  935 */           if (!type.isServer() && seller.isOnline()) {
/*  936 */             Player p = seller.getPlayer();
/*  937 */             if (p != null) {
/*  938 */               double seller_balance = Economy.getBalance(seller, world);
/*  939 */               SoundUtil.soldSound(p);
/*  940 */               MessageUtil.sendMessage(listing.getWorld(), p, "message.purchase.notify", Config.locale, new Object[] { buyer_name, Integer.valueOf(amount), item_name });
/*  941 */               MessageUtil.sendMessage(listing.getWorld(), p, "message.purchase.earnings", Config.locale, new Object[] { Double.valueOf(revenue), (revenue < price) ? LocaleStorage.translate("message.purchase.after_taxes", Config.locale) : "" });
/*  942 */               MessageUtil.sendMessage(listing.getWorld(), p, "message.purchase.seller_balance", Config.locale, new Object[] { Double.valueOf(seller_balance) });
/*  943 */               if (Config.announce_action_bar_purchases) {
/*  944 */                 long delay = 0L;
/*  945 */                 MessageUtil.sendActionBar(listing.getWorld(), p, Long.valueOf(delay), "message.purchase.notify", Config.locale, new Object[] { buyer_name, Integer.valueOf(amount), item_name });
/*  946 */                 delay += 20L;
/*  947 */                 MessageUtil.sendActionBar(listing.getWorld(), p, Long.valueOf(delay), "message.purchase.notify", Config.locale, new Object[] { buyer_name, Integer.valueOf(amount), item_name });
/*  948 */                 delay += 40L;
/*  949 */                 MessageUtil.clearActionBar(listing.getWorld(), p, Long.valueOf(delay));
/*  950 */                 delay += 5L;
/*  951 */                 MessageUtil.sendActionBar(listing.getWorld(), p, Long.valueOf(delay), "message.purchase.earnings", Config.locale, new Object[] { Double.valueOf(revenue), (revenue < price) ? LocaleStorage.translate("message.purchase.after_taxes", Config.locale) : "" });
/*  952 */                 delay += 40L;
/*  953 */                 MessageUtil.clearActionBar(listing.getWorld(), p, Long.valueOf(delay));
/*  954 */                 delay += 5L;
/*  955 */                 MessageUtil.sendActionBar(listing.getWorld(), p, Long.valueOf(delay), "message.purchase.seller_balance", Config.locale, new Object[] { Double.valueOf(seller_balance) });
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  960 */           for (Player p : AuctionHouse.plugin.getServer().getOnlinePlayers()) {
/*  961 */             if (p != player && (!seller.isOnline() || p != seller.getPlayer())) {
/*  962 */               if (Config.announce_chat_purchases) MessageUtil.sendMessage(listing.getWorld(), p, "message.sell.player.item_sold", Config.locale, new Object[] { seller_name, Integer.valueOf(amount), item_name, buyer_name, Float.valueOf(price) }); 
/*  963 */               if (Config.announce_action_bar_purchases) MessageUtil.sendActionBar(listing.getWorld(), p, Long.valueOf(0L), "message.sell.player.item_sold", Config.locale, new Object[] { seller_name, Integer.valueOf(amount), item_name, buyer_name, Float.valueOf(price) }); 
/*      */             } 
/*      */           } 
/*  966 */           if (Config.announce_discord_purchases) {
/*  967 */             MessageUtil.discordMessage("discord.sell.player.item_sold", Config.locale, new Object[] { seller_name, Integer.valueOf(amount), MessageUtil.nocolor(item_name), buyer_name, Float.valueOf(price) });
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  973 */           AuctionItemEvent auctionItemEvent = new AuctionItemEvent(ItemAction.ITEM_SOLD, listing.getWorld(), listing.getSeller_UUID(), buyer.getUniqueId().toString(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/*  974 */           Bukkit.getPluginManager().callEvent((Event)auctionItemEvent);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  979 */           if (Config.debug) AuctionHouse.logger.info("Warning! AuctionHouse could not get a timestamp"); 
/*  980 */           Economy.depositPlayer(buyer, world, price);
/*  981 */           if (!type.isServer()) Economy.withdrawPlayer(seller, world, price); 
/*  982 */           MessageUtil.sendMessage(player, "warning.purchase.problem", Config.locale);
/*  983 */           return false;
/*      */         } 
/*      */       } else {
/*  986 */         Economy.depositPlayer(buyer, world, price);
/*  987 */         MessageUtil.sendMessage(player, "warning.purchase.problem", Config.locale);
/*  988 */         return false;
/*      */       } 
/*      */     } else {
/*  991 */       MessageUtil.sendMessage(player, "warning.purchase.problem", Config.locale);
/*  992 */       return false;
/*      */     } 
/*  994 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clearSoldItems(Player player) {
/*  999 */     Listings soldItems = AuctionHouse.listings.getSoldItemsChrono(player, true);
/* 1000 */     if (soldItems == null) {
/* 1001 */       MessageUtil.sendMessage(player, "warning.sold_items.none", Config.locale);
/*      */       return;
/*      */     } 
/* 1004 */     AuctionHouse.listings.removeListings(soldItems.getListings().keySet());
/* 1005 */     updateCounts(player);
/* 1006 */     MessageUtil.sendMessage(player, "warning.sold_items.cleared", Config.locale);
/*      */   }
/*      */   
/*      */   public static boolean expireItem(Player player, Long timestamp) {
/* 1010 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/* 1011 */     if (listing != null && 
/* 1012 */       listing.getBuyer() == null) {
/* 1013 */       OfflinePlayer seller = listing.getSeller();
/* 1014 */       AuctionHouse.listings.cancelListing(timestamp);
/* 1015 */       updateCounts(player);
/*      */ 
/*      */ 
/*      */       
/* 1019 */       AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_CANCELLED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1020 */       Bukkit.getPluginManager().callEvent((Event)event);
/* 1021 */       return true;
/*      */     } 
/*      */     
/* 1024 */     MessageUtil.sendMessage(player, "warning.cancel.failed", Config.locale);
/* 1025 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean cancelItem(Player player, Long timestamp) {
/* 1030 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/* 1031 */     if (listing != null && 
/* 1032 */       listing.getBuyer() == null && 
/* 1033 */       returnItem(player, timestamp)) {
/*      */ 
/*      */ 
/*      */       
/* 1037 */       AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_CANCELLED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1038 */       Bukkit.getPluginManager().callEvent((Event)event);
/* 1039 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1043 */     MessageUtil.sendMessage(player, "warning.cancel.failed", Config.locale);
/* 1044 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void cancelAllItems(Player player) {
/* 1049 */     updateCounts(player);
/* 1050 */     if (getPlayerListingsCount((OfflinePlayer)player) == 0) {
/* 1051 */       MessageUtil.sendMessage(player, "message.cancel_all.none", Config.locale);
/*      */       return;
/*      */     } 
/* 1054 */     boolean success = true;
/* 1055 */     Listings playerListings = AuctionHouse.listings.getPlayerListingsChrono(player, true);
/* 1056 */     Map<Long, Listing> active = playerListings.getListings();
/* 1057 */     for (Long timestamp : active.keySet()) {
/* 1058 */       Listing listing = active.get(timestamp);
/* 1059 */       if (listing != null && 
/* 1060 */         listing.getBuyer() == null) {
/* 1061 */         if (returnItem(player, timestamp)) {
/*      */ 
/*      */ 
/*      */           
/* 1065 */           AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_CANCELLED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1066 */           Bukkit.getPluginManager().callEvent((Event)event); continue;
/*      */         } 
/* 1068 */         success = false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1073 */     if (success) {
/* 1074 */       MessageUtil.sendMessage(player, "message.cancel_all.success", Config.locale);
/*      */     } else {
/* 1076 */       MessageUtil.sendMessage(player, "warning.cancel_all.failed", Config.locale);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean returnItem(Player player, Long timestamp) {
/* 1082 */     Listing listing = AuctionHouse.listings.getListing(timestamp.longValue());
/* 1083 */     if (listing != null && 
/* 1084 */       listing.getBuyer() == null) {
/* 1085 */       ItemStack item = listing.getItem().clone();
/* 1086 */       if (AuctionHouse.listings.removeListing(timestamp.longValue())) {
/*      */         
/* 1088 */         givePlayerItem(Objects.<Player>requireNonNull(player), item);
/* 1089 */         updateCounts(player);
/*      */ 
/*      */ 
/*      */         
/* 1093 */         AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_RETURNED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1094 */         Bukkit.getPluginManager().callEvent((Event)event);
/* 1095 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1099 */     return false;
/*      */   }
/*      */   
/*      */   public static void returnAllItems(Player player) {
/* 1103 */     updateCounts(player);
/* 1104 */     if (getExpiredListingsCount((OfflinePlayer)player) == 0) {
/* 1105 */       MessageUtil.sendMessage(player, "message.return_all.none", Config.locale);
/*      */       return;
/*      */     } 
/* 1108 */     boolean success = true;
/* 1109 */     Listings expiredListings = AuctionHouse.listings.getExpiredListingsChrono(player, true);
/* 1110 */     Map<Long, Listing> active = expiredListings.getListings();
/* 1111 */     for (Long timestamp : active.keySet()) {
/* 1112 */       Listing listing = active.get(timestamp);
/* 1113 */       if (listing != null && 
/* 1114 */         listing.getBuyer() == null) {
/* 1115 */         if (returnItem(player, timestamp)) {
/*      */ 
/*      */ 
/*      */           
/* 1119 */           AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_RETURNED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1120 */           Bukkit.getPluginManager().callEvent((Event)event); continue;
/*      */         } 
/* 1122 */         success = false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1127 */     if (success) {
/* 1128 */       MessageUtil.sendMessage(player, "message.return_all.success", Config.locale);
/*      */     } else {
/* 1130 */       MessageUtil.sendMessage(player, "warning.return_all.failed", Config.locale);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void givePlayerItem(Player player, ItemStack drop) {
/* 1136 */     if (Config.drop_at_feet) {
/* 1137 */       player.getWorld().dropItem(player.getLocation(), drop);
/*      */     } else {
/* 1139 */       Map<Integer, ItemStack> remaining = player.getInventory().addItem(new ItemStack[] { drop });
/* 1140 */       for (Integer index : remaining.keySet()) {
/* 1141 */         player.getWorld().dropItem(player.getLocation(), remaining.get(index));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkExpiredItems(Player player) {
/* 1148 */     int expired_count = getExpiredListingsCount((OfflinePlayer)player);
/* 1149 */     if (expired_count > 0) {
/* 1150 */       MessageUtil.sendMessage(player, "message.expired_listings.notice1", Config.locale);
/* 1151 */       MessageUtil.sendMessage(player, "message.expired_listings.notice2", Config.locale);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Player> checkUnclaimedItems() {
/* 1158 */     List<Player> players = new ArrayList<>();
/* 1159 */     Listings unclaimedListings = AuctionHouse.listings.getUnclaimedListings();
/* 1160 */     if (unclaimedListings.count() == 0) return players; 
/* 1161 */     Map<Long, Listing> map = unclaimedListings.getListings();
/* 1162 */     for (Long timestamp : map.keySet()) {
/* 1163 */       Listing listing = map.get(timestamp);
/* 1164 */       OfflinePlayer op = listing.getSeller();
/* 1165 */       if (op.isOnline()) {
/* 1166 */         Player player = op.getPlayer();
/* 1167 */         if (!players.contains(player)) players.add(player); 
/*      */       } 
/*      */     } 
/* 1170 */     return players;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void returnUnclaimedItems(Player player) {
/* 1176 */     Listings unclaimedListings = AuctionHouse.listings.getUnclaimedListings(player);
/* 1177 */     if (unclaimedListings.count() == 0)
/* 1178 */       return;  Map<Long, Listing> map = unclaimedListings.getListings();
/* 1179 */     boolean nospace = false;
/* 1180 */     for (Long timestamp : map.keySet()) {
/* 1181 */       Listing listing = map.get(timestamp);
/* 1182 */       ItemStack item = listing.getItem();
/* 1183 */       if (canGiveItem(player, item)) {
/* 1184 */         if (AuctionHouse.listings.removeListing(timestamp.longValue())) {
/*      */           
/* 1186 */           givePlayerItem(player, item);
/*      */ 
/*      */ 
/*      */           
/* 1190 */           AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_RETURNED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1191 */           Bukkit.getPluginManager().callEvent((Event)event);
/*      */         }  continue;
/*      */       } 
/* 1194 */       nospace = true;
/*      */     } 
/*      */     
/* 1197 */     if (nospace) {
/* 1198 */       MessageUtil.sendMessage(player, "warning.player.no_inventory", Config.locale);
/*      */     }
/* 1200 */     SoundUtil.dropSound(player);
/* 1201 */     MessageUtil.sendMessage(player, "warning.unclaimed_listings.notice", Config.locale);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int deleteAbandonedItems() {
/* 1206 */     int count = 0;
/* 1207 */     Listings abandonedListings = AuctionHouse.listings.getAbandonedListings();
/* 1208 */     if (abandonedListings.count() == 0) return count; 
/* 1209 */     Map<Long, Listing> map = abandonedListings.getListings();
/* 1210 */     Set<Long> marked = new HashSet<>();
/* 1211 */     for (Long timestamp : map.keySet()) {
/* 1212 */       Listing listing = map.get(timestamp);
/* 1213 */       if (listing != null) {
/* 1214 */         ListingType type = listing.getType();
/* 1215 */         ItemStack item = listing.getItem();
/* 1216 */         if (item != null) {
/* 1217 */           int amount = item.getAmount();
/* 1218 */           String name = item.getType().getKey().getKey();
/* 1219 */           String seller_uuid = listing.getSeller_UUID();
/* 1220 */           OfflinePlayer op = listing.getSeller();
/* 1221 */           String seller = type.isServer() ? AuctionHouse.servername : ((op != null) ? op.getName() : seller_uuid);
/* 1222 */           if (Config.debug) AuctionHouse.logger.info(String.format("Deleted %dx %s abandoned by %s", new Object[] { Integer.valueOf(amount), name, seller })); 
/* 1223 */           marked.add(timestamp);
/* 1224 */           count++;
/*      */ 
/*      */ 
/*      */           
/* 1228 */           AuctionItemEvent event = new AuctionItemEvent(ItemAction.ITEM_PURGED, listing.getWorld(), listing.getSeller_UUID(), listing.getBuyer_UUID(), listing.getBidder_UUID(), listing.getPrice(), listing.getReserve(), listing.getBid(), listing.getType(), listing.getItem());
/* 1229 */           Bukkit.getPluginManager().callEvent((Event)event);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1234 */     AuctionHouse.listings.removeListings(marked);
/* 1235 */     return count;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int deleteExpiredSoldItems() {
/* 1240 */     int count = 0;
/* 1241 */     Listings allSoldItems = AuctionHouse.listings.getExpiredSoldItems();
/* 1242 */     if (allSoldItems.count() == 0) return count; 
/* 1243 */     Map<Long, Listing> map = allSoldItems.getListings();
/* 1244 */     Set<Long> marked = new HashSet<>();
/* 1245 */     for (Long timestamp : map.keySet()) {
/* 1246 */       Listing listing = map.get(timestamp);
/* 1247 */       ListingType type = listing.getType();
/* 1248 */       ItemStack item = listing.getItem();
/* 1249 */       int amount = item.getAmount();
/* 1250 */       String name = item.getType().getKey().getKey();
/* 1251 */       String seller = type.isServer() ? AuctionHouse.servername : ((listing.getSeller() != null) ? listing.getSeller().getName() : listing.getSeller_UUID());
/* 1252 */       if (Config.debug) AuctionHouse.logger.info(String.format("Deleted expired sold item %dx %s by %s", new Object[] { Integer.valueOf(amount), name, seller })); 
/* 1253 */       marked.add(timestamp);
/* 1254 */       count++;
/*      */     } 
/* 1256 */     AuctionHouse.listings.removeListings(marked);
/* 1257 */     return count;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int purgeAllItems(OfflinePlayer seller) {
/* 1262 */     int count = 0;
/* 1263 */     Map<Long, Listing> map = AuctionHouse.listings.getListings();
/* 1264 */     Set<Long> marked = new HashSet<>();
/* 1265 */     for (Long timestamp : map.keySet()) {
/* 1266 */       Listing listing = map.get(timestamp);
/* 1267 */       if (listing.getSeller() == seller) {
/* 1268 */         marked.add(timestamp);
/* 1269 */         count++;
/*      */       } 
/*      */     } 
/* 1272 */     AuctionHouse.listings.removeListings(marked);
/* 1273 */     return count;
/*      */   }
/*      */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\modules\Auctions.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */