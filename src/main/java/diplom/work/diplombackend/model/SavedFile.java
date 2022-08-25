package diplom.work.diplombackend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "t_file")
@Getter
@Setter
@Entity
@Accessors(chain = true)
public class SavedFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", updatable = false, nullable = false)
	private User user;

	@Column(nullable = false, unique = true)
	private String fileName;

	@Column(name = "file", length = 10485760)
	private byte[] file;

	@Column(nullable = false)
	protected String contentType;


}
