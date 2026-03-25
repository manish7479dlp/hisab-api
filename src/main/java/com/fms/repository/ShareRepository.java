package com.fms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fms.entity.GroupEntity;
import com.fms.entity.ShareEntity;
import com.fms.entity.UserEntity;

public interface ShareRepository extends JpaRepository<ShareEntity, Long> {

    // shares received by user
    List<ShareEntity> findByShareTo_UserName(String userName);

    // shares sent by user
    List<ShareEntity> findByShareBy_UserName(String userName);

    // any share received?
    boolean existsByShareTo_UserName(String userName);

    // only accepted shares (object version)
    List<ShareEntity> findByShareToAndIsAcceptedTrue(UserEntity user);

    // accepted shared groups (best version 🔥)
    @Query("""
        SELECT s.group
        FROM ShareEntity s
        WHERE s.shareTo.userName = :username
        AND s.isAccepted = true
    """)
    List<GroupEntity> findAcceptedSharedGroups(@Param("username") String username);
}
