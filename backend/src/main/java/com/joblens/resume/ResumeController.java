package com.joblens.resume;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@RestController @RequestMapping("/api/resumes") @RequiredArgsConstructor
public class ResumeController {
    private static final long MAX_SIZE = 10 * 1024 * 1024;
    private final ResumeRepository resumes;
    private final ResumeProcessingService processingService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResumeDocument upload(Authentication auth, @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resume file is empty");
        if (file.getSize() > MAX_SIZE) throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Resume must be at most 10 MB");
        try {
            ResumeDocument resume = new ResumeDocument(); resume.setUserEmail(auth.getName()); resume.setOriginalFilename(file.getOriginalFilename()); resume.setContentType(file.getContentType()); resume.setFileSize(file.getSize()); resume.setOriginalFile(file.getBytes()); resume.setProcessingStatus("PENDING"); resume.setUploadedAt(Instant.now()); ResumeDocument saved=resumes.save(resume); processingService.process(saved.getId(),file.getBytes(),file.getContentType(),file.getOriginalFilename()); return saved;
        } catch (IOException e) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Could not extract resume text", e); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), e); }
    }

    @GetMapping("/latest")
    ResumeDocument latest(Authentication auth) { return resumes.findFirstByUserEmailOrderByUploadedAtDesc(auth.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No resume uploaded")); }

    @GetMapping("/latest/profile")
    ExtractedProfile profile(Authentication auth) { return resumes.findFirstByUserEmailOrderByUploadedAtDesc(auth.getName()).map(ResumeDocument::getExtractedProfile).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No resume uploaded")); }

    @GetMapping("/status")
    Map<String, Object> status(Authentication auth) { return resumes.findFirstByUserEmailOrderByUploadedAtDesc(auth.getName()).map(r -> { Map<String,Object> result=new java.util.HashMap<>(); result.put("id",r.getId());result.put("status",r.getProcessingStatus());result.put("uploadedAt",r.getUploadedAt());if(r.getProcessingError()!=null)result.put("error",r.getProcessingError());return result; }).orElse(Map.of("status", "NOT_UPLOADED")); }

    @PostMapping("/{id}/retry")
    ResumeDocument retry(Authentication auth,@PathVariable String id) { ResumeDocument resume=resumes.findByIdAndUserEmail(id,auth.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resume not found")); if(!"FAILED".equals(resume.getProcessingStatus())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Only failed resumes can be retried"); resume.setProcessingStatus("PENDING");resume.setProcessingError(null);ResumeDocument saved=resumes.save(resume);processingService.process(saved.getId(),saved.getOriginalFile(),saved.getContentType(),saved.getOriginalFilename());return saved; }
}
