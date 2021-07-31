package dev.abelab.crms.client;

import java.util.List;
import java.lang.StringBuilder;

import org.springframework.stereotype.Component;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;

import lombok.extern.slf4j.Slf4j;
import dev.abelab.crms.model.ReservationWithUserModel;
import dev.abelab.crms.enums.ReservationActionEnum;
import dev.abelab.crms.property.SlackProperty;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.InternalServerErrorException;
import dev.abelab.crms.util.UserUtil;
import dev.abelab.crms.util.DateTimeUtil;

@Slf4j
@Component
public class SlackClient {

    private final Slack slack;

    private final SlackProperty slackProperty;

    public SlackClient(SlackProperty slackProperty) {
        this.slackProperty = slackProperty;
        this.slack = Slack.getInstance();
    }

    /**
     * 抽選結果を送信
     *
     * @param reservations 予約（+ユーザ）一覧
     */
    public void sendLotteryResult(final List<ReservationWithUserModel> reservations) {
        final var builder = new StringBuilder();

        builder.append("【明日の予約】\n");

        // 予約一覧
        if (reservations.isEmpty()) {
            builder.append("この日の予約はありません");
        } else {
            reservations.stream().forEach(reservation -> {
                builder.append(UserUtil.getFullName(reservation.getUser())).append("  ");
                builder.append(DateTimeUtil.convertTimeToString(reservation.getStartAt())).append(" - ");
                builder.append(DateTimeUtil.convertTimeToString(reservation.getFinishAt())).append("\n");
            });
        }

        this.sendMessage(builder.toString());
    }

    /**
     * 予約変更を通知
     *
     * @param reservation 予約（+ユーザ）
     *
     * @param action      予約アクション
     */
    public void sendEditReservationNotification(final ReservationWithUserModel reservation, final ReservationActionEnum action) {
        final var builder = new StringBuilder();

        switch (action) {
            case REGISTERED:
                builder.append("【予約追加】\n");
                break;
            case CHANGED:
                builder.append("【予約変更】\n");
                break;
            case DELETED:
                builder.append("【予約削除】\n");
                break;
            default:
                break;
        }

        builder.append(DateTimeUtil.convertDateToString(DateTimeUtil.getNextDate())).append("\n");
        builder.append(UserUtil.getFullName(reservation.getUser())).append("  ");
        builder.append(DateTimeUtil.convertTimeToString(reservation.getStartAt())).append(" - ");
        builder.append(DateTimeUtil.convertTimeToString(reservation.getFinishAt())).append("\n");

        this.sendMessage(builder.toString());

    }

    /**
     * メッセージ送信
     *
     * @param message 送信メッセージ
     */
    public void sendMessage(String message) {
        if (!this.slackProperty.isEnabled()) {
            return;
        }

        final var payload = Payload.builder().text(message).build();
        try {
            final var response = this.slack.send(this.slackProperty.getWebhookUrl(), payload);
            log.info(response.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException(ErrorCode.FAILED_TO_SEND_SLACK);
        }
    }

}
