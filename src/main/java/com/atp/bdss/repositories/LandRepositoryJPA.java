package com.atp.bdss.repositories;


import com.atp.bdss.dtos.requests.RequestPaginationLand;
import com.atp.bdss.entities.Land;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LandRepositoryJPA extends JpaRepository<Land, String> {

    boolean existsByNameIgnoreCase(String name);

    @Query(value = "select new com.atp.bdss.entities.Land( " +
            "   l.id, " +
            "   l.name, " +
            "   l.description, " +
            "   l.thumbnail, " +
            "   l.address, " +
            "   l.status, " +
            "   l.price, " +
            "   l.deposit, " +
            "   l.acreage" +
            ")" +
            "from Land l " +
            "left join l.area a " +
            "where (:#{#request.projectId} is null or l.area.project.id = :#{#request.projectId}) " +
            "and (:#{#request.areaId} IS NULL OR :#{#request.areaId} = '' OR l.name LIKE %:#{#request.searchName}%) " +
            "and (:#{#request.areaId} is null or l.area.id = :#{#request.areaId}) " +
            "order by l.id desc"
    )
    Page<Land> getLandPagination(@Param("request") RequestPaginationLand request, Pageable pageable);

    boolean existsByNameIgnoreCaseAndAreaId(String name, String areaId);
}
