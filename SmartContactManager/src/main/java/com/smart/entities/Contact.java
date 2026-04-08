package com.smart.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;

import com.smart.enums.ContactCategory;
import jakarta.persistence.*;

@Entity
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Cid;
	private String name;
	private String secondname;
	private String work;
	private String email;
	private String phone;
	private String image;

//	@Column(name = "category", columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
	private ContactCategory category;
	@Column(length=5000)
	private String description;
	@ManyToOne
	@JsonIgnore
	private User user;
	
	//getter and setter
	public int getCid() {
		return Cid;
	}
	public void setCid(int cid) {
		Cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondname() {
		return secondname;
	}
	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ContactCategory getCategory() {
		return category;
	}

	public void setCategory(ContactCategory category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Contact [Cid=" + Cid + ", name=" + name + ", secondname=" + secondname + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", category=" + category + ", image=" + image + ", description=" + description + ", user=" + user
				+ "]";
	}
	
	 //method for comparing contact
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.Cid==((Contact)obj).getCid();
	}
      
	
}
