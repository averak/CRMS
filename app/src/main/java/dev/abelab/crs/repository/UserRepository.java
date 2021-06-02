package dev.abelab.crs.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.db.entity.UserExample;
import dev.abelab.crs.db.mapper.UserMapper;

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
        return this.userMapper.selectByPrimaryKey(id);
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

}
