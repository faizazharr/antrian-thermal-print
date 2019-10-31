/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrintAntrian;

/**
 *
 * @author Faiz
 */

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTextArea;

public class Print {

    public Print(int angka, String jenis) throws PrinterException{
        PageFormat format = new PageFormat();
        Paper paper = new Paper();

        double paperWidth = 3;//3.25
        double paperHeight = 3.69;//11.69
        double leftMargin = 0.12;
        double rightMargin = 0.10;
        double topMargin = 0;
        double bottomMargin = 0.01;

        paper.setSize(paperWidth * 200, paperHeight * 200);
        paper.setImageableArea(leftMargin * 200, topMargin * 200,
                (paperWidth - leftMargin - rightMargin) * 200,
                (paperHeight - topMargin - bottomMargin) * 200);

        format.setPaper(paper);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(OrientationRequested.PORTRAIT);
//testing 
        PrintService service = findPrintService("RP58 Printer");
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        Printable printable = new ReceiptPrint(angka,jenis);
        
        format = printerJob.validatePage(format);
        printerJob.setPrintService(service);
        printerJob.setPrintable(printable, format);
        try {
            printerJob.print(aset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PrintService findPrintService(String printerName)
    {
        for (PrintService service : PrinterJob.lookupPrintServices())
        {
            if (service.getName().equalsIgnoreCase(printerName))
                return service;
        }

        return null;
    }

}

class ReceiptPrint implements Printable {
    int angka ;
    String jenis;

    public ReceiptPrint(int angka, String jenis) {
        this.angka = angka;
        this.jenis = jenis;
    }

    
    SimpleDateFormat df = new SimpleDateFormat();

    
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {

        df.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strText = null;
        String strText2 = null;
        String strText3 = null;

        final String LF = "\n";// text string to output
        int lineStart;           // start index of line in textarea
        int lineEnd;             // end index of line in textarea
        int lineNumber;
        int lineCount;
        int lineCount2;
        int lineCount3;

        Graphics2D g2d = (Graphics2D) graphics;

        Font font = new Font("MONOSPACED", Font.BOLD, 12);
       
        if (pageIndex < 0 || pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        JTextArea textarea = new JTextArea(10, 40);
        JTextArea textarea2 = new JTextArea(10, 40);
        
        textarea.append("   Selamat Datang  \n");
        textarea.append("     Puskesmas \n");
        textarea.append("  Babakan Tarogong \n");
        textarea.append("    nomor antrian \n");
        textarea.append("\n");
        if (angka < 9) {
            textarea.append("   " + angka + "\n\n");    
        } else {
            textarea.append("  " + angka + "\n\n");
        }
        textarea.append("         " + jenis + "\n");        
        textarea.append(df.format(new Date()));
        
        textarea.setEditable(false);
        
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setFont(font);

        lineNumber = 0;
        lineCount = textarea.getLineCount();

        while (lineCount != 0) {
            try {
                lineStart = textarea.getLineStartOffset(lineNumber);
                lineEnd = textarea.getLineEndOffset(lineNumber);
                strText = textarea.getText(lineStart, lineEnd - lineStart);
            } catch (Exception exception) {
                System.out.println("Printing error:" + exception);  // have to catch BadLocationException
            }
            if (lineNumber == 5) {
                g2d.setFont(new Font("MONOSPACED", Font.BOLD, 35));
            }
            if (lineNumber == 6) {
                g2d.setFont(font);
            }

            g2d.drawString(strText, 1, (lineNumber + 1) * 18);
            //spacing    between lines
            lineNumber = lineNumber + 1;
            lineCount--;
        }
        
        return Printable.PAGE_EXISTS;
    }
    
}