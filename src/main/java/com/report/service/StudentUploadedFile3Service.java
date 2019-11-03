package com.report.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.report.config.StudentUploadProperties;
import com.report.dto.Homework;
import com.report.mapper.StudentUploadedFileMapper;

@Service
public class StudentUploadedFile3Service {

    @Autowired StudentUploadProperties studentUploadProperties; // application.properties 파일의 설정 값
    @Autowired StudentUploadedFileMapper studentUploadedFileMapper;
    @Autowired ServletContext servletContext; // webapp 폴더의 경로명을 계산하기 위해 필요한 객체

    // 파일 목록 조회
    public List<Homework> findAll() {
        return studentUploadedFileMapper.findAll();
    }

    // application.properties 파일에서 urlPath 설정값을 리턴한다.
    // webapp 폴더 아래의,  다운로드할 파일들이 들어있는 폴더의 경로이다
    public String getUrlPath() {
        return studentUploadProperties.getUrlPath();
    }

    // 파일 저장
    public void save(MultipartFile multipartFile) throws IOException {
        // 업로드된 파일의 이름을 구한다.
        String fileName = Paths.get(multipartFile.getOriginalFilename()).getFileName().toString();
        String extension = getExtension(fileName); // 파일의 확장자를 구한다.

        Homework homework = new Homework();  // 엔터티 객체 생성
        homework.setFile_name(""); // 새 파일명을 아직 결정할 수 없어서 공백만 저장한다.
        homework.setFile_size((int) multipartFile.getSize()); // 파일 크기 설정
        homework.setSubmitdate(new Date()); // 현재 시각 설정
        studentUploadedFileMapper.insert(homework); // uploadedFile 테이블에 저장
        // 저장하자마자 auto increment인 id 필드 값이 결정된다.
        // id 필드 값과, 업로드된 파일명에서 확장자 부분을 결합하여 새 파일명 생성
        String newFileName = homework.getHw_no() + extension;
        homework.setFile_name(newFileName); // 새 파일명 설정
        studentUploadedFileMapper.insert(homework); // 새 파일명을 테이블에 update 한다.

        // 파일의 내용을 업로드 폴더 아래에 파일을 생성하여 저장
        Files.write(getFilePath(homework), multipartFile.getBytes());
    }

    // 파일 삭제
    public void delete(int no) {
        try {
            // 삭제할 파일 레코드 조회
            studentUploadedFileMapper.deleteByNo(no);  // uploadedFile 테이블에서 레코드 삭제
            Homework homework = studentUploadedFileMapper.findByNo(no);
            Files.delete(getFilePath(homework)); // 업로드 폴더에 저장된 파일 삭제
        } catch (Exception e) {
        }
    }

    // 파일명에서 확장자 부분을 리턴한다.
    private String getExtension(String fileName) {
        String extension = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) extension = fileName.substring(index);
        return extension;
    }

    // 파일의 내용을 저장할 파일의 경로명을 리턴한다.
    private Path getFilePath(Homework homework) {
        // servletContext.getRealPath(File.separator) => webapp 폴더의 절대 경로명을 구한다.
        // uploadProperties.getUrlPath() => 파일을 저장할 폴더의, webapp 폴더 아래 경로명을 구한다.
        String folderPath = servletContext.getRealPath(File.separator) + studentUploadProperties.getUrlPath();

        // 폴더의 경로명과 파일이름을 결합한 경로 객체를 리턴한다.
        return Paths.get(folderPath, homework.getFile_name());
    }

}
