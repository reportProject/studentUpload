package com.report.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.report.dto.Homework;
import com.report.service.StudentUploadedFile1Service;

// 파일 데이터를 DB에 저장
@Controller
public class StudentFile1Controller {

    @Autowired StudentUploadedFile1Service studentUploadedFile1Service;

    @RequestMapping("file1/list")
    public String list(Model model) {
        model.addAttribute("files", studentUploadedFile1Service.findAll());
        return "file/list";
    }

    @RequestMapping(value="file1/upload", method=RequestMethod.POST)
    public String upload(@RequestParam("fileUpload") MultipartFile[] multipartFiles) throws IOException {
        for(MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.getSize() <= 0) continue;
            studentUploadedFile1Service.save(multipartFile);
        }
        return "redirect:list";
    }

    @RequestMapping("file1/delete")
    public String delete(@RequestParam("id") int id) throws Exception {
    	studentUploadedFile1Service.delete(id);
        return "redirect:list";
    }

    @RequestMapping("file1/download")
    public void download(@RequestParam("id") int id, HttpServletResponse response) throws Exception {
        Homework homework = studentUploadedFile1Service.getUploadedFile(id);
       if (homework == null) return;
        String fileName = URLEncoder.encode(homework.getFile_name(),"UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            output.write(homework.getData());
        }
    }
}
