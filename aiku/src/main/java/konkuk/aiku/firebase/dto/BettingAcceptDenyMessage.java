package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.*;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public class BettingAcceptDenyMessage extends Message{
    protected String title;

    protected Long scheduleId;
    protected String scheduleName;

    protected Long bettingId;
    protected BettingType bettingType;
    protected BettingStatus bettingStatus;

    protected Long bettorId;
    protected String bettorName;
    protected String bettorImg;
    protected UserImgData.ImgType bettorImgData;
    protected String bettorColorCode;

    protected Long targetId;
    protected String targetName;
    protected String targetImg;
    protected UserImgData.ImgType targetImgData;
    protected String targetColorCode;

    protected boolean isAccept;

    public static BettingAcceptDenyMessage createMessage(MessageTitle title, Schedule schedule, Betting betting, Users bettor, Users targetUser, boolean isAccept){
        return BettingAcceptDenyMessage.builder()
                .title(title.getTitle())
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .bettingId(betting.getId())
                .bettingType(betting.getBettingType())
                .bettingStatus(betting.getBettingStatus())
                .bettorId(bettor.getId())
                .bettorName(bettor.getUsername())
                .bettorImg(bettor.getUserImg())
                .bettorImgData(bettor.getUserImgData().getImgData())
                .bettorColorCode(bettor.getUserImgData().getColorCode())
                .targetId(targetUser.getId())
                .targetName(targetUser.getUsername())
                .targetImg(targetUser.getUserImg())
                .targetImgData(targetUser.getUserImgData().getImgData())
                .targetColorCode(targetUser.getUserImgData().getColorCode())
                .isAccept(isAccept)
                .build();
    }
}
