package com.app.wcc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.wcc.domain.PostCodeDetail;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface PostCodeDetailRepository extends CrudRepository<PostCodeDetail, Long> {
    PostCodeDetail findPostCodeDetailByPostcode(String postcode);

}
