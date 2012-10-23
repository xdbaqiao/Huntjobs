package com.huntjobs.android;

public class News
{
	public static final String DELIMITER_FIELD = "<>";
	public static final String DELIMITER_LINE = "<<>>";
	private String date;
	private String time;
	private String company;
	private String companyurl;
	private String bakup;
	private String address;
	
	public News(String date1, String time1, String address1 ,String company1, String companyurl1, String bakup1) {
		setDate(date1);
		setTime(time1);
		setAddress(address1);
		setCompany(company1);
		setCompanyurl(companyurl1);
		setBakup(bakup1);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getDate());
		sb.append(DELIMITER_FIELD);
		sb.append(getTime());
		sb.append(DELIMITER_FIELD);
		sb.append(getAddress());
		sb.append(DELIMITER_FIELD);
		sb.append(getCompany());
		sb.append(DELIMITER_FIELD);
		sb.append(getCompanyurl());
		sb.append(DELIMITER_FIELD);
		sb.append(getBakup());
		return sb.toString();
	}
	
	public static News createNews(String s) {
		String [] tokens = s.split(DELIMITER_FIELD);
		if(tokens.length != 6)
			return null;
		return new News(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompany() {
		return company;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	public void setCompanyurl(String companyurl) {
		this.companyurl = companyurl;
	}
	public String getCompanyurl() {
		return companyurl;
	}
	public void setBakup(String bakup) {
		this.bakup = bakup;
	}
	public String getBakup() {
		return bakup;
	}

}
