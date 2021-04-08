package com.app.wcc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.wcc.domain.PostCodeHistory;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface PostCodeHistoryRepository extends CrudRepository<PostCodeHistory, Long> {

}
