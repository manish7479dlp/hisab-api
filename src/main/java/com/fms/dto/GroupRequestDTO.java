package com.fms.dto;

public class GroupRequestDTO {
	private String name;
	private Boolean isPinned;
	
	public GroupRequestDTO() {
		
	}

	public GroupRequestDTO(String name, Boolean isPinned) {
		super();
		this.name = name;
		this.isPinned = isPinned;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}
	
}
