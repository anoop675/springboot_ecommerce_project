package com.anoopsen.SpringProject.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "roles")
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id", length = 50)
	private Integer id;
	
	@NotEmpty
	@Column(name = "role_name", length = 100, nullable = false, unique = true)
	private String name;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private List<User> users;
}