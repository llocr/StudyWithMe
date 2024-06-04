package december.spring.studywithme.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user")
public class User extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String userId;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private String introduce;
	
	@Enumerated(value = EnumType.STRING)
	private UserType userType;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private RefreshToken refreshToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime statusChangedAt;
	
	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<Post> postList;
	
	@Builder
	public User(String userId, String password, String name, String email, String introduce, UserType userType) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.introduce = introduce;
		this.userType = userType;
	}
	
}
