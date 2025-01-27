package org.example.sample_book.common;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.example.sample_book.dto.BookFileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Component
public class FileUtils {
    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    // 단일 파일 업로드를 처리하고, 파일 정보를 반환하는 메서드
    public BookFileDto parseFileInfo(int boardIdx, MultipartHttpServletRequest request) throws Exception {
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }

        Iterator<String> fileTagNames = request.getFileNames();
        if (!fileTagNames.hasNext()) {
            return null;
        }

        String fileTagName = fileTagNames.next();
        MultipartFile file = request.getFile(fileTagName);
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 파일을 저장할 디렉터리를 설정
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime now = ZonedDateTime.now();
        String storedDir = uploadDir + "images/" + now.format(dtf);
        File fileDir = new File(storedDir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        String originalFileExtension;

        // 파일 확장자를 ContentType에 맞춰서 지정
        String contentType = file.getContentType();
        if (ObjectUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException("파일의 ContentType이 유효하지 않습니다.");
        } else {
            if (contentType.contains("image/jpeg")) {
                originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
                originalFileExtension = ".png";
            } else if (contentType.contains("image/gif")) {
                originalFileExtension = ".gif";
            } else {
                throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
            }
        }

        // 저장에 사용할 파일 이름을 조합
        String storedFileName = System.nanoTime() + originalFileExtension;
        String storedFilePath = storedDir + "/" + storedFileName;

        // 파일 정보를 DTO에 저장
        BookFileDto dto = new BookFileDto();
        dto.setBookId(boardIdx);
        dto.setFileSize(Long.toString(file.getSize()));
        dto.setOriginalFileName(file.getOriginalFilename());
        dto.setStoredFilePath(storedFilePath);

        // 파일 저장
        File savedFile = new File(storedFilePath);
        file.transferTo(savedFile);

        return dto;
    }
}
