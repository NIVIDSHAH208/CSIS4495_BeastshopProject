package com.beastshop.common.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "users")
public class User extends IdBasedEntity{

	

	@Column(length = 128, nullable = false, unique = true)
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstname;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastname;

	@Column(length = 64)
	private String photos;

	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

//	Empty constructor
	public User() {
		// TODO Auto-generated constructor stub
	}

//	Overloaded constructor

	public User(String email, String password, String firstname, String lastname) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	//We use transient annotation so hibernate won't mark this to database
	@Transient
	public String getPhotosImagePath() {
		if(id == null || photos == null) {
			return "/images/default-user.png";
		}
		
		return "/user-photos/"+this.id+"/"+this.photos;
	}
	
	@Transient
	public String getFullName() {
		return firstname+" "+lastname;
	}
	
	

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", roles=" + roles + "]";
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();
		while(iterator.hasNext()) {
			Role role = iterator.next();
			if(role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}
	

}
