package dev.charles.SimpleService.users;

import dev.charles.SimpleService.errors.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {
    final private UsersQueryRepository usersQueryRepository;

    UserDto getUserByEmail (String email){
        return usersQueryRepository.findByEmailDto(email);
    }

    Page<UserDto> getUsers(final String keyword, final Integer offset, final Long total){
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset,pageSize);
        List<UserDto> usersList= usersQueryRepository.paginationUsers(keyword, pageable);
        Long totalCount = total != null ? total: usersQueryRepository.totalPagination(keyword);
        return new PageImpl<>(usersList, pageable, totalCount);

    }

    @Modifying
    @Transactional
    @PreAuthorize("principal.claims['email'] == #userDto.email")
    void create(final UserDto userDto){
        if(usersQueryRepository.checkByEmail(userDto.getEmail())){
            throw new DuplicateResourceException("Already user existed by email");
        }
        usersQueryRepository.save(userDto);
    }

    @Modifying
    @Transactional
    void delete(final String email){
        usersQueryRepository.delete(email);
    }

    @Modifying
    @Transactional
    void update(final String email, final UserDto userDto){
        if(usersQueryRepository.checkByEmail(email)){
            throw new DuplicateResourceException("Already user existed by email");
        }
        usersQueryRepository.update(userDto);
    }

}

