package com.spawnchunk.auctionhouse.nms;

import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public interface NMS {
  String getNBTString(ItemStack paramItemStack);
  
  ItemStack setNBTString(ItemStack paramItemStack, String paramString);
  
  ItemStack addNBTLocator(ItemStack paramItemStack);
  
  boolean hasNBTLocator(ItemStack paramItemStack);
  
  ItemStack getCustomSkull(String paramString);
  
  boolean isDye(ItemStack paramItemStack);
  
  boolean isContainer(ItemStack paramItemStack);
  
  Map<Integer, ItemStack> getContainerItems(ItemStack paramItemStack);
  
  List<String> getMobs(ItemStack paramItemStack);
  
  int getCustomModelData(ItemStack paramItemStack);
  
  ItemStack setCustomModelData(ItemStack paramItemStack, int paramInt);
  
  ItemStack setLore(ItemStack paramItemStack, List<String> paramList);
  
  ItemStack setDisplayName(ItemStack paramItemStack, String paramString);
  
  boolean hasPersistentDataKey(ItemStack paramItemStack, String paramString);
  
  Object getPersistentDataKey(ItemStack paramItemStack, String paramString);
  
  Map<String, Object> getPersistentData(ItemStack paramItemStack);
  
  ItemStack setPersistentDataKey(ItemStack paramItemStack, String paramString, Object paramObject);
  
  ItemStack deserialize(String paramString);
  
  ItemStack updateItem(ItemStack paramItemStack);
  
  String parseInternal(String paramString);
}


/* Location:              C:\Users\Kyler\Downloads\AuctionHouse-1.19-3.3.1.jar!\com\spawnchunk\auctionhouse\nms\NMS.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */