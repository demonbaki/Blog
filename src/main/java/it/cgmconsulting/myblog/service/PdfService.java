package it.cgmconsulting.myblog.service;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import it.cgmconsulting.myblog.payload.response.PostPdfResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class PdfService {

    @Value("${app.post.image.path}")
    private String pathImage;

    public InputStream createPdf(PostPdfResponse p) throws IOException {

        String title = p.getTitle();
        String content = p.getContent();
        String postImage = p.getImage();
        String author = p.getUsername();
        String updatedAt = p.getUpdatedAt() == null ? LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :  p.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document document = new Document(pdf, PageSize.A4);

        addMetaData(pdf, title, author);

        // to do
        // riportare nel pdf : title, image, content, author(username), updatedAt
        // cercare di aggiungere anche metadati al pdf

        // TITLE
        Paragraph pTitle = new Paragraph(title).setFontSize(20).setBold().setFontColor((new DeviceRgb(100,149,237)), 100);
        document.add(pTitle);

        addEmptyLines(document, 1);

        // IMAGE
        if(postImage != null) {
            ImageData imageData = ImageDataFactory.create(pathImage + p.getImage());
            document.add(new Image(imageData));
            addEmptyLines(document, 1);
        }

        // CONTENT
        Paragraph pContent = new Paragraph(content).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(pContent);

        addEmptyLines(document, 1);

        // UPDATED AT
        Paragraph pUpdtatedAt = new Paragraph("Data: "+updatedAt).setItalic().setTextAlignment(TextAlignment.RIGHT);
        document.add(pUpdtatedAt);

        // AUTHOR
        Paragraph pAuthor = new Paragraph("Autore: "+author).setItalic().setTextAlignment(TextAlignment.RIGHT);
        document.add(pAuthor);

        // NUMERI DI PAGINA (bottom/right)
        int numberOfPages = pdf.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            document.showTextAligned(new Paragraph(String.format("page %s of %s", i, numberOfPages)).setFontSize(8).setItalic(),
                    560, 20, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
        }

        // CHIUSURA DOCUMENTO
        document.close();

        InputStream in = new ByteArrayInputStream(out.toByteArray());

        return in;
    }

    private static void addEmptyLines(Document document, int number) {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph("\n"));
        }
    }

    private void addMetaData(PdfDocument pdf, String title, String author) {
        PdfDocumentInfo info = pdf.getDocumentInfo();
        info.setTitle(title);
        info.setAuthor(author);

    }

}