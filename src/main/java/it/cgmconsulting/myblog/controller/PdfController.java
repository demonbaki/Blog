package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.cgmconsulting.myblog.payload.response.PostPdfResponse;
import it.cgmconsulting.myblog.service.PdfService;
import it.cgmconsulting.myblog.service.PostService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("pdf") // http:localhost:8080/pdf/...
@SecurityRequirement(name = "myBlogSecurityScheme")
public class PdfController {

    private final PostService postService;
    private final PdfService pdfService;
    public PdfController(PostService postService, PdfService pdfService) {
        this.postService = postService;
        this.pdfService = pdfService;
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<?> createPdf(@PathVariable long id){

        PostPdfResponse p = postService.getByIdToPdf(id);
        if(p == null)
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        InputStream pdfFile = null;
        ResponseEntity<InputStreamResource> responseEntity = null;

        try {
            pdfFile = pdfService.createPdf(p);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Method", "GET");
            headers.add("Access-Control-Allow-Header", "Content-Type");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-disposition", "inline; filename=" + p.getId() + ".pdf");

            responseEntity = new ResponseEntity<InputStreamResource>(
                    new InputStreamResource(pdfFile),
                    headers,
                    HttpStatus.OK
            );
        } catch(Exception e) {
            responseEntity = new ResponseEntity<InputStreamResource>(
                    new InputStreamResource(null, "ERROR CREATING PDF"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return responseEntity;
    }
}
