package es.juvecyl.app.utils;

import java.util.ArrayList;

public class Lodging {
	private String title, province, desc, loc, image;
	private ArrayList<String> phones, emails;

	public Lodging(String title, String province, String desc, String loc,
			ArrayList<String> emails, ArrayList<String> phones, String image) {
		super();
		this.title = title;
		this.province = province;
		this.desc = desc;
		this.loc = loc;
		this.emails = emails;
		this.phones = phones;
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	public void SetImage(String image) {
		this.image = image;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getLoc() {
		return this.loc;
	}

	public void setEmail(ArrayList<String> emails) {
		this.emails = emails;
	}

	public ArrayList<String> getEmail() {
		return this.emails;
	}

	public ArrayList<String> getPhone() {
		return this.phones;
	}

    public String getPhones() {
        String output = "";
        for (int i = 0; i < phones.size(); i++) {
            output += phones.get(i) + "\n\t";
        }
        return output;
    }

    public String getEmails() {
        String output = "";
        for (int i = 0; i < emails.size(); i++) {
            output += emails.get(i) + "\n\t";
        }
        return output;
    }

   
	public String getContactData(){
	    String output = "Teléfono(s):\n";
	    output += this.getPhones();
	    output += "Email(s):\n";
	    output += this.getEmails();
	    return output;
	}
	
	public void setPhone(ArrayList<String> phones) {
		this.phones = phones;
	}

}
