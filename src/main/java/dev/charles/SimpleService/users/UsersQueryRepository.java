package dev.charles.SimpleService.users;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.charles.SimpleService.errors.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static dev.charles.SimpleService.users.QUsers.users;


@RequiredArgsConstructor
@Repository
public class UsersQueryRepository {
    private final JPAQueryFactory queryFactory;
//    QUser
    boolean checkByEmail(String email){
        return Optional.ofNullable(queryFactory
                .select(users.id)
                .from(users)
                .where(users.email.eq(email))
                .fetchOne()).isPresent();
    }

    Users findByEmail(String email){
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(users)
                        .where(users.email.eq(email))
                        .fetchOne())
                .orElseThrow(() -> new NotFoundResourceException("Not found user by email"));
    }
    UserDto findByEmailDto(String email) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.fields(UserDto.class,
                                users.email,
                                users.username
                        ))
                        .from(users)
                        .where(users.email.eq(email))  // 사용자의 이메일을 기준으로 검색
                        .fetchOne())
                .orElseThrow(() -> new NotFoundResourceException("Not found user by email"));
    }

    List<UserDto> paginationUsers(String keyword, Pageable pageable){
        return queryFactory
                .select(Projections.fields(UserDto.class,
                        users.username,
                        users.email))
                .from(users)
                .where(
                        users.username.like("%"+keyword + "%")
                )
                .orderBy(users.id.desc())
                .limit(pageable.getPageSize())
                .offset((long) pageable.getPageSize() * pageable.getPageNumber())
                .fetch();

    }
    Long totalPagination(String keyword){
        return (long) queryFactory
                .select(users.id)
                .from(users)
                .where(
                        users.username.like("%"+keyword + "%")
                )
                .fetch().size();
    }

    void save(UserDto userDto ){
        queryFactory
                .insert(users)
                .columns(users.email, users.username)
                .values(userDto.getEmail(), userDto.getUsername())
                .execute();
    }

    void update(UserDto userDto){
        long updatedRowcount = queryFactory
                .update(users)
                .set(users.email, userDto.getEmail())
                .set(users.username, userDto.getUsername())
                .where(users.email.eq(userDto.getEmail()))
                .execute();
        if (updatedRowcount == 0) {
            throw new NotFoundResourceException("Not found user on update");
        }
    }

    void delete(String email){
        long deletedRowCount = queryFactory
                .delete(users)
                .where(users.email.eq(email))
                .execute();
        if (deletedRowCount == 0) {
            throw new NotFoundResourceException("Not found user on delete");
        }
    }


}
