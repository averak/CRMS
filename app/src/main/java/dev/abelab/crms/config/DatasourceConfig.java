package dev.abelab.crms.config;

import java.util.Date;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.property.CrmsProperty;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.enums.UserRoleEnum;

@Profile("prod | local")
@Configuration
public class DatasourceConfig {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLogic userLogic;

    @Autowired
    CrmsProperty crmsProperty;

    @PostConstruct
    public void createDefaultAdmin() {
        // 管理者アカウントが既に存在する
        if (this.userRepository.existsByEmail(this.crmsProperty.getAdmin().getEmail())) {
            return;
        }

        // 管理者アカウントを作成
        final var adminUser = User.builder() //
            .firstName(this.crmsProperty.getAdmin().getFirstName()) //
            .lastName(this.crmsProperty.getAdmin().getLastName()) //
            .email(this.crmsProperty.getAdmin().getEmail()) //
            .password(this.userLogic.encodePassword(this.crmsProperty.getAdmin().getPassword())) //
            .roleId(UserRoleEnum.ADMIN.getId()) //
            .admissionYear(this.crmsProperty.getAdmin().getAdmissionYear()) //
            .build();
        this.userRepository.insert(adminUser);
    }
}
