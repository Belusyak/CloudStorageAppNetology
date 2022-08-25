package diplom.work.diplombackend.controller;

import diplom.work.diplombackend.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	@PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void uploadFile(@RequestPart(value = "file") @NotNull MultipartFile file,
						   @RequestParam("filename") String fileName,
						   @RequestHeader("auth-token") String authToken) {
		fileService.uploadFile(file, fileName, authToken);
	}

	@GetMapping
	public byte[] getFile(@RequestParam("filename") String fileName,
						  @RequestHeader("auth-token") String authToken) {
		return fileService.getFile(fileName, authToken);
	}

	@DeleteMapping
	public void deleteFile(@RequestParam("filename") String fileName,
						   @RequestHeader("auth-token") String authToken) {
		fileService.deleteFile(fileName, authToken);
	}

	@PutMapping
	public String editFileName(@RequestParam("filename") String fileName,
							   @RequestParam("newfilename") String newFileName,
							   @RequestHeader("auth-token") String authToken) {
		return fileService.editFileName(fileName, newFileName, authToken);
	}

}
