package com.atp.bdss.repositories;


import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.entities.Land;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LandRepositoryJPA extends JpaRepository<Land, String> {

    @Query(value = "select distinct new com.atp.bdss.entities.Land( " +
            "   l.id, " +
            "   l.name, " +
            "   l.description, " +
            "   l.thumbnail, " +
            "   l.address, " +
            "   l.status, " +
            "   l.price, " +
            "   l.deposit, " +
            "   l.acreage," +
            "   l.area" +
            ")" +
            "FROM Land l " +
            "LEFT JOIN l.area a " +
            "LEFT JOIN a.project p " +
            "WHERE ((:#{#request.searchName} IS NULL OR :#{#request.searchName} = '') " +
            "OR (l.name LIKE %:#{#request.searchName}% " +
            "OR a.name LIKE %:#{#request.searchName}% " +
            "OR p.name LIKE %:#{#request.searchName}%)) " +
            "AND (:#{#request.projectId} IS NULL OR p.id = :#{#request.projectId}) " +
            "AND (:#{#request.areaId} IS NULL OR a.id = :#{#request.areaId}) " +
            "ORDER BY l.id DESC")

    Page<Land> getLandPagination(@Param("request") RequestPaginationLand request, Pageable pageable);

    @Query("SELECT DISTINCT l " +
            "FROM Land l " +
            "inner JOIN Area a on a.id = l.area.id " +
            "inner JOIN Project p on p.id = a.project.id " +
            "WHERE :#{#projectId} = p.id " +
            "ORDER BY l.id DESC")
    List<Land> getLandFindByProjectId(@Param("projectId") String projectId);

    boolean existsByNameIgnoreCaseAndAreaId(String name, String areaId);
}
