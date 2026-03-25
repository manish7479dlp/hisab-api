package com.fms.dto;

public class ShareRequestDTO {
	Long groupId;
	String shareBy;
	String shareTo;
	
	public ShareRequestDTO() {
		
	}

	public ShareRequestDTO(Long groupId, String shareBy, String shareTo) {
		super();
		this.groupId = groupId;
		this.shareBy = shareBy;
		this.shareTo = shareTo;
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
