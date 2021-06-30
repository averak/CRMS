package dev.abelab.crms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.ReservationExample;
import dev.abelab.crms.db.mapper.ReservationMapper;
import dev.abelab.crms.exception.ErrorCode;
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
        return this.reservationMapper.insertSelective(reservation);
    }

    /**
     * 予約を削除
     *
     * @param reservationId 予約ID
     */
    public void deleteById(final int reservationId) {
        if (this.existsById(reservationId)) {
            this.reservationMapper.deleteByPrimaryKey(reservationId);
        } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION);
        }
    }

    /**
     * IDから予約を検索
     *
     * @param reservationId 予約ID
     *
     * @return 予約
     */
    public Reservation selectById(final int reservationId) {
        return Optional.ofNullable(this.reservationMapper.selectByPrimaryKey(reservationId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
    }

    /**
     * ユーザIDから予約一覧を検索
     *
     * @param userId ユーザID
     *
     * @return 予約一覧
     */
    public List<Reservation> selectByUserId(final int userId) {
        final var example = new ReservationExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return this.reservationMapper.selectByExample(example);
    }

    /**
     * 予約一覧を取得
     *
     * @return 予約一覧
     */
    public List<Reservation> findAll() {
        final var example = new ReservationExample();
        example.setOrderByClause("updated_at desc");
        return this.reservationMapper.selectByExample(example);
    }

    /**
     * 予約IDの存在確認
     *
     * @param reservationId 予約ID
     *
     * @return 予約IDが存在するか
     */
    public boolean existsById(final int reservationId) {
        try {
            this.selectById(reservationId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

}
