/*    */ package com.spawnchunk.auctionhouse.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ 
/*    */ public class FileUtil {
/*    */   public static boolean backupFile(File fn) {
/*    */     try {
/* 14 */       Path source = Paths.get(fn.getAbsolutePath(), new String[0]);
/* 15 */       Path backup = Paths.get(fn.getAbsolutePath() + ".backup", new String[0]);
/* 16 */       Files.copy(source, backup, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 17 */     } catch (IOException e) {
/* 18 */       return false;
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhous\\util\FileUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */