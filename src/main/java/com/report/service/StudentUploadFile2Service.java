package com.report.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.report.config.StudentUploadProperties;
import com.report.dto.Homework;
import com.report.mapper.StudentUploadedFileMapper;


@Service
public class StudentUploadFile2Service {

    @Autowired StudentUploadProperties studentUploadProperties; // application.properties 파일의 설정 값
    @Autowired StudentUploadedFileMapper studentUploadedFileMapper;

    // 파일 목록 조회
    public List<Homework> findAll() {
        return studentUploadedFileMapper.findAll(); // uploadedFile 테이블의 모든 레코드를 조회
    }

    // 파일 저장
    public void save(MultipartFile multipartFile) throws IOException {
        // 업로드된 파일의 이름을 구한다.
        String fileName = Paths.get(multipartFile.getOriginalFilename()).getFileName().toString();

        Homework homework = new Homework();  // 엔터티 객체 생성
        homework.setFile_name(fileName);  // 파일명 설정
        homework.setFile_size((int) multipartFile.getSize());  // 파일크 기 설정
        homework.setSubmitdate(new Date()); // 현재 시각 설정
        studentUploadedFileMapper.insert(homework);  // uploadedFile 테이블에 저장

        // 파일의 내용을 업로드 폴더 아래에 파일을 생성하여 저장
        Files.write(getFilePath(homework), multipartFile.getBytes());
    }

    // 파일 삭제
    public void delete(int no) throws IOException {
        try {
            // 삭제할 파일 레코드 조회
            studentUploadedFileMapper.deleteByNo(no); // uploadedFile 테이블에서 레코드 삭제
            Homework homework = studentUploadedFileMapper.findByNo(no);
            Files.delete(getFilePath(homework)); // 업로드 폴더에 저장된 파일 삭제
        } catch (Exception e) {
        }
    }

    // 다운로드하기 위해 파일을 조회하여 리턴
    public Homework getUploadedFile(int no) throws IOException {
        // uploadedFile 테이블에서 레코드 조회
        Homework homework = studentUploadedFileMapper.findByNo(no);

        // 파일의 내용을 업로드 폴더 아래의 파일에서 읽어온다.
        byte[] data = Files.readAllBytes(getFilePath(homework));
        homework.setData(data); // 파일의 내용을 UploadedFile 객체에 채운다.
        return homework; // UploadedFile 객체 리턴
    }

    // 파일의 내용을 저장할 파일의 경로명을 리턴한다.
    private Path getFilePath(Homework homework) {
        // UploadedFile 객체의 id 값은 uploadedFile 테이블의 primary key 이다.
        // 이 값을 파일명으로 사용한다.
        //
        // uploadProperties.getLocalPath() 값은 application.properties 파일의 설정값이고,
        // 파일을 저장할 폴더의 경로명을 지정한다.
        String newFileName = String.valueOf(homework.getHw_no());

        // 폴더의 경로명과 파일이름을 결합한 경로 객체를 리턴한다.
        return Paths.get(studentUploadProperties.getLocalPath(), newFileName);
    }

}
