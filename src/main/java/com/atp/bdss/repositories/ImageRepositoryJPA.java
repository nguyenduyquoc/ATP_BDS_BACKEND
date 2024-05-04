package com.atp.bdss.repositories;

import com.atp.bdss.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepositoryJPA extends JpaRepository<Image, Integer> {
    List<Image> getImagesByLandId(String landId);
}
