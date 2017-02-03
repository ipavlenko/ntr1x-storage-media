package com.ntr1x.storage.media.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.media.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	
	@Query(
        " SELECT p"
      + " FROM Publication p"
      + " WHERE (:scope IS NULL OR p.scope = :scope)"
      + "	AND (:user IS NULL OR (:user = 0 AND p.user IS NULL) OR p.user.id = :user)"
      + "	AND (:relate IS NULL OR (:relate = 0 AND p.relate IS NULL) OR p.relate.id = :relate)"
      + "	AND (:since IS NULL OR p.published >= :since)"
      + "	AND (:until IS NULL OR p.published <= :until)"
    )
    Page<Publication> query(
		@Param("scope") Long scope,
		@Param("user") Long user,
		@Param("relate") Long relate,
		@Param("since") LocalDateTime since,
		@Param("until") LocalDateTime until,
		Pageable pageable
	);
	
	@Query(
        " SELECT p"
      + " FROM Publication p"
      + " WHERE (:scope IS NULL OR p.scope = :scope)"
      + "	AND (p.id = :id)"
    )
	Publication select(@Param("scope") Long scope, @Param("id") long id);
}
