package dev.abelab.crms.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.enums.ReservationActionEnum;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.request.ReservationUpdateRequest;
import dev.abelab.crms.api.response.ReservationResponse;
import dev.abelab.crms.api.response.ReservationsResponse;
import dev.abelab.crms.api.response.UserResponse;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.logic.ReservationLogic;
import dev.abelab.crms.client.SlackClient;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ModelMapper modelMapper;

    private final UserLogic userLogic;

    private final ReservationLogic reservationLogic;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final SlackClient slackClient;

    /**
     * 予約一覧を取得
     *
     * @param credentials 資格情報
     *
     * @return 予約一覧レスポンス
     */
    @Transactional
    public ReservationsResponse getReservations(final String credentials) {
        // ログインユーザを取得
        this.userLogic.getLoginUser(credentials);

        final var reservations = this.reservationRepository.findAll();
        final var reservationResponses = reservations.stream().map(reservation -> {
            // 予約者を取得
            final var user = this.userRepository.selectById(reservation.getUserId());
            final var reservationResponse = this.modelMapper.map(reservation, ReservationResponse.class);
            reservationResponse.setUser(this.modelMapper.map(user, UserResponse.class));
            return reservationResponse;
        }).collect(Collectors.toList());

        return new ReservationsResponse(reservationResponses);
    }

    /**
     * 予約を作成
     *
     * @param credentials 資格情報
     *
     * @param requestBody 予約作成リクエスト
     */
    @Transactional
    public void createReservation(final String credentials, final ReservationCreateRequest requestBody) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 開始時刻と終了時刻のバリデーション
        this.reservationLogic.validateReservationTime(requestBody.getStartAt(), requestBody.getFinishAt(), loginUser.getId(), 0);

        // 予約の作成
        final var reservation = this.modelMapper.map(requestBody, Reservation.class);
        reservation.setUserId(loginUser.getId());
        this.reservationRepository.insert(reservation);

        // Slackに通知
        this.slackClient.sendEditReservationNotification(this.reservationLogic.getWithUser(reservation), ReservationActionEnum.REGISTERED);
    }

    /**
     * 予約を更新
     *
     * @param credentials 資格情報
     *
     * @param requestBody 予約更新リクエスト
     */
    @Transactional
    public void updateReservation(final String credentials, final int reservationId, final ReservationUpdateRequest requestBody) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 権限をチェック
        this.reservationLogic.checkEditPermission(reservationId, loginUser.getId());

        // 開始時刻と終了時刻のバリデーション
        final var userId = this.reservationRepository.selectById(reservationId).getUserId();
        this.reservationLogic.validateReservationTime(requestBody.getStartAt(), requestBody.getFinishAt(), userId, reservationId);

        final var reservation = this.reservationRepository.selectById(reservationId);
        reservation.setStartAt(requestBody.getStartAt());
        reservation.setFinishAt(requestBody.getFinishAt());
        this.reservationRepository.update(reservation);

        // Slackに通知
        this.slackClient.sendEditReservationNotification(this.reservationLogic.getWithUser(reservation), ReservationActionEnum.CHANGED);
    }

    /**
     * 予約を削除
     *
     * @param credentials   資格情報
     *
     * @param reservationId 予約ID
     */
    @Transactional
    public void deleteReservation(final String credentials, final int reservationId) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 権限をチェック
        this.reservationLogic.checkEditPermission(reservationId, loginUser.getId());

        // 削除可能な予約かチェック
        final var reservation = this.reservationRepository.selectById(reservationId);
        this.reservationLogic.checkDeletableReservation(reservation);

        this.reservationRepository.deleteById(reservationId);

        // Slackに通知
        this.slackClient.sendEditReservationNotification(this.reservationLogic.getWithUser(reservation), ReservationActionEnum.DELETED);
    }

    /**
     * 予約の抽選
     */
    @Transactional
    public void lotteryReservations() {
        // 翌日の予約一覧を取得
        final var reservations = this.reservationLogic.getNextDayReservations();

        // Slackに通知
        this.slackClient.sendLotteryResult(reservations);
    }

}
