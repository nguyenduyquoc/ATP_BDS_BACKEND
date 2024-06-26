package com.atp.bdss.controllers;

import com.atp.bdss.entities.Land;
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


    @MessageMapping("/lands_unlock")@SendTo("/topic/unlock_land")
    public String handleAddProjectRequest(Land land) throws IOException {
        System.out.println("Lô đất "+ land.getName() + " đã được mở!!!");
        return  "Lô đất "+ land.getName() + "  đã được mở !!!";
    }

    @MessageMapping("/lands_lock")@SendTo("/topic/block_land")
    public String handleBlockLandRequest(Land land) throws IOException {
        System.out.println("Lô đất "+ land.getName() + " đã được khóa!!!");
        return  "Lô đất "+ land.getName() + "  đã được khóa!!!";
    }


}
