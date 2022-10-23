/*     */ package com.spawnchunk.auctionhouse.listeners;
/*     */ 
/*     */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*     */ import com.spawnchunk.auctionhouse.config.Config;
/*     */ import com.spawnchunk.auctionhouse.events.AuctionItemEvent;
/*     */ import com.spawnchunk.auctionhouse.events.ItemAction;
/*     */ import com.spawnchunk.auctionhouse.events.ListItemEvent;
/*     */ import com.spawnchunk.auctionhouse.events.PrePurchaseItemEvent;
/*     */ import com.spawnchunk.auctionhouse.events.PurchaseItemEvent;
/*     */ import com.spawnchunk.auctionhouse.modules.ListingType;
/*     */ import com.spawnchunk.auctionhouse.util.ItemUtil;
/*     */ import com.spawnchunk.auctionhouse.util.MessageUtil;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import java.util.StringJoiner;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuctionListener
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onListItemEvent(ListItemEvent event) {
/*  34 */     if (Config.debug) AuctionHouse.logger.info("ListItemEvent");
/*     */   
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onPrePurchaseItemEvent(PrePurchaseItemEvent event) {
/*  40 */     if (Config.debug) AuctionHouse.logger.info("PrePurchaseItemEvent");
/*     */   
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onPurchaseItemEvent(PurchaseItemEvent event) {
/*  46 */     if (Config.debug) AuctionHouse.logger.info("PurchaseItemEvent");
/*     */   
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onAuctionItemEvent(AuctionItemEvent event) {
/*  52 */     if (Config.debug) AuctionHouse.logger.info("AuctionItemEvent"); 
/*  53 */     final ItemAction action = event.getItemAction();
/*  54 */     final ItemStack item = event.getItem();
/*  55 */     final float price = event.getPrice();
/*  56 */     ListingType type = event.getType();
/*  57 */     final String world = event.getWorld();
/*  58 */     final String seller = type.isServer() ? AuctionHouse.servername : ((event.getSeller() != null) ? event.getSeller().getName() : event.getSeller_UUID());
/*  59 */     final String buyer = (event.getBuyer() != null) ? event.getBuyer().getName() : "";
/*     */     
/*  61 */     (new BukkitRunnable()
/*     */       {
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
/*     */         public void run()
/*     */         {
/*     */           // Byte code:
/*     */           //   0: getstatic com/spawnchunk/auctionhouse/config/Config.log_listed : Z
/*     */           //   3: ifne -> 36
/*     */           //   6: getstatic com/spawnchunk/auctionhouse/config/Config.log_sold : Z
/*     */           //   9: ifne -> 36
/*     */           //   12: getstatic com/spawnchunk/auctionhouse/config/Config.log_cancelled : Z
/*     */           //   15: ifne -> 36
/*     */           //   18: getstatic com/spawnchunk/auctionhouse/config/Config.log_returned : Z
/*     */           //   21: ifne -> 36
/*     */           //   24: getstatic com/spawnchunk/auctionhouse/config/Config.log_dropped : Z
/*     */           //   27: ifne -> 36
/*     */           //   30: getstatic com/spawnchunk/auctionhouse/config/Config.log_purged : Z
/*     */           //   33: ifeq -> 741
/*     */           //   36: new java/text/SimpleDateFormat
/*     */           //   39: dup
/*     */           //   40: ldc 'yyyy-MM-dd'
/*     */           //   42: invokespecial <init> : (Ljava/lang/String;)V
/*     */           //   45: astore_1
/*     */           //   46: new java/text/SimpleDateFormat
/*     */           //   49: dup
/*     */           //   50: ldc 'HH:mm:ss'
/*     */           //   52: invokespecial <init> : (Ljava/lang/String;)V
/*     */           //   55: astore_2
/*     */           //   56: new java/util/Date
/*     */           //   59: dup
/*     */           //   60: invokespecial <init> : ()V
/*     */           //   63: astore_3
/*     */           //   64: getstatic com/spawnchunk/auctionhouse/AuctionHouse.plugin : Lcom/spawnchunk/auctionhouse/AuctionHouse;
/*     */           //   67: invokevirtual getDataFolder : ()Ljava/io/File;
/*     */           //   70: invokevirtual getPath : ()Ljava/lang/String;
/*     */           //   73: getstatic java/io/File.separator : Ljava/lang/String;
/*     */           //   76: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   81: astore #4
/*     */           //   83: ldc '%s.log'
/*     */           //   85: iconst_1
/*     */           //   86: anewarray java/lang/Object
/*     */           //   89: dup
/*     */           //   90: iconst_0
/*     */           //   91: aload_1
/*     */           //   92: aload_3
/*     */           //   93: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
/*     */           //   96: aastore
/*     */           //   97: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   100: astore #5
/*     */           //   102: aload #4
/*     */           //   104: getstatic java/io/File.separator : Ljava/lang/String;
/*     */           //   107: aload #5
/*     */           //   109: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   114: astore #6
/*     */           //   116: ldc '[%s]'
/*     */           //   118: iconst_1
/*     */           //   119: anewarray java/lang/Object
/*     */           //   122: dup
/*     */           //   123: iconst_0
/*     */           //   124: aload_2
/*     */           //   125: aload_3
/*     */           //   126: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
/*     */           //   129: aastore
/*     */           //   130: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   133: astore #7
/*     */           //   135: new java/io/File
/*     */           //   138: dup
/*     */           //   139: aload #4
/*     */           //   141: invokespecial <init> : (Ljava/lang/String;)V
/*     */           //   144: astore #8
/*     */           //   146: aload #8
/*     */           //   148: invokevirtual exists : ()Z
/*     */           //   151: ifne -> 160
/*     */           //   154: aload #8
/*     */           //   156: invokevirtual mkdirs : ()Z
/*     */           //   159: pop
/*     */           //   160: aload_0
/*     */           //   161: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   164: invokevirtual getAmount : ()I
/*     */           //   167: istore #9
/*     */           //   169: aload_0
/*     */           //   170: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   173: invokestatic getName : (Lorg/bukkit/inventory/ItemStack;)Ljava/lang/String;
/*     */           //   176: astore #10
/*     */           //   178: getstatic com/spawnchunk/auctionhouse/listeners/AuctionListener$2.$SwitchMap$com$spawnchunk$auctionhouse$events$ItemAction : [I
/*     */           //   181: aload_0
/*     */           //   182: getfield val$action : Lcom/spawnchunk/auctionhouse/events/ItemAction;
/*     */           //   185: invokevirtual ordinal : ()I
/*     */           //   188: iaload
/*     */           //   189: tableswitch default -> 741, 1 -> 224, 2 -> 328, 3 -> 440, 4 -> 544, 5 -> 637
/*     */           //   224: getstatic com/spawnchunk/auctionhouse/config/Config.log_listed : Z
/*     */           //   227: ifeq -> 327
/*     */           //   230: aload_0
/*     */           //   231: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   234: aload_0
/*     */           //   235: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   238: invokevirtual getEnchants : (Lorg/bukkit/inventory/ItemStack;)Ljava/lang/String;
/*     */           //   241: astore #11
/*     */           //   243: aload_0
/*     */           //   244: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   247: aload #6
/*     */           //   249: ldc '%s [ITEM_LISTED]: %s'
/*     */           //   251: iconst_2
/*     */           //   252: anewarray java/lang/Object
/*     */           //   255: dup
/*     */           //   256: iconst_0
/*     */           //   257: aload #7
/*     */           //   259: aastore
/*     */           //   260: dup
/*     */           //   261: iconst_1
/*     */           //   262: ldc 'message.log.item_listed'
/*     */           //   264: getstatic com/spawnchunk/auctionhouse/config/Config.locale : Ljava/lang/String;
/*     */           //   267: invokestatic translate : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   270: bipush #6
/*     */           //   272: anewarray java/lang/Object
/*     */           //   275: dup
/*     */           //   276: iconst_0
/*     */           //   277: aload_0
/*     */           //   278: getfield val$seller : Ljava/lang/String;
/*     */           //   281: aastore
/*     */           //   282: dup
/*     */           //   283: iconst_1
/*     */           //   284: iload #9
/*     */           //   286: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */           //   289: aastore
/*     */           //   290: dup
/*     */           //   291: iconst_2
/*     */           //   292: aload #10
/*     */           //   294: aastore
/*     */           //   295: dup
/*     */           //   296: iconst_3
/*     */           //   297: aload #11
/*     */           //   299: aastore
/*     */           //   300: dup
/*     */           //   301: iconst_4
/*     */           //   302: aload_0
/*     */           //   303: getfield val$price : F
/*     */           //   306: invokestatic valueOf : (F)Ljava/lang/Float;
/*     */           //   309: aastore
/*     */           //   310: dup
/*     */           //   311: iconst_5
/*     */           //   312: aload_0
/*     */           //   313: getfield val$world : Ljava/lang/String;
/*     */           //   316: aastore
/*     */           //   317: invokestatic populate : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   320: aastore
/*     */           //   321: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   324: invokevirtual logMessage : (Ljava/lang/String;Ljava/lang/String;)V
/*     */           //   327: return
/*     */           //   328: getstatic com/spawnchunk/auctionhouse/config/Config.log_sold : Z
/*     */           //   331: ifeq -> 439
/*     */           //   334: aload_0
/*     */           //   335: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   338: aload_0
/*     */           //   339: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   342: invokevirtual getEnchants : (Lorg/bukkit/inventory/ItemStack;)Ljava/lang/String;
/*     */           //   345: astore #11
/*     */           //   347: aload_0
/*     */           //   348: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   351: aload #6
/*     */           //   353: ldc '%s [ITEM_SOLD]: %s'
/*     */           //   355: iconst_2
/*     */           //   356: anewarray java/lang/Object
/*     */           //   359: dup
/*     */           //   360: iconst_0
/*     */           //   361: aload #7
/*     */           //   363: aastore
/*     */           //   364: dup
/*     */           //   365: iconst_1
/*     */           //   366: ldc 'message.log.item_sold'
/*     */           //   368: getstatic com/spawnchunk/auctionhouse/config/Config.locale : Ljava/lang/String;
/*     */           //   371: invokestatic translate : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   374: bipush #7
/*     */           //   376: anewarray java/lang/Object
/*     */           //   379: dup
/*     */           //   380: iconst_0
/*     */           //   381: aload_0
/*     */           //   382: getfield val$seller : Ljava/lang/String;
/*     */           //   385: aastore
/*     */           //   386: dup
/*     */           //   387: iconst_1
/*     */           //   388: iload #9
/*     */           //   390: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */           //   393: aastore
/*     */           //   394: dup
/*     */           //   395: iconst_2
/*     */           //   396: aload #10
/*     */           //   398: aastore
/*     */           //   399: dup
/*     */           //   400: iconst_3
/*     */           //   401: aload #11
/*     */           //   403: aastore
/*     */           //   404: dup
/*     */           //   405: iconst_4
/*     */           //   406: aload_0
/*     */           //   407: getfield val$buyer : Ljava/lang/String;
/*     */           //   410: aastore
/*     */           //   411: dup
/*     */           //   412: iconst_5
/*     */           //   413: aload_0
/*     */           //   414: getfield val$price : F
/*     */           //   417: invokestatic valueOf : (F)Ljava/lang/Float;
/*     */           //   420: aastore
/*     */           //   421: dup
/*     */           //   422: bipush #6
/*     */           //   424: aload_0
/*     */           //   425: getfield val$world : Ljava/lang/String;
/*     */           //   428: aastore
/*     */           //   429: invokestatic populate : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   432: aastore
/*     */           //   433: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   436: invokevirtual logMessage : (Ljava/lang/String;Ljava/lang/String;)V
/*     */           //   439: return
/*     */           //   440: getstatic com/spawnchunk/auctionhouse/config/Config.log_cancelled : Z
/*     */           //   443: ifeq -> 543
/*     */           //   446: aload_0
/*     */           //   447: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   450: aload_0
/*     */           //   451: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   454: invokevirtual getEnchants : (Lorg/bukkit/inventory/ItemStack;)Ljava/lang/String;
/*     */           //   457: astore #11
/*     */           //   459: aload_0
/*     */           //   460: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   463: aload #6
/*     */           //   465: ldc '%s [ITEM_CANCELLED]: %s'
/*     */           //   467: iconst_2
/*     */           //   468: anewarray java/lang/Object
/*     */           //   471: dup
/*     */           //   472: iconst_0
/*     */           //   473: aload #7
/*     */           //   475: aastore
/*     */           //   476: dup
/*     */           //   477: iconst_1
/*     */           //   478: ldc 'message.log.item_cancelled'
/*     */           //   480: getstatic com/spawnchunk/auctionhouse/config/Config.locale : Ljava/lang/String;
/*     */           //   483: invokestatic translate : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   486: bipush #6
/*     */           //   488: anewarray java/lang/Object
/*     */           //   491: dup
/*     */           //   492: iconst_0
/*     */           //   493: aload_0
/*     */           //   494: getfield val$seller : Ljava/lang/String;
/*     */           //   497: aastore
/*     */           //   498: dup
/*     */           //   499: iconst_1
/*     */           //   500: iload #9
/*     */           //   502: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */           //   505: aastore
/*     */           //   506: dup
/*     */           //   507: iconst_2
/*     */           //   508: aload #10
/*     */           //   510: aastore
/*     */           //   511: dup
/*     */           //   512: iconst_3
/*     */           //   513: aload #11
/*     */           //   515: aastore
/*     */           //   516: dup
/*     */           //   517: iconst_4
/*     */           //   518: aload_0
/*     */           //   519: getfield val$price : F
/*     */           //   522: invokestatic valueOf : (F)Ljava/lang/Float;
/*     */           //   525: aastore
/*     */           //   526: dup
/*     */           //   527: iconst_5
/*     */           //   528: aload_0
/*     */           //   529: getfield val$world : Ljava/lang/String;
/*     */           //   532: aastore
/*     */           //   533: invokestatic populate : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   536: aastore
/*     */           //   537: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   540: invokevirtual logMessage : (Ljava/lang/String;Ljava/lang/String;)V
/*     */           //   543: return
/*     */           //   544: getstatic com/spawnchunk/auctionhouse/config/Config.log_returned : Z
/*     */           //   547: ifeq -> 636
/*     */           //   550: aload_0
/*     */           //   551: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   554: aload_0
/*     */           //   555: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   558: invokevirtual getEnchants : (Lorg/bukkit/inventory/ItemStack;)Ljava/lang/String;
/*     */           //   561: astore #11
/*     */           //   563: aload_0
/*     */           //   564: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   567: aload #6
/*     */           //   569: ldc '%s [ITEM_RETURNED]: %s'
/*     */           //   571: iconst_2
/*     */           //   572: anewarray java/lang/Object
/*     */           //   575: dup
/*     */           //   576: iconst_0
/*     */           //   577: aload #7
/*     */           //   579: aastore
/*     */           //   580: dup
/*     */           //   581: iconst_1
/*     */           //   582: ldc 'message.log.item_returned'
/*     */           //   584: getstatic com/spawnchunk/auctionhouse/config/Config.locale : Ljava/lang/String;
/*     */           //   587: invokestatic translate : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   590: iconst_5
/*     */           //   591: anewarray java/lang/Object
/*     */           //   594: dup
/*     */           //   595: iconst_0
/*     */           //   596: iload #9
/*     */           //   598: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */           //   601: aastore
/*     */           //   602: dup
/*     */           //   603: iconst_1
/*     */           //   604: aload #10
/*     */           //   606: aastore
/*     */           //   607: dup
/*     */           //   608: iconst_2
/*     */           //   609: aload #11
/*     */           //   611: aastore
/*     */           //   612: dup
/*     */           //   613: iconst_3
/*     */           //   614: aload_0
/*     */           //   615: getfield val$seller : Ljava/lang/String;
/*     */           //   618: aastore
/*     */           //   619: dup
/*     */           //   620: iconst_4
/*     */           //   621: aload_0
/*     */           //   622: getfield val$world : Ljava/lang/String;
/*     */           //   625: aastore
/*     */           //   626: invokestatic populate : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   629: aastore
/*     */           //   630: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   633: invokevirtual logMessage : (Ljava/lang/String;Ljava/lang/String;)V
/*     */           //   636: return
/*     */           //   637: getstatic com/spawnchunk/auctionhouse/config/Config.log_purged : Z
/*     */           //   640: ifeq -> 740
/*     */           //   643: aload_0
/*     */           //   644: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   647: aload_0
/*     */           //   648: getfield val$item : Lorg/bukkit/inventory/ItemStack;
/*     */           //   651: invokevirtual getEnchants : (Lorg/bukkit/inventory/ItemStack;)Ljava/lang/String;
/*     */           //   654: astore #11
/*     */           //   656: aload_0
/*     */           //   657: getfield this$0 : Lcom/spawnchunk/auctionhouse/listeners/AuctionListener;
/*     */           //   660: aload #6
/*     */           //   662: ldc '%s [ITEM_PURGED]: %s'
/*     */           //   664: iconst_2
/*     */           //   665: anewarray java/lang/Object
/*     */           //   668: dup
/*     */           //   669: iconst_0
/*     */           //   670: aload #7
/*     */           //   672: aastore
/*     */           //   673: dup
/*     */           //   674: iconst_1
/*     */           //   675: ldc 'message.log.item_purged'
/*     */           //   677: getstatic com/spawnchunk/auctionhouse/config/Config.locale : Ljava/lang/String;
/*     */           //   680: invokestatic translate : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */           //   683: bipush #6
/*     */           //   685: anewarray java/lang/Object
/*     */           //   688: dup
/*     */           //   689: iconst_0
/*     */           //   690: aload_0
/*     */           //   691: getfield val$seller : Ljava/lang/String;
/*     */           //   694: aastore
/*     */           //   695: dup
/*     */           //   696: iconst_1
/*     */           //   697: iload #9
/*     */           //   699: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */           //   702: aastore
/*     */           //   703: dup
/*     */           //   704: iconst_2
/*     */           //   705: aload #10
/*     */           //   707: aastore
/*     */           //   708: dup
/*     */           //   709: iconst_3
/*     */           //   710: aload #11
/*     */           //   712: aastore
/*     */           //   713: dup
/*     */           //   714: iconst_4
/*     */           //   715: aload_0
/*     */           //   716: getfield val$price : F
/*     */           //   719: invokestatic valueOf : (F)Ljava/lang/Float;
/*     */           //   722: aastore
/*     */           //   723: dup
/*     */           //   724: iconst_5
/*     */           //   725: aload_0
/*     */           //   726: getfield val$world : Ljava/lang/String;
/*     */           //   729: aastore
/*     */           //   730: invokestatic populate : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   733: aastore
/*     */           //   734: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */           //   737: invokevirtual logMessage : (Ljava/lang/String;Ljava/lang/String;)V
/*     */           //   740: return
/*     */           //   741: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #64	-> 0
/*     */           //   #65	-> 36
/*     */           //   #66	-> 46
/*     */           //   #67	-> 56
/*     */           //   #68	-> 64
/*     */           //   #69	-> 83
/*     */           //   #70	-> 102
/*     */           //   #71	-> 116
/*     */           //   #72	-> 135
/*     */           //   #73	-> 146
/*     */           //   #75	-> 154
/*     */           //   #77	-> 160
/*     */           //   #78	-> 169
/*     */           //   #79	-> 178
/*     */           //   #82	-> 224
/*     */           //   #83	-> 230
/*     */           //   #84	-> 243
/*     */           //   #86	-> 327
/*     */           //   #89	-> 328
/*     */           //   #90	-> 334
/*     */           //   #91	-> 347
/*     */           //   #93	-> 439
/*     */           //   #96	-> 440
/*     */           //   #97	-> 446
/*     */           //   #98	-> 459
/*     */           //   #100	-> 543
/*     */           //   #103	-> 544
/*     */           //   #104	-> 550
/*     */           //   #105	-> 563
/*     */           //   #107	-> 636
/*     */           //   #110	-> 637
/*     */           //   #111	-> 643
/*     */           //   #112	-> 656
/*     */           //   #114	-> 740
/*     */           //   #118	-> 741
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   243	84	11	enchants	Ljava/lang/String;
/*     */           //   347	92	11	enchants	Ljava/lang/String;
/*     */           //   459	84	11	enchants	Ljava/lang/String;
/*     */           //   563	73	11	enchants	Ljava/lang/String;
/*     */           //   656	84	11	enchants	Ljava/lang/String;
/*     */           //   46	695	1	dateFormat	Ljava/text/DateFormat;
/*     */           //   56	685	2	timeFormat	Ljava/text/DateFormat;
/*     */           //   64	677	3	date	Ljava/util/Date;
/*     */           //   83	658	4	path	Ljava/lang/String;
/*     */           //   102	639	5	file	Ljava/lang/String;
/*     */           //   116	625	6	filename	Ljava/lang/String;
/*     */           //   135	606	7	time	Ljava/lang/String;
/*     */           //   146	595	8	dir	Ljava/io/File;
/*     */           //   169	572	9	count	I
/*     */           //   178	563	10	name	Ljava/lang/String;
/*     */           //   0	742	0	this	Lcom/spawnchunk/auctionhouse/listeners/AuctionListener$1;
/*     */         }
/* 119 */       }).runTaskAsynchronously((Plugin)AuctionHouse.plugin);
/*     */   }
/*     */   
/*     */   private String getEnchants(ItemStack item) {
/* 123 */     String enchants = "";
/* 124 */     if (ItemUtil.hasEnchants(item)) {
/* 125 */       List<String> enchantments = ItemUtil.getEnchants(item);
/* 126 */       StringJoiner joiner = new StringJoiner(", ");
/* 127 */       for (String enchant : enchantments) {
/* 128 */         joiner.add(enchant);
/*     */       }
/* 130 */       enchants = String.format(" [%s]", new Object[] { joiner.toString() });
/*     */     } 
/* 132 */     return enchants;
/*     */   }
/*     */   private void logMessage(String filename, String message) {
/*     */     
/* 136 */     try { FileWriter fw = new FileWriter(filename, true); 
/* 137 */       try { BufferedWriter bw = new BufferedWriter(fw); 
/* 138 */         try { PrintWriter out = new PrintWriter(bw);
/*     */           
/* 140 */           try { out.println(MessageUtil.nocolor(message));
/* 141 */             out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  bw.close(); } catch (Throwable throwable) { try { bw.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  fw.close(); } catch (Throwable throwable) { try { fw.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 142 */     { e.printStackTrace(); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\listeners\AuctionListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */