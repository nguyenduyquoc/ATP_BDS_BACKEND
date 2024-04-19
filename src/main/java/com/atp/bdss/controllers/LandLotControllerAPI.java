package com.atp.bdss.controllers;/*
package com.atp.bds.controllers;

import com.atp.bds.dtos.requests.RequestCreateLandLot;
import com.atp.bds.dtos.requests.RequestPaginationLandLot;
import com.atp.bds.dtos.responses.ResponseData;
import com.atp.bds.dtos.responses.ResponseDataWithPagination;
import com.atp.bds.services.JLandLotService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/land_lots")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandLotControllerAPI {

    final JLandLotService landLotService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allLandLots(@PathVariable String id, @RequestBody RequestPaginationLandLot requestParam){
        requestParam.setProjectId(id);
        return landLotService.allLandLotOfProject(requestParam);
    }

    @PostMapping(value = "/{id}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createLandLot(@PathVariable String id, @Valid @RequestBody RequestCreateLandLot request) {
        request.setProject_id(id);
        return  landLotService.createLandLotOfProject(request);
    }
}
*/
