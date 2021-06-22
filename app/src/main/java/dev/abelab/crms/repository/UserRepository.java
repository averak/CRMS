package dev.abelab.crms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.db.entity.UserExample;
import dev.abelab.crms.db.mapper.UserMapper;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final UserMapper userMapper;

    /**
     * ユーザを作成
     *
     * @param user ユーザ
     *
     * @return ユーザID
     */
    public int insert(final User user) {
        if (this.existsByEmail(user.getEmail())) {
            throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        }
        return this.userMapper.insertSelective(user);
    }

    /**
     * ユーザを更新
     *
     * @param user ユーザ
     */
    public void update(final User user) {
        user.setUpdatedAt(null);
        this.userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * ユーザを削除
     *
     * @param userId ユーザID
     */
    public void deleteById(final int userId) {
        if (this.existsById(userId)) {
            this.userMapper.deleteByPrimaryKey(userId);
        } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER);
        }
    }

    /**
     * IDからユーザを検索
     *
     * @param userId ユーザID
     *
     * @return ユーザ
     */
    public User selectById(final int userId) {
        return Optional.ofNullable(this.userMapper.selectByPrimaryKey(userId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * メールアドレスからユーザを検索
     *
     * @param email メールアドレス
     *
     * @return ユーザ
     */
    public User selectByEmail(final String email) {
        final var example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        return this.userMapper.selectByExample(example).stream().findFirst() //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * ユーザ一覧を取得
     *
     * @return ユーザ一覧
     */
    public List<User> findAll() {
        final var example = new UserExample();
        example.setOrderByClause("updated_at desc");
        return userMapper.selectByExample(example);
    }

    /**
     * ユーザIDの存在確認
     *
     * @param userId ユーザID
     *
     * @return is userId exists?
     */
    public boolean existsById(final int userId) {
        try {
            this.selectById(userId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    /**
     * emailの存在確認
     *
     * @param email email
     *
     * @return is email exists?
     */
    public boolean existsByEmail(final String email) {
        try {
            this.selectByEmail(email);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

}
