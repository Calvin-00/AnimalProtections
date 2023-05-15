package com.animal.animalProtection.services;


import com.animal.animalProtection.model.User;
import com.animal.animalProtection.model.Volunteer;
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

public class PeoplePDFService {
    public static ByteArrayInputStream peoplePDFReport(List<User> users) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Content to PDF file ->
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Paragraph para = new Paragraph("Users List", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            // Add PDF Table Header ->
            Stream.of("ID", "First Name", "Last Name","Email", "Mobile", "Role").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
                header.setBackgroundColor(Color.GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (User user : users) {
                PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(user.getId())));
                idCell.setPaddingLeft(4);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell firstNameCell = new PdfPCell(new Phrase(user.getFirstName()));
                firstNameCell.setPaddingLeft(4);
                firstNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                firstNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(firstNameCell);

                PdfPCell lastNameCell = new PdfPCell(new Phrase(String.valueOf(user.getLastName())));
                lastNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                lastNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                lastNameCell.setPaddingRight(4);
                table.addCell(lastNameCell);

                PdfPCell emailCell = new PdfPCell(new Phrase(String.valueOf(user.getEmail())));
                emailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emailCell.setPaddingRight(4);
                table.addCell(emailCell);

                PdfPCell phoneNumCell = new PdfPCell(new Phrase(String.valueOf(user.getMobile())));
                phoneNumCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                phoneNumCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                phoneNumCell.setPaddingRight(4);
                table.addCell(phoneNumCell);
                PdfPCell roleCell = new PdfPCell(new Phrase(String.valueOf(user.getRole())));
                roleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                roleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                roleCell.setPaddingRight(4);
                table.addCell(roleCell);
            }
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
