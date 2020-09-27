package com.wanniu.game.poes;

import com.wanniu.core.game.entity.GEntity;
import com.wanniu.game.DBTable;

@DBTable("player_temp_data")
public class PlayerTempPO extends GEntity {
    public int historyAreaId;

    public float historyX;

    public float historyY;

    public float historyDirection;

    public int bornAreaId;

    public float bornX;

    public float bornY;

    public int areaId;

    public float x;

    public float y;

    public float direction;

    public int hp;

    public int mp;

    public String teamId;

    public int sendMailItemNum;
}


