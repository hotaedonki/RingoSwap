package net.ringo.ringoSwap.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//파일 경로를 처리하는 메서드 목록
@Controller
public class FileController {

    @GetMapping("/boardFile/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        // 파일 경로 설정
        String uploadDirectory = "C:/boardfile";
        Path filePath = Paths.get(uploadDirectory, filename);
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists()) {
            // 파일 다운로드를 위한 Response 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } else {
            // 파일이 존재하지 않을 경우 예외 처리
            throw new FileNotFoundException("File not found: " + filename);
        }
    }
}