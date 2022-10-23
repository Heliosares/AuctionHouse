/*     */ package com.spawnchunk.auctionhouse.modules;
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.storage.DatabaseStorage;
/*     */ import com.spawnchunk.auctionhouse.util.TimeUtil;
/*     */ import com.spawnchunk.auctionhouse.util.WorldUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.meta.EnchantmentStorageMeta;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class Listings {
/*     */   public Listings() {
/*  28 */     this.listings = new LinkedHashMap<>();
/*     */   }
/*     */   private Map<Long, Listing> listings;
/*     */   public Listings(Map<Long, Listing> listings) {
/*  32 */     this.listings = new LinkedHashMap<>();
/*  33 */     Set<Long> keys = listings.keySet();
/*  34 */     for (Long key : keys) {
/*  35 */       Listing listing = listings.get(key);
/*  36 */       this.listings.put(key, listing);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<Long, Listing> getListings() {
/*  41 */     return this.listings;
/*     */   }
/*     */   
/*     */   public void setListings(Map<Long, Listing> listings) {
/*  45 */     this.listings = listings;
/*     */   }
/*     */   
/*     */   private boolean containsIgnoreCase(String string, String filter) {
/*  49 */     if (string == null || string.isEmpty() || filter == null || filter.isEmpty()) return false; 
/*  50 */     return string.toLowerCase().contains(filter.toLowerCase());
/*     */   }
/*     */   
/*     */   private boolean containsIgnoreCase(List<String> strings, String filter) {
/*  54 */     if (strings == null || strings.isEmpty() || filter == null || filter.isEmpty()) return false; 
/*  55 */     return strings.stream().anyMatch(string -> containsIgnoreCase(string, filter));
/*     */   }
/*     */   
/*     */   private boolean containsIgnoreCase(Set<Enchantment> enchantments, String filter) {
/*  59 */     if (enchantments == null || enchantments.isEmpty() || filter == null || filter.isEmpty()) return false; 
/*  60 */     return enchantments.stream().anyMatch(enchantment -> containsIgnoreCase(enchantment.getKey().getKey(), filter));
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getFilteredListings(Player player, String filter, SortOrder order) {
/*  65 */     switch (order) {
/*     */       case PRICE_LOWEST:
/*  67 */         return getFilteredListingsPrice(player, filter, true);
/*     */       case PRICE_HIGHEST:
/*  69 */         return getFilteredListingsPrice(player, filter, false);
/*     */       case CHRONO_NEWEST:
/*  71 */         return getFilteredListingsChrono(player, filter, false);
/*     */     } 
/*     */     
/*  74 */     return getFilteredListingsChrono(player, filter, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getFilteredListingsChrono(Player player, String filter, boolean chronological) {
/*  80 */     Comparator<? super Long> comparator = chronological ? Comparator.<Long>naturalOrder() : Comparator.<Long>reverseOrder();
/*  81 */     String world = player.getWorld().getName();
/*  82 */     String prefix = WorldUtil.getWorldPrefix(world);
/*  83 */     long now = TimeUtil.now();
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
/*  97 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() > now)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).filter(entry -> (containsIgnoreCase(((Listing)entry.getValue()).getItem().getType().getKey().getKey(), filter) || containsIgnoreCase(((Listing)entry.getValue()).getSeller().getName(), filter) || containsIgnoreCase(((ItemMeta)Objects.<ItemMeta>requireNonNull(((Listing)entry.getValue()).getItem().getItemMeta())).getDisplayName(), filter) || containsIgnoreCase(((Listing)entry.getValue()).getItem().getItemMeta().getLore(), filter) || ((Listing)entry.getValue()).getItem().getEnchantments().keySet().stream().anyMatch(()) || (((Listing)entry.getValue()).getItem().getItemMeta() instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta)((Listing)entry.getValue()).getItem().getItemMeta()).getStoredEnchants().keySet().stream().anyMatch(())))).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/*  98 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getFilteredListingsPrice(Player player, String filter, boolean ascending) {
/* 103 */     Comparator<Listing> comparator = ascending ? Comparator.<Listing, Comparable>comparing(Listing::getPrice) : Comparator.<Listing, Comparable>comparing(Listing::getPrice).reversed();
/* 104 */     String world = player.getWorld().getName();
/* 105 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 106 */     long now = TimeUtil.now();
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
/* 121 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() > now)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).filter(entry -> (containsIgnoreCase(((Listing)entry.getValue()).getItem().getType().getKey().getKey(), filter) || containsIgnoreCase(((Listing)entry.getValue()).getSeller().getName(), filter) || containsIgnoreCase(((ItemMeta)Objects.<ItemMeta>requireNonNull(((Listing)entry.getValue()).getItem().getItemMeta())).getDisplayName(), filter) || containsIgnoreCase(((Listing)entry.getValue()).getItem().getItemMeta().getLore(), filter) || containsIgnoreCase(((Listing)entry.getValue()).getItem().getEnchantments().keySet(), filter) || ((Listing)entry.getValue()).getItem().getEnchantments().keySet().stream().anyMatch(()) || (((Listing)entry.getValue()).getItem().getItemMeta() instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta)((Listing)entry.getValue()).getItem().getItemMeta()).getStoredEnchants().keySet().stream().anyMatch(())))).sorted(Map.Entry.comparingByValue(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 122 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getActiveListings(Player player, SortOrder order) {
/* 127 */     switch (order) {
/*     */       case PRICE_LOWEST:
/* 129 */         return getActiveListingsPrice(player, true);
/*     */       case PRICE_HIGHEST:
/* 131 */         return getActiveListingsPrice(player, false);
/*     */       case CHRONO_NEWEST:
/* 133 */         return getActiveListingsChrono(player, false);
/*     */     } 
/*     */     
/* 136 */     return getActiveListingsChrono(player, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getActiveListingsChrono(Player player, boolean chronological) {
/* 142 */     Comparator<? super Long> comparator = chronological ? Comparator.<Long>naturalOrder() : Comparator.<Long>reverseOrder();
/* 143 */     String world = player.getWorld().getName();
/* 144 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 145 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() > now)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 153 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getActiveListingsPrice(Player player, boolean ascending) {
/* 158 */     Comparator<Listing> comparator = ascending ? Comparator.<Listing, Comparable>comparing(Listing::getPrice) : Comparator.<Listing, Comparable>comparing(Listing::getPrice).reversed();
/* 159 */     String world = player.getWorld().getName();
/* 160 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 161 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() > now)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByValue(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 169 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getActiveListingsUnsorted(Player player) {
/* 174 */     String world = player.getWorld().getName();
/* 175 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 176 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() > now)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 183 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getPlayerListings(Player player, SortOrder order) {
/* 188 */     switch (order) {
/*     */       case PRICE_LOWEST:
/* 190 */         return getPlayerListingsPrice(player, true);
/*     */       case PRICE_HIGHEST:
/* 192 */         return getPlayerListingsPrice(player, false);
/*     */       case CHRONO_NEWEST:
/* 194 */         return getPlayerListingsChrono(player, false);
/*     */     } 
/*     */     
/* 197 */     return getPlayerListingsChrono(player, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getPlayerListingsChrono(Player player, boolean chronological) {
/* 203 */     Comparator<? super Long> comparator = chronological ? Comparator.<Long>naturalOrder() : Comparator.<Long>reverseOrder();
/* 204 */     String world = player.getWorld().getName();
/* 205 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 206 */     long now = TimeUtil.now();
/* 207 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() >= now)).filter(entry -> !((Listing)entry.getValue()).getType().isServer()).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 217 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getPlayerListingsPrice(Player player, boolean ascending) {
/* 222 */     Comparator<Listing> comparator = ascending ? Comparator.<Listing, Comparable>comparing(Listing::getPrice) : Comparator.<Listing, Comparable>comparing(Listing::getPrice).reversed();
/* 223 */     String world = player.getWorld().getName();
/* 224 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 225 */     long now = TimeUtil.now();
/* 226 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() >= now)).filter(entry -> !((Listing)entry.getValue()).getType().isServer()).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByValue(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 236 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getPlayerListingsUnsorted(Player player) {
/* 241 */     String world = player.getWorld().getName();
/* 242 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 243 */     long now = TimeUtil.now();
/* 244 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() >= now)).filter(entry -> !((Listing)entry.getValue()).getType().isServer()).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 253 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getSoldItems(Player player, SortOrder order) {
/* 258 */     switch (order) {
/*     */       case PRICE_LOWEST:
/* 260 */         return getSoldItemsPrice(player, true);
/*     */       case PRICE_HIGHEST:
/* 262 */         return getSoldItemsPrice(player, false);
/*     */       case CHRONO_NEWEST:
/* 264 */         return getSoldItemsChrono(player, false);
/*     */     } 
/*     */     
/* 267 */     return getSoldItemsChrono(player, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getSoldItemsChrono(Player player, boolean chronological) {
/* 273 */     Comparator<? super Long> comparator = chronological ? Comparator.<Long>naturalOrder() : Comparator.<Long>reverseOrder();
/* 274 */     String world = player.getWorld().getName();
/* 275 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 276 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> !((Listing)entry.getValue()).getType().isServer()).filter(entry -> !((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 285 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getSoldItemsPrice(Player player, boolean ascending) {
/* 290 */     Comparator<Listing> comparator = ascending ? Comparator.<Listing, Comparable>comparing(Listing::getPrice) : Comparator.<Listing, Comparable>comparing(Listing::getPrice).reversed();
/* 291 */     String world = player.getWorld().getName();
/* 292 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 293 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> !((Listing)entry.getValue()).getType().isServer()).filter(entry -> !((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByValue(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 302 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getSoldItemsUnsorted(Player player) {
/* 307 */     String world = player.getWorld().getName();
/* 308 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 309 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> !((Listing)entry.getValue()).getType().isServer()).filter(entry -> !((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 317 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getExpiredListings(Player player, SortOrder order) {
/* 322 */     switch (order) {
/*     */       case PRICE_LOWEST:
/* 324 */         return getExpiredListingsPrice(player, true);
/*     */       case PRICE_HIGHEST:
/* 326 */         return getExpiredListingsPrice(player, false);
/*     */       case CHRONO_NEWEST:
/* 328 */         return getExpiredListingsChrono(player, false);
/*     */     } 
/*     */     
/* 331 */     return getExpiredListingsChrono(player, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getExpiredListingsChrono(Player player, boolean chronological) {
/* 337 */     Comparator<? super Long> comparator = chronological ? Comparator.<Long>naturalOrder() : Comparator.<Long>reverseOrder();
/* 338 */     String world = player.getWorld().getName();
/* 339 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 340 */     long now = TimeUtil.now();
/* 341 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 350 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now)).filter(entry -> (((Long)entry.getKey()).longValue() >= now - Config.auction_expired_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 351 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getExpiredListingsPrice(Player player, boolean ascending) {
/* 356 */     Comparator<Listing> comparator = ascending ? Comparator.<Listing, Comparable>comparing(Listing::getPrice) : Comparator.<Listing, Comparable>comparing(Listing::getPrice).reversed();
/* 357 */     String world = player.getWorld().getName();
/* 358 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 359 */     long now = TimeUtil.now();
/* 360 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now)).filter(entry -> (((Long)entry.getKey()).longValue() >= now - Config.auction_expired_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).sorted(Map.Entry.comparingByValue(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 370 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getExpiredListingsUnsorted(Player player) {
/* 375 */     String world = player.getWorld().getName();
/* 376 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 377 */     long now = TimeUtil.now();
/* 378 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now)).filter(entry -> (((Long)entry.getKey()).longValue() >= now - Config.auction_expired_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 387 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getUnclaimedListings(Player player) {
/* 393 */     String world = player.getWorld().getName();
/* 394 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 395 */     long now = TimeUtil.now();
/* 396 */     String player_uuid = player.getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 406 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now - Config.auction_expired_duration)).filter(entry -> (((Long)entry.getKey()).longValue() >= now - Config.auction_expired_duration + Config.auction_unclaimed_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).filter(entry -> ((Listing)entry.getValue()).getSeller_UUID().equals(player_uuid)).filter(entry -> (!Config.per_world_listings || (Config.group_worlds ? ((Listing)entry.getValue()).getWorld().startsWith(prefix) : ((Listing)entry.getValue()).getWorld().equals(world)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 407 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getExpiredListings() {
/* 412 */     Comparator<? super Long> comparator = Comparator.naturalOrder();
/* 413 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 420 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now)).filter(entry -> (((Long)entry.getKey()).longValue() >= now - Config.auction_expired_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 421 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public Listings getExpiredSoldItems() {
/* 426 */     Comparator<? super Long> comparator = Comparator.naturalOrder();
/* 427 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 433 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now - Config.auction_sold_duration)).filter(entry -> !((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).sorted(Map.Entry.comparingByKey(comparator)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 434 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getUnclaimedListings() {
/* 440 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 447 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now - Config.auction_expired_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 448 */     return new Listings(l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Listings getAbandonedListings() {
/* 454 */     long now = TimeUtil.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     LinkedHashMap<Long, Listing> l = (LinkedHashMap<Long, Listing>)this.listings.entrySet().parallelStream().filter(entry -> (((Long)entry.getKey()).longValue() < now - Config.auction_expired_duration + Config.auction_unclaimed_duration)).filter(entry -> ((Listing)entry.getValue()).getBuyer_UUID().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
/* 462 */     return new Listings(l);
/*     */   }
/*     */   
/*     */   public Listing getListing(long timestamp) {
/* 466 */     return this.listings.get(Long.valueOf(timestamp));
/*     */   }
/*     */   
/*     */   public void putListing(Listing listing, long timestamp) {
/* 470 */     this.listings.put(Long.valueOf(timestamp), listing);
/*     */   }
/*     */   
/*     */   public boolean newListing(Listing listing) {
/* 474 */     ListingType type = listing.getType();
/* 475 */     long now = TimeUtil.now();
/* 476 */     long future = type.isServer() ? (now + Config.auction_future_duration) : (now + Config.auction_listing_duration);
/* 477 */     for (; this.listings.containsKey(Long.valueOf(future)); future++);
/*     */     
/* 479 */     long id = future;
/* 480 */     boolean result = DatabaseStorage.createListing(AuctionHouse.conn, id, listing);
/* 481 */     if (result) this.listings.put(Long.valueOf(future), listing);
/*     */     
/* 483 */     return result;
/*     */   }
/*     */   
/*     */   public boolean importListing(Listing listing, long timestamp) {
/* 487 */     for (; this.listings.containsKey(Long.valueOf(timestamp)); timestamp++);
/*     */     
/* 489 */     long id = timestamp;
/* 490 */     boolean result = DatabaseStorage.createListing(AuctionHouse.conn, id, listing);
/* 491 */     if (result) this.listings.put(Long.valueOf(timestamp), listing);
/*     */     
/* 493 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeListings(Set<Long> timestamps) {
/* 498 */     boolean result = true;
/* 499 */     final List<Long> removed = new ArrayList<>();
/* 500 */     for (Iterator<Long> iterator = timestamps.iterator(); iterator.hasNext(); ) { long timestamp = ((Long)iterator.next()).longValue();
/* 501 */       boolean success = this.listings.keySet().remove(Long.valueOf(timestamp));
/* 502 */       if (success) {
/* 503 */         removed.add(Long.valueOf(timestamp)); continue;
/*     */       } 
/* 505 */       result = false; }
/*     */ 
/*     */ 
/*     */     
/* 509 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 512 */           DatabaseStorage.deleteListings(AuctionHouse.conn, removed);
/*     */         }
/* 514 */       }).runTaskAsynchronously((Plugin)AuctionHouse.plugin);
/* 515 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeListing(final long timestamp) {
/* 520 */     boolean result = this.listings.keySet().remove(Long.valueOf(timestamp));
/* 521 */     if (result)
/*     */     {
/* 523 */       (new BukkitRunnable()
/*     */         {
/*     */           public void run() {
/* 526 */             DatabaseStorage.deleteListing(AuctionHouse.conn, timestamp);
/*     */           }
/* 528 */         }).runTaskAsynchronously((Plugin)AuctionHouse.plugin);
/*     */     }
/* 530 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 535 */     this.listings.clear();
/*     */     
/* 537 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 540 */           DatabaseStorage.deleteAllListings(AuctionHouse.conn);
/*     */         }
/* 542 */       }).runTaskAsynchronously((Plugin)AuctionHouse.plugin);
/*     */   }
/*     */   
/*     */   public boolean soldListing(final long timestamp, OfflinePlayer buyer) {
/* 546 */     if (this.listings.containsKey(Long.valueOf(timestamp))) {
/* 547 */       final Listing listing = this.listings.get(Long.valueOf(timestamp));
/* 548 */       ListingType type = listing.getType();
/* 549 */       if (type != ListingType.SERVER_LISTING_UNLIMITED) {
/* 550 */         long now = TimeUtil.now();
/*     */         
/*     */         while (true) {
/* 553 */           if (!this.listings.containsKey(Long.valueOf(now))) {
/*     */             
/* 555 */             final long id = now;
/* 556 */             listing.setBuyer(buyer);
/* 557 */             this.listings.put(Long.valueOf(now), listing);
/* 558 */             this.listings.keySet().remove(Long.valueOf(timestamp));
/*     */             
/* 560 */             (new BukkitRunnable()
/*     */               {
/*     */                 public void run() {
/* 563 */                   DatabaseStorage.updateListing(AuctionHouse.conn, timestamp, id, listing);
/*     */                 }
/* 565 */               }).runTaskAsynchronously((Plugin)AuctionHouse.plugin);
/* 566 */             return true;
/*     */           } 
/* 568 */           now++;
/*     */         } 
/*     */       } 
/* 571 */       return true;
/*     */     } 
/*     */     
/* 574 */     return false;
/*     */   }
/*     */   
/*     */   public void cancelListing(final Long timestamp) {
/* 578 */     if (this.listings.containsKey(timestamp)) {
/* 579 */       final Listing listing = this.listings.get(timestamp);
/* 580 */       long now = TimeUtil.now();
/*     */       
/*     */       while (true) {
/* 583 */         if (!this.listings.containsKey(Long.valueOf(now))) {
/*     */           
/* 585 */           this.listings.put(Long.valueOf(now), listing);
/* 586 */           this.listings.keySet().remove(timestamp);
/*     */           
/* 588 */           final long id = now;
/* 589 */           (new BukkitRunnable()
/*     */             {
/*     */               public void run() {
/* 592 */                 DatabaseStorage.updateListing(AuctionHouse.conn, timestamp.longValue(), id, listing);
/*     */               }
/* 594 */             }).runTaskAsynchronously((Plugin)AuctionHouse.plugin);
/*     */           return;
/*     */         } 
/* 597 */         now++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int count() {
/* 603 */     return this.listings.keySet().size();
/*     */   }
/*     */   
/*     */   public void updateCounts(Player player) {
/* 607 */     UUID uuid = player.getUniqueId();
/* 608 */     String uuid_string = uuid.toString();
/* 609 */     String world = player.getWorld().getName();
/* 610 */     String prefix = WorldUtil.getWorldPrefix(world);
/* 611 */     long now = TimeUtil.now();
/* 612 */     int activeListingsCount = 0;
/* 613 */     int playerListingsCount = 0;
/* 614 */     int expiredListingsCount = 0;
/* 615 */     int soldItemsCount = 0;
/* 616 */     Set<Long> keys = this.listings.keySet();
/* 617 */     for (Long ts : keys) {
/* 618 */       Listing listing = this.listings.get(ts);
/* 619 */       String lWorld = listing.getWorld();
/* 620 */       boolean bWorld = (!Config.per_world_listings || (Config.group_worlds ? lWorld.startsWith(prefix) : lWorld.equals(world)));
/* 621 */       boolean bServer = listing.getType().isServer();
/* 622 */       if (!bServer && 
/* 623 */         bWorld) {
/* 624 */         boolean bPlayer = listing.getSeller_UUID().equals(uuid_string);
/* 625 */         boolean bNoBuyer = listing.getBuyer_UUID().isEmpty();
/* 626 */         if (bPlayer) {
/* 627 */           if (bNoBuyer) {
/* 628 */             if (ts.longValue() >= now) {
/* 629 */               activeListingsCount++;
/* 630 */               playerListingsCount++; continue;
/*     */             } 
/* 632 */             if (ts.longValue() >= now - Config.auction_expired_duration) {
/* 633 */               expiredListingsCount++;
/*     */             }
/*     */             continue;
/*     */           } 
/* 637 */           soldItemsCount++;
/*     */           continue;
/*     */         } 
/* 640 */         if (bNoBuyer && 
/* 641 */           ts.longValue() >= now) {
/* 642 */           activeListingsCount++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 649 */     Auctions.setActiveListingsCount(activeListingsCount);
/* 650 */     Auctions.setPlayerListingsCount(uuid, playerListingsCount);
/* 651 */     Auctions.setSoldItemsCount(uuid, soldItemsCount);
/* 652 */     Auctions.setExpiredListingsCount(uuid, expiredListingsCount);
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\modules\Listings.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */