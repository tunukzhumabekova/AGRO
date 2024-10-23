package com.agro.repository;

import com.agro.public_.tables.records.UsersRecord;
import org.jooq.DSLContext;
import com.agro.model.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.agro.public_.Tables.USERS;
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

    public int save(UsersRecord usersRecord, int userInfoId) {
        return Objects.requireNonNull(dslContext.insertInto(USERS)
                        .set(usersRecord)
                        .set(USERS.USER_INFO_ID, userInfoId)
                        .returningResult(USERS.ID)
                        .fetchOne())
                .getValue(USERS.ID);
    }

    public UsersRecord findByUserInfoId(Integer userInfoId) {
        return dslContext.selectFrom(USERS)
                .where(USERS.USER_INFO_ID.eq(userInfoId))
                .fetchOne();
    }
}