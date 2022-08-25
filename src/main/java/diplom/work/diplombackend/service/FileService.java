package diplom.work.diplombackend.service;

import diplom.work.diplombackend.exception.FileException;
import diplom.work.diplombackend.exception.UserNotFoundException;
import diplom.work.diplombackend.model.SavedFile;
import diplom.work.diplombackend.model.User;
import diplom.work.diplombackend.repository.SavedFileDao;
import diplom.work.diplombackend.repository.UserDao;
import diplom.work.diplombackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

	private final SavedFileDao savedFileDao;
	private final UserDao userDao;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public void uploadFile(MultipartFile file, String fileName, String authToken) {
		User currentUser = getCurrentUser(authToken);
		SavedFile saveFile;
		try {
			saveFile = new SavedFile()
					.setUser(currentUser)
					.setFile(file.getBytes())
					.setContentType(file.getContentType())
					.setFileName(fileName);
		} catch (IOException e) {
			throw new FileException("unable to save file");
		}
		savedFileDao.save(saveFile);
	}

	@Transactional
	public void deleteFile(String fileName, String authToken) {
		User currentUser = getCurrentUser(authToken);
		savedFileDao.deleteByUserIdAndFileName(currentUser.getId(), fileName);
	}

	@Transactional(readOnly = true)
	public byte[] getFile(String fileName, String authToken) {
		User currentUser = getCurrentUser(authToken);
		SavedFile savedFile = savedFileDao.findByUserIdAndFileName(currentUser.getId(), fileName)
				.orElseThrow(() -> new FileException("File not found"));
		return savedFile.getFile();
	}

	@Transactional
	public String editFileName(String fileName, String newFileName, String authToken) {
		User currentUser = getCurrentUser(authToken);
		SavedFile savedFile = savedFileDao.findByUserIdAndFileName(currentUser.getId(), fileName)
				.orElseThrow(() -> new FileException("File not found"));
		savedFileDao.save(savedFile.setFileName(newFileName));
		return newFileName;
	}

	private User getCurrentUser(String token) {
		return userDao.findByUsername(jwtTokenProvider.getUsername(token)).orElseThrow(UserNotFoundException::new);
	}

}
