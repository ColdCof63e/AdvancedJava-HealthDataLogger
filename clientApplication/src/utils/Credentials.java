package utils;

import java.time.LocalDate;

public class Credentials {

    private String emailID;
    private String password;
    private String name;
    private String isTech;
    private String phoneNumber, address;
    private int height;
    private int weight;
    private LocalDate dob;
    private String bloodType;
    private String gender;

    public String getEmailID() {
    	return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
    	return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getIsTech() {
    	return isTech;
    }

    public void setIsTech(String isTech) {
    	this.isTech = isTech;
    }

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public LocalDate getdob() {
		return dob;
	}

	public void setdob(LocalDate dOB) {
		dob = dOB;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}


	/*
	 * 1. email-id
	 * 2. password
	 * 3. name
	 * 4. isTech
	 * 5. phone number
	 * 6. height
	 * 7. weight
	 * 8. address
	 * 9. gender
	 * 10. blood type
	 * 11. DOB
	 */
    public Credentials(String emailID, String password, String name, String isTech, String phoneNumber,
    		int height, int weight, String address, String gender, String bloodType, LocalDate dob) {
    	this.emailID = emailID;
        this.password = password;
        this.name = name;
        this.isTech = isTech;
        this.phoneNumber = phoneNumber;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.gender = gender;
        this.bloodType = bloodType;
        this.dob = dob;
    }

    public Credentials(String email, String password, String name, String isTech) {
    	this.emailID = email;
    	this.password = password;
    	this.name = name;
    	this.isTech = isTech;
    }
}