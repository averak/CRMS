package dev.abelab.crs.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.crs.db.entity.User;
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

}
