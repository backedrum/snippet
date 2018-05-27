package com.backedrum.repository;

import com.backedrum.model.HowTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface HowToRepository extends JpaRepository<HowTo, Long> {

    @Query("SELECT h.tag FROM HowTo h WHERE h.tag IN (select distinct hh.tag from HowTo hh)")
    List<String> findAllTags();

    Stream<HowTo> findByTag(String tag);
}
