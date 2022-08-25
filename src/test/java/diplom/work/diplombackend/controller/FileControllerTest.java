package diplom.work.diplombackend.controller;

import diplom.work.diplombackend.BaseTest;
import diplom.work.diplombackend.model.SavedFile;
import diplom.work.diplombackend.model.User;
import diplom.work.diplombackend.repository.SavedFileDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerTest extends BaseTest {
	@Autowired
	private SavedFileDao savedFileDao;

	@Test
	public void uploadFile_Success() throws Exception {
		User user = createUser();
		String token = getToken(user);
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"file.csv",
				"text/plain",
				"file".getBytes()
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload")
						.file(file)
						.param("filename", "fileName")
						.header("auth-token", token)
				)
				.andExpect(status().is2xxSuccessful());



		SavedFile newFile = savedFileDao.findByUserIdAndFileName(user.getId(), "fileName").orElseThrow();
		assertEquals(newFile.getFile(), file.getBytes());
	}

	@Test
	public void doFile_incorrectToken_fail() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"name",
				"name.csv",
				"text/plain",
				"file".getBytes()
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
						.file(file)
						.header("auth-token", "token")
				)
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void doFile_withoutToken_fail() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"name",
				"name.csv",
				"text/plain",
				"file".getBytes()
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
						.file(file)
				)
				.andExpect(status().is4xxClientError()); // todo сделать хэндлер
	}

	@Test
	public void getFile_Success() throws Exception {
		User user = createUser();
		String token = getToken(user);
		MockMultipartFile file = new MockMultipartFile(
				"name",
				"name.csv",
				"text/plain",
				"file".getBytes()
		);

		savedFileDao.save(
				new SavedFile()
						.setFileName(file.getName())
						.setFile(file.getBytes())
						.setContentType(file.getContentType())
						.setUser(user));

		mockMvc.perform(MockMvcRequestBuilders.get("/file")
						.param("filename", file.getName())
						.header("auth-token", token)
				)
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().bytes("file".getBytes()));
	}

	@Test
	public void editFileName_Success() throws Exception {
		User user = createUser();
		String token = getToken(user);
		String newFileName = "newFileName";
		MockMultipartFile file = new MockMultipartFile(
				"name",
				"name.csv",
				"text/plain",
				"file".getBytes()
		);

		savedFileDao.save(
				new SavedFile()
						.setFileName(file.getName())
						.setFile(file.getBytes())
						.setContentType(file.getContentType())
						.setUser(user));

		mockMvc.perform(MockMvcRequestBuilders.put("/file")
						.param("filename", file.getName())
						.param("newfilename", newFileName)
						.header("auth-token", token)
				)
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().string(newFileName));

		var expectedSavedFile = savedFileDao.findByUserIdAndFileName(user.getId(), newFileName).orElse(null);
		assertNotNull(expectedSavedFile);
		var expectedNull = savedFileDao.findByUserIdAndFileName(user.getId(), file.getName()).orElse(null);
		assertNull(expectedNull);
	}

	@Test
	public void deleteFile_Success() throws Exception {
		User user = createUser();
		String token = getToken(user);
		String newFileName = "newFileName";
		MockMultipartFile file = new MockMultipartFile(
				"name",
				"name.csv",
				"text/plain",
				"file".getBytes()
		);

		savedFileDao.save(
				new SavedFile()
						.setFileName(file.getName())
						.setFile(file.getBytes())
						.setContentType(file.getContentType())
						.setUser(user));

		mockMvc.perform(MockMvcRequestBuilders.delete("/file")
						.param("filename", file.getName())
						.header("auth-token", token)
				)
				.andExpect(status().is2xxSuccessful());

		var expectedNull = savedFileDao.findByUserIdAndFileName(user.getId(), file.getName()).orElse(null);
		assertNull(expectedNull);
	}

}
