package com.brunadelmouro.challengespring.service.impl;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.dto.PageResponse;
import com.brunadelmouro.challengespring.mappers.AlunoMapper;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Curso;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlunoServiceImpl implements AlunoService {

    AlunoRepository alunoRepository;

    CursoRepository cursoRepository;

    UniversidadeRepository universidadeRepository;

    @Autowired
    AlunoMapper alunoMapper;

    public AlunoServiceImpl(final AlunoRepository alunoRepository, final CursoRepository cursoRepository, final UniversidadeRepository universidadeRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.universidadeRepository = universidadeRepository;
    }

    @Override
    public void importSheetToDatabase(final List<MultipartFile> multipartfiles) {
        //TODO add logs
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

                        transaction.setMedia((transaction.getNota1() + transaction.getNota2() + transaction.getNota3()) / 3);

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
            });
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
    public PageResponse getAllStudents(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        //set a condition if return a desc type or asc type on sort variable
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending(); //if ? return thing : return other thing

        //set page and pageable
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Aluno> alunosPage = alunoRepository.findAll(pageable);

        //get content for page object
        List<Aluno> listaDeAlunos = alunosPage.getContent();

        //convert each domain type to dto type
        List<AlunoResponseDTO> content =
                listaDeAlunos.stream().map(aluno -> alunoMapper.domainToResponseDTO(aluno)).collect(Collectors.toList());

        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(content);
        pageResponse.setPageNo(alunosPage.getNumber());
        pageResponse.setPageSize(alunosPage.getSize());
        pageResponse.setTotalElements(alunosPage.getTotalElements());
        pageResponse.setTotalPages(alunosPage.getTotalPages());
        pageResponse.setLast(alunosPage.isLast());

        return pageResponse;
    }
//https://www.javaguides.net/2021/10/spring-boot-pagination-and-sorting-rest-api.html

    @Override
    public Page<AlunoResponseDTO> getStudentsByCursoAndUniversidadeFilter(Integer cursoId, Integer universidadeId, int pageNo, int pageSize, String sortBy, String sortDir) {

        Page<Aluno> alunosFiltrados = null;
        if (cursoId != null && universidadeId != null) {
            alunosFiltrados =  alunoRepository.findAllBy(
                                    cursoId,
                                    universidadeId,
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
