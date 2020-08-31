package com.alexcorp.example.itextsandbox;

import com.alexcorp.example.itextsandbox.domain.QuadroBlock;
import com.alexcorp.example.itextsandbox.domain.QuadroCell;
import com.alexcorp.example.itextsandbox.domain.QuadroRow;
import com.alexcorp.example.itextsandbox.domain.QuadroSection;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

public class ItextSandboxApplication {

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        QuadroSection quadroSection = new QuadroSection();
        quadroSection.setDescription(Arrays.asList("A", "B"));

        QuadroBlock quadroBlock = new QuadroBlock();
        quadroBlock.setDescription("C1");
        QuadroRow rowA = new QuadroRow();
        QuadroRow rowB = new QuadroRow();
        List<QuadroCell> cells = Arrays.asList(
                new QuadroCell(1, "desc1", "cont1"),
                new QuadroCell(2, "desc2", "cont2"),
                new QuadroCell(3, "desc3", "cont3"),
                new QuadroCell(4, "desc4", "cont4"),
                new QuadroCell(5, "desc5", "cont5"),
                new QuadroCell(6, "desc6", "cont6"));
        rowA.setRelativeWidths(new float[]{1f, 1f, 1f, 1f, 1f, 1f});
        rowB.setRelativeWidths(new float[]{1f, 1f, 1f, 1f, 1f, 1f});
        rowA.setCells(cells);
        rowB.setCells(cells);

        quadroBlock.setRows(Arrays.asList(rowA, rowB));

        quadroSection.setQuadroBlocks(Arrays.asList(quadroBlock));


        String dest = "C:/itextExamples/addingTable.pdf";
        File file = new File(dest);

        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();
        float[] r = {1f, 10f};
        PdfPTable table = new PdfPTable(r);
        table.setWidthPercentage(100f);


        PdfPCell a = new PdfPCell();
//        a.addElement(new Phrase("A"));
//        a.setBorder(PdfPCell.NO_BORDER);
        a.addElement(createSideBlock(quadroSection));
        table.addCell(a);

        PdfPCell b = new PdfPCell();
        b.setBorder(PdfPCell.NO_BORDER);
        b.addElement(createBlock(quadroBlock));
        table.addCell(b);

        document.add(table);
        document.close();
    }

    public static PdfPTable createSideBlock(QuadroSection section) {
        PdfPTable pdfPTable = new PdfPTable(new float[] {1f});
        section.getDescription().forEach(s -> {
            PdfPCell pdfPCell = noBorderedColoredCell(BaseColor.WHITE);
            pdfPCell.addElement(new Phrase(s));
            pdfPTable.addCell(pdfPCell);
        });
        return pdfPTable;
    }


    public static PdfPTable createBlock(QuadroBlock block) {
        PdfPTable blockTable = new PdfPTable(new float[]{1f, 5f});

        PdfPCell pdfPCell = new PdfPCell(new Phrase(block.getDescription()));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setBackgroundColor(BaseColor.GRAY);
        pdfPCell.setBorder(PdfPCell.NO_BORDER);

        blockTable.addCell(pdfPCell);

        PdfPTable dataTable = new PdfPTable(new float[]{1f});
        block.getRows().forEach(row -> {
            PdfPCell rowCell = noBorderedColoredCell(BaseColor.GRAY);
            rowCell.setBorder(PdfPCell.NO_BORDER);
            rowCell.addElement(createRow(row));
            dataTable.addCell(rowCell);
        });

        blockTable.addCell(dataTable);

        return blockTable;
    }

    public static PdfPTable createRow(QuadroRow row) {
        PdfPTable rowTable = new PdfPTable(row.getRelativeWidths());
        row.getCells().forEach(cell -> rowTable.addCell(createCell(cell)));
        return rowTable;
    }

    public static PdfPCell createCell(QuadroCell cell) {
        PdfPTable descriptionTable = new PdfPTable(new float[]{1f});
        if (cell.getDescription() != null) {
            PdfPCell descriptionCell = noBorderedColoredCell(BaseColor.GRAY);
            descriptionCell.addElement(new Phrase(cell.getDescription()));
            descriptionTable.addCell(descriptionCell);
        }

        PdfPTable dataTable = new PdfPTable(new float[]{1f, 5f});

        PdfPCell numberCell = noBorderedColoredCell(BaseColor.GRAY);
        numberCell.addElement(new Phrase(String.valueOf(cell.getNumber())));
        dataTable.addCell(numberCell);

        PdfPCell contentCell = noBorderedColoredCell(BaseColor.WHITE);
        contentCell.addElement(new Phrase(cell.getContent()));
        dataTable.addCell(contentCell);

        PdfPCell pdfPCell = noBorderedColoredCell(BaseColor.GRAY);
        pdfPCell.addElement(descriptionTable);
        pdfPCell.addElement(dataTable);

        return pdfPCell;
    }

    private static PdfPCell noBorderedColoredCell(BaseColor color) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorder(PdfPCell.NO_BORDER);
        pdfPCell.setBackgroundColor(color);
        pdfPCell.setPaddingTop(0f);
        pdfPCell.setPaddingRight(0f);
        pdfPCell.setPaddingBottom(0f);
        pdfPCell.setPaddingLeft(0f);
        return pdfPCell;
    }
}
