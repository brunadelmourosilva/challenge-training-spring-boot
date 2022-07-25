package com.brunadelmouro.challengespring.service.impl;

import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.models.Universidade;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import com.brunadelmouro.challengespring.repositories.UniversidadeRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoServiceImpl implements AlunoService {

    AlunoRepository alunoRepository;

    CursoRepository cursoRepository;

    UniversidadeRepository universidadeRepository;

    public AlunoServiceImpl(final AlunoRepository alunoRepository, final CursoRepository cursoRepository, final UniversidadeRepository universidadeRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.universidadeRepository = universidadeRepository;
    }

    @Override
    public void importSheetToDatabase(final List<MultipartFile> multipartfiles) {

        if (!multipartfiles.isEmpty()) {

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
                        String universidadeSigla = row.getCell(6).getStringCellValue();
                        String cursoSigla = row.getCell(7).getStringCellValue();

                        Date dataMatriculaDate = new SimpleDateFormat("dd/MM/yyyy").parse(dataMatriculaString);

                        Aluno transaction = new Aluno(null, matricula, dataMatriculaDate, nome, nota1, nota2, nota3);

                        //----------------------------------------------------------------

                        Curso cursoEncontrado = cursoRepository.findBySigla(cursoSigla);
                        addCourseToStudents(cursoEncontrado, transaction);

                        Universidade universidadeEncontrada = universidadeRepository.findBySigla(universidadeSigla);
                        addUniversityToStudents(universidadeEncontrada, transaction);

                        alunoRepository.save(transaction);
                        cursoRepository.save(cursoEncontrado); //update
                        universidadeRepository.save(universidadeEncontrada); //update
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public static void addCourseToStudents(Curso cursoEncontrado, Aluno transaction){
        cursoEncontrado.getAlunos().add(transaction);
        transaction.setCurso(cursoEncontrado);
    }

    public static void addUniversityToStudents(Universidade universidadeEncontrada, Aluno transaction){
        universidadeEncontrada.getAlunos().add(transaction);
        transaction.setUniversidade(universidadeEncontrada);
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

    @Override
    public Aluno getStudentById(final Integer id) {
        //TODO ADD logs
        Optional<Aluno> alunoEncontrado = alunoRepository.findById(id);

        return alunoEncontrado.orElseThrow(() -> new ObjectNotFoundException(1, "Object not found"));
    }
}

