package diplom.work.diplombackend.security;

import diplom.work.diplombackend.model.Role;
import diplom.work.diplombackend.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class JwtUser implements UserDetails {
	private final Long id;
	private final String username;
	private final String password;
	private final List<GrantedAuthority> authorities;

	private static List<GrantedAuthority> map(List<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toUnmodifiableList());
	}

	public JwtUser(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.authorities = map(user.getRoles());
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
