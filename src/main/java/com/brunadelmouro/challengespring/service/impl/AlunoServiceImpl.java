package com.brunadelmouro.challengespring.service.impl;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.models.Job;
import com.brunadelmouro.challengespring.models.Universidade;
import com.brunadelmouro.challengespring.repositories.AlunoRepository;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import com.brunadelmouro.challengespring.repositories.UniversidadeRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.apache.commons.math3.util.Precision;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class AlunoServiceImpl implements AlunoService {

    private final AlunoRepository alunoRepository;

    private final CursoRepository cursoRepository;

    private final UniversidadeRepository universidadeRepository;
    @Autowired
    AlunoMapper alunoMapper;

    public AlunoServiceImpl(final AlunoRepository alunoRepository, final CursoRepository cursoRepository, final UniversidadeRepository universidadeRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.universidadeRepository = universidadeRepository;
    }

    @Override
    public void importSheetToDatabase(final Job job) {
        try {
            FileInputStream excelInput = new FileInputStream(job.getUrlArquivo().concat(job.getNomeArquivo()));

            XSSFWorkbook workBook = new XSSFWorkbook(excelInput);

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

                Double middle = Precision.round(
                        (transaction.getNota1() +
                                transaction.getNota2() +
                                transaction.getNota3()) / 3,
                        2);
                transaction.setMedia(middle);

                //----------------------------------------------------------------

                Curso cursoEncontrado = cursoRepository.findBySigla(cursoSigla);
                addCourseToStudents(cursoEncontrado, transaction);

                Universidade universidadeEncontrada = universidadeRepository.findBySigla(universidadeSigla);
                addUniversityToStudents(universidadeEncontrada, transaction);

                alunoRepository.save(transaction);
                log.info("Aluno {} saved on database", transaction.getNome());

                cursoRepository.save(cursoEncontrado); //update
                log.info("Curso {} updated on database", cursoEncontrado.getNome());

                universidadeRepository.save(universidadeEncontrada); //update
                log.info("Universidade {} updated on database", universidadeEncontrada.getNome());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addCourseToStudents(Curso cursoEncontrado, Aluno transaction) {
        cursoEncontrado.getAlunos().add(transaction);
        transaction.setCurso(cursoEncontrado);
    }

    public static void addUniversityToStudents(Universidade universidadeEncontrada, Aluno transaction) {
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
        Optional<Aluno> alunoEncontrado = alunoRepository.findById(id);
        log.info("Aluno found");

        return alunoEncontrado.orElseThrow(() -> new ObjectNotFoundException(1, "Object not found"));
    }

    @Override
    public Page<AlunoResponseDTO> getStudentsByCursoAndUniversidadeFilter(Integer cursoId, Integer universidadeId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Aluno> alunosFiltrados = null;

        //universidade e curso
        if (cursoId != null || universidadeId != null) {
            alunosFiltrados = alunoRepository.findAllBy(
                    cursoId,
                    universidadeId,
                    PageRequest.of(
                            pageNo,
                            pageSize,
                            Sort.by(
                                    sortDir.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)));
        }

        //lista todos os alunos por ordem decrescente de nota
        if (cursoId == null && universidadeId == null) {
            alunosFiltrados = alunoRepository.findAll(
                    PageRequest.of(
                            pageNo,
                            pageSize,
                            Sort.by(
                                    sortDir.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)));
        }

        //convert Aluno to ALunoResponseDTO
        return alunosFiltrados.map(alunoMapper::domainToResponseDTO);
    }

}
//https://stackoverflow.com/questions/39036771/how-to-map-pageobjectentity-to-pageobjectdto-in-spring-data-rest
//https://www.javaguides.net/2021/10/spring-boot-pagination-and-sorting-rest-api.html
