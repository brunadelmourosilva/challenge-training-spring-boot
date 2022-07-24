package com.brunadelmouro.challengespring.service.impl;

import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlunoServiceImpl implements AlunoService {

    AlunoRepository alunoRepository;

    public AlunoServiceImpl(final AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public void importSheetToDatabase(final List<MultipartFile> multipartfiles) {

        if (!multipartfiles.isEmpty()) {
            List<Aluno> transactions = new ArrayList<>();

            multipartfiles.forEach(multipartfile -> {
                try {
                    XSSFWorkbook workBook = new XSSFWorkbook(multipartfile.getInputStream());

                    XSSFSheet sheet = workBook.getSheetAt(0);
                    // looping through each row
                    for (int rowIndex = 0; rowIndex <= getNumberOfNonEmptyCells(sheet, 0) - 1; rowIndex++) {
                        // current row
                        XSSFRow row = sheet.getRow(rowIndex);
                        // skip the first row because it is a header row

                        String matricula = row.getCell(0).getStringCellValue();
                        String dataMatriculaString = row.getCell(1).getStringCellValue();
                        String nome = row.getCell(2).getStringCellValue();
                        Double nota1 = row.getCell(3).getNumericCellValue();
                        Double nota2 = row.getCell(4).getNumericCellValue();
                        Double nota3 = row.getCell(5).getNumericCellValue();

                        Date dataMatriculaDate = new SimpleDateFormat("dd/MM/yyyy").parse(dataMatriculaString);

                        Aluno transaction = new Aluno(null, matricula, dataMatriculaDate, nome, nota1, nota2, nota3);
                        transactions.add(transaction);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });

            if (!transactions.isEmpty()) {
                // save to database
                alunoRepository.saveAll(transactions);
            }
        }
    }

    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }
}

