package org.beginsoft.vo;

import java.io.Serializable;

public class MassQus implements Serializable{
	private String massQus;
	private String monly;
	
	public MassQus() {
		super();
	}
	public MassQus(String massQus, String monly) {
		super();
		this.massQus = massQus;
		this.monly = monly;
	}
	public String getMassQus() {
		return massQus;
	}
	public void setMassQus(String massQus) {
		this.massQus = massQus;
	}
	public String getMonly() {
		return monly;
	}
	public void setMonly(String monly) {
		this.monly = monly;
	}
}
