package com.joblens.resume;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ResumeTextExtractor {
    public String extract(byte[] bytes, String contentType, String filename) throws IOException {
        return extract(new org.springframework.mock.web.MockMultipartFile("file", filename, contentType, bytes));
    }
    public String extract(MultipartFile file) throws IOException {
        String type = file.getContentType() == null ? "" : file.getContentType().toLowerCase();
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (type.contains("pdf") || name.endsWith(".pdf")) {
            try (var document = Loader.loadPDF(file.getBytes())) { return new PDFTextStripper().getText(document); }
        }
        if (type.contains("wordprocessingml") || name.endsWith(".docx")) {
            try (InputStream input = file.getInputStream(); XWPFDocument document = new XWPFDocument(input)) {
                StringBuilder text = new StringBuilder();
                document.getParagraphs().forEach(p -> text.append(p.getText()).append('\n'));
                document.getTables().forEach(table -> table.getRows().forEach(row -> row.getTableCells().forEach(cell -> text.append(cell.getText()).append(' '))));
                return text.toString();
            }
        }
        throw new IllegalArgumentException("Only PDF and DOCX resumes are supported");
    }
}
