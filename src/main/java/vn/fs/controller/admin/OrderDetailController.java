package vn.fs.controller.admin;
import java.security.Principal;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.entities.Order;
import vn.fs.entities.OrderDetail;
import vn.fs.entities.Product;
import vn.fs.entities.User;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.OrderDetailService;
import vn.fs.service.SendMailService;
@Controller
@RequestMapping("/admin")
public class OrderDetailController {
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SendMailService sendMailService;

	@Autowired
	UserRepository userRepository;
	
	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}
	@GetMapping("/orderdetail/detail/{id}")
	public String editOrderDetail(@PathVariable("id") Long id,ModelMap model) {
		OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
		List<Product> cboPro = productRepository.findAll();
		model.addAttribute("orderDetails",orderDetail);
		model.addAttribute("cboPro",cboPro);
		return "admin/editOrderDetail";
	}
	@PostMapping("/orderdetail/addOrderDetail")
	public String addOrDetail(@Validated @ModelAttribute("orderdetail") OrderDetail orderDetail,ModelMap model,RedirectAttributes attributes) {
		try {
			orderDetailRepository.save(orderDetail);
			attributes.addFlashAttribute("successadd", "Thành công");
			System.out.println("acdckajs" + orderDetail.getOrderDetailId());
		} catch (Exception e) {
			attributes.addFlashAttribute("erroradd", "Thất bại");
			return "/admin/orders";

		}
		return "redirect:/admin/orders";
	}
	@PostMapping("/orderdetail/updatePriceForOrder")
	public String updatePriceForOrder(@ModelAttribute("orders") Order orders,ModelMap model,RedirectAttributes attributes) {
		try {
	        // Kiểm tra orderId trước khi cập nhật
	        Long orderId = orders.getOrderId();
	        Order existingOrder = orderRepository.findById(orderId).orElse(null);
	        existingOrder.setAmount(orders.getAmount());
	        orderRepository.save(existingOrder);

	        attributes.addFlashAttribute("successadd", "Thành công");
	        System.out.println("OrderId: " + orderId);
	    } catch (Exception e) {
	        attributes.addFlashAttribute("erroradd", "Thất bại");
	        return "/admin/orders";
	    }
	    return "redirect:/admin/orders";
	}
	@GetMapping("/orderdetail/delete/{id}")
	public String deleteDetail(@PathVariable("id") Long id,RedirectAttributes attributes) {
		try {
			orderDetailRepository.deleteById(id);
			attributes.addFlashAttribute("successmessage", "Đã xóa thành công");

		} catch (Exception e) {
			attributes.addFlashAttribute("errormessage", "Không thể xóa");

		}
	    return "redirect:/admin/orders";
	}
}
