package dev.abelab.crms.client;

import java.lang.StringBuilder;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.List;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;

import lombok.extern.slf4j.Slf4j;
import dev.abelab.crms.model.ReservationAndUserModel;
import dev.abelab.crms.property.SlackProperty;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.InternalServerErrorException;

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
     */
    public void sendLotteryResult(final List<ReservationAndUserModel> reservationsAndUsersModel) {
        final var builder = new StringBuilder();
        final var timeFormatter = new SimpleDateFormat("HH:mm", Locale.JAPAN);
        reservationsAndUsersModel.stream().forEach(reservationAndUserModel -> {
            final var reservation = reservationAndUserModel.getReservation();
            final var user = reservationAndUserModel.getUser();
            builder.append(String.format("%s %s", user.getLastName(), user.getFirstName())).append("  ") //
                .append(timeFormatter.format(reservation.getStartAt())).append(" - ") //
                .append(timeFormatter.format(reservation.getFinishAt())) //
                .append("\n");
        });

        final var dateFormatter = new SimpleDateFormat("MM月dd日(E)", Locale.JAPAN);
        final var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final var message = dateFormatter.format(calendar.getTime()) + "\n" + builder.toString();
        this.sendMessage(message);
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
