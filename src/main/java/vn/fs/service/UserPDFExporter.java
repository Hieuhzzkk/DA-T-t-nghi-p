package vn.fs.service;
import javax.servlet.http.HttpServletResponse;

import javax.swing.text.StyleConstants.FontConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UserPDFExporter {
	@Autowired
	ProductRepository productRepository;
	 private List<InvoiceDetail> listCate;
	
	    public UserPDFExporter(List<InvoiceDetail> listCate) {
	        this.listCate = listCate;
	    }
	 
	    private void writeTableHeader(PdfPTable table) throws DocumentException, IOException {
	    	 ClassPathResource resource = new ClassPathResource("static/fonts/arial/arial.ttf");
		    	Font font = FontFactory.getFont(resource.getURI().toURL().toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	        PdfPCell cell = new PdfPCell();
	        cell.setBackgroundColor(BaseColor.GRAY);
	        cell.setPadding(6);
	        cell.setPhrase(new Phrase("Tên khách hàng", font));
	        table.addCell(cell);

	        cell = new PdfPCell();
	        cell.setBackgroundColor(BaseColor.GRAY);
	        cell.setPadding(5);
	        cell.setPhrase(new Phrase("Số điện thoại", font));
	        table.addCell(cell);

	        cell = new PdfPCell();
	        cell.setBackgroundColor(BaseColor.GRAY);
	        cell.setPadding(5);
	        cell.setPhrase(new Phrase("Tên sản phẩm", font));
	        table.addCell(cell);

	        cell = new PdfPCell();
	        cell.setBackgroundColor(BaseColor.GRAY);
	        cell.setPadding(5);
	        cell.setPhrase(new Phrase("Số lượng", font));
	        table.addCell(cell);

	        cell = new PdfPCell();
	        cell.setBackgroundColor(BaseColor.GRAY);
	        cell.setPadding(5);
	        cell.setPhrase(new Phrase("Đơn giá", font));
	        table.addCell(cell);
	    }



	     
	    private void writeTableData(PdfPTable table) throws DocumentException, IOException {
	    	  ClassPathResource resource = new ClassPathResource("static/fonts/arial/arial.ttf");
	    	Font font = FontFactory.getFont(resource.getURI().toURL().toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        for (InvoiceDetail user : listCate) {
	            table.addCell(new PdfPCell(new Phrase(user.getInvoice().getUsername(), font)));
	            table.addCell(new PdfPCell(new Phrase(user.getInvoice().getPhonenumber(), font)));
	            table.addCell(new PdfPCell(new Phrase(user.getProducts().getProductName(), font)));
	            table.addCell(new PdfPCell(new Phrase(String.valueOf(user.getQuantity()), font)));
	            table.addCell(new PdfPCell(new Phrase(String.valueOf(user.getPrice() - (user.getPrice() * user.getProducts().getDiscount() / 100)), font)));
	        }
	    }

	     
	    public void export(HttpServletResponse response) throws DocumentException, IOException {
	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, response.getOutputStream());
	         
	        document.open();
	        ClassPathResource resource = new ClassPathResource("static/fonts/arial/arial.ttf");

	        // Tạo BaseFont từ dữ liệu font
	        BaseFont bf = BaseFont.createFont(resource.getURI().toURL().toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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
