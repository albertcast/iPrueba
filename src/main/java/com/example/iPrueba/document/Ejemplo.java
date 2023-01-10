package com.example.iPrueba.document;

import org.springframework.data.annotation.Id;


public class Ejemplo {
	  @Id
	  public String id;

	  public String firstName;
	  public String lastName;
	  public String abc;

	  public Ejemplo() {}

	  public Ejemplo(String firstName, String lastName, String abc) {
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.abc = abc;
	  }

	  @Override
	  public String toString() {
	    return String.format(
	        "Customer[id=%s, firstName='%s', lastName='%s', abc='%s']",
	        id, firstName, lastName, abc);
	  }

	
}
