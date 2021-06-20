package dev.abelab.crms.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.api.response.UserResponse;
import dev.abelab.crms.api.response.UsersResponse;
import dev.abelab.crms.api.request.UserCreateRequest;
import dev.abelab.crms.api.request.UserUpdateRequest;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.logic.UserRoleLogic;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserLogic userLogic;

    private final UserRoleLogic userRoleLogic;

    private final UserRepository userRepository;

    /**
     * ユーザ一覧を取得
     *
     * @return ユーザ一覧レスポンス
     */
    @Transactional
    public UsersResponse getUsers() {
        // ユーザの取得
        final var users = userRepository.findAll();
        final var userResponses = users.stream().map(user -> {
            return UserResponse.builder() //
                .id(user.getId()) //
                .firstName(user.getFirstName()) //
                .lastName(user.getLastName()) //
                .email(user.getEmail()) //
                .roleId(user.getRoleId()) //
                .build();
        }).collect(Collectors.toList());

        return new UsersResponse(userResponses);
    }

    /**
     * ユーザを作成
     *
     * @param requestBody ユーザ作成リクエスト
     */
    @Transactional
    public void createUser(UserCreateRequest requestBody) {
        // 有効なロールかチェック
        this.userRoleLogic.checkForValidRoleId(requestBody.getRoleId());

        // ユーザの作成
        final var user = User.builder() //
            .firstName(requestBody.getFirstName()) //
            .lastName(requestBody.getLastName()) //
            .email(requestBody.getEmail()) //
            .password(this.userLogic.encodePassword(requestBody.getPassword())) //
            .roleId(requestBody.getRoleId()) //
            .build();

        this.userRepository.insert(user);
    }

    /**
     * ユーザを更新
     *
     * @param userId      ユーザID
     *
     * @param requestBody ユーザ更新リクエスト
     */
    @Transactional
    public void updateUser(int userId, UserUpdateRequest requestBody) {
        final var user = this.userRepository.selectById(userId);
        user.setFirstName(requestBody.getFirstName());
        user.setLastName(requestBody.getLastName());
        user.setEmail(requestBody.getEmail());
        user.setPassword(this.userLogic.encodePassword(requestBody.getPassword()));
        user.setRoleId(requestBody.getRoleId());
        this.userRepository.update(user);
    }

    /**
     * ユーザを削除
     *
     * @param userId ユーザID
     */
    @Transactional
    public void deleteUser(int userId) {
        this.userRepository.deleteById(userId);
    }

}
