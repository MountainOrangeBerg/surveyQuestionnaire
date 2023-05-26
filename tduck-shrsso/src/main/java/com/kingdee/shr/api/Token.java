package com.kingdee.shr.api;

import java.io.Serializable;

public class Token implements Serializable {

	private static final long serialVersionUID = -7212631525740921239L;
	
	private String value;
	private String domain;
	private String path;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return this.value;
	}
	
}
