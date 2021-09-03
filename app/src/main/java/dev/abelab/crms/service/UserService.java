package dev.abelab.crms.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

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

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;

    private final UserLogic userLogic;

    private final UserRoleLogic userRoleLogic;

    private final UserRepository userRepository;

    /**
     * ユーザ一覧を取得
     *
     * @param credentials 資格情報
     *
     * @return ユーザ一覧レスポンス
     */
    @Transactional
    public UsersResponse getUsers(final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        // ユーザの取得
        final var users = this.userRepository.findAll();
        final var userResponses = users.stream() //
            .map(user -> this.modelMapper.map(user, UserResponse.class)) //
            .collect(Collectors.toList());

        return new UsersResponse(userResponses);
    }

    /**
     * ユーザを作成
     *
     * @param credentials 資格情報
     *
     * @param requestBody ユーザ作成リクエスト
     */
    @Transactional
    public void createUser(final UserCreateRequest requestBody, final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        // 作成するアカウントのロールの有効性をチェック
        this.userRoleLogic.checkForValidRoleId(requestBody.getRoleId());

        // 有効なパスワードかチェック
        this.userLogic.validatePassword(requestBody.getPassword());

        // ユーザの作成
        final var user = this.modelMapper.map(requestBody, User.class);
        user.setPassword(this.userLogic.encodePassword(requestBody.getPassword()));
        this.userRepository.insert(user);
    }

    /**
     * ユーザを更新
     *
     * @param credentials 資格情報
     *
     * @param userId      ユーザID
     *
     * @param requestBody ユーザ更新リクエスト
     */
    @Transactional
    public void updateUser(final int userId, final UserUpdateRequest requestBody, final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        // 作成するアカウントのロールの有効性をチェック
        this.userRoleLogic.checkForValidRoleId(requestBody.getRoleId());

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
     * @param credentials 資格情報
     *
     * @param userId      ユーザID
     */
    @Transactional
    public void deleteUser(final int userId, final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // 管理者かチェック
        this.userLogic.checkAdmin(loginUser.getId());

        this.userRepository.deleteById(userId);
    }

    /**
     * ログインユーザ詳細を取得
     *
     * @param credentials 資格情報
     *
     * @return ユーザ詳細レスポンス
     */
    @Transactional
    public UserResponse getLoginUser(final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // ユーザの取得
        final var user = userRepository.selectById(loginUser.getId());

        return this.modelMapper.map(user, UserResponse.class);
    }

    /**
     * ログインユーザを更新
     *
     * @param credentials 資格情報
     *
     * @return ユーザ詳細レスポンス
     */
    @Transactional
    public void updateLoginUser(final LoginUserUpdateRequest requestBody, final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // ログインユーザの更新
        loginUser.setFirstName(requestBody.getFirstName());
        loginUser.setLastName(requestBody.getLastName());
        loginUser.setEmail(requestBody.getEmail());
        this.userRepository.update(loginUser);
    }

    /**
     * ログインユーザのパスワードを更新
     *
     * @param credentials 資格情報
     *
     * @return ユーザ詳細レスポンス
     */
    @Transactional
    public void updateLoginPasswordUser(final LoginUserPasswordUpdateRequest requestBody, final String credentials) {
        // ログインユーザを取得
        final var loginUser = this.userLogic.getLoginUser(credentials);

        // パスワードチェック
        this.userLogic.verifyPassword(loginUser, requestBody.getCurrentPassword());

        // 有効なパスワードかチェック
        this.userLogic.validatePassword(requestBody.getNewPassword());

        // ログインユーザの更新
        loginUser.setPassword(this.userLogic.encodePassword(requestBody.getNewPassword()));
        this.userRepository.update(loginUser);
    }

}
