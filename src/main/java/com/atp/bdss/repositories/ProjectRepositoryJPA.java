package com.atp.bdss.repositories;

import com.atp.bdss.dtos.requests.RequestPaginationProject;
import com.atp.bdss.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepositoryJPA extends JpaRepository<Project, String> {



    /**
     @Query(value = "select new com.atp.bds.dtos.ProjectDTO( " +
            "   p.id, " +
            "   p.name, " +
            "   p.description, " +
            "   p.thumbnail, " +
            "   p.status, " +
            "   p.address, " +
            "   p.startDate, " +
            "   p.endDate, " +
            "   p.projectType, " +
            "   p.investor" +
            ")" +
            "from Project p " +
            " WHERE " +
            "   (:#{#request.status} IS NULL OR p.status = :#{#request.status} OR p.status = 3) " +
            "   AND (:#{#request.nameProject} IS NULL OR :#{#request.nameProject} = '' OR p.name LIKE %:#{#request.nameProject}%) " +
            "   AND (:#{#request.investorId} IS NULL OR p.investor.id = :#{#request.investorId}) " +
            "   AND (:#{#request.provinceId} IS NULL OR p.district.province.id = :#{#request.provinceId}) " +
            "   AND (:#{#request.districtId} IS NULL OR p.district.id = :#{#request.districtId}) " +

            "   AND (:#{#request.projectTypeId} IS NULL OR p.projectType.id = :#{#request.projectTypeId}) " +

            "   AND (:#{#request.priceFrom} IS NULL OR :#{#request.priceTo} IS NULL OR EXISTS (SELECT 1 FROM Area a JOIN a.lands l WHERE a.project.id = p.id AND l.price BETWEEN :#{#request.priceFrom} AND :#{#request.priceTo})) " +
            " ORDER BY p.id DESC")
    Page<ProjectDTO> getProjectDTOPagination(@Param("request") RequestPaginationProject request, Pageable pageable);
     */


    @Query(value = "select new com.atp.bdss.entities.Project( " +
            "   p.id, " +
            "   p.name, " +
            "   p.description," +
            "   p.thumbnail," +
            "   p.address," +
            "   p.status," +
            "   p.startDate," +
            "   p.endDate," +
            "   p.qrImg," +
            "   p.bankNumber," +
            "   p.bankName," +
            "   p.hostBank," +
            "   p.investor," +
            "   p.investorPhone," +
            "   p.projectType," +
            "   p.district" +
            ")" +
            "from Project p " +
            "WHERE " +
            "   (:#{#request.status} IS NULL OR p.status = :#{#request.status} OR p.status = 2) " +
            "   AND (:#{#request.nameProject} IS NULL OR :#{#request.nameProject} = '' OR p.name LIKE %:#{#request.nameProject}%) " +
            "   AND (:#{#request.investor} IS NULL OR :#{#request.investor} = '' OR p.investor LIKE %:#{#request.investor}%) " +
            "   AND (:#{#request.provinceId} IS NULL OR p.district.province.id = :#{#request.provinceId}) " +
            "   AND (:#{#request.districtId} IS NULL OR p.district.id = :#{#request.districtId}) " +

            "   AND (:#{#request.projectTypeId} IS NULL OR p.projectType.id = :#{#request.projectTypeId}) " +

            "   AND (:#{#request.priceFrom} IS NULL OR :#{#request.priceTo} IS NULL OR EXISTS (SELECT 1 FROM Area a JOIN a.lands l WHERE a.project.id = p.id AND l.price BETWEEN :#{#request.priceFrom} AND :#{#request.priceTo})) " +
            " ORDER BY CASE " +
            "            WHEN p.status = 2 THEN 0 " + // Ưu tiên các dự án có status là 2 lên trên cùng
            "            WHEN p.status = 1 THEN 1 " + // Sau đó đến các dự án sắp diên ra (status = 1)
            "            WHEN p.status = 3 THEN 2 " + // Sau đó đến các dự án chưa bắt đầu (status = 1)
            "            WHEN p.status = 0 THEN 3 " + // Sau đó đến các dự án chưa bắt đầu (status = 1)
            "            ELSE 2 " + // Cuối cùng là các dự án đã kết thúc (các giá trị status khác)
            "          " +
            "          END, " +
            "          p.id DESC")
    Page<Project> getProjectPagination(@Param("request") RequestPaginationProject request, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
