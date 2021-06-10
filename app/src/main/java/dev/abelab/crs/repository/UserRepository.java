package dev.abelab.crs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.db.entity.UserExample;
import dev.abelab.crs.db.mapper.UserMapper;
import dev.abelab.crs.exception.ErrorCode;
import dev.abelab.crs.exception.ConflictException;
import dev.abelab.crs.exception.NotFoundException;

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
    public int insert(User user) {
        if (this.existsByEmail(user.getEmail())) {
            throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        }
        return this.userMapper.insertSelective(user);
    }

    /**
     * IDからユーザを検索
     *
     * @param id ユーザID
     *
     * @return ユーザ
     */
    public User selectById(int id) {
        return Optional.ofNullable(this.userMapper.selectByPrimaryKey(id)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * メールアドレスからユーザを検索
     *
     * @param email メールアドレス
     *
     * @return ユーザ
     */
    public User selectByEmail(String email) {
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
     * emailの存在確認
     *
     * @param email email
     *
     * @return is email exists?
     */
    public boolean existsByEmail(String email) {
        try {
            this.selectByEmail(email);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

}
