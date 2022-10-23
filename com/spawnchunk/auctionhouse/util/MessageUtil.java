/*     */ package com.spawnchunk.auctionhouse.util;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.storage.LocaleStorage;
/*     */ import github.scarsz.discordsrv.DiscordSRV;
/*     */ import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
/*     */ import github.scarsz.discordsrv.util.DiscordUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import javax.annotation.Nullable;
/*     */ import net.md_5.bungee.api.ChatMessageType;
/*     */ import net.md_5.bungee.api.chat.BaseComponent;
/*     */ import net.md_5.bungee.api.chat.ComponentBuilder;
/*     */ import net.md_5.bungee.api.chat.TextComponent;
/*     */ import net.md_5.bungee.api.chat.TranslatableComponent;
/*     */ import net.md_5.bungee.chat.ComponentSerializer;
/*     */ import org.apache.commons.lang.WordUtils;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageUtil
/*     */ {
/*     */   public static final String discord_prefix = ":shopping_cart: ";
/*     */   public static final String bold = "**";
/*     */   public static final String ansi_reset = "\033[0m";
/*     */   public static final String ansi_red = "\033[31m";
/*     */   public static final String ansi_green = "\033[32m";
/*     */   public static final String ansi_yellow = "\033[33m";
/*     */   public static final String ansi_bright_red = "\033[1;31m";
/*     */   public static final String ansi_bright_green = "\033[1;32m";
/*     */   public static final String ansi_bright_yellow = "\033[1;33m";
/*     */   
/*     */   public static void logSevere(String message) {
/*  45 */     AuctionHouse.logger.log(Level.SEVERE, String.format("%s%s%s", new Object[] { Config.log_ansi ? "\033[1;31m" : "", message, Config.log_ansi ? "\033[0m" : "" }));
/*     */   }
/*     */   
/*     */   public static void logWarning(String message) {
/*  49 */     AuctionHouse.logger.log(Level.WARNING, String.format("%s%s%s", new Object[] { Config.log_ansi ? "\033[1;33m" : "", message, Config.log_ansi ? "\033[0m" : "" }));
/*     */   }
/*     */   
/*     */   public static void logTest(String message) {
/*  53 */     AuctionHouse.logger.log(Level.INFO, String.format("%s%s%s", new Object[] { Config.log_ansi ? "\033[1;32m" : "", message, Config.log_ansi ? "\033[0m" : "" }));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String sectionSymbol(String message) {
/*  58 */     String m = message.replaceAll("&#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])", "§x§$1§$2§$3§$4§$5§$6");
/*  59 */     m = m.replaceAll("&([0-9a-fk-orx])", "§$1");
/*  60 */     return m;
/*     */   }
/*     */   
/*     */   public static String nocolor(String message) {
/*  64 */     return message.replaceAll("§", "&").replaceAll("&#[&0-9a-fA-F]{12}", "").replaceAll("&[0-9a-fA-Fk-orx]", "");
/*     */   }
/*     */   
/*     */   public static String readable(String string) {
/*  68 */     return WordUtils.capitalizeFully(string.replace("_", " "));
/*     */   }
/*     */   
/*     */   public static String populate(String message, Object... args) {
/*  72 */     int pos = 0;
/*  73 */     for (Object arg : args) {
/*  74 */       String value = "";
/*  75 */       if (arg instanceof String) value = String.format("%s", new Object[] { arg }); 
/*  76 */       if (arg instanceof Byte) value = String.format("%d", new Object[] { arg }); 
/*  77 */       if (arg instanceof Short) value = String.format("%d", new Object[] { arg }); 
/*  78 */       if (arg instanceof Integer) value = String.format("%d", new Object[] { arg }); 
/*  79 */       if (arg instanceof Long) value = String.format("%dL", new Object[] { arg }); 
/*  80 */       if (arg instanceof Float) value = Config.numberFormat.format(arg); 
/*  81 */       if (arg instanceof Double) value = Config.numberFormat.format(arg); 
/*  82 */       message = message.replace(String.format("{%d}", new Object[] { Integer.valueOf(pos) }), value + "§r");
/*  83 */       message = message.replace(" ", " ");
/*  84 */       pos++;
/*     */     } 
/*  86 */     return message;
/*     */   }
/*     */   
/*     */   public static void discordMessage(String key, String locale) {
/*  90 */     if (AuctionHouse.discord && Config.discord_channel != null) {
/*  91 */       String message = nocolor(LocaleStorage.translate("discord.prefix", locale) + LocaleStorage.translate("discord.prefix", locale));
/*  92 */       message = TextComponent.toPlainText(translateItem(message));
/*  93 */       if (!message.isEmpty()) {
/*  94 */         TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(Config.discord_channel);
/*  95 */         if (textChannel != null) {
/*  96 */           DiscordUtil.sendMessage(textChannel, message);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void discordMessage(String key, String locale, Object... args) {
/* 103 */     if (AuctionHouse.discord && Config.discord_channel != null) {
/* 104 */       String message = nocolor(populate(LocaleStorage.translate("discord.prefix", locale) + LocaleStorage.translate("discord.prefix", locale), args));
/* 105 */       message = TextComponent.toPlainText(translateItem(message));
/* 106 */       if (!message.isEmpty()) {
/* 107 */         TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(Config.discord_channel);
/* 108 */         if (textChannel != null) {
/* 109 */           DiscordUtil.sendMessage(textChannel, message);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void logMessage(String key, String locale) {
/* 116 */     String message = nocolor(LocaleStorage.translate(key, locale));
/* 117 */     if (!message.isEmpty()) AuctionHouse.logger.info(message); 
/*     */   }
/*     */   
/*     */   public static void logMessage(String key, String locale, Object... args) {
/* 121 */     String message = nocolor(populate(LocaleStorage.translate(key, locale), args));
/* 122 */     if (!message.isEmpty()) AuctionHouse.logger.info(message); 
/*     */   }
/*     */   
/*     */   public static void sendMessage(final CommandSender sender, final String key, final String locale) {
/* 126 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 129 */           String message = MessageUtil.sectionSymbol(LocaleStorage.translate(key, locale));
/* 130 */           if (!(sender instanceof Player)) message = MessageUtil.nocolor(message); 
/* 131 */           if (!message.isEmpty()) {
/* 132 */             MessageUtil.chatMessage(sender, MessageUtil.translateItem(message));
/*     */           }
/*     */         }
/* 135 */       }).runTask((Plugin)AuctionHouse.plugin);
/*     */   }
/*     */   
/*     */   public static void sendMessage(final CommandSender sender, final String key, final String locale, Object... args) {
/* 139 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 142 */           String message = MessageUtil.sectionSymbol(MessageUtil.populate(LocaleStorage.translate(key, locale), args));
/* 143 */           if (!(sender instanceof Player)) message = MessageUtil.nocolor(message); 
/* 144 */           if (!message.isEmpty())
/*     */           {
/* 146 */             MessageUtil.chatMessage(sender, MessageUtil.translateItem(message));
/*     */           }
/*     */         }
/* 149 */       }).runTask((Plugin)AuctionHouse.plugin);
/*     */   }
/*     */   
/*     */   public static void sendMessage(final Player player, final String key, final String locale) {
/* 153 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 156 */           String message = MessageUtil.sectionSymbol(LocaleStorage.translate(key, locale));
/* 157 */           if (!message.isEmpty()) {
/* 158 */             MessageUtil.chatMessage(player, MessageUtil.translateItem(message));
/*     */           }
/*     */         }
/* 161 */       }).runTask((Plugin)AuctionHouse.plugin);
/*     */   }
/*     */   
/*     */   public static void sendMessage(String world, Player player, String key, String locale) {
/* 165 */     String w = player.getWorld().getName();
/* 166 */     if (Config.per_world_listings)
/* 167 */       if (Config.group_worlds) {
/* 168 */         if (!w.contains(WorldUtil.getWorldPrefix(world)))
/*     */           return; 
/* 170 */       } else if (!w.equals(world)) {
/*     */         return;
/*     */       }  
/* 173 */     sendMessage(player, key, locale);
/*     */   }
/*     */   
/*     */   public static void sendMessage(final Player player, final String key, final String locale, Object... args) {
/* 177 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/* 180 */           String message = MessageUtil.sectionSymbol(MessageUtil.populate(LocaleStorage.translate(key, locale), args));
/* 181 */           if (!message.isEmpty())
/*     */           {
/* 183 */             MessageUtil.chatMessage(player, MessageUtil.translateItem(message));
/*     */           }
/*     */         }
/* 186 */       }).runTask((Plugin)AuctionHouse.plugin);
/*     */   }
/*     */   
/*     */   public static void sendMessage(String world, Player player, String key, String locale, Object... args) {
/* 190 */     String w = player.getWorld().getName();
/* 191 */     if (Config.per_world_listings)
/* 192 */       if (Config.group_worlds) {
/* 193 */         if (!w.contains(WorldUtil.getWorldPrefix(world)))
/*     */           return; 
/* 195 */       } else if (!w.equals(world)) {
/*     */         return;
/*     */       }  
/* 198 */     sendMessage(player, key, locale, args);
/*     */   }
/*     */   
/*     */   public static List<String> expand(List<String> in) {
/* 202 */     List<String> out = new ArrayList<>();
/* 203 */     for (String s : in) {
/* 204 */       String[] line = s.split("\n");
/* 205 */       for (String t : line) {
/* 206 */         if (!t.isEmpty()) out.add(t); 
/*     */       } 
/*     */     } 
/* 209 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private static BaseComponent[] translateItem(String message) {
/* 214 */     ComponentBuilder builder = new ComponentBuilder("");
/*     */     
/* 216 */     String[] parts = message.split("::");
/* 217 */     for (String part : parts) {
/* 218 */       if (Config.debug) AuctionHouse.logger.info(String.format("part = %s", new Object[] { part })); 
/* 219 */       if (part.startsWith("{") && part.endsWith("}")) {
/* 220 */         String p = part.substring(1, part.length() - 1);
/* 221 */         if (Config.debug) AuctionHouse.logger.info(String.format("p = %s", new Object[] { p })); 
/* 222 */         builder.append((BaseComponent)new TranslatableComponent(p, new Object[0]), ComponentBuilder.FormatRetention.NONE);
/*     */       } else {
/* 224 */         builder.append(TextComponent.fromLegacyText(part), ComponentBuilder.FormatRetention.NONE);
/*     */       } 
/*     */     } 
/* 227 */     BaseComponent[] components = builder.create();
/* 228 */     if (Config.debug) {
/* 229 */       String s = ComponentSerializer.toString(components);
/* 230 */       if (Config.debug) AuctionHouse.logger.info(String.format("serialized = %s", new Object[] { s })); 
/*     */     } 
/* 232 */     return components;
/*     */   }
/*     */   
/*     */   private static void chatMessage(CommandSender sender, BaseComponent[] components) {
/* 236 */     if (sender == null || components.length == 0)
/* 237 */       return;  sender.spigot().sendMessage(components);
/*     */   }
/*     */   
/*     */   private static void chatMessage(Player player, BaseComponent[] components) {
/* 241 */     if (player == null || components.length == 0)
/* 242 */       return;  player.spigot().sendMessage(ChatMessageType.CHAT, components);
/*     */   }
/*     */   
/*     */   public static void sendActionBar(String world, Player player, @Nullable Long delay, String key, String locale, Object... args) {
/* 246 */     String w = player.getWorld().getName();
/* 247 */     if (Config.per_world_listings)
/* 248 */       if (Config.group_worlds) {
/* 249 */         if (!w.contains(WorldUtil.getWorldPrefix(world)))
/*     */           return; 
/* 251 */       } else if (!w.equals(world)) {
/*     */         return;
/*     */       }  
/* 254 */     sendActionBar(player, delay, key, locale, args);
/*     */   }
/*     */   
/*     */   public static void sendActionBar(String world, Player player, @Nullable Long delay, String message) {
/* 258 */     String w = player.getWorld().getName();
/* 259 */     if (Config.per_world_listings)
/* 260 */       if (Config.group_worlds) {
/* 261 */         if (!w.contains(WorldUtil.getWorldPrefix(world)))
/*     */           return; 
/* 263 */       } else if (!w.equals(world)) {
/*     */         return;
/*     */       }  
/* 266 */     sendActionBar(player, delay, message);
/*     */   }
/*     */   
/*     */   public static void sendActionBar(Player player, @Nullable Long delay, String key, String locale, Object... args) {
/* 270 */     String translation = LocaleStorage.translate(key, locale);
/* 271 */     translation = sectionSymbol(populate(translation, args));
/* 272 */     if (!translation.isEmpty())
/*     */     {
/* 274 */       actionBar(player, delay, translateItem(translation));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sendActionBar(Player player, @Nullable Long delay, String message) {
/* 279 */     actionBar(player, delay, TextComponent.fromLegacyText(sectionSymbol(message)));
/*     */   }
/*     */   
/*     */   public static void actionBar(final Player player, @Nullable Long delay, BaseComponent[] component) {
/* 283 */     if (delay == null || delay.longValue() == 0L) {
/* 284 */       player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
/*     */     } else {
/* 286 */       final BaseComponent[] components = (BaseComponent[])component.clone();
/* 287 */       (new BukkitRunnable()
/*     */         {
/*     */           public void run() {
/* 290 */             player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
/*     */           }
/* 292 */         }).runTaskLater((Plugin)AuctionHouse.plugin, delay.longValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clearActionBar(String world, Player player, @Nullable Long delay) {
/* 297 */     sendActionBar(world, player, delay, "");
/*     */   }
/*     */   
/*     */   public static void clearActionBar(Player player, @Nullable Long delay) {
/* 301 */     sendActionBar(player, delay, "");
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\MessageUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */