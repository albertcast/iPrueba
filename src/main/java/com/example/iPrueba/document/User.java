package com.example.iPrueba.document;

import java.util.ArrayList;
import java.util.List;

import nonapi.io.github.classgraph.json.Id;


public class User{

	@Id
	private String id;
	private String email;
	private String name;
	private String lastname;
	private Integer age;
	private String accountAuthentication;
	private String image;
	private boolean isAdmin;
	
	public User() {
	}
	
	public User(String email, String name, String lastname, Integer age, String accountAuthentication, String image, boolean isAdmin) {
		
		this.email=email;
		this.name=name;
		this.lastname=lastname;
		this.age=age;
		this.accountAuthentication = accountAuthentication;
		this.image = image;
		this.isAdmin = isAdmin;
	}
	
	
	
    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsuarioId() {
        return this.id;
    }
	
    public void setUsuarioId(String id) {
    	this.id = id;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombre() {
        return this.name;
    }

    public void setNombre(String name) {
        this.name = name;
    }
    
    public String getApellidos() {
        return this.lastname;
    }

    public void setApellidos(String lastname) {
        this.lastname = lastname;
    }
    
    public Integer getEdad() {
        return this.age;
    }

    public void setEdad(Integer age) {
        this.age = age;
    }

	public String getAccountAuthentication() {
		return accountAuthentication;
	}

	public void setAccountAuthentication(String accountAuthentication) {
		this.accountAuthentication = accountAuthentication;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
    
    
}
