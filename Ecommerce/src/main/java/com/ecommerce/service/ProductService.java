package com.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ecommerce.Exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.model.Seller;
import com.ecommerce.response.CreateProductRequest;

@Service
public interface ProductService {
	public Product createProduct(CreateProductRequest req,Seller seller);
	public void deleteProduct(Long productId);
	public Product updateProduct(Long productId,Product product) throws ProductException;
	Product findProductById(Long productId) throws ProductException;
	List<Product> searchProducts(String query);
	public Page<Product> getAllProducts(String category,String brand,String colors, String sizes,Integer minPrice,Integer minDiscount,Integer maxPrice,String sort,String stock,Integer pageNumber);  //sort by brand colors category
//	List<Product> getProductBySellerId(Long sellerId);
	Page<Product> getProductBySellerId(Long sellerId, int page, int size);
}
