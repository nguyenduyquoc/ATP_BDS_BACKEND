package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestCreateProject;
import com.atp.bdss.entities.Land;
import com.atp.bdss.entities.Transaction;
import com.atp.bdss.services.IProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSocketControllerAPI {

    final IProjectService projectService;

    @MessageMapping("/projects")@SendTo("/topic/add_project")
    public String handleAddProjectRequest(RequestCreateProject project) throws IOException {
        System.out.println("add project"+ project.getName() + "successfully");
        return  "add project"+ project.getName() + "successfully";
    }

    @MessageMapping("/projects")@SendTo("/topic/block_land")
    public String handleBlockLandRequest(Land land) throws IOException {
        System.out.println("Lô đất "+ land.getId() + " đã được khóa!!!");
        return  "Lô đất "+ land.getId() + "  đã được khóa!!!";
    }


}
