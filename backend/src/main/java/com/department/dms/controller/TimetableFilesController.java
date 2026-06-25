package com.department.dms.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/timetable-files")
@CrossOrigin
public class TimetableFilesController {
  private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "docx", "xlsx");

  private final GridFsTemplate gridFsTemplate;
  private final GridFsOperations gridFsOperations;

  public TimetableFilesController(GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {
    this.gridFsTemplate = gridFsTemplate;
    this.gridFsOperations = gridFsOperations;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "file is required"));
    }

    String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("timetable");
    if (!isAllowedFilename(originalName)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("error", "Only PDF, DOCX, and XLSX files are allowed"));
    }

    Document metadata = new Document();
    metadata.put("contentType", Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"));
    metadata.put("uploadedAt", Instant.now().toString());

    ObjectId id = gridFsTemplate.store(
        file.getInputStream(),
        originalName,
        file.getContentType(),
        metadata
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id.toString()));
  }

  @GetMapping
  public Map<String, Object> list() {
    List<Map<String, Object>> items = new ArrayList<>();
    gridFsTemplate.find(new Query().with(Sort.by(Sort.Direction.DESC, "uploadDate")))
        .forEach(file -> items.add(toListItem(file)));
    return Map.of("items", items);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> download(@PathVariable String id) {
    ObjectId objectId;
    try {
      objectId = new ObjectId(id);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(objectId)));
    if (file == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    GridFsResource resource = gridFsOperations.getResource(file);
    String contentType = resolveContentType(file);
    String filename = Optional.ofNullable(file.getFilename()).orElse("timetable");

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .contentLength(file.getLength())
        .body(resource);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> delete(@PathVariable String id) {
    ObjectId objectId;
    try {
      objectId = new ObjectId(id);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "not found"));
    }
    gridFsTemplate.delete(Query.query(Criteria.where("_id").is(objectId)));
    return ResponseEntity.ok(Map.of("ok", true));
  }

  private Map<String, Object> toListItem(GridFSFile file) {
    Document metadata = file.getMetadata();
    String contentType = metadata != null ? metadata.getString("contentType") : null;
    String uploadedAt = metadata != null ? metadata.getString("uploadedAt") : null;
    return Map.of(
        "id", file.getObjectId().toString(),
        "filename", file.getFilename(),
        "contentType", contentType,
        "size", file.getLength(),
        "uploadDate", file.getUploadDate(),
        "uploadedAt", uploadedAt
    );
  }

  private String resolveContentType(GridFSFile file) {
    Document metadata = file.getMetadata();
    if (metadata != null && metadata.getString("contentType") != null) {
      return metadata.getString("contentType");
    }
    return "application/octet-stream";
  }

  private boolean isAllowedFilename(String name) {
    if (name == null) return false;
    int dot = name.lastIndexOf('.');
    if (dot < 0 || dot == name.length() - 1) return false;
    String ext = name.substring(dot + 1).toLowerCase();
    return ALLOWED_EXTENSIONS.contains(ext);
  }
}
