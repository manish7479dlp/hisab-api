package com.fms.dto;

public class ItemRequestDTO {
    private Long groupId;
    private String name;
    private Long price;
    
    public ItemRequestDTO() {}
    
	public ItemRequestDTO(Long groupId, String name, Long price) {
		super();
		this.groupId = groupId;
		this.name = name;
		this.price = price;
	}
	
	public Long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getPrice() {
		return price;
	}
	
	public void setPrice(Long price) {
		this.price = price;
	}
}
