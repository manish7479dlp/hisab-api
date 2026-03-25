package com.fms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "share")
public class ShareEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "groupId")
  private GroupEntity group;
  
  private Boolean isAccepted;
  
  @ManyToOne
  @JoinColumn(name = "share_by")
  private UserEntity shareBy;
  
  @ManyToOne
  @JoinColumn(name = "share_to")
  private UserEntity shareTo;
  
  @Column(updatable = false) // CreatedAt should never change after insert
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  
  public ShareEntity() {}

  public ShareEntity(Long id, GroupEntity group, Boolean isAccepted, UserEntity shareBy, UserEntity shareTo, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
	  super();
	  this.id = id;
	  this.group = group;
	  this.isAccepted = isAccepted;
	  this.shareBy = shareBy;
	  this.shareTo = shareTo;
	  this.createdAt = createdAt;
	  this.updatedAt = updatedAt;
  }
  
	// Automatically set dates on Insert
  @PrePersist
  protected void onCreate() {
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
      this.isAccepted = false;
  }

  // Automatically set date on Update
  @PreUpdate
  protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
  }

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public GroupEntity getGroup() {
		return group;
	}
	
	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	
	public UserEntity getShareBy() {
		return shareBy;
	}
	
	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	
	public Boolean getIsAccepted() {
		return isAccepted;
	}
	
	public void setShareBy(UserEntity shareBy) {
		this.shareBy = shareBy;
	}
	
	public UserEntity getShareTo() {
		return shareTo;
	}
	
	public void setShareTo(UserEntity shareTo) {
		this.shareTo = shareTo;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
  
}
