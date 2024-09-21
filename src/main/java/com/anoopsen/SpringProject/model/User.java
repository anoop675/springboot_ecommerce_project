package com.anoopsen.SpringProject.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", length = 50)
	private int id;
	
	@NotEmpty
	@Column(name = "user_firstname", length = 100, nullable = false)
	private String firstName;
	
	@Column(name = "user_phone", length = 100, unique = true, nullable = true)
	private String phoneNum;
	
	@Column(name = "user_email", length = 100, unique = true, nullable = false)
	@Email(message = "{errors.invalid_email}")
	private String email;
	
	@Column(name = "user_password", length = 250, nullable = true)  // Removed 'unique'
	private String password;
	
	@Column(name = "oauth2_user", nullable = false)
	private boolean oauth2User;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
	)
	@EqualsAndHashCode.Exclude
	private List<Role> roles;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Cart cart;

	public User() {
		
	}
	
	public User(User user) {
		this.firstName = user.getFirstName();
		this.phoneNum = user.getPhoneNum();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles();
		this.cart = user.getCart();
	}
}
