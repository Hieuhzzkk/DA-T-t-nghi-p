package vn.fs.controller.admin;
import javax.servlet.http.HttpServletResponse;

import javax.swing.text.StyleConstants.FontConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import vn.fs.entities.Category;
import vn.fs.entities.Invoice;
import vn.fs.entities.InvoiceDetail;
import vn.fs.entities.Product;
import vn.fs.repository.ProductRepository;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class UserPDFExporter {
	@Autowired
	ProductRepository productRepository;
	 private List<InvoiceDetail> listCate;
	
	    public UserPDFExporter(List<InvoiceDetail> listCate) {
	        this.listCate = listCate;
	    }
	 
	    private void writeTableHeader(PdfPTable table) {
	        PdfPCell cell = new PdfPCell();
	        cell.setBackgroundColor(BaseColor.GRAY);
	        cell.setPadding(5);
	         
	        Font font = FontFactory.getFont(FontFactory.HELVETICA);
	        font.setColor(BaseColor.WHITE);
	         
	        
  
	        cell.setPhrase(new Phrase("Tên khách hàng", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Số điện thoại", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Tên sản phẩm", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Số lượng", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Đơn giá", font));
	        table.addCell(cell);
	           
	    }
	     
	    private void writeTableData(PdfPTable table) {
	        for (InvoiceDetail user : listCate) {
	        	
	            
	            table.addCell(user.getInvoice().getUsername());
	            table.addCell(user.getInvoice().getPhonenumber());
	            table.addCell(user.getProducts().getProductName());
	            table.addCell(String.valueOf(user.getQuantity()));
	            table.addCell(String.valueOf(user.getPrice() - (user.getPrice() * user.getProducts().getDiscount()/100)));
	        }
	    }
	     
	    public void export(HttpServletResponse response) throws DocumentException, IOException {
	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, response.getOutputStream());
	         
	        document.open();
	        BaseFont bf = BaseFont.createFont("C:/Java5/GITTTTT/DA-T-t-nghi-p/src/main/resources/static/fonts/font-vntime/Font/VNTIME.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        Font font = new Font(bf, 18, Font.NORMAL, BaseColor.GRAY);

	         
	        Paragraph p = new Paragraph("Đơn hàng", font);
	        p.setAlignment(Paragraph.ALIGN_CENTER);
	         
	        document.add(p);
	         
	        PdfPTable table = new PdfPTable(5);
	        table.setWidthPercentage(100f);
	        table.setWidths(new float[]{1.5f, 1.5f, 3.5f, 1.5f, 2.5f});
	        table.setSpacingBefore(10);
	         
	        writeTableHeader(table);
	        writeTableData(table);
	         
	        document.add(table);
	         
	        document.close();
	         
	    }
}

