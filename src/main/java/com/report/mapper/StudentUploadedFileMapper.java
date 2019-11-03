package com.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.report.dto.Homework;

@Mapper
public interface StudentUploadedFileMapper {

	Homework findByNo(int hw_no);
	List<Homework> findAll();
	void insert(Homework homework);
	void deleteByNo(int hw_no);
	
}
