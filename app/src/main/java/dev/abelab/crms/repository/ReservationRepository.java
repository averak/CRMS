package dev.abelab.crms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.ReservationExample;
import dev.abelab.crms.db.mapper.ReservationMapper;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class ReservationRepository {

    private final ReservationMapper reservationMapper;

    /**
     * 予約を作成
     *
     * @param reservation 予約
     *
     * @return 予約ID
     */
    public int insert(Reservation reservation) {
        // FIXME: 重複確認
        // throw new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME);
        return this.reservationMapper.insertSelective(reservation);
    }

    /**
     * 予約を削除
     *
     * @param reservationId 予約ID
     */
    public void deleteById(final int reservationId) {
        // FIXME: 存在確認
        // throw new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION);
        this.reservationMapper.deleteByPrimaryKey(reservationId);
    }

    /**
     * IDから予約を検索
     *
     * @param reservationId 予約ID
     *
     * @return 予約
     */
    public Reservation  selectById(final int reservationId) {
        return Optional.ofNullable(this.reservationMapper.selectByPrimaryKey(reservationId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
    }

}
