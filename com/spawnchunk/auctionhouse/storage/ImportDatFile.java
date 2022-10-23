/*    */ package com.spawnchunk.auctionhouse.storage;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import com.spawnchunk.auctionhouse.modules.Listing;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.TreeMap;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImportDatFile
/*    */ {
/*    */   public static void importData() {
/* 20 */     List<String> validWorlds = new ArrayList<>();
/* 21 */     for (World world : Bukkit.getServer().getWorlds()) validWorlds.add(world.getName()); 
/* 22 */     int count = 0;
/* 23 */     int error = 0;
/* 24 */     String path = AuctionHouse.plugin.getDataFolder().getPath();
/* 25 */     String url = path + path + "auctions.dat";
/* 26 */     String url2 = path + path + "auctions.dat.old";
/* 27 */     File dat = new File(url);
/* 28 */     if (dat.exists()) {
/* 29 */       AuctionHouse.logger.info("Found existing auctions.dat, attempting import...");
/*    */       
/* 31 */       TreeMap<Long, String> map = AuctionsStorage.loadAuctionsFile();
/* 32 */       if (map == null)
/* 33 */         return;  for (Long id : map.keySet()) {
/* 34 */         Listing listing = new Listing(map.get(id));
/* 35 */         ItemStack item = listing.getItem();
/* 36 */         if (item != null) {
/*    */           
/* 38 */           item = AuctionHouse.nms.updateItem(item);
/* 39 */           listing.setItem(item);
/* 40 */           if (!item.getType().equals(Material.CAVE_AIR) && !item.getType().equals(Material.AIR) && !item.getType().equals(Material.VOID_AIR)) {
/*    */             
/* 42 */             if (!validWorlds.contains(listing.getWorld())) listing.setWorld(((World)Bukkit.getServer().getWorlds().get(0)).getName());
/*    */             
/* 44 */             boolean result = AuctionHouse.listings.importListing(listing, id.longValue());
/* 45 */             if (result) {
/* 46 */               count++; continue;
/*    */             } 
/* 48 */             error++;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 53 */       AuctionHouse.logger.info(String.format("Imported %d listing%s", new Object[] { Integer.valueOf(count), (count != 1) ? "s" : "" }));
/* 54 */       if (error > 0) AuctionHouse.logger.info(String.format("Could not read %d listing%s", new Object[] { Integer.valueOf(error), (error != 1) ? "s" : "" }));
/*    */       
/* 56 */       File old = new File(url2);
/* 57 */       if (dat.renameTo(old)) AuctionHouse.logger.info("Renamed auctions.dat to auctions.dat.old"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\storage\ImportDatFile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */