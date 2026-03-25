package com.fms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fms.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
//	List<ItemEntity> findAllByUser_UserName(String userName);
	
	@Query("SELECT i FROM ItemEntity i WHERE i.createdBy.userName = :name")
	Page<ItemEntity> findByUserName(@Param("name") String userName, Pageable pageable);
	
	@Query("SELECT i FROM ItemEntity i WHERE i.groupId.id = :groupId")
	Page<ItemEntity> findByGroupId(@Param("groupId") Long groupId, Pageable pageable);

	
	@Query("SELECT i FROM ItemEntity i WHERE i.createdBy.userName = :name AND i.groupId IS NULL")
	Page<ItemEntity> findByUserNameAndGroupIdIsNull(@Param("name") String userName, Pageable pageable);
}
