package com.cyr.image.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.cyr.image.FileUploadUtil;
import com.cyr.image.model.Product;
import com.cyr.image.services.ProductService;

@Controller
public class ProductController {
	@Autowired
	private ProductService productservice;
	
	@GetMapping("/") 
	  public String Project1(Model model,@Param("keyword") String keyword) {
		  List<Product> listproduct = productservice.getAllProduct(keyword);
		
	  model.addAttribute("listproduct",listproduct);
	  model.addAttribute("keyword",keyword); 
	  
	  return findPaginated(1, "productName", "asc", model);
	  }

		/*
		 * @GetMapping("/") public String viewHomePage(Model model) { return
		 * findPaginated(1, "id", "asc", model); }
		 */
	 
    @GetMapping("/showNewProductForm")
	public String showNewProductForm(Model model) {
		// create model attribute to bind form data
		Product product = new Product();
		model.addAttribute("product", product);
		return "add_product";
	}
    @PostMapping("/saveProduct")
    public RedirectView saveProduct(@ModelAttribute("product")Product product,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
         
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setPhotos(fileName);
         
        Product saveProduct = productservice.saveProduct(product);
 
        String uploadDir = "product-photos/" + saveProduct.getId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
         
        return new RedirectView("/", true);
    }
    @GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
		Product product = productservice.getProductById(id);
		model.addAttribute("product", product);
		return "Edit_product";
	}
    @GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable(value = "id") long id) {
		this.productservice.deleteProductById(id);
		return "redirect:/";
	}
	
    @GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 5;

		Page<Product> page = productservice.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Product> listProducts = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("listproduct", listProducts);
		return "index";
	}
}
