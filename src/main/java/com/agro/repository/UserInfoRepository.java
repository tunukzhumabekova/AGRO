package com.agro.repository;

import com.agro.public_.tables.records.UserInfosRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.agro.public_.Tables.USER_INFOS;

@Repository
public class UserInfoRepository {

    private final DSLContext dslContext;

    public UserInfoRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }


    public boolean existsByName(String username) {
        return dslContext.fetchExists(
                dslContext.selectFrom(USER_INFOS)
                        .where(USER_INFOS.USERNAME.eq(username))
        );
    }
    public int save(UserInfosRecord userInfosRecord) {
        return Objects.requireNonNull(dslContext.insertInto(USER_INFOS)
                        .set(userInfosRecord)
                        .returningResult(USER_INFOS.ID)
                        .fetchOne())
                .getValue(USER_INFOS.ID);
    }

    public UserInfosRecord findByUsername(String username) {
        return dslContext.selectFrom(USER_INFOS)
                .where(USER_INFOS.USERNAME.eq(username))
                .fetchOne();
    }
}
