package dev.abelab.crs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.api.response.UserResponse;
import dev.abelab.crs.api.response.UsersResponse;
import dev.abelab.crs.api.request.UserRequest;
import dev.abelab.crs.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * ユーザ一覧を取得
     *
     * @return ユーザ一覧レスポンス
     */
    @Transactional
    public UsersResponse getUsers() {
        final var users = userRepository.findAll();
        final var userResponses = users.stream().map(user -> {
            return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getRoleId());
        }).collect(Collectors.toList());

        return new UsersResponse(userResponses);
    }

    /**
     * ユーザを作成
     *
     * @param userRequest ユーザ作成リクエスト
     */
    @Transactional
    public void createUser(UserRequest userRequest) {
        // ユーザの作成
        final var user = User.builder() //
            .firstName(userRequest.getFirstName()) //
            .lastName(userRequest.getLastName()) //
            .password(userRequest.getPassword()) //
            .roleId(userRequest.getRoleId()) //
            .build();

        this.userRepository.insert(user);
    }

}
