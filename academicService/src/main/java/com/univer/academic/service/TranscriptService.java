package com.univer.academic.service;

//import com.lowagie.text.*;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
import com.univer.academic.domain.Grade;
import com.univer.academic.domain.Transcript;
import com.univer.academic.dto.TranscriptResponse;
import com.univer.academic.repository.GradeRepository;
import com.univer.academic.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.S3Exception;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TranscriptService {

    private final GradeRepository gradeRepository;
    private final TranscriptRepository transcriptRepository;
//    private final S3Client s3;
//    private final S3Presigner presigner;

    @Value("${app.s3.bucket:}")
    private String bucket;

    @Value("${app.s3.presign-seconds:3600}")
    private long presignSeconds;

    public TranscriptService(GradeRepository gradeRepository,
                             TranscriptRepository transcriptRepository) {
        this.gradeRepository = gradeRepository;
        this.transcriptRepository = transcriptRepository;

    }

    @Transactional
    public TranscriptResponse generateTranscript(Long studentId) {
        if (bucket == null || bucket.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3_BUCKET not configured");
        }

        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No grades found for student " + studentId);
        }

        // GPA per semester (simple average of points)
        Map<String, Double> gpaBySemester = grades.stream()
                .collect(Collectors.groupingBy(Grade::getSemester, Collectors.averagingDouble(g ->
                        GradeService.pointsForLetter(g.getGrade())
                )));

        // CGPA overall
        double cgpa = grades.stream()
                .mapToDouble(g -> GradeService.pointsForLetter(g.getGrade()))
                .average()
                .orElse(0.0);

        // Most recent semester GPA
        String latestSem = grades.stream().map(Grade::getSemester)
                .distinct().sorted(Comparator.naturalOrder())
                .reduce((a, b) -> b).orElse(null);
        double latestGpa = latestSem == null ? cgpa : gpaBySemester.getOrDefault(latestSem, cgpa);

        // Build PDF
        byte[] pdf = buildPdf(studentId, grades, gpaBySemester, cgpa);

        // Upload to S3
        String key = "transcripts/%d/%s.pdf".formatted(studentId, OffsetDateTime.now().toString().replace(":", "-"));
        try {
            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType("application/pdf")
                            .build(),
                    RequestBody.fromBytes(pdf)
            );
        } catch (S3Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 upload failed: " + e.awsErrorDetails().errorMessage(), e);
        }

        // Persist metadata
        var t = new Transcript()
                .setStudentId(studentId)
                .setS3Bucket(bucket)
                .setS3Key(key)
                .setGpa(BigDecimal.valueOf(latestGpa).setScale(3, RoundingMode.HALF_UP))
                .setCgpa(BigDecimal.valueOf(cgpa).setScale(3, RoundingMode.HALF_UP));
        t = transcriptRepository.save(t);

        // Presign URL
        var getReq = GetObjectRequest.builder().bucket(bucket).key(key).build();
        var presigned = presigner.presignGetObject(GetObjectPresignRequest.builder()
                .getObjectRequest(getReq)
                .signatureDuration(Duration.ofSeconds(presignSeconds))
                .build());

        return new TranscriptResponse()
                .setStudentId(studentId)
                .setGpa(t.getGpa())
                .setCgpa(t.getCgpa())
                .setBucket(bucket)
                .setKey(key)
                .setUrl(presigned.url().toString());
    }

    private byte[] buildPdf(Long studentId,
                            List<Grade> grades,
                            Map<String, Double> gpaBySemester,
                            double cgpa) {
        try {
            var baos = new ByteArrayOutputStream();
            var document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            var title = new Paragraph("Uno University - Official Transcript");
            title.getFont().setSize(16);
            document.add(title);
            document.add(new Paragraph("Student ID: " + studentId));
            document.add(new Paragraph("Generated: " + OffsetDateTime.now()));
            document.add(new Paragraph(" "));

            // Table header
            var table = new PdfPTable(new float[]{3, 2, 2, 2});
            table.setWidthPercentage(100);
            table.addCell("Course ID");
            table.addCell("Semester");
            table.addCell("Marks");
            table.addCell("Grade");

            // Rows
            grades.stream()
                    .sorted(Comparator.comparing(Grade::getSemester).thenComparing(Grade::getCourseId))
                    .forEach(g -> {
                        table.addCell(g.getCourseId());
                        table.addCell(g.getSemester());
                        table.addCell(g.getMarks().toPlainString());
                        table.addCell(g.getGrade());
                    });
            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("GPA by Semester:"));
            gpaBySemester.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> document.add(new Paragraph(" - %s: %.3f".formatted(e.getKey(), e.getValue()))));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("CGPA: " + String.format("%.3f", cgpa)));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build PDF", e);
        }
    }
}