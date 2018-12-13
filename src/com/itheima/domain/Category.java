package com.itheima.domain;

public class Category {

	private String cid;
	private String cname;
	private int    isOpen;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", isOpen=" + isOpen + "]";
	}
	
}
