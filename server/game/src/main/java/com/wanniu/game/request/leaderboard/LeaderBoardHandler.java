package com.wanniu.game.request.leaderboard;

import com.wanniu.core.game.entity.GClientEvent;
import com.wanniu.core.game.protocol.PomeloRequest;
import com.wanniu.core.game.protocol.PomeloResponse;
import com.wanniu.core.logfs.Out;
import com.wanniu.game.GWorld;
import com.wanniu.game.leaderBoard.LeaderBoardDetail;
import com.wanniu.game.leaderBoard.LeaderBoardProto;
import com.wanniu.game.player.WNPlayer;
import com.wanniu.game.rank.RankType;
import io.netty.util.internal.StringUtil;

import java.io.IOException;


@GClientEvent("area.leaderBoardHandler.leaderBoardRequest")
public class LeaderBoardHandler
        extends PomeloRequest {
    public short getType() {
        return 785;
    }

    public PomeloResponse request() throws Exception {
        final WNPlayer player = (WNPlayer) this.pak.getPlayer();
        final pomelo.area.LeaderBoardHandler.LeaderBoardRequest req = pomelo.area.LeaderBoardHandler.LeaderBoardRequest.parseFrom(this.pak.getRemaingBytes());
        return new PomeloResponse() {
            protected void write() throws IOException {
                pomelo.area.LeaderBoardHandler.LeaderBoardResponse.Builder res = pomelo.area.LeaderBoardHandler.LeaderBoardResponse.newBuilder();
                RankType type = RankType.valueOf(req.getC2SKind());
                if (type == null || type.getHandler() == null) {
                    Out.warn(new Object[]{"未实现的排行榜:", Integer.valueOf(this.val$req.getC2SKind())});

                    res.setS2CCode(200);
                    this.body.writeBytes(res.build().toByteArray());

                    return;
                }
                LeaderBoardProto result = type.getHandler().getRankData(player, req.getC2SSeason());
                if (null == result) {

                    res.setS2CCode(200);
                    this.body.writeBytes(res.build().toByteArray());

                    return;
                }
                res.setS2CCode(200);
                res.addAllS2CLists(result.s2c_lists);
                String rank = "0";
                if (result.s2c_myData.getContentsCount() > 0) {
                    rank = result.s2c_myData.getContents(0);
                }


                if (null == result.s2c_myData || StringUtil.isNullOrEmpty(rank) || Integer.parseInt(rank) <= 0) {
                    boolean isNeedSelfData = true;
                    if (type == RankType.FIGHTPOWER_1 || type == RankType.FIGHTPOWER_3 || type == RankType.FIGHTPOWER_5) {
                        int kindPro = type.getValue() - RankType.FIGHTPOWER_1.getValue() + 1;
                        if (player.getPro() != kindPro) {
                            isNeedSelfData = false;
                        }
                    }
                    if (isNeedSelfData && (type == RankType.GUILD_BOSS_SINGLE || type == RankType.GUILD_BOSS_GUILD || type == RankType.GUILD_BOSS_PRE_SINGLE || type == RankType.GUILD_BOSS_PRE_GUILD)) {
                        isNeedSelfData = false;
                    }

                    if (isNeedSelfData) {
                        LeaderBoardDetail detail = new LeaderBoardDetail();
                        detail.memberId = type.getHandler().getSelfId(player);
                        detail.rank = type.getHandler().getSeasonRank(GWorld.__SERVER_ID, req.getC2SSeason(), detail.memberId);
                        if (detail.rank > 0L) {
                            detail.score = type.getHandler().getSeasonScore(GWorld.__SERVER_ID, req.getC2SSeason(), detail.memberId);

                            pomelo.area.LeaderBoardHandler.LeaderBoardData myData = type.getHandler().genBuilderInfo(detail.memberId, (int) detail.score, (int) detail.rank);
                            if (null != myData && myData.getContentsCount() > 1) {
                                result.s2c_myData = myData;
                            }
                        }
                    }
                }

                if (result.s2c_lists.size() > 0 && null != result.s2c_myData && result.s2c_myData.getContentsCount() > 0) {
                    res.setS2CMyData(result.s2c_myData);
                }
                this.body.writeBytes(res.build().toByteArray());
            }
        };
    }
}


