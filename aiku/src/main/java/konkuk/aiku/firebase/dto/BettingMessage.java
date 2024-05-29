package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.*;
import konkuk.aiku.domain.UserImgData.ImgType;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public class BettingMessage extends Message{
    protected String title;

    protected Long scheduleId;
    protected String scheduleName;

    protected Long bettingId;
    protected BettingType bettingType;
    protected BettingStatus bettingStatus;

    protected Long bettorId;
    protected String bettorName;
    protected String bettorImg;
    protected ImgType bettorImgData;
    protected String bettorColorCode;

    protected Long targetId;
    protected String targetName;
    protected String targetImg;
    protected ImgType targetImgData;
    protected String targetColorCode;

    public static BettingMessage createMessage(MessageTitle title, Schedule schedule, Betting betting, Users bettor, Users targetUser){
        return BettingMessage.builder()
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
                .build();
    }
}
