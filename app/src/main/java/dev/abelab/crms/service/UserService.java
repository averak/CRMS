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
import dev.abelab.crms.api.request.LoginUserUpdateRequest;
import dev.abelab.crms.api.request.LoginUserPasswordUpdateRequest;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.logic.UserRoleLogic;
import dev.abelab.crms.util.UserUtil;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserLogic userLogic;

    private final UserRoleLogic userRoleLogic;

    private final UserRepository userRepository;

    /**
     * ユーザ一覧を取得
     *
     * @param jwt JWT
     *
     * @return ユーザ一覧レスポンス
     */
    @Transactional
    public UsersResponse getUsers(final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        // ユーザの取得
        final var users = this.userRepository.findAll();
        final var userResponses = users.stream().map(UserUtil::buildUserResponse) //
            .collect(Collectors.toList());

        return new UsersResponse(userResponses);
    }

    /**
     * ユーザを作成
     *
     * @param jwt         JWT
     *
     * @param requestBody ユーザ作成リクエスト
     */
    @Transactional
    public void createUser(final UserCreateRequest requestBody, final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        // 作成するアカウントのロールの有効性をチェック
        this.userRoleLogic.checkForValidRoleId(requestBody.getRoleId());

        // 有効なパスワードかチェック
        this.userLogic.validatePassword(requestBody.getPassword());

        // ユーザの作成
        final var user = User.builder() //
            .firstName(requestBody.getFirstName()) //
            .lastName(requestBody.getLastName()) //
            .email(requestBody.getEmail()) //
            .password(this.userLogic.encodePassword(requestBody.getPassword())) //
            .roleId(requestBody.getRoleId()) //
            .admissionYear(requestBody.getAdmissionYear()) //
            .build();

        this.userRepository.insert(user);
    }

    /**
     * ユーザを更新
     *
     * @param jwt         JWT
     *
     * @param userId      ユーザID
     *
     * @param requestBody ユーザ更新リクエスト
     */
    @Transactional
    public void updateUser(final int userId, final UserUpdateRequest requestBody, final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        final var user = this.userRepository.selectById(userId);
        user.setFirstName(requestBody.getFirstName());
        user.setLastName(requestBody.getLastName());
        user.setEmail(requestBody.getEmail());
        user.setRoleId(requestBody.getRoleId());
        user.setAdmissionYear(requestBody.getAdmissionYear());
        this.userRepository.update(user);
    }

    /**
     * ユーザを削除
     *
     * @param jwt    JWT
     *
     * @param userId ユーザID
     */
    @Transactional
    public void deleteUser(final int userId, final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        this.userRepository.deleteById(userId);
    }

    /**
     * ログインユーザ詳細を取得
     *
     * @param jwt JWT
     *
     * @return ユーザ詳細レスポンス
     */
    @Transactional
    public UserResponse getLoginUser(final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // ユーザの取得
        final var user = userRepository.selectById(loginUser.getId());

        return UserUtil.buildUserResponse(user);
    }

    /**
     * ログインユーザを更新
     *
     * @param jwt JWT
     *
     * @return ユーザ詳細レスポンス
     */
    @Transactional
    public void updateLoginUser(final LoginUserUpdateRequest requestBody, final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // ログインユーザの更新
        loginUser.setFirstName(requestBody.getFirstName());
        loginUser.setLastName(requestBody.getLastName());
        loginUser.setEmail(requestBody.getEmail());
        this.userRepository.update(loginUser);
    }

    /**
     * ログインユーザのパスワードを更新
     *
     * @param jwt JWT
     *
     * @return ユーザ詳細レスポンス
     */
    @Transactional
    public void updateLoginPasswordUser(final LoginUserPasswordUpdateRequest requestBody, final String jwt) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(jwt);

        // パスワードチェック
        this.userLogic.verifyPassword(loginUser.getId(), requestBody.getCurrentPassword());

        // 有効なパスワードかチェック
        this.userLogic.validatePassword(requestBody.getNewPassword());

        // ログインユーザの更新
        loginUser.setPassword(this.userLogic.encodePassword(requestBody.getNewPassword()));
        this.userRepository.update(loginUser);
    }

}
