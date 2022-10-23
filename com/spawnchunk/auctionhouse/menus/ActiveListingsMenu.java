/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.MenuClickEvent;
/*     */ import com.spawnchunk.auctionhouse.events.PrePurchaseItemEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ServerTickEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.Listing;
/*     */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*     */ import com.spawnchunk.auctionhouse.modules.Listings;
/*     */ import com.spawnchunk.auctionhouse.modules.SortOrder;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.PlayerUtil;
/*     */ import com.spawnchunk.auctionhouse.util.SoundUtil;
/*     */ import com.spawnchunk.auctionhouse.util.TimeUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.ConsoleCommandSender;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class ActiveListingsMenu implements Listener {
/*     */   private final String id;
/*  45 */   private SortOrder sortOrder = Config.auction_sort_order; private final Menu menu;
/*     */   private int activeListingsCount;
/*     */   private int playerListingsCount;
/*     */   private int soldItemsCount;
/*     */   private int current_page;
/*     */   private String filter;
/*     */   private boolean menu_mode;
/*  52 */   private List<Long> page_keys = new ArrayList<>();
/*  53 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*  54 */   private double playerBalance = 0.0D;
/*     */   
/*  56 */   final String active_listings_title = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.title", Config.locale));
/*  57 */   final String menu_exit = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.exit", Config.locale));
/*  58 */   final String menu_previous = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.page.previous", Config.locale));
/*  59 */   final String menu_next = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.page.next", Config.locale));
/*  60 */   final String menu_info = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.info", Config.locale));
/*  61 */   final String menu_howto_sell = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.howto_sell", Config.locale));
/*  62 */   final String menu_sort_listings = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.sort_listings", Config.locale));
/*  63 */   final String menu_player_listings = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.button.title", Config.locale));
/*  64 */   final String player_listings_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.button.desc1", Config.locale));
/*  65 */   final String player_listings_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.player_listings.button.desc2", Config.locale));
/*  66 */   final String player_listings_none = MessageUtil.sectionSymbol(LocaleStorage.translate("warning.player_listings.none", Config.locale));
/*  67 */   final String listings_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.listings.value", Config.locale));
/*  68 */   final String balance_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.balance.value", Config.locale));
/*  69 */   final String order_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.order.value", Config.locale));
/*  70 */   final String howto_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.howto.desc1", Config.locale));
/*  71 */   final String howto_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.howto.desc2", Config.locale));
/*  72 */   final String info_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.info.desc1", Config.locale));
/*  73 */   final String info_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.info.desc2", Config.locale));
/*  74 */   final String info_desc3 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.info.desc3", Config.locale));
/*  75 */   final String info_desc4 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.info.desc4", Config.locale));
/*  76 */   final String info_desc5 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.info.desc5", Config.locale));
/*  77 */   final String info_desc6 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.info.desc6", Config.locale));
/*     */   
/*  79 */   final String top_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*  80 */   final String click = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.click", Config.locale));
/*  81 */   final String unavailable = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.unavailable", Config.locale));
/*  82 */   final String unaffordable = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.unaffordable", Config.locale));
/*  83 */   final String top_spacing = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.spacing.top", Config.locale));
/*  84 */   final String price_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.price.value", Config.locale));
/*  85 */   final String seller_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.seller.value", Config.locale));
/*  86 */   final String expire_key = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.expire.key", Config.locale));
/*  87 */   final String expire_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.expire.value", Config.locale));
/*  88 */   final String item_expired = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.item_expired", Config.locale));
/*  89 */   final String bottom_spacing = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.spacing.bottom", Config.locale));
/*  90 */   final String shift_left_click = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.shift_left_click", Config.locale));
/*  91 */   final String shift_right_click = MessageUtil.sectionSymbol(LocaleStorage.translate("message.active_listings.shift_right_click", Config.locale));
/*  92 */   final String repair_cost = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.repair_cost", Config.locale));
/*  93 */   final String bottom_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*     */   
/*     */   public ActiveListingsMenu(Player player, boolean menu_mode) {
/*  96 */     this.menu_mode = menu_mode;
/*  97 */     UUID uuid = player.getUniqueId();
/*  98 */     String title = MessageUtil.sectionSymbol(this.active_listings_title);
/*  99 */     if (title.isEmpty()) {
/* 100 */       title = "Auction House";
/*     */     }
/* 102 */     this.id = AuctionHouse.menuManager.createMenu(uuid, title, 54);
/* 103 */     this.menu = AuctionHouse.menuManager.getMenu(this.id);
/* 104 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)AuctionHouse.plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 109 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 110 */     if (menu == null)
/*     */       return; 
/* 112 */     this.sortOrder = Config.auction_sort_order;
/*     */     
/* 114 */     for (int slot = 0; slot < 45; ) { this.tags.put(Integer.valueOf(slot), ItemTag.ITEM); slot++; }
/* 115 */      if (this.menu_mode) {
/* 116 */       this.tags.put(Integer.valueOf(45), ItemTag.EXIT);
/* 117 */       this.tags.put(Integer.valueOf(46), ItemTag.PLAYER_LISTINGS);
/*     */     } else {
/* 119 */       this.tags.put(Integer.valueOf(45), ItemTag.PLAYER_LISTINGS);
/* 120 */       this.tags.remove(Integer.valueOf(46));
/*     */     } 
/* 122 */     this.tags.put(Integer.valueOf(48), ItemTag.PREVIOUS);
/* 123 */     this.tags.put(Integer.valueOf(49), ItemTag.SORT_LISTINGS);
/* 124 */     this.tags.put(Integer.valueOf(50), ItemTag.NEXT);
/*     */     
/* 126 */     MenuItem blank = new MenuItem(null, "minecraft:air", 1, null);
/* 127 */     MenuItem exit = new MenuItem(this.menu_exit, Config.exit_button, 1, null);
/*     */     
/* 129 */     MenuItem player_listings = new MenuItem(this.menu_player_listings, Config.player_listings_button, 1, MessageUtil.expand(Arrays.asList(new String[] {
/*     */ 
/*     */               
/* 132 */               this.player_listings_desc1, this.player_listings_desc2, MessageUtil.populate(this.listings_value, new Object[] { Integer.valueOf(0)
/* 133 */                 }), MessageUtil.populate(this.balance_value, new Object[] { Integer.valueOf(0) }) })));
/* 134 */     String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/*     */     
/* 136 */     MenuItem sort_listings = new MenuItem(this.menu_sort_listings, Config.sort_listings_button, 1, MessageUtil.expand(Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }))));
/*     */     
/* 138 */     MenuItem howto = new MenuItem(this.menu_howto_sell, Config.howto_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.howto_desc1, this.howto_desc2 })));
/*     */ 
/*     */ 
/*     */     
/* 142 */     MenuItem info = new MenuItem(this.menu_info, Config.info_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.info_desc1, this.info_desc2, this.info_desc3, this.info_desc4, this.info_desc5, this.info_desc6 })));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (this.menu_mode) {
/* 151 */       menu.setItem(45, exit);
/* 152 */       menu.setItem(46, player_listings);
/*     */     } else {
/* 154 */       menu.setItem(45, player_listings);
/* 155 */       menu.setItem(46, blank);
/*     */     } 
/* 157 */     menu.setItem(49, sort_listings);
/* 158 */     menu.setItem(52, howto);
/* 159 */     menu.setItem(53, info);
/*     */   }
/*     */   
/*     */   public void show(final Player player, boolean menu_mode, String filter, final int page) {
/* 163 */     this.filter = filter;
/* 164 */     this.menu_mode = menu_mode;
/* 165 */     initialize();
/* 166 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 169 */           ActiveListingsMenu.this.paginate_task(player, page);
/*     */         }
/* 171 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void paginate_task(final Player player, int page) {
/* 175 */     this.current_page = page;
/* 176 */     paginate(player);
/* 177 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 180 */           ActiveListingsMenu.this.build_task(player);
/*     */         }
/* 182 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void build_task(final Player player) {
/* 186 */     build(player, this.current_page);
/*     */     
/* 188 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 191 */           ActiveListingsMenu.this.open_task(player);
/*     */         }
/* 193 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void open_task(Player player) {
/* 197 */     UUID uuid = player.getUniqueId();
/* 198 */     AuctionHouse.menuManager.openMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   public void close(Player player) {
/* 202 */     UUID uuid = player.getUniqueId();
/* 203 */     AuctionHouse.menuManager.closeMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   private int paginate(Player player) {
/* 207 */     Listings filteredListings = (this.filter == null) ? AuctionHouse.listings.getActiveListings(player, this.sortOrder) : AuctionHouse.listings.getFilteredListings(player, this.filter, this.sortOrder);
/* 208 */     this.activeListingsCount = filteredListings.count();
/* 209 */     Map<Long, Listing> map = filteredListings.getListings();
/* 210 */     this.page_keys = new ArrayList<>(map.keySet());
/* 211 */     int keys = this.page_keys.size();
/* 212 */     return Math.floorDiv(Math.max(keys - 1, 0), 45);
/*     */   }
/*     */ 
/*     */   
/*     */   private void build(Player player, int page) {
/* 217 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 218 */     String playerName = player.getName();
/* 219 */     this.playerBalance = PlayerUtil.getPlayerBalance(player, player.getWorld().getName());
/* 220 */     if (menu == null)
/* 221 */       return;  int size = this.page_keys.size();
/* 222 */     long now = TimeUtil.now();
/* 223 */     int pages = Math.floorDiv(Math.max(size - 1, 0), 45);
/* 224 */     if (this.menu_mode) {
/* 225 */       this.tags.put(Integer.valueOf(45), ItemTag.EXIT);
/* 226 */       this.tags.put(Integer.valueOf(46), ItemTag.PLAYER_LISTINGS);
/*     */     } else {
/* 228 */       this.tags.put(Integer.valueOf(45), ItemTag.PLAYER_LISTINGS);
/* 229 */       this.tags.remove(Integer.valueOf(46));
/*     */     } 
/* 231 */     MenuItem exit = new MenuItem(this.menu_exit, Config.exit_button, 1, null);
/*     */     
/* 233 */     MenuItem player_listings = new MenuItem(this.menu_player_listings, Config.player_listings_button, 1, MessageUtil.expand(Arrays.asList(new String[] {
/*     */ 
/*     */               
/* 236 */               this.player_listings_desc1, this.player_listings_desc2, MessageUtil.populate(this.listings_value, new Object[] { Integer.valueOf(0)
/* 237 */                 }), MessageUtil.populate(this.balance_value, new Object[] { Integer.valueOf(0) }) })));
/* 238 */     MenuItem blank = new MenuItem(null, "minecraft:air", 1, null);
/* 239 */     MenuItem previous = new MenuItem(this.menu_previous, Config.previous_button, 1, null);
/* 240 */     MenuItem next = new MenuItem(this.menu_next, Config.next_button, 1, null);
/* 241 */     Listings filteredListings = (this.filter == null) ? AuctionHouse.listings.getActiveListings(player, this.sortOrder) : AuctionHouse.listings.getFilteredListings(player, this.filter, this.sortOrder);
/* 242 */     int offset = page * 45;
/* 243 */     for (int i = 0; i < 45; i++) {
/* 244 */       int index = offset + i;
/* 245 */       if (index < size) {
/* 246 */         long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 247 */         boolean expires = (timestamp < Config.auction_future_duration);
/* 248 */         long remaining = timestamp - now;
/* 249 */         Listing listing = filteredListings.getListing(timestamp);
/* 250 */         if (listing != null) {
/* 251 */           ItemStack is = listing.getItem();
/* 252 */           if (is != null) {
/* 253 */             String name = ItemUtil.getCustomName(is);
/* 254 */             Material material = is.getType();
/* 255 */             String key = material.getKey().toString();
/* 256 */             int amount = is.getAmount();
/* 257 */             ItemMeta meta = is.getItemMeta();
/* 258 */             LinkedList<String> lore = new LinkedList<>();
/* 259 */             if (meta != null) {
/* 260 */               if (meta.hasLore() && meta.getLore() != null) lore.addAll(meta.getLore()); 
/* 261 */               if (AuctionHouse.nms.isContainer(is)) {
/*     */                 
/* 263 */                 Map<Integer, ItemStack> items = AuctionHouse.nms.getContainerItems(is);
/* 264 */                 Map<ItemStack, Integer> item_counts = new HashMap<>();
/* 265 */                 for (Integer slot : items.keySet()) {
/* 266 */                   ItemStack itemStack = items.get(slot);
/* 267 */                   int count = itemStack.getAmount();
/* 268 */                   itemStack.setAmount(0);
/* 269 */                   if (item_counts.containsKey(itemStack)) {
/* 270 */                     item_counts.put(itemStack, Integer.valueOf(((Integer)item_counts.getOrDefault(itemStack, Integer.valueOf(0))).intValue() + count)); continue;
/*     */                   } 
/* 272 */                   item_counts.put(itemStack, Integer.valueOf(count));
/*     */                 } 
/*     */                 
/* 275 */                 for (ItemStack itemStack : item_counts.keySet()) {
/* 276 */                   String item_name = ItemUtil.getItemName(itemStack);
/* 277 */                   Integer item_count = item_counts.get(itemStack);
/* 278 */                   lore.addFirst(String.format("§e%d§7x %s", new Object[] { item_count, item_name }));
/*     */                 }
/*     */               
/* 281 */               } else if (material == Material.SPAWNER && Config.spawner_info) {
/* 282 */                 List<String> mobs = AuctionHouse.nms.getMobs(is);
/* 283 */                 List<String> mob_lore = new ArrayList<>();
/* 284 */                 if (!mobs.isEmpty()) {
/* 285 */                   for (String mob : mobs) {
/* 286 */                     mob_lore.add(String.format("§r§9%s", new Object[] { MessageUtil.readable(mob.replace("minecraft:", "")) }));
/*     */                   } 
/*     */                 }
/* 289 */                 lore.addAll(0, mob_lore);
/*     */               } 
/*     */             } 
/* 292 */             String nbt = AuctionHouse.nms.getNBTString(is);
/* 293 */             double price = listing.getPrice();
/* 294 */             ListingType type = listing.getType();
/* 295 */             String seller = type.isServer() ? AuctionHouse.servername : ((listing.getSeller() != null) ? listing.getSellerName() : listing.getSeller_UUID());
/* 296 */             List<String> desc = new ArrayList<>();
/* 297 */             desc.add(this.top_rule);
/* 298 */             if (seller != null && !seller.equals(playerName)) {
/* 299 */               if (this.playerBalance >= price) {
/* 300 */                 desc.add(this.click);
/*     */               } else {
/* 302 */                 desc.add(this.unaffordable);
/*     */               } 
/*     */             } else {
/* 305 */               desc.add(this.unavailable);
/*     */             } 
/* 307 */             desc.add(this.top_spacing);
/* 308 */             desc.add(MessageUtil.populate(this.price_value, new Object[] { Double.valueOf(price) }));
/* 309 */             desc.add(MessageUtil.populate(this.seller_value, new Object[] { seller }));
/* 310 */             if (expires)
/* 311 */               desc.add((remaining > 0L) ? MessageUtil.populate(this.expire_value, new Object[] { TimeUtil.duration(remaining, Config.show_seconds) }) : this.item_expired); 
/* 312 */             desc.add(this.bottom_spacing);
/* 313 */             if (player.hasPermission("auctionhouse.cancel.others") || player.isOp()) {
/* 314 */               desc.add(this.shift_left_click);
/*     */             }
/* 316 */             if ((player.hasPermission("auctionhouse.expire.others") || player.isOp()) && 
/* 317 */               !type.isServer()) {
/* 318 */               desc.add(this.shift_right_click);
/*     */             }
/*     */             
/* 321 */             desc.add(this.bottom_rule);
/* 322 */             if (Config.show_repair_cost) {
/* 323 */               int rc = ItemUtil.getRepairCost(is);
/* 324 */               if (rc >= 0) desc.add(MessageUtil.populate(this.repair_cost, new Object[] { Integer.valueOf(rc) })); 
/*     */             } 
/* 326 */             for (String s : MessageUtil.expand(desc)) {
/* 327 */               if (!s.isEmpty()) lore.add(s); 
/*     */             } 
/* 329 */             MenuItem menuItem = new MenuItem(name, key, amount, lore, nbt);
/* 330 */             menu.setItem(i, menuItem);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 334 */         menu.setItem(i, blank);
/*     */       } 
/*     */     } 
/* 337 */     if (this.menu_mode) {
/* 338 */       menu.setItem(45, exit);
/* 339 */       menu.setItem(46, player_listings);
/*     */     } else {
/* 341 */       menu.setItem(45, player_listings);
/* 342 */       menu.setItem(46, blank);
/*     */     } 
/* 344 */     menu.setItem(48, (this.current_page > 0) ? previous : blank);
/* 345 */     menu.setItem(50, (this.current_page < pages) ? next : blank);
/*     */     
/* 347 */     Auctions.updateCounts(player);
/* 348 */     int listed = Auctions.getPlayerListingsCount((OfflinePlayer)player);
/* 349 */     updatePlayerListingsButton(menu, player, listed, this.playerBalance);
/*     */   }
/*     */   
/*     */   private void updatePlayerListingsButton(Menu menu, Player player, int listed, double playerBalance) {
/* 353 */     this.playerListingsCount = listed;
/* 354 */     List<String> lore = Arrays.asList(new String[] { this.player_listings_desc1, this.player_listings_desc2, 
/*     */ 
/*     */           
/* 357 */           (listed > 0) ? MessageUtil.populate(this.listings_value, new Object[] { Integer.valueOf(listed) }) : this.player_listings_none, 
/* 358 */           MessageUtil.populate(this.balance_value, new Object[] { Double.valueOf(playerBalance) }) });
/* 359 */     MenuItem player_listings = new MenuItem(player, this.menu_player_listings, Config.player_listings_button, 1, MessageUtil.expand(lore));
/* 360 */     menu.setItem(this.menu_mode ? 46 : 45, player_listings);
/* 361 */     menu.setItemAmount(this.menu_mode ? 46 : 45, 1);
/*     */   }
/*     */   
/*     */   private void update(Player player) {
/* 365 */     if (this.menu == null)
/*     */       return; 
/* 367 */     String playerName = player.getName();
/*     */ 
/*     */     
/* 370 */     double balance = PlayerUtil.getPlayerBalance(player, player.getWorld().getName());
/*     */     
/* 372 */     Auctions.updateCounts(player);
/* 373 */     int count = Auctions.getActiveListingsCount();
/* 374 */     int listed = Auctions.getPlayerListingsCount((OfflinePlayer)player);
/* 375 */     int sold = Auctions.getSoldItemsCount((OfflinePlayer)player);
/*     */ 
/*     */     
/* 378 */     if (this.activeListingsCount != count) {
/* 379 */       this.activeListingsCount = count;
/*     */ 
/*     */       
/* 382 */       int pages = paginate(player);
/* 383 */       if (this.current_page > pages) this.current_page = pages; 
/* 384 */       build(player, this.current_page);
/*     */     } 
/*     */     
/* 387 */     int size = this.page_keys.size();
/* 388 */     long now = TimeUtil.now();
/* 389 */     int offset = this.current_page * 45;
/* 390 */     for (int i = 0; i < 54; i++) {
/* 391 */       ItemTag tag = this.tags.get(Integer.valueOf(i));
/* 392 */       if (tag != null && tag.equals(ItemTag.ITEM)) {
/* 393 */         int index = offset + i;
/* 394 */         if (index < size) {
/* 395 */           long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 396 */           boolean expires = (timestamp < Config.auction_future_duration);
/* 397 */           long remaining = timestamp - now;
/* 398 */           Listing listing = AuctionHouse.listings.getListing(timestamp);
/* 399 */           if (listing != null) {
/* 400 */             ListingType type = listing.getType();
/* 401 */             OfflinePlayer seller = listing.getSeller();
/* 402 */             String sellerName = type.isServer() ? AuctionHouse.servername : ((seller != null) ? seller.getName() : listing.getSeller_UUID());
/* 403 */             float price = listing.getPrice();
/* 404 */             if (seller != null && sellerName != null && !sellerName.equals(playerName)) {
/* 405 */               if (balance >= price) {
/* 406 */                 this.menu.replaceItemLore(i, this.unaffordable, this.click);
/*     */               } else {
/* 408 */                 this.menu.replaceItemLore(i, this.click, this.unaffordable);
/*     */               } 
/*     */             }
/* 411 */             if (expires) {
/* 412 */               this.menu.replaceItemLore(i, this.expire_key, (remaining > 0L) ? MessageUtil.populate(this.expire_value, new Object[] { TimeUtil.duration(remaining, Config.show_seconds) }) : this.item_expired);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 418 */     if (this.playerListingsCount != listed || this.soldItemsCount != sold || this.playerBalance != balance) {
/* 419 */       this.playerListingsCount = listed;
/* 420 */       this.soldItemsCount = sold;
/* 421 */       this.playerBalance = balance;
/* 422 */       updatePlayerListingsButton(this.menu, player, listed, this.playerBalance);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClick(MenuClickEvent event) {
/* 429 */     Player player = event.getPlayer();
/* 430 */     String id = event.getId();
/* 431 */     if (player == null || id == null)
/* 432 */       return;  if (id.equals(this.id)) {
/* 433 */       Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 434 */       if (menu == null)
/* 435 */         return;  int slot = event.getSlot();
/* 436 */       ItemStack item = menu.getItem(slot);
/* 437 */       if (item == null)
/* 438 */         return;  if (item.getType().equals(Material.AIR))
/* 439 */         return;  MenuClickType click = event.getMenuClickType();
/*     */       
/* 441 */       if (PlayerUtil.spamCheck(player))
/*     */         return; 
/* 443 */       if (click == MenuClickType.SHIFT_LEFT && (
/* 444 */         player.hasPermission("auctionhouse.cancel.others") || player.isOp())) {
/*     */         
/* 446 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 447 */         if (tag != null && 
/* 448 */           tag.equals(ItemTag.ITEM)) {
/* 449 */           int index = this.current_page * 45 + slot;
/* 450 */           int size = this.page_keys.size();
/* 451 */           if (index < size && size > 0) {
/* 452 */             SoundUtil.clickSound(player);
/* 453 */             long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 454 */             Listing listing = AuctionHouse.listings.getListing(timestamp);
/* 455 */             ItemStack is = Auctions.getItem(Long.valueOf(timestamp));
/* 456 */             ListingType type = listing.getType();
/* 457 */             if (is != null) {
/* 458 */               OfflinePlayer buyer = Auctions.getBuyer(Long.valueOf(timestamp));
/* 459 */               if (buyer != null) {
/* 460 */                 MessageUtil.sendMessage(player, "warning.listing.expired", Config.locale);
/* 461 */                 SoundUtil.failSound(player);
/*     */               } else {
/* 463 */                 String name = MessageUtil.sectionSymbol(ItemUtil.getTranslatableName(is));
/* 464 */                 int count = is.getAmount();
/* 465 */                 OfflinePlayer seller = Auctions.getSeller(Long.valueOf(timestamp));
/* 466 */                 if (seller != null) {
/*     */                   
/* 468 */                   if (Auctions.cancelItem(player, Long.valueOf(timestamp))) {
/* 469 */                     update(player);
/* 470 */                     if (player == seller) {
/* 471 */                       MessageUtil.sendMessage(player, "message.cancel.success", Config.locale, new Object[] { player.getName(), Integer.valueOf(count), name });
/*     */                     } else {
/* 473 */                       MessageUtil.sendMessage(player, "message.cancel.success.other", Config.locale, new Object[] { seller.getName(), Integer.valueOf(count), name });
/* 474 */                       if (seller.isOnline()) {
/* 475 */                         MessageUtil.sendMessage(seller.getPlayer(), "message.cancel.admin", Config.locale, new Object[] { player.getName(), Integer.valueOf(count), name });
/*     */                       }
/*     */                     } 
/*     */                   } else {
/* 479 */                     SoundUtil.failSound(player);
/*     */                   }
/*     */                 
/*     */                 }
/* 483 */                 else if (Auctions.cancelItem(player, Long.valueOf(timestamp))) {
/* 484 */                   update(player);
/* 485 */                   MessageUtil.sendMessage(player, "message.cancel.success", Config.locale, new Object[] { AuctionHouse.servername, Integer.valueOf(count), name });
/*     */                 } else {
/* 487 */                   SoundUtil.failSound(player);
/*     */                 } 
/*     */               } 
/*     */             } else {
/*     */               
/* 492 */               SoundUtil.failSound(player);
/*     */             } 
/*     */           } else {
/* 495 */             SoundUtil.failSound(player);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 501 */       if (click == MenuClickType.SHIFT_RIGHT && (
/* 502 */         player.hasPermission("auctionhouse.expire.others") || player.isOp())) {
/*     */         
/* 504 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 505 */         if (tag != null && 
/* 506 */           tag.equals(ItemTag.ITEM)) {
/* 507 */           int index = this.current_page * 45 + slot;
/* 508 */           int size = this.page_keys.size();
/* 509 */           if (index < size && size > 0) {
/* 510 */             SoundUtil.clickSound(player);
/* 511 */             long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 512 */             Listing listing = AuctionHouse.listings.getListing(timestamp);
/* 513 */             ItemStack is = Auctions.getItem(Long.valueOf(timestamp));
/* 514 */             ListingType type = listing.getType();
/* 515 */             if (!type.isServer()) {
/* 516 */               if (is != null) {
/* 517 */                 OfflinePlayer buyer = Auctions.getBuyer(Long.valueOf(timestamp));
/* 518 */                 if (buyer != null) {
/* 519 */                   MessageUtil.sendMessage(player, "warning.listing.expired", Config.locale);
/* 520 */                   SoundUtil.failSound(player);
/*     */                 } else {
/* 522 */                   String name = MessageUtil.sectionSymbol(ItemUtil.getTranslatableName(is));
/* 523 */                   int count = is.getAmount();
/* 524 */                   OfflinePlayer seller = Auctions.getSeller(Long.valueOf(timestamp));
/* 525 */                   if (seller != null) {
/*     */                     
/* 527 */                     if (Auctions.expireItem(player, Long.valueOf(timestamp))) {
/* 528 */                       update(player);
/* 529 */                       if (player == seller) {
/* 530 */                         MessageUtil.sendMessage(player, "message.cancel.success", Config.locale, new Object[] { player.getName(), Integer.valueOf(count), name });
/*     */                       } else {
/* 532 */                         MessageUtil.sendMessage(player, "message.cancel.success.other", Config.locale, new Object[] { seller.getName(), Integer.valueOf(count), name });
/* 533 */                         if (seller.isOnline()) {
/* 534 */                           MessageUtil.sendMessage(seller.getPlayer(), "message.cancel.admin", Config.locale, new Object[] { player.getName(), Integer.valueOf(count), name });
/*     */                         }
/*     */                       } 
/*     */                     } else {
/* 538 */                       SoundUtil.failSound(player);
/*     */                     } 
/*     */                   } else {
/* 541 */                     SoundUtil.failSound(player);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 546 */               SoundUtil.failSound(player);
/*     */             } 
/*     */           } else {
/* 549 */             SoundUtil.failSound(player);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 555 */       if (click == MenuClickType.LEFT) {
/* 556 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 557 */         if (tag != null) {
/* 558 */           if (tag.equals(ItemTag.ITEM)) {
/* 559 */             SoundUtil.clickSound(player);
/* 560 */             int index = this.current_page * 45 + slot;
/* 561 */             if (index < this.page_keys.size() && this.page_keys.size() > 0) {
/* 562 */               long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 563 */               Listing listing = AuctionHouse.listings.getListing(timestamp);
/* 564 */               if (listing != null) {
/* 565 */                 ListingType type = listing.getType();
/* 566 */                 OfflinePlayer buyer = Auctions.getBuyer(Long.valueOf(timestamp));
/* 567 */                 if (buyer != null) {
/* 568 */                   MessageUtil.sendMessage(player, "warning.listing.expired", Config.locale);
/* 569 */                   SoundUtil.failSound(player);
/*     */                 } else {
/* 571 */                   OfflinePlayer seller = Auctions.getSeller(Long.valueOf(timestamp));
/* 572 */                   String sellerName = type.isServer() ? AuctionHouse.servername : ((seller != null) ? seller.getName() : listing.getSeller_UUID());
/* 573 */                   String playerName = player.getName();
/* 574 */                   if (seller != null && sellerName != null && (!seller.getUniqueId().equals(player.getUniqueId()) || type.isServer())) {
/* 575 */                     this.playerBalance = PlayerUtil.getPlayerBalance(player, player.getWorld().getName());
/* 576 */                     double price = Auctions.getPrice(Long.valueOf(timestamp));
/* 577 */                     if (this.playerBalance >= price) {
/* 578 */                       if (seller.hasPlayedBefore() || seller.isOnline()) {
/*     */                         
/* 580 */                         PrePurchaseItemEvent prePurchaseItemEvent = new PrePurchaseItemEvent(player.getWorld().getName(), seller.getUniqueId().toString(), player.getUniqueId().toString(), Auctions.getPrice(Long.valueOf(timestamp)), Auctions.getItem(Long.valueOf(timestamp)));
/* 581 */                         Bukkit.getPluginManager().callEvent((Event)prePurchaseItemEvent);
/* 582 */                         if (prePurchaseItemEvent.isCancelled()) {
/* 583 */                           SoundUtil.failSound(player);
/*     */                         } else {
/*     */                           
/* 586 */                           Auctions.openPurchaseItemMenu(player, Long.valueOf(timestamp), this.current_page);
/*     */                         } 
/*     */                       } else {
/* 589 */                         SoundUtil.failSound(player);
/*     */                       } 
/*     */                     } else {
/* 592 */                       MessageUtil.sendMessage(player, "warning.purchase.insufficient_funds", Config.locale);
/* 593 */                       SoundUtil.failSound(player);
/*     */                     } 
/*     */                   } else {
/*     */                     
/* 597 */                     MessageUtil.sendMessage(player, "warning.purchase.own_item", Config.locale);
/* 598 */                     SoundUtil.failSound(player);
/*     */                   } 
/*     */                 } 
/*     */               } else {
/* 602 */                 MessageUtil.sendMessage(player, "warning.listing.expired", Config.locale);
/* 603 */                 SoundUtil.failSound(player);
/*     */               } 
/*     */             } else {
/* 606 */               SoundUtil.failSound(player);
/*     */             } 
/*     */             return;
/*     */           } 
/* 610 */           if (tag.equals(ItemTag.EXIT)) {
/* 611 */             SoundUtil.clickSound(player);
/* 612 */             close(player);
/* 613 */             exit(player);
/*     */             return;
/*     */           } 
/* 616 */           if (tag.equals(ItemTag.PLAYER_LISTINGS)) {
/* 617 */             SoundUtil.clickSound(player);
/*     */             
/* 619 */             Auctions.openPlayerListingsMenu(player);
/*     */             return;
/*     */           } 
/* 622 */           if (tag.equals(ItemTag.PREVIOUS)) {
/* 623 */             SoundUtil.clickSound(player);
/* 624 */             paginate(player);
/* 625 */             if (this.current_page > 0) this.current_page--; 
/* 626 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 629 */           if (tag.equals(ItemTag.SORT_LISTINGS)) {
/* 630 */             SoundUtil.clickSound(player);
/* 631 */             this.sortOrder = this.sortOrder.next();
/* 632 */             String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/* 633 */             List<String> lore = Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }));
/* 634 */             menu.setItemLore(49, MessageUtil.expand(lore));
/* 635 */             paginate(player);
/* 636 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 639 */           if (tag.equals(ItemTag.NEXT)) {
/* 640 */             SoundUtil.clickSound(player);
/*     */ 
/*     */ 
/*     */             
/* 644 */             int pages = paginate(player);
/* 645 */             if (this.current_page < pages) this.current_page++; 
/* 646 */             build(player, this.current_page);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void exit(Player player) {
/* 654 */     if (Config.exit_command != null && !Config.exit_command.isEmpty()) {
/* 655 */       String command = Config.exit_command.replaceAll("%player%", player.getName()).trim();
/* 656 */       if (!command.isEmpty()) {
/* 657 */         Server server = player.getServer();
/* 658 */         ConsoleCommandSender console = server.getConsoleSender();
/* 659 */         server.dispatchCommand((CommandSender)console, command);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onServerTick(ServerTickEvent event) {
/* 667 */     if (this.menu == null)
/* 668 */       return;  for (HumanEntity entity : this.menu.getViewers()) {
/* 669 */       if (entity instanceof Player) {
/* 670 */         Player player = (Player)entity;
/* 671 */         update(player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\ActiveListingsMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */