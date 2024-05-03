package com.atp.bdss.controllers;

import com.atp.bdss.dtos.ProjectTypeDTO;
import com.atp.bdss.dtos.requests.create.RequestCreateProjectType;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.IProjectTypeService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/project-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectTypeControllerAPI {

    final IProjectTypeService projectTypeService;
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData allProjectType() {

        return projectTypeService.listProjectType();
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getProjectById(@PathVariable Short id){

        return projectTypeService.findProjectTypeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createProjectType(@ModelAttribute @Valid RequestCreateProjectType request) {

        return projectTypeService.create(request);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateProjectType(@ModelAttribute @Valid ProjectTypeDTO request) {

        return  projectTypeService.update(request);
    }


    // delete project
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData deleteProjectType(@PathVariable Short id)
    {
        return projectTypeService.delete(id);
    }


}
