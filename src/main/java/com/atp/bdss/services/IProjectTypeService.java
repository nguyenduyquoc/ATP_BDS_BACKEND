package com.atp.bdss.services;

import com.atp.bdss.dtos.ProjectTypeDTO;
import com.atp.bdss.dtos.requests.create.RequestCreateProjectType;
import com.atp.bdss.dtos.responses.ResponseData;
import org.springframework.stereotype.Service;


@Service
public interface IProjectTypeService {

    ResponseData listProjectType();

    ResponseData create(RequestCreateProjectType request);

    ResponseData update(ProjectTypeDTO request);

    ResponseData delete(Short id);

    ResponseData findProjectTypeById(Short id);
}
