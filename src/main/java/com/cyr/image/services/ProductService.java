package com.cyr.image.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cyr.image.model.Product;

public interface ProductService {
Product saveProduct(Product product);
Product getProductById(long id);
void deleteProductById(long id);
List<Product> getAllProduct(String keyword);
Page<Product> findPaginated(int pageNo,int pageSize,String sortField,String sortDir);

}
