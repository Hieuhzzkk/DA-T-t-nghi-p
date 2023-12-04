package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.entities.Invoice;
import vn.fs.entities.InvoiceDetail;
import vn.fs.entities.OrderDetail;
import vn.fs.entities.Product;
import vn.fs.entities.User;
import vn.fs.repository.InvoiceDetailRepository;
import vn.fs.repository.InvoiceRepository;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.OrderDetailService;
import vn.fs.service.SendMailService;


@Controller
@RequestMapping("/admin")
public class InvoiceController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	InvoiceDetailRepository invoiceDetailRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}
	@ModelAttribute(value = "invoices")
	public List<Invoice> invoices(ModelMap model) {
		List<Invoice> invoice = invoiceRepository.findAll();
		model.addAttribute("invoices",invoice);
        model.addAttribute("optionPro", new Product());

		return invoice;
	}
	
	@GetMapping(value = "/invoices")
	public String orders(ModelMap model) {
		Invoice invoices = new Invoice();
		model.addAttribute("invoice",invoices);
        model.addAttribute("matchedProducts", productRepository.findAll());
        model.addAttribute("invoiceDetail",invoiceDetailRepository.findAll());
		return "admin/invoice";
	}
	
	@PostMapping(value = "/invoices/detail/addInvoice")
	public String addInvoice(@Validated @ModelAttribute("invoice") Invoice invoices,RedirectAttributes attributes,ModelMap model) {
		try {
			invoiceRepository.save(invoices);
			attributes.addFlashAttribute("successadd", "Thành công");

		} catch (Exception e) {
			attributes.addFlashAttribute("erroradd", "Thất bại");
			return "admin/invoice";
		}
		return "redirect:/admin/invoices";
	}
	@GetMapping("/invoices/search")
	public String searchProduct(@RequestParam("searchTerm") String searchTerm, ModelMap model, RedirectAttributes attributes) {
	    try {
	    	 Invoice invoices = new Invoice();
	  		model.addAttribute("invoice",invoices);
	  		if(searchTerm.trim().equalsIgnoreCase("")) {
	  			attributes.addFlashAttribute("successadd", "Danh sách sản phẩm");
	    	    return "redirect:/admin/invoices";
	  		}
	        List<Product> matchedProducts = productRepository.findbyProWithIdOrName(searchTerm);
	        if (!matchedProducts.isEmpty()) {
	            model.addAttribute("matchedProducts", matchedProducts);
	            model.addAttribute("successadd", "Tìm kiếm thành công");
	        } 
	        else {
	            model.addAttribute("erroradd", "Không tìm thấy sản phẩm");
	    	    return "forward:/admin/invoices";
	        }	       
	    } catch (Exception e) {
  			attributes.addFlashAttribute("erroradd", "Có lỗi sảy ra");
    	    return "redirect:/admin/invoices";
	    }
	    return "admin/invoice";
	}
	@GetMapping("/invoices/lsInvoice")
	public String lsInvoice(ModelMap model) {
		
		List<Invoice> lsinvoice = invoiceRepository.findAll();
		model.addAttribute("lsinvoice",lsinvoice);
	    return "admin/listInvoice";
	}
	@RequestMapping("/invoices/addDetail/{id}")
	public ModelAndView detailProductinInvoice(@PathVariable("id") Long id,ModelMap model, RedirectAttributes redirectAttributes) {
		Invoice invoices = new Invoice();
  		model.addAttribute("invoice",invoices);
		Product dePro = productRepository.findById(id).get();
		InvoiceDetail voiceDetail = new InvoiceDetail();
		voiceDetail.setInvoiceDetailId(id);
//        redirectAttributes.addFlashAttribute("optionPro", dePro);
		return new ModelAndView("redirect:/admin/invoices");
	}
	
	@RequestMapping("/invoices/payForinvoice/{invoice_id}")
	public ModelAndView payForinvoice(ModelMap model,@PathVariable("invoice_id") Long id) {
//		Optional<Invoice> iv = invoiceRepository.findById(id);
//		if(iv.isEmpty()) {
//			return new ModelAndView("forward:/admin/invoices", model);
//		}
//		Invoice ivReal = iv.get();
//		ivReal.setStatus((short) 1);
//		invoiceRepository.save(ivReal);
//		Product p = null;
//		List<Invoice> listVe = invoiceRepository.findByInvoices(id);
//		for (Invoice lsIv : listVe) {
//			p = lsIv.getProduct();
//			p.setQuantity(p.getQuantity() - lsIv.getQuantity());
//			productRepository.save(p);
//		}
		return new ModelAndView("forward:/admin/invoices/lsInvoice", model);

	}
	

}
