package dev.abelab.crs.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.db.entity.UserSample;
import dev.abelab.crs.db.mapper.UserMapper;

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
	void 正_ユーザが存在しない() {
		new Expectations(this.userRepository) {
			{
				userRepository.selectById(user.getId());
				result = null;
			}
		};

		assertThat(this.userRepository.selectById(this.user.getId())).isNull();
	}

}
