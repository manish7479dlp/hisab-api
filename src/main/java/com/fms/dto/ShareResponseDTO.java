package com.fms.dto;

public class ShareResponseDTO {
	Long id;
	Long groupId;
	String shareBy;
	String shareTo;
	
	public ShareResponseDTO() {
		
	}

	public ShareResponseDTO(Long id, Long groupId, String shareBy, String shareTo) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.shareBy = shareBy;
		this.shareTo = shareTo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getShareBy() {
		return shareBy;
	}

	public void setShareBy(String shareBy) {
		this.shareBy = shareBy;
	}

	public String getShareTo() {
		return shareTo;
	}

	public void setShareTo(String shareTo) {
		this.shareTo = shareTo;
	}

	
	
	
}
