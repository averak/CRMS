package dev.abelab.crms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.ReservationSample;
import dev.abelab.crms.db.mapper.ReservationMapper;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BaseException;
import dev.abelab.crms.exception.NotFoundException;
import dev.abelab.crms.exception.ConflictException;

/**
 * ReservationRepository Unit Test
 */
class ReservationRepository_UT extends AbstractRepository_UT {

	@Injectable
	ReservationMapper reservationMapper;

	@Tested
	ReservationRepository reservationRepository;

	private final Reservation reservation = ReservationSample.builder().build();

	/**
	 * Test for insert reservation
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class InsertReservationTest {

		@Test
		void 正_予約を保存する() throws Exception {
			new Expectations(reservationRepository) {
				{
					reservationRepository.insert(reservation);
					result = reservation.getId();
				}
			};

			assertThat(reservationRepository.insert(reservation)).isEqualTo(reservation.getId());
		}

		@Test
		void 異_指定した予約時間が既に予約済み() throws Exception {
			new Expectations(reservationRepository) {
				{
					reservationRepository.insert(reservation);
					result = new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME);
				}
			};

			final var exception = assertThrows(BaseException.class, () -> reservationRepository.insert(reservation));
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONFLICT_RESERVATION_TIME);
		}

	}

	/**
	 * Test for select reservation
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class SelectReservationTest {

		@Test
		void 正_予約が存在する() throws Exception {
			new Expectations(reservationRepository) {
				{
					reservationRepository.selectById(reservation.getId());
					result = reservation;
				}
			};

			assertThat(reservationRepository.selectById(reservation.getId())).isEqualTo(reservation);
		}

		@Test
		void 異_予約が存在しない() throws Exception {
			new Expectations(reservationRepository) {
				{
					reservationRepository.selectById(anyInt);
					result = new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION);
				}
			};

			final var exception = assertThrows(BaseException.class, () -> reservationRepository.selectById(anyInt()));
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_RESERVATION);
		}

	}

}
