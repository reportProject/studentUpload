package com.report.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Homework {
    
    int hw_no;
    int student_no;
    int notice_no;

    String file_name;
    int file_size;
    Date submitdate;
    
    int grade;

    byte[] data; // 업로드된 파일의 내용을 저장하기 위한 멤버 변수 선언
}
