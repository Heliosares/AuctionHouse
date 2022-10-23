/*     */ package com.spawnchunk.auctionhouse.menus;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.MenuClickEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ServerTickEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.Listing;
/*     */ import com.spawnchunk.auctionhouse.modules.Listings;
/*     */ import com.spawnchunk.auctionhouse.modules.SortOrder;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.PlayerUtil;
/*     */ import com.spawnchunk.auctionhouse.util.SoundUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class ExpiredListingsMenu
/*     */   implements Listener
/*     */ {
/*     */   private final String id;
/*  40 */   private SortOrder sortOrder = Config.auction_sort_order;
/*     */   private int expiredListingsCount;
/*     */   private int current_page;
/*  43 */   private List<Long> page_keys = new ArrayList<>();
/*  44 */   private final Map<Integer, ItemTag> tags = new HashMap<>();
/*     */   
/*  46 */   final String menu_back = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.back", Config.locale));
/*  47 */   final String menu_previous = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.page.previous", Config.locale));
/*  48 */   final String menu_next = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.page.next", Config.locale));
/*  49 */   final String menu_sort_listings = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.sort_listings", Config.locale));
/*  50 */   final String order_value = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.order.value", Config.locale));
/*  51 */   final String menu_return_all = MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.return_all", Config.locale));
/*  52 */   final String return_none = MessageUtil.sectionSymbol(LocaleStorage.translate("warning.expired_listings.return.none", Config.locale));
/*  53 */   final String return_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.return.desc1", Config.locale));
/*  54 */   final String return_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.return.desc2", Config.locale));
/*  55 */   final String info_desc1 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.info.desc1", Config.locale));
/*  56 */   final String info_desc2 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.info.desc2", Config.locale));
/*  57 */   final String info_desc3 = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.info.desc3", Config.locale));
/*     */   
/*  59 */   final String expired_listings_title = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.title", Config.locale));
/*  60 */   final String top_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*  61 */   final String top_spacing = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.spacing.top", Config.locale));
/*  62 */   final String click = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.click", Config.locale));
/*  63 */   final String bottom_spacing = MessageUtil.sectionSymbol(LocaleStorage.translate("message.expired_listings.spacing.bottom", Config.locale));
/*  64 */   final String bottom_rule = MessageUtil.sectionSymbol(LocaleStorage.translate("message.listing.horizontal.rule.top", Config.locale));
/*     */   
/*     */   public ExpiredListingsMenu(Player player) {
/*  67 */     UUID uuid = player.getUniqueId();
/*  68 */     String title = this.expired_listings_title;
/*  69 */     if (title.isEmpty()) {
/*  70 */       title = "Cancelled / Expired Listings";
/*     */     }
/*  72 */     this.id = AuctionHouse.menuManager.createMenu(uuid, title, 54);
/*  73 */     Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)AuctionHouse.plugin);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/*  78 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/*  79 */     if (menu == null)
/*     */       return; 
/*  81 */     this.sortOrder = Config.auction_sort_order;
/*     */     
/*  83 */     for (int slot = 0; slot < 45; ) { this.tags.put(Integer.valueOf(slot), ItemTag.ITEM); slot++; }
/*  84 */      this.tags.put(Integer.valueOf(45), ItemTag.BACK);
/*  85 */     this.tags.put(Integer.valueOf(48), ItemTag.PREVIOUS);
/*  86 */     this.tags.put(Integer.valueOf(49), ItemTag.SORT_LISTINGS);
/*  87 */     this.tags.put(Integer.valueOf(50), ItemTag.NEXT);
/*  88 */     this.tags.put(Integer.valueOf(52), ItemTag.RETURN_ALL);
/*     */     
/*  90 */     MenuItem back = new MenuItem(this.menu_back, Config.back_button, 1, null);
/*  91 */     String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/*     */     
/*  93 */     MenuItem sort_listings = new MenuItem(this.menu_sort_listings, Config.sort_listings_button, 1, MessageUtil.expand(Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }))));
/*     */     
/*  95 */     MenuItem return_all = new MenuItem(this.menu_return_all, Config.return_all_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.return_desc1, this.return_desc2 })));
/*     */ 
/*     */ 
/*     */     
/*  99 */     MenuItem info = new MenuItem(MessageUtil.sectionSymbol(LocaleStorage.translate("message.menu.info", Config.locale)), Config.info_button, 1, MessageUtil.expand(Arrays.asList(new String[] { this.info_desc1, this.info_desc2, this.info_desc3 })));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     menu.setItem(45, back);
/* 105 */     menu.setItem(49, sort_listings);
/* 106 */     menu.setItem(52, return_all);
/* 107 */     menu.setItem(53, info);
/*     */   }
/*     */   
/*     */   public void show(final Player player, final int page) {
/* 111 */     initialize();
/* 112 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 115 */           ExpiredListingsMenu.this.paginate_task(player, page);
/*     */         }
/* 117 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void paginate_task(final Player player, int page) {
/* 121 */     this.current_page = page;
/* 122 */     paginate(player);
/* 123 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 126 */           ExpiredListingsMenu.this.build_task(player);
/*     */         }
/* 128 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void build_task(final Player player) {
/* 132 */     build(player, this.current_page);
/*     */     
/* 134 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 137 */           ExpiredListingsMenu.this.open_task(player);
/*     */         }
/* 139 */       }).runTaskLater((Plugin)AuctionHouse.plugin, 1L);
/*     */   }
/*     */   
/*     */   public void open_task(Player player) {
/* 143 */     UUID uuid = player.getUniqueId();
/* 144 */     AuctionHouse.menuManager.openMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   public void close(Player player) {
/* 148 */     UUID uuid = player.getUniqueId();
/* 149 */     AuctionHouse.menuManager.closeMenu(uuid, this.id);
/*     */   }
/*     */   
/*     */   private int paginate(Player player) {
/* 153 */     Listings expiredListings = AuctionHouse.listings.getExpiredListings(player, this.sortOrder);
/* 154 */     this.expiredListingsCount = expiredListings.count();
/* 155 */     Map<Long, Listing> map = expiredListings.getListings();
/* 156 */     this.page_keys = new ArrayList<>(map.keySet());
/* 157 */     int keys = this.page_keys.size();
/* 158 */     return Math.floorDiv(Math.max(keys - 1, 0), 45);
/*     */   }
/*     */   
/*     */   private void build(Player player, int page) {
/* 162 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 163 */     if (menu == null)
/* 164 */       return;  int size = this.page_keys.size();
/* 165 */     int pages = Math.floorDiv(Math.max(size - 1, 0), 45);
/* 166 */     MenuItem blank = new MenuItem(null, "minecraft:air", 1, null);
/* 167 */     MenuItem previous = new MenuItem(this.menu_previous, Config.previous_button, 1, null);
/* 168 */     MenuItem next = new MenuItem(this.menu_next, Config.next_button, 1, null);
/* 169 */     Listings expiredListings = AuctionHouse.listings.getExpiredListings(player, this.sortOrder);
/* 170 */     int offset = page * 45;
/* 171 */     for (int i = 0; i < 45; i++) {
/* 172 */       int index = offset + i;
/* 173 */       if (index < size) {
/* 174 */         long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 175 */         Listing listing = expiredListings.getListing(timestamp);
/* 176 */         if (listing != null) {
/* 177 */           ItemStack is = listing.getItem();
/* 178 */           if (is != null) {
/* 179 */             String name = ItemUtil.getCustomName(is);
/* 180 */             String key = is.getType().getKey().toString();
/* 181 */             int amount = is.getAmount();
/* 182 */             ItemMeta meta = is.getItemMeta();
/* 183 */             List<String> oldLore = (meta != null) ? meta.getLore() : new ArrayList<>();
/* 184 */             String nbt = AuctionHouse.nms.getNBTString(is);
/* 185 */             List<String> desc = Arrays.asList(new String[] { this.top_rule, this.top_spacing, this.click, this.bottom_spacing, this.bottom_rule });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 192 */             List<String> lore = new ArrayList<>();
/* 193 */             if (oldLore != null) lore.addAll(oldLore); 
/* 194 */             for (String s : MessageUtil.expand(desc)) {
/* 195 */               if (!s.isEmpty()) lore.add(s); 
/*     */             } 
/* 197 */             MenuItem menuItem = new MenuItem(name, key, amount, lore, nbt);
/* 198 */             menu.setItem(i, menuItem);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 202 */         menu.setItem(i, blank);
/*     */       } 
/*     */     } 
/* 205 */     menu.setItem(48, (this.current_page > 0) ? previous : blank);
/* 206 */     menu.setItem(50, (this.current_page < pages) ? next : blank);
/* 207 */     Auctions.updateCounts(player);
/* 208 */     int expired = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/* 209 */     updateReturnAllButton(menu, expired);
/*     */   }
/*     */   private void updateReturnAllButton(Menu menu, int expired) {
/*     */     List<String> lore;
/* 213 */     this.expiredListingsCount = expired;
/*     */     
/* 215 */     if (expired > 0) {
/* 216 */       lore = MessageUtil.expand(Arrays.asList(new String[] { this.return_desc1, this.return_desc2 }));
/*     */     }
/*     */     else {
/*     */       
/* 220 */       lore = MessageUtil.expand(Collections.singletonList(this.return_none));
/*     */     } 
/*     */     
/* 223 */     menu.setItemLore(52, lore);
/* 224 */     menu.setItemAmount(52, 1);
/*     */   }
/*     */   
/*     */   private void update(Player player) {
/* 228 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 229 */     if (menu == null)
/*     */       return; 
/* 231 */     Auctions.updateCounts(player);
/* 232 */     int expired = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/*     */     
/* 234 */     if (this.expiredListingsCount != expired) {
/* 235 */       this.expiredListingsCount = expired;
/*     */       
/* 237 */       int pages = paginate(player);
/* 238 */       if (this.current_page > pages) this.current_page = pages; 
/* 239 */       build(player, this.current_page);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onMenuClick(MenuClickEvent event) {
/* 246 */     Player player = event.getPlayer();
/* 247 */     String id = event.getId();
/* 248 */     if (player == null || id == null)
/* 249 */       return;  if (id.equals(this.id)) {
/* 250 */       Menu menu = AuctionHouse.menuManager.getMenu(id);
/* 251 */       if (menu == null)
/* 252 */         return;  int slot = event.getSlot();
/* 253 */       ItemStack item = menu.getItem(slot);
/* 254 */       if (item == null)
/* 255 */         return;  if (item.getType().equals(Material.AIR))
/* 256 */         return;  MenuClickType click = event.getMenuClickType();
/*     */       
/* 258 */       if (PlayerUtil.spamCheck(player))
/*     */         return; 
/* 260 */       if (click == MenuClickType.LEFT) {
/* 261 */         ItemTag tag = this.tags.get(Integer.valueOf(slot));
/* 262 */         if (tag != null) {
/* 263 */           if (tag.equals(ItemTag.ITEM)) {
/* 264 */             int index = this.current_page * 45 + slot;
/* 265 */             int size = this.page_keys.size();
/* 266 */             if (index < size && size > 0) {
/* 267 */               SoundUtil.clickSound(player);
/* 268 */               long timestamp = ((Long)this.page_keys.get(index)).longValue();
/* 269 */               Auctions.returnItem(player, Long.valueOf(timestamp));
/* 270 */               paginate(player);
/* 271 */               int expired_listing_count = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/* 272 */               int pages = Math.floorDiv(Math.max(expired_listing_count - 1, 0), 45);
/* 273 */               for (; this.current_page > pages; this.current_page--);
/* 274 */               build(player, this.current_page);
/*     */             } else {
/* 276 */               SoundUtil.failSound(player);
/*     */             } 
/*     */             return;
/*     */           } 
/* 280 */           if (tag.equals(ItemTag.BACK)) {
/* 281 */             SoundUtil.clickSound(player);
/*     */             
/* 283 */             Auctions.openPlayerListingsMenu(player);
/*     */             return;
/*     */           } 
/* 286 */           if (tag.equals(ItemTag.PREVIOUS)) {
/* 287 */             SoundUtil.clickSound(player);
/* 288 */             paginate(player);
/* 289 */             if (this.current_page > 0) this.current_page--; 
/* 290 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 293 */           if (tag.equals(ItemTag.SORT_LISTINGS)) {
/* 294 */             SoundUtil.clickSound(player);
/* 295 */             this.sortOrder = this.sortOrder.next();
/* 296 */             String order = LocaleStorage.translate(this.sortOrder.key(), Config.locale);
/* 297 */             List<String> lore = Collections.singletonList(MessageUtil.populate(this.order_value, new Object[] { order }));
/* 298 */             menu.setItemLore(49, MessageUtil.expand(lore));
/* 299 */             paginate(player);
/* 300 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 303 */           if (tag.equals(ItemTag.NEXT)) {
/* 304 */             SoundUtil.clickSound(player);
/*     */ 
/*     */ 
/*     */             
/* 308 */             int pages = paginate(player);
/* 309 */             if (this.current_page < pages) this.current_page++; 
/* 310 */             build(player, this.current_page);
/*     */             return;
/*     */           } 
/* 313 */           if (tag.equals(ItemTag.RETURN_ALL)) {
/* 314 */             SoundUtil.clickSound(player);
/* 315 */             Listings expiredListings = AuctionHouse.listings.getExpiredListings(player, this.sortOrder);
/* 316 */             int expired_listing_count = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/* 317 */             if (expired_listing_count > 0) {
/* 318 */               Auctions.returnAllItems(player);
/* 319 */               expired_listing_count = Auctions.getExpiredListingsCount((OfflinePlayer)player);
/*     */             } 
/* 321 */             if (expired_listing_count > 0) {
/* 322 */               build(player, this.current_page);
/*     */             } else {
/*     */               
/* 325 */               Auctions.openPlayerListingsMenu(player);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onServerTick(ServerTickEvent event) {
/* 336 */     Menu menu = AuctionHouse.menuManager.getMenu(this.id);
/* 337 */     if (menu == null)
/* 338 */       return;  List<HumanEntity> viewers = menu.getViewers();
/* 339 */     for (HumanEntity entity : viewers) {
/* 340 */       if (entity instanceof Player) {
/* 341 */         Player player = (Player)entity;
/* 342 */         update(player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\menus\ExpiredListingsMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */