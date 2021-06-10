package dev.abelab.crs.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.Test;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.db.entity.UserSample;
import dev.abelab.crs.db.mapper.UserMapper;
import dev.abelab.crs.exception.ErrorCode;
import dev.abelab.crs.exception.BaseException;
import dev.abelab.crs.exception.NotFoundException;
import dev.abelab.crs.exception.ConflictException;

/**
 * UserRepository Unit Test
 */
class UserRepository_UT extends AbstractRepository_UT {

	@Injectable
	UserMapper userMapper;

	@Tested
	UserRepository userRepository;

	private final User user = UserSample.builder().build();

	@Test
	void 正_ユーザを保存する() {
		new Expectations(this.userRepository) {
			{
				userRepository.insert(user);
				result = user.getId();
			}
		};

		assertThat(this.userRepository.insert(this.user)).isEqualTo(this.user.getId());
	}

	@Test
	void 異_メールアドレスが既に登録済み() {
		new Expectations(this.userRepository) {
			{
				userRepository.insert(user);
				result = new ConflictException(ErrorCode.CONFLICT_EMAIL);
			}
		};

		final var exception = assertThrows(BaseException.class, () -> userRepository.insert(this.user));
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONFLICT_EMAIL);
	}

	@Test
	void 正_ユーザが存在する() {
		new Expectations(this.userRepository) {
			{
				userRepository.selectById(user.getId());
				result = user;
			}
		};

		assertThat(this.userRepository.selectById(this.user.getId())).isEqualTo(this.user);
	}

	@Test
	void 異_ユーザが存在しない() {
		new Expectations(this.userRepository) {
			{
				userRepository.selectById(anyInt);
				result = new NotFoundException(ErrorCode.NOT_FOUND_USER);
			}
		};

		final var exception = assertThrows(BaseException.class, () -> userRepository.selectById(anyInt()));
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);
	}

}
