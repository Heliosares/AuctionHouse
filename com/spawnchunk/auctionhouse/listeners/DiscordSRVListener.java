/*    */ package com.spawnchunk.auctionhouse.listeners;
/*    */ 
/*    */ import com.spawnchunk.auctionhouse.AuctionHouse;
/*    */ import github.scarsz.discordsrv.api.ListenerPriority;
/*    */ import github.scarsz.discordsrv.api.Subscribe;
/*    */ import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
/*    */ import github.scarsz.discordsrv.api.events.DiscordGuildMessageSentEvent;
/*    */ import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DiscordSRVListener
/*    */ {
/*    */   @Subscribe(priority = ListenerPriority.MONITOR)
/*    */   public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
/* 19 */     Message message = event.getMessage();
/* 20 */     AuctionHouse.logger.info("Received a chat message on Discord: " + message);
/*    */   }
/*    */   
/*    */   @Subscribe(priority = ListenerPriority.MONITOR)
/*    */   public void discordMessageSent(DiscordGuildMessageSentEvent event) {
/* 25 */     Message message = event.getMessage();
/* 26 */     AuctionHouse.logger.info("Sent a chat message to Discord: " + message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\listeners\DiscordSRVListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */