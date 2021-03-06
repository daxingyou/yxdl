package com.wanniu.game.friend.data;

import com.wanniu.game.data.GameData;
import com.wanniu.game.data.SShopCO;

import java.util.ArrayList;
import java.util.Map;


public class FriendShopProps {
    public static SShopCO findPropByItemIDAndIsValid(int itemId, int isValid) {
        for (Map.Entry<Integer, SShopCO> node : (Iterable<Map.Entry<Integer, SShopCO>>) GameData.SShops.entrySet()) {
            SShopCO data = node.getValue();
            if (data.isValid == isValid && data.itemID == itemId) {
                return data;
            }
        }
        return null;
    }

    public static ArrayList<SShopCO> findPropByIsValid(int isValid) {
        ArrayList<SShopCO> list = new ArrayList<>();
        for (Map.Entry<Integer, SShopCO> node : (Iterable<Map.Entry<Integer, SShopCO>>) GameData.SShops.entrySet()) {
            SShopCO data = node.getValue();
            if (data.isValid == isValid) {
                list.add(data);
            }
        }
        return list;
    }
}


