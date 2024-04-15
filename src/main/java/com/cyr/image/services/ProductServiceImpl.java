package com.cyr.image.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cyr.image.model.Product;
import com.cyr.image.repository.ProductRepository;


@Service
public class ProductServiceImpl implements ProductService {
@Autowired	
private ProductRepository productrepository;

@Override
public List<Product> getAllProduct(String keyword){
	if(keyword !=null) {
		return productrepository.search(keyword);
	}
	else {
		return (List<Product>) productrepository.findAll();
	}
}
	@Override
	public Product saveProduct(Product product) {
		return this.productrepository.save(product);
	}
	@Override
	public Product getProductById(long id) {
		Optional<Product> optional = productrepository.findById(id);
		Product product=null;
		if(optional.isPresent()) {
			product = optional.get();
		}
		else {
			throw new RuntimeException("Employee not found for id::"+id);
		}
		return product;
	}
	public void deleteProductById(long id) {
		this.productrepository.deleteById(id);
	}
	@Override
	public Page < Product > findPaginated(int pageNo, int pageSize,String sortField,String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
	   

	    Pageable pageable = PageRequest.of(pageNo - 1, pageSize,sort);
	    return this.productrepository.findAll(pageable);
	}

}
