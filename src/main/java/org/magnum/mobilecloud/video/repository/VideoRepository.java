package org.magnum.mobilecloud.video.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    @Query("FROM Video WHERE name LIKE CONCAT('%',:title,'%')")
    public List<Video> searchByTitle(@Param("title") String title);

    @Query("FROM Video WHERE duration < :duration")
    public List<Video> searchByBelowDuration(@Param("duration") long duration);
}
