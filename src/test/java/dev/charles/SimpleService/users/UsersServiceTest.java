package dev.charles.SimpleService.users;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest{
    @InjectMocks
    private UsersService usersService;
    @Mock
    private UsersQueryRepository usersRepository;

    @Nested
    @DisplayName("Given there are two registered users")
    public class RegisteredTwoUsersTest{
        //GIVEN
        private UserDto mikeDto;

        @BeforeEach
        void setup(){
            mikeDto = new UserDto("mike","mike@gmail.com");
        }
        @Nested
        @DisplayName("When use service")
        class GetMikeEntityByEmailTest{
            @Test
            @DisplayName("Then  find mike of a user entity by email and repository is called ")
            public void UserGetTest() {
                //given
                String targetEmail = "mike@gmail.com";
                given(usersRepository.findByEmailDto("mike@gmail.com"))
                        .willReturn(mikeDto);
                //when
                UserDto result = usersService.getUserByEmail(targetEmail);
                // then
                verify(usersRepository).findByEmailDto(targetEmail);
                assertThat(result).extracting("email", "username")
                        .contains("mike","mike@gmail.com");
            }
            @Test
            @DisplayName("Then the repository is called with correct Pageable and returns the Page")
            void getUsers_ShouldCallRepositoryWithCorrectPageable() {
                Page<UserDto> mockPage;
                int targetOffset = 0; // 3번째 페이지 (인덱스 2)
                final int pageSize = 10;
                // When: 3번째 페이지 (offset=2) 요청
                List<UserDto> pageContent = List.of(
                        new UserDto("user1", "u1@mail.com"),
                        new UserDto("user2", "u2@mail.com"),
                        new UserDto("user3", "u3@mail.com"),
                        new UserDto("user4", "u4@mail.com"),
                        new UserDto("user5", "u5@mail.com"),
                        new UserDto("user6", "u6@mail.com"),
                        new UserDto("user7", "u7@mail.com"),
                        new UserDto("user8", "u8@mail.com"),
                        new UserDto("user9", "u9@mail.com"),
                        new UserDto("user10", "u10@mail.com")
                );
                // Given: Repository가 targetOffset으로 호출되면 mockPage를 반환하도록 설정
                given(usersRepository.paginationUsers("user",
                        PageRequest.of(targetOffset, pageSize)
                )).willReturn(pageContent);

                Page<UserDto> resultPage = usersService.getUsers("user", targetOffset, null);

                assertThat(resultPage.getTotalElements()).as("Total Elements").isEqualTo(10);
                assertThat(resultPage.getNumberOfElements()).as("Number of Elements").isEqualTo(10);
            }
            @Test
            @DisplayName("Then the repository's save method is called with a Users entity")
            void UserCreateTest() {
                // when
                usersService.create(mikeDto);
                // then
                verify(usersRepository).save(any());
            }

            @Test
            @DisplayName("Then find the user entity by email and then the entity is deleted")
            void UserDeleteTest() {
                // when
                usersService.delete("mike@gmail.com");
                // then
                verify(usersRepository).delete(any());
            }

            @Test
            @DisplayName("Then find the user by email and update the user properties by given parameters")
            void UserUpdate(){
                //given
                UserDto newDto = new UserDto("mike2", "mike2@gmail.com");
                // when
                usersService.update("mike@gmail.com", newDto);
                // then
                verify(usersRepository).checkByEmail("mike@gmail.com");
                verify(usersRepository).update(newDto);
            }



        }

    }


}