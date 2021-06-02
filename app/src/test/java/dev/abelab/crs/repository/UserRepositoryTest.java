package dev.abelab.crs.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.db.mapper.UserMapper;

class UserRepositoryTest {

	@Injectable
	UserMapper userMapper;

	@Tested
	UserRepository userRepository;

	private final User userSample = User.builder() //
		.id(anyInt()) //
		.firstName(anyString()) //
		.lastName(anyString()) //
		.password(anyString()) //
		.roleId(anyInt()) //
		.build();

	@Test
	void 正_ユーザを保存する() {
		new Expectations(this.userRepository) {
			{
				userRepository.insert(userSample);
				result = userSample.getId();
			}
		};

		assertThat(this.userRepository.insert(this.userSample)).isEqualTo(this.userSample.getId());
	}

	@Test
	void 正_ユーザが存在する() {
		new Expectations(this.userRepository) {
			{
				userRepository.selectById(userSample.getId());
				result = userSample;
			}
		};

		assertThat(this.userRepository.selectById(this.userSample.getId())).isEqualTo(this.userSample);
	}

	@Test
	void 正_ユーザが存在しない() {
		new Expectations(this.userRepository) {
			{
				userRepository.selectById(userSample.getId());
				result = null;
			}
		};

		assertThat(this.userRepository.selectById(this.userSample.getId())).isNull();
	}

}
