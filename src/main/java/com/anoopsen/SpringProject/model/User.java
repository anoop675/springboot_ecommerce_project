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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_id", length = 50)
	private int id;
	
	@NotEmpty
	@Column(name = "user_firstname", length = 100, unique = false, nullable = false)
	private String firstName;
	
	@Column(name = "user_lastname", length = 100, unique = false, nullable = true)
	private String lastName;
	
	@Column(name = "user_email", length = 100, unique = true, nullable = false)
	@Email(message = "{errors.invalid_email}")
	private String email;
	
	//@NotEmpty
	@Column(name = "user_password", length = 250, unique = true, nullable = false)
	private String password;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER) //Defines many-to-many relationship between User ("users") and Role ("roles")
	@JoinTable(	//Creates a join table named ("user_role") where user_id and role_id are foreign keys
			name = "user_role",
			joinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "user_id")
				},
			inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "role_id")
				}
	)
	//many-to-many relationship with roles
	private List<Role> roles;
	
	/*
	 * DB schema model
	 	users            user_role         roles
	    -------          -----------       -------
		user_id    ----> user_id           role_id
		firstName        role_id  <----    name
		lastName
		email
		password

	 */

	public User(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles();
	} 
	
	public User() {
		
	}	
}
