/*     */ package com.spawnchunk.auctionhouse.commands;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.modules.Auctions;
/*     */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import com.spawnchunk.auctionhouse.util.PlayerUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.TabExecutor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class AHCommand
/*     */   implements TabExecutor
/*     */ {
/*     */   private boolean isDisabledWorld(Player player) {
/*  31 */     return Config.disabled_worlds.contains(player.getWorld().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
/*  38 */     boolean isConsole = (sender instanceof org.bukkit.command.ConsoleCommandSender || sender instanceof org.bukkit.command.RemoteConsoleCommandSender);
/*  39 */     boolean isCommand = sender instanceof org.bukkit.command.BlockCommandSender;
/*  40 */     boolean isPlayer = sender instanceof Player;
/*  41 */     Player player = isPlayer ? (Player)sender : null;
/*  42 */     UUID uuid = (player != null) ? player.getUniqueId() : null;
/*  43 */     boolean isOp = (sender instanceof Player && sender.isOp());
/*     */     
/*  45 */     if (isPlayer && player.isSleeping()) {
/*  46 */       MessageUtil.sendMessage(sender, "warning.command.sleeping", Config.locale);
/*  47 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  51 */     if (args.length == 0) {
/*  52 */       if (isPlayer) {
/*  53 */         if (player.hasPermission("auctionhouse.use") || isOp) {
/*  54 */           if (isDisabledWorld(player)) {
/*  55 */             MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/*     */           } else {
/*  57 */             Auctions.filter = null;
/*  58 */             Auctions.menu_mode = false;
/*  59 */             Auctions.openActiveListingsMenu(player);
/*     */           } 
/*     */         }
/*     */       } else {
/*  63 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/*  65 */       return true;
/*     */     } 
/*     */     
/*  68 */     String arg0 = args[0].trim();
/*     */ 
/*     */     
/*  71 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.reload", Config.locale))) {
/*  72 */       if (args.length == 1) {
/*  73 */         if (isCommand) {
/*  74 */           MessageUtil.sendMessage(sender, "warning.command.deny", Config.locale);
/*  75 */         } else if (isPlayer && !player.hasPermission("auctionhouse.reload") && !isOp) {
/*  76 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } else {
/*     */           
/*  79 */           AuctionHouse.menuManager.closeAllMenus();
/*  80 */           Auctions.initializeMenus();
/*     */           
/*  82 */           AuctionHouse.config.reloadConfig();
/*  83 */           MessageUtil.sendMessage(sender, "message.command.reloaded", Config.locale);
/*     */         } 
/*     */       } else {
/*  86 */         String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.reload", Config.locale) });
/*  87 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/*  89 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.clear_all_data", Config.locale))) {
/*  94 */       if (args.length == 1) {
/*  95 */         if (isConsole) {
/*  96 */           Auctions.clearAllData(sender);
/*     */         } else {
/*  98 */           MessageUtil.sendMessage(sender, "warning.command.console_only", Config.locale);
/*     */         } 
/*     */       } else {
/* 101 */         String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.clear_all_data", Config.locale) });
/* 102 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/* 104 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 108 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.create_test_data", Config.locale))) {
/* 109 */       if (args.length == 1) {
/* 110 */         if (isConsole) {
/*     */           
/* 112 */           Auctions.createTestData(sender, 450);
/*     */         } else {
/* 114 */           MessageUtil.sendMessage(sender, "warning.command.console_only", Config.locale);
/*     */         } 
/* 116 */       } else if (args.length == 2) {
/* 117 */         String arg1 = args[1].trim();
/*     */         
/*     */         try {
/* 120 */           int entries = Config.numberFormat.parse(arg1).intValue();
/* 121 */           Auctions.createTestData(sender, entries);
/* 122 */         } catch (NumberFormatException|java.text.ParseException ignored) {
/* 123 */           String syntax = String.format("ah %s [%s]", new Object[] { LocaleStorage.translate("command.create_test_data", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 124 */           MessageUtil.sendMessage(sender, "warning.command.invalid_count", Config.locale, new Object[] { syntax });
/*     */         } 
/*     */       } else {
/* 127 */         String syntax = String.format("ah %s [%s]", new Object[] { LocaleStorage.translate("command.create_test_data", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 128 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/* 130 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.debug", Config.locale))) {
/* 135 */       if (args.length == 1) {
/* 136 */         if (isConsole) {
/* 137 */           Config.debug = !Config.debug;
/* 138 */           MessageUtil.sendMessage(sender, "message.debug", Config.locale, new Object[] { Config.debug ? "true" : "false" });
/*     */         } else {
/* 140 */           MessageUtil.sendMessage(sender, "warning.command.console_only", Config.locale);
/*     */         } 
/*     */       } else {
/* 143 */         String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.debug", Config.locale) });
/* 144 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/* 146 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 150 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.help", Config.locale))) {
/* 151 */       if (isPlayer && isDisabledWorld(player)) {
/* 152 */         MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 153 */       } else if (args.length == 1) {
/* 154 */         showHelp(sender);
/*     */       } else {
/* 156 */         String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.help", Config.locale) });
/* 157 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/* 159 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 163 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.menu", Config.locale))) {
/* 164 */       if (isPlayer && !player.hasPermission("auctionhouse.menu") && !isOp) {
/* 165 */         MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/* 166 */       } else if (args.length == 1) {
/* 167 */         if (isPlayer) {
/* 168 */           Auctions.filter = null;
/* 169 */           Auctions.menu_mode = true;
/* 170 */           Auctions.openActiveListingsMenu(player);
/*     */         } else {
/* 172 */           MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */         } 
/* 174 */       } else if (args.length == 2) {
/* 175 */         String arg1 = args[1].trim();
/* 176 */         Player p = PlayerUtil.getPlayer(arg1);
/* 177 */         if (p != null && p.isOnline()) {
/* 178 */           if (isDisabledWorld(p)) {
/* 179 */             MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 180 */             return true;
/*     */           } 
/* 182 */           Auctions.filter = null;
/* 183 */           Auctions.menu_mode = true;
/* 184 */           Auctions.openActiveListingsMenu(p);
/*     */         } else {
/*     */           
/* 187 */           String syntax = String.format("ah %s [%s]", new Object[] { LocaleStorage.translate("command.menu", Config.locale), LocaleStorage.translate("parameter.player", Config.locale) });
/* 188 */           MessageUtil.sendMessage(sender, "warning.command.invalid_player", Config.locale, new Object[] { syntax });
/*     */         } 
/* 190 */       } else if (args.length == 3) {
/* 191 */         String arg1 = args[1].trim();
/* 192 */         Player p = PlayerUtil.getPlayer(arg1);
/* 193 */         if (p != null && p.isOnline()) {
/* 194 */           if (isDisabledWorld(p)) {
/* 195 */             MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 196 */             return true;
/*     */           } 
/* 198 */           String arg2 = args[2].trim();
/* 199 */           Auctions.filter = null;
/* 200 */           Auctions.menu_mode = Boolean.parseBoolean(arg2);
/* 201 */           Auctions.openActiveListingsMenu(p);
/*     */         } else {
/*     */           
/* 204 */           String syntax = String.format("ah %s [%s] [%s]", new Object[] { LocaleStorage.translate("command.menu", Config.locale), LocaleStorage.translate("parameter.player", Config.locale), LocaleStorage.translate("parameter.flag", Config.locale) });
/* 205 */           MessageUtil.sendMessage(sender, "warning.command.invalid_player", Config.locale, new Object[] { syntax });
/*     */         } 
/*     */       } else {
/* 208 */         String syntax = String.format("ah %s [%s] [%s]", new Object[] { LocaleStorage.translate("command.menu", Config.locale), LocaleStorage.translate("parameter.player", Config.locale), LocaleStorage.translate("parameter.flag", Config.locale) });
/* 209 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/* 211 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 215 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.search", Config.locale))) {
/* 216 */       if (isPlayer) {
/* 217 */         if (isDisabledWorld(player)) {
/* 218 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 219 */         } else if (player.hasPermission("auctionhouse.search") || isOp) {
/* 220 */           if (args.length > 1) {
/* 221 */             Auctions.filter = StringUtils.join(Arrays.asList(Arrays.<String>copyOfRange(args, 1, args.length)), " ");
/* 222 */             Auctions.menu_mode = false;
/* 223 */             Auctions.openActiveListingsMenu(player);
/*     */           } else {
/* 225 */             String syntax = String.format("ah %s <%s>...", new Object[] { LocaleStorage.translate("command.search", Config.locale), LocaleStorage.translate("parameter.keyword", Config.locale) });
/* 226 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } 
/*     */       } else {
/* 230 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 232 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.sell", Config.locale))) {
/* 237 */       if (isPlayer) {
/* 238 */         if (isDisabledWorld(player)) {
/* 239 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 240 */         } else if (player.hasPermission("auctionhouse.sell") || isOp) {
/* 241 */           if (args.length > 1 && args.length < 4) {
/* 242 */             float price; if (Auctions.lock.contains(uuid)) {
/* 243 */               MessageUtil.sendMessage(sender, "warning.command.in_progress", Config.locale);
/* 244 */               return true;
/*     */             } 
/* 246 */             String arg1 = args[1].trim();
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 251 */               price = Config.numberFormat.parse(arg1).floatValue();
/* 252 */               price = Config.integer_price ? Math.round(price) : price;
/* 253 */             } catch (NumberFormatException|java.text.ParseException ignored) {
/* 254 */               String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.sell", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 255 */               MessageUtil.sendMessage(sender, "warning.command.invalid_price", Config.locale, new Object[] { syntax });
/* 256 */               return true;
/*     */             } 
/* 258 */             if (args.length == 2) {
/* 259 */               Auctions.sellItemInHand(player, price, ListingType.PLAYER_LISTING);
/*     */             } else {
/* 261 */               int count; String arg2 = args[2].trim();
/*     */               
/*     */               try {
/* 264 */                 count = Config.numberFormat.parse(arg2).intValue();
/* 265 */               } catch (NumberFormatException|java.text.ParseException ignored) {
/* 266 */                 String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.sell", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 267 */                 MessageUtil.sendMessage(sender, "warning.command.invalid_count", Config.locale, new Object[] { syntax });
/* 268 */                 return true;
/*     */               } 
/* 270 */               Auctions.sellItemInHand(player, price, Integer.valueOf(count), ListingType.PLAYER_LISTING);
/*     */             } 
/*     */           } else {
/*     */             
/* 274 */             String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.sell", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 275 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 278 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 281 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 283 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 287 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.list", Config.locale))) {
/* 288 */       if (isPlayer) {
/* 289 */         if (isDisabledWorld(player)) {
/* 290 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 291 */         } else if (player.hasPermission("auctionhouse.list") || isOp) {
/* 292 */           if (args.length > 1 && args.length < 4) {
/* 293 */             float price; if (Auctions.lock.contains(uuid)) {
/* 294 */               MessageUtil.sendMessage(sender, "warning.command.in_progress", Config.locale);
/* 295 */               return true;
/*     */             } 
/* 297 */             String arg1 = args[1].trim();
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 302 */               price = Config.numberFormat.parse(arg1).floatValue();
/* 303 */               price = Config.integer_price ? Math.round(price) : price;
/* 304 */             } catch (NumberFormatException|java.text.ParseException ignored) {
/* 305 */               String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.list", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 306 */               MessageUtil.sendMessage(sender, "warning.command.invalid_price", Config.locale, new Object[] { syntax });
/* 307 */               return true;
/*     */             } 
/* 309 */             if (args.length == 2) {
/* 310 */               Auctions.sellItemInHand(player, price, ListingType.SERVER_LISTING);
/*     */             } else {
/* 312 */               int count; String arg2 = args[2].trim();
/*     */               
/*     */               try {
/* 315 */                 count = Config.numberFormat.parse(arg2).intValue();
/* 316 */               } catch (NumberFormatException|java.text.ParseException ignored) {
/* 317 */                 String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.list", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 318 */                 MessageUtil.sendMessage(sender, "warning.command.invalid_count", Config.locale, new Object[] { syntax });
/* 319 */                 return true;
/*     */               } 
/* 321 */               Auctions.sellItemInHand(player, price, Integer.valueOf(count), ListingType.SERVER_LISTING);
/*     */             } 
/*     */           } else {
/* 324 */             String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.list", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 325 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 328 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 331 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 333 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 337 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.ulist", Config.locale))) {
/* 338 */       if (isPlayer) {
/* 339 */         if (isDisabledWorld(player)) {
/* 340 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 341 */         } else if (player.hasPermission("auctionhouse.ulist") || isOp) {
/* 342 */           if (args.length > 1 && args.length < 4) {
/* 343 */             float price; if (Auctions.lock.contains(uuid)) {
/* 344 */               MessageUtil.sendMessage(sender, "warning.command.in_progress", Config.locale);
/* 345 */               return true;
/*     */             } 
/* 347 */             String arg1 = args[1].trim();
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 352 */               price = Config.numberFormat.parse(arg1).floatValue();
/* 353 */               price = Config.integer_price ? Math.round(price) : price;
/* 354 */             } catch (NumberFormatException|java.text.ParseException ignored) {
/* 355 */               String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.ulist", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 356 */               MessageUtil.sendMessage(sender, "warning.command.invalid_price", Config.locale, new Object[] { syntax });
/* 357 */               return true;
/*     */             } 
/* 359 */             if (args.length == 2) {
/* 360 */               Auctions.sellItemInHand(player, price, ListingType.SERVER_LISTING_UNLIMITED);
/*     */             } else {
/* 362 */               int count; String arg2 = args[2].trim();
/*     */               
/*     */               try {
/* 365 */                 count = Config.numberFormat.parse(arg2).intValue();
/* 366 */               } catch (NumberFormatException|java.text.ParseException ignored) {
/* 367 */                 String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.ulist", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 368 */                 MessageUtil.sendMessage(sender, "warning.command.invalid_count", Config.locale, new Object[] { syntax });
/* 369 */                 return true;
/*     */               } 
/* 371 */               Auctions.sellItemInHand(player, price, Integer.valueOf(count), ListingType.SERVER_LISTING_UNLIMITED);
/*     */             } 
/*     */           } else {
/* 374 */             String syntax = String.format("ah %s <%s> [%s]", new Object[] { LocaleStorage.translate("command.ulist", Config.locale), LocaleStorage.translate("parameter.price", Config.locale), LocaleStorage.translate("parameter.count", Config.locale) });
/* 375 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 378 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 381 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 383 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 387 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.selling", Config.locale))) {
/* 388 */       if (isPlayer) {
/* 389 */         if (isDisabledWorld(player)) {
/* 390 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 391 */         } else if (player.hasPermission("auctionhouse.selling") || isOp) {
/* 392 */           if (args.length == 1) {
/* 393 */             Auctions.openPlayerListingsMenu(player);
/*     */           } else {
/* 395 */             String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.selling", Config.locale) });
/* 396 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 399 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 402 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 404 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 408 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.sold", Config.locale))) {
/* 409 */       if (isPlayer) {
/* 410 */         if (isDisabledWorld(player)) {
/* 411 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 412 */         } else if (player.hasPermission("auctionhouse.sold") || isOp) {
/* 413 */           if (args.length == 1) {
/* 414 */             Auctions.openSoldItemsMenu(player);
/*     */           } else {
/* 416 */             String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.sold", Config.locale) });
/* 417 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 420 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 423 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 425 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 429 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.expired", Config.locale))) {
/* 430 */       if (isPlayer) {
/* 431 */         if (isDisabledWorld(player)) {
/* 432 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 433 */         } else if (player.hasPermission("auctionhouse.expired") || isOp) {
/* 434 */           if (args.length == 1) {
/* 435 */             Auctions.openExpiredListingsMenu(player);
/*     */           } else {
/* 437 */             String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.expired", Config.locale) });
/* 438 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 441 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 444 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 446 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 450 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.cancel", Config.locale))) {
/* 451 */       if (isPlayer) {
/* 452 */         if (isDisabledWorld(player)) {
/* 453 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 454 */         } else if (player.hasPermission("auctionhouse.cancel") || isOp) {
/* 455 */           if (args.length == 1) {
/* 456 */             Auctions.cancelAllItems(player);
/*     */           } else {
/* 458 */             String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.cancel", Config.locale) });
/* 459 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 462 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 465 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 467 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 471 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.return", Config.locale))) {
/* 472 */       if (isPlayer) {
/* 473 */         if (isDisabledWorld(player)) {
/* 474 */           MessageUtil.sendMessage(sender, "warning.command.disabled_world", Config.locale);
/* 475 */         } else if (player.hasPermission("auctionhouse.return") || isOp) {
/* 476 */           if (args.length == 1) {
/* 477 */             Auctions.returnAllItems(player);
/*     */           } else {
/* 479 */             String syntax = String.format("ah %s", new Object[] { LocaleStorage.translate("command.return", Config.locale) });
/* 480 */             MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */           } 
/*     */         } else {
/* 483 */           MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/*     */         } 
/*     */       } else {
/* 486 */         MessageUtil.sendMessage(sender, "warning.command.player_only", Config.locale);
/*     */       } 
/* 488 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 493 */     if (arg0.equalsIgnoreCase(LocaleStorage.translate("command.purge", Config.locale))) {
/* 494 */       if (isPlayer && !player.hasPermission("auctionhouse.purge") && !isOp) {
/* 495 */         MessageUtil.sendMessage(player, "warning.permission.deny", Config.locale);
/* 496 */       } else if (args.length == 2) {
/* 497 */         String arg1 = args[1].trim();
/* 498 */         OfflinePlayer p = PlayerUtil.getOfflinePlayer(arg1);
/* 499 */         if (p != null) {
/*     */           
/* 501 */           int count = Auctions.purgeAllItems(p);
/* 502 */           MessageUtil.sendMessage(sender, "message.purge.listings", Config.locale, new Object[] { Integer.valueOf(count), p.getName() });
/*     */         } else {
/* 504 */           String syntax = String.format("ah %s <%s>", new Object[] { LocaleStorage.translate("command.purge", Config.locale), LocaleStorage.translate("parameter.player", Config.locale) });
/* 505 */           MessageUtil.sendMessage(sender, "warning.command.invalid_player", Config.locale, new Object[] { syntax });
/*     */         } 
/*     */       } else {
/* 508 */         String syntax = String.format("ah %s <%s>", new Object[] { LocaleStorage.translate("command.purge", Config.locale), LocaleStorage.translate("parameter.player", Config.locale) });
/* 509 */         MessageUtil.sendMessage(sender, "warning.command.invalid_syntax", Config.locale, new Object[] { syntax });
/*     */       } 
/* 511 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 516 */     MessageUtil.sendMessage(sender, "warning.command.unknown", Config.locale);
/* 517 */     return true;
/*     */   }
/*     */   
/*     */   private void showHelp(CommandSender sender) {
/* 521 */     if (sender instanceof Player) {
/* 522 */       Player p = (Player)sender;
/* 523 */       boolean isOp = p.isOp();
/* 524 */       if (p.hasPermission("auctionhouse.use") || isOp) MessageUtil.sendMessage(sender, "message.help.ah", Config.locale); 
/* 525 */       if (p.hasPermission("auctionhouse.cancel") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_cancel", Config.locale); 
/* 526 */       if (p.hasPermission("auctionhouse.expired") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_expired", Config.locale); 
/* 527 */       if (p.hasPermission("auctionhouse.list") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_list", Config.locale); 
/* 528 */       if (p.hasPermission("auctionhouse.menu") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_menu", Config.locale); 
/* 529 */       if (p.hasPermission("auctionhouse.reload") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_reload", Config.locale); 
/* 530 */       if (p.hasPermission("auctionhouse.return") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_return", Config.locale); 
/* 531 */       if (p.hasPermission("auctionhouse.search") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_search", Config.locale); 
/* 532 */       if (p.hasPermission("auctionhouse.sell") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_sell", Config.locale); 
/* 533 */       if (p.hasPermission("auctionhouse.selling") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_selling", Config.locale); 
/* 534 */       if (p.hasPermission("auctionhouse.sold") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_sold", Config.locale); 
/* 535 */       if (p.hasPermission("auctionhouse.ulist") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_ulist", Config.locale); 
/* 536 */       if (p.hasPermission("auctionhouse.purge") || isOp) MessageUtil.sendMessage(sender, "message.help.ah_purge", Config.locale); 
/*     */     } else {
/* 538 */       MessageUtil.sendMessage(sender, "message.help.ah_menu", Config.locale);
/* 539 */       if (sender instanceof org.bukkit.command.ConsoleCommandSender || sender instanceof org.bukkit.command.RemoteConsoleCommandSender) {
/* 540 */         MessageUtil.sendMessage(sender, "message.help.ah_reload", Config.locale);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
/* 547 */     if (!alias.equalsIgnoreCase("ah") && !alias.equals("/ah")) return null; 
/* 548 */     String buffer = "";
/* 549 */     for (String s : args) buffer = buffer.isEmpty() ? s : (buffer + " " + buffer); 
/* 550 */     LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
/* 551 */     String cmd = "";
/* 552 */     if (sender instanceof Player) {
/* 553 */       buffer = "/ah " + buffer;
/* 554 */       if (buffer.matches("/ah list [0-9]+ ")) {
/* 555 */         cmd = buffer;
/* 556 */         map.put("[" + LocaleStorage.translate("parameter.count", Config.locale) + "]", Arrays.asList(new String[] { "auctionhouse.list", "true" }));
/* 557 */       } else if (buffer.matches("/ah menu [a-zA-Z0-9_]+ ")) {
/* 558 */         cmd = buffer;
/* 559 */         map.put(LocaleStorage.translate("value.true", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 560 */         map.put(LocaleStorage.translate("value.false", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 561 */       } else if (buffer.matches("/ah sell [0-9.]+ ")) {
/* 562 */         cmd = buffer;
/* 563 */         map.put("[" + LocaleStorage.translate("parameter.count", Config.locale) + "]", Arrays.asList(new String[] { "auctionhouse.sell", "true" }));
/* 564 */       } else if (buffer.matches("/ah ulist [0-9]+ ")) {
/* 565 */         cmd = buffer;
/* 566 */         map.put("[" + LocaleStorage.translate("parameter.count", Config.locale) + "]", Arrays.asList(new String[] { "auctionhouse.ulist", "true" }));
/* 567 */       } else if (buffer.equals("/ah list ")) {
/* 568 */         cmd = buffer;
/* 569 */         map.put("<" + LocaleStorage.translate("parameter.price", Config.locale) + ">", Arrays.asList(new String[] { "auctionhouse.list", "true" }));
/* 570 */       } else if (buffer.equals("/ah menu ")) {
/* 571 */         cmd = buffer;
/* 572 */         Collection<? extends Player> onlinePlayers = AuctionHouse.plugin.getServer().getOnlinePlayers();
/* 573 */         List<Player> players = (List<Player>)onlinePlayers.parallelStream().sorted(Comparator.comparing(OfflinePlayer::getName)).collect(Collectors.toList());
/* 574 */         for (Player p : players) {
/* 575 */           map.put(p.getName(), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/*     */         } 
/* 577 */       } else if (buffer.equals("/ah purge ")) {
/* 578 */         cmd = buffer;
/* 579 */         Collection<? extends Player> onlinePlayers = AuctionHouse.plugin.getServer().getOnlinePlayers();
/* 580 */         List<Player> players = (List<Player>)onlinePlayers.parallelStream().sorted(Comparator.comparing(OfflinePlayer::getName)).collect(Collectors.toList());
/* 581 */         for (Player p : players) {
/* 582 */           map.put(p.getName(), Arrays.asList(new String[] { "auctionhouse.purge", "false" }));
/*     */         } 
/* 584 */       } else if (buffer.equals("/ah sell ")) {
/* 585 */         cmd = buffer;
/* 586 */         map.put("<" + LocaleStorage.translate("parameter.price", Config.locale) + ">", Arrays.asList(new String[] { "auctionhouse.sell", "true" }));
/* 587 */       } else if (buffer.equals("/ah ulist ")) {
/* 588 */         cmd = buffer;
/* 589 */         map.put("<" + LocaleStorage.translate("parameter.price", Config.locale) + ">", Arrays.asList(new String[] { "auctionhouse.ulist", "true" }));
/* 590 */       } else if (buffer.startsWith("/ah ")) {
/* 591 */         cmd = "/ah ";
/* 592 */         map.put(LocaleStorage.translate("command.cancel", Config.locale), Arrays.asList(new String[] { "auctionhouse.cancel", "false" }));
/* 593 */         map.put(LocaleStorage.translate("command.expired", Config.locale), Arrays.asList(new String[] { "auctionhouse.expired", "false" }));
/* 594 */         map.put(LocaleStorage.translate("command.help", Config.locale), Arrays.asList(new String[] { "auctionhouse.use", "false" }));
/* 595 */         map.put(LocaleStorage.translate("command.list", Config.locale), Arrays.asList(new String[] { "auctionhouse.list", "false" }));
/* 596 */         map.put(LocaleStorage.translate("command.menu", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 597 */         map.put(LocaleStorage.translate("command.reload", Config.locale), Arrays.asList(new String[] { "auctionhouse.reload", "false" }));
/* 598 */         map.put(LocaleStorage.translate("command.return", Config.locale), Arrays.asList(new String[] { "auctionhouse.return", "false" }));
/* 599 */         map.put(LocaleStorage.translate("command.search", Config.locale), Arrays.asList(new String[] { "auctionhouse.search", "false" }));
/* 600 */         map.put(LocaleStorage.translate("command.sell", Config.locale), Arrays.asList(new String[] { "auctionhouse.sell", "false" }));
/* 601 */         map.put(LocaleStorage.translate("command.selling", Config.locale), Arrays.asList(new String[] { "auctionhouse.selling", "false" }));
/* 602 */         map.put(LocaleStorage.translate("command.sold", Config.locale), Arrays.asList(new String[] { "auctionhouse.sold", "false" }));
/* 603 */         map.put(LocaleStorage.translate("command.ulist", Config.locale), Arrays.asList(new String[] { "auctionhouse.ulist", "false" }));
/* 604 */         map.put(LocaleStorage.translate("command.purge", Config.locale), Arrays.asList(new String[] { "auctionhouse.purge", "false" }));
/*     */       } 
/* 606 */     } else if (sender instanceof org.bukkit.command.ConsoleCommandSender || sender instanceof org.bukkit.command.RemoteConsoleCommandSender) {
/* 607 */       buffer = "ah " + buffer;
/* 608 */       if (buffer.matches("ah menu [a-zA-Z0-9_]+ ")) {
/* 609 */         cmd = buffer;
/* 610 */         map.put(LocaleStorage.translate("value.true", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 611 */         map.put(LocaleStorage.translate("value.false", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 612 */       } else if (buffer.equals("ah menu ")) {
/* 613 */         cmd = buffer;
/* 614 */         Collection<? extends Player> onlinePlayers = AuctionHouse.plugin.getServer().getOnlinePlayers();
/* 615 */         List<Player> players = (List<Player>)onlinePlayers.parallelStream().sorted(Comparator.comparing(OfflinePlayer::getName)).collect(Collectors.toList());
/* 616 */         for (Player p : players) {
/* 617 */           map.put(p.getName(), Arrays.asList(new String[] { "", "false" }));
/*     */         } 
/* 619 */       } else if (buffer.equals("ah purge ")) {
/* 620 */         cmd = buffer;
/* 621 */         Collection<? extends Player> onlinePlayers = AuctionHouse.plugin.getServer().getOnlinePlayers();
/* 622 */         List<Player> players = (List<Player>)onlinePlayers.parallelStream().sorted(Comparator.comparing(OfflinePlayer::getName)).collect(Collectors.toList());
/* 623 */         for (Player p : players) {
/* 624 */           map.put(p.getName(), Arrays.asList(new String[] { "", "false" }));
/*     */         } 
/* 626 */       } else if (buffer.startsWith("ah ")) {
/* 627 */         cmd = "ah ";
/* 628 */         map.put(LocaleStorage.translate("command.help", Config.locale), Arrays.asList(new String[] { "", "false" }));
/* 629 */         map.put(LocaleStorage.translate("command.menu", Config.locale), Arrays.asList(new String[] { "", "false" }));
/* 630 */         map.put(LocaleStorage.translate("command.reload", Config.locale), Arrays.asList(new String[] { "", "false" }));
/* 631 */         map.put(LocaleStorage.translate("command.purge", Config.locale), Arrays.asList(new String[] { "", "false" }));
/*     */       } 
/*     */     } else {
/* 634 */       buffer = "ah " + buffer;
/* 635 */       if (buffer.matches("ah menu [a-zA-Z0-9_]+ ")) {
/* 636 */         cmd = buffer;
/* 637 */         map.put(LocaleStorage.translate("value.true", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 638 */         map.put(LocaleStorage.translate("value.false", Config.locale), Arrays.asList(new String[] { "auctionhouse.menu", "false" }));
/* 639 */       } else if (buffer.equals("ah menu ")) {
/* 640 */         cmd = buffer;
/* 641 */         Collection<? extends Player> onlinePlayers = AuctionHouse.plugin.getServer().getOnlinePlayers();
/* 642 */         List<Player> players = (List<Player>)onlinePlayers.parallelStream().sorted(Comparator.comparing(OfflinePlayer::getName)).collect(Collectors.toList());
/* 643 */         for (Player p : players) {
/* 644 */           map.put(p.getName(), Arrays.asList(new String[] { "", "false" }));
/*     */         } 
/* 646 */       } else if (buffer.startsWith("ah ")) {
/* 647 */         cmd = "ah ";
/* 648 */         map.put(LocaleStorage.translate("command.menu", Config.locale), Arrays.asList(new String[] { "", "false" }));
/*     */       } 
/*     */     } 
/*     */     
/* 652 */     return buildCompletions(sender, buffer, cmd, map);
/*     */   }
/*     */   
/*     */   public LinkedList<String> buildCompletions(CommandSender sender, String buffer, String cmd, LinkedHashMap<String, List<String>> map) {
/* 656 */     LinkedList<String> completions = new LinkedList<>();
/* 657 */     for (String key : map.keySet()) {
/* 658 */       List<String> opts = map.get(key);
/* 659 */       String p = opts.get(0);
/* 660 */       boolean parameter = Boolean.parseBoolean(opts.get(1));
/* 661 */       String c = parameter ? cmd : (cmd + cmd);
/* 662 */       if (c.contains(buffer)) {
/* 663 */         if (sender instanceof Player) {
/* 664 */           Player player = (Player)sender;
/* 665 */           if (p.isEmpty() || player.hasPermission(p) || player.isOp()) completions.add(key);  continue;
/*     */         } 
/* 667 */         completions.add(key);
/*     */       } 
/*     */     } 
/*     */     
/* 671 */     return completions;
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\commands\AHCommand.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */