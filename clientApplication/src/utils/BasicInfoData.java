package utils;

import java.time.LocalDate;

public class BasicInfoData {
	private String email, password, name, isTech, phoneNumber, address, gender, bloodType;
	private int weight;
	private int height;
	private LocalDate dob;

	public void setAccountData(String email, String password, String name, String isTech) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.isTech = isTech;
	}

	public void setAllBasicData(String email, String password, String name, String isTech, String phoneNumber,
			int height, int weight, String address, String gender, String bloodType, LocalDate dob) {
		this.email = email;
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

	public void setEmailID(String emailID) {
		this.email = emailID;
	}

	public String getGender() {
		return gender;
	}

	public String getBloodType() {
		return bloodType;
	}

	public LocalDate getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getIsTech() {
		return isTech;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getWeight() {
		return weight;
	}

	public int getHeight() {
		return height;
	}
}
