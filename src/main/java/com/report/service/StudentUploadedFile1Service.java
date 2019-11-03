package com.report.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.report.dto.Homework;
import com.report.mapper.StudentUploadedFileMapper;

@Service
public class StudentUploadedFile1Service {

    @Autowired StudentUploadedFileMapper studentUploadedFileMapper;

    // 파일 목록 조회
    public List<Homework> findAll() {
        return studentUploadedFileMapper.findAll(); // uploadedFile 테이블의 모든 레코드를 조회
    }

    // 파일 저장
    public void save(MultipartFile multipartFile) throws IOException {
        // 업로드된 파일의 이름을 구한다.
        String fileName = Paths.get(multipartFile.getOriginalFilename()).getFileName().toString();

        Homework homework = new Homework(); // 엔터티 객체 생성
        homework.setFile_name(fileName); // 파일명 설정
        homework.setFile_size((int) multipartFile.getSize()); // 파일크 기 설정
        homework.setSubmitdate(new Date()); // 현재 시각 설정
        homework.setData(multipartFile.getBytes()); // 파일의 내용을 data 속성에 저장
        studentUploadedFileMapper.insert(homework); // uploadedFile 테이블에 저장
    }

    // 파일 삭제
    public void delete(int no) throws IOException {
        studentUploadedFileMapper.deleteByNo(no); // uploadedFile 테이블에서 레코드 삭제
    }

    // 다운로드하기 위해 파일을 조회하여 리턴
    public Homework getUploadedFile(int no) throws IOException {
        return studentUploadedFileMapper.findByNo(no); // uploadedFile 테이블에서 레코드 조회
    }
}
