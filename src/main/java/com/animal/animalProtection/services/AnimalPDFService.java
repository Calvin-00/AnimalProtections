package com.animal.animalProtection.services;


import com.animal.animalProtection.model.Animal;
import com.animal.animalProtection.model.User;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class AnimalPDFService {
    public static ByteArrayInputStream animalPDFReport(List<Animal> animals) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Content to PDF file ->
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Paragraph para = new Paragraph("Animal List", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            // Add PDF Table Header ->
            Stream.of("ID", "Name", "Breed","Age", "Gender", "Medical Status").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
                header.setBackgroundColor(Color.GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (Animal animal : animals) {
                PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(animal.getId())));
                idCell.setPaddingLeft(4);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell NameCell = new PdfPCell(new Phrase(animal.getName()));
                NameCell.setPaddingLeft(4);
                NameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                NameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(NameCell);

                PdfPCell breedCell = new PdfPCell(new Phrase(String.valueOf(animal.getBreed())));
                breedCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                breedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                breedCell.setPaddingRight(4);
                table.addCell(breedCell);

                PdfPCell ageCell = new PdfPCell(new Phrase(String.valueOf(animal.getAge())));
                ageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                ageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                ageCell.setPaddingRight(4);
                table.addCell(ageCell);

                PdfPCell genderCell = new PdfPCell(new Phrase(String.valueOf(animal.getSex())));
                genderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                genderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                genderCell.setPaddingRight(4);
                table.addCell(genderCell);
                PdfPCell issueCell = new PdfPCell(new Phrase(String.valueOf(animal.getIssue())));
                issueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                issueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                issueCell.setPaddingRight(4);
                table.addCell(issueCell);
            }
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
