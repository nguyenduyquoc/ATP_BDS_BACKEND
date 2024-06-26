package com.atp.bdss.repositories;

import com.atp.bdss.dtos.AreaDTO;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationArea;
import com.atp.bdss.entities.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepositoryJPA extends JpaRepository<Area, String> {

    boolean existsByNameIgnoreCase(String name);

    @Query(value = "select new com.atp.bdss.entities.Area( " +
            "   a.id, " +
            "   a.name, " +
            "   a.lands" +
            ")" +
            "from Area a " +
            "where ( a.project.id = :#{#projectId}) " +
            "order by a.id desc"
    )
    List<Area> getAllAreaForProject(@Param("projectId")String projectId);
    // lay cac phan khu cho mot du an


    @Query(value = "select new com.atp.bdss.dtos.AreaDTO( " +
            "   a.id, " +
            "   a.name, " +
            "   a.project.name " +
            ")" +
            "from Area a " +
            "WHERE " +
            "   (:#{#request.areaName} IS NULL OR :#{#request.areaName} = '' OR a.name LIKE %:#{#request.areaName}%) " +
            "   AND (:#{#request.projectId} IS NULL OR a.project.id = :#{#request.projectId}) " +

            "ORDER BY a.id DESC")
    Page<AreaDTO> getAreaPagination(@Param("request") RequestPaginationArea request, Pageable pageable);
    // phan trang tat ca cac phan khu

    boolean existsByNameIgnoreCaseAndProjectId(String name, String projectId);



}
