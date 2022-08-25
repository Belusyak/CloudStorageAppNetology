package diplom.work.diplombackend.controller;

import diplom.work.diplombackend.BaseTest;
import diplom.work.diplombackend.dto.LoginInDto;
import diplom.work.diplombackend.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseTest {

	@Test
	public void signIn_Success() throws Exception {
		LoginInDto dto = getLoginInDto("loginTest", "passwordTest");

		mockMvc.perform(MockMvcRequestBuilders.post("/sign-in")
						.contentType(APPLICATION_JSON)
						.content(toJson(dto)))
				.andExpect(status().is2xxSuccessful());

		Assertions.assertTrue(userDao.existsByUsername("loginTest"));

	}

	@Test
	public void signIn_alreadyExist_fail() throws Exception {
		User user = createUser();
		LoginInDto dto = getLoginInDto(user.getUsername(), user.getPassword());

		mockMvc.perform(MockMvcRequestBuilders.post("/sign-in")
						.contentType(APPLICATION_JSON)
						.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("This login already exists")));
	}

	@Test
	public void loginUser_Success() throws Exception {
		User user = createUser();
		LoginInDto dto = getLoginInDto(user.getUsername(), "password");

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(APPLICATION_JSON)
						.content(toJson(dto)))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void loginUser_notFoundUser_Fail() throws Exception {
		LoginInDto dto = getLoginInDto("login", "password");

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(APPLICATION_JSON)
						.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("User not found")));
	}

	@Test
	public void loginUser_IncorrectPassword_Fail() throws Exception {
		User user = createUser();
		LoginInDto dto = getLoginInDto(user.getUsername(), "password123");

		mockMvc.perform(MockMvcRequestBuilders.post("/login")
						.contentType(APPLICATION_JSON)
						.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Incorrect login or password")));
	}

	private LoginInDto getLoginInDto(String login, String password) {
		return LoginInDto.builder()
				.login(login)
				.password(password)
				.build();
	}
}
