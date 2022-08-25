package diplom.work.diplombackend.repository;

import diplom.work.diplombackend.model.SavedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedFileDao extends JpaRepository<SavedFile, Long> {

	Optional<SavedFile> findByUserIdAndFileName(long userId, String fileName);

	void deleteByUserIdAndFileName(long userId, String fileName);
}
