package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.create.RequestCreateProject;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationProject;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface IProjectService {

    ResponseDataWithPagination allProjects (RequestPaginationProject request);

    ResponseData create(RequestCreateProject request) throws IOException;

    ResponseData findProjectById(String id);

    ResponseData updateProject(RequestCreateProject request) throws IOException;

    ResponseData allProjectsNoPagination();

    ResponseData delete(String id);
}
