package com.fms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;import com.fms.entity.GroupEntity;
import com.fms.entity.UserEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
	  // groups created by user
//    List<GroupEntity> findByCreatedBy(String createdBy);

    // fetch multiple groups by ids
//    List<GroupEntity> findByIdIn(List<Long> ids);
	
	
	List<GroupEntity> findByCreatedBy(UserEntity user);
	List<GroupEntity> findByIdIn(List<Long> ids);
}
