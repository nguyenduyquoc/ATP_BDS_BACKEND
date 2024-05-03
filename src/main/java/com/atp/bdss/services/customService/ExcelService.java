package com.atp.bdss.services.customService;

import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Land;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AreaRepositoryJPA;
import com.atp.bdss.repositories.LandRepositoryJPA;
import com.atp.bdss.utils.ErrorsApp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExcelService {

    final LandRepositoryJPA landRepository;
    final AreaRepositoryJPA areaRepository;

    public ResponseData importExcelFile(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();

                // Kiểm tra nếu dòng là dòng tiêu đề thì bỏ qua
                if (row.getRowNum() == 0) {
                    continue;
                }

                Land land = Land.builder()
                        .name(getStringValueFromCell(row.getCell(0)))
                        .description(getStringValueFromCell(row.getCell(1)))
                        .thumbnail(getStringValueFromCell(row.getCell(2)))
                        .address(getStringValueFromCell(row.getCell(3)))
                        .status(getNumericValueFromCell(row.getCell(4)))
                        .price(getStringValueFromCell(row.getCell(5)))
                        .deposit(getStringValueFromCell(row.getCell(6)))
                        .acreage(getNumericValueFromCell(row.getCell(7)))
                        .typeOfApartment(getStringValueFromCell(row.getCell(8)))
                        .direction(getStringValueFromCell(row.getCell(9)))
                        .area(areaRepository.findById(getStringValueFromCell(row.getCell(10)))
                                .orElseThrow(() -> new CustomException(ErrorsApp.LAND_NOT_FOUND)))
                        .build();

                landRepository.save(land);
            }

            return ResponseData.builder()
                    .code(HttpStatus.OK.value())
                    .message("Import file successfully").build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseData.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to import file").build();
        }
    }

    private String getStringValueFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // Format giá trị số thành chuỗi
            return String.valueOf(cell.getNumericCellValue());
        } else {
            // Xử lý các kiểu dữ liệu khác nếu cần
            return null;
        }
    }

    private short getNumericValueFromCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return 0; // Hoặc giá trị mặc định khác tùy vào yêu cầu của bạn
        }
        return (short) cell.getNumericCellValue();
    }
}
