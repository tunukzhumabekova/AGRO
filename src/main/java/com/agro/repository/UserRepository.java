package com.agro.repository;

import org.jooq.DSLContext;
import com.agro.model.UserInfo;
import org.springframework.stereotype.Repository;

import static com.agro.public_.Tables.USER_INFOS;

@Repository
public class UserRepository {
    private final DSLContext dslContext;

    public UserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public UserInfo findUserByUsername(String username) {
        return dslContext.select(USER_INFOS.ID, USER_INFOS.PASSWORD, USER_INFOS.ROLE)
                .from(USER_INFOS)
                .where(USER_INFOS.USERNAME.eq(username))
                .fetchOneInto(UserInfo.class);
    }
}