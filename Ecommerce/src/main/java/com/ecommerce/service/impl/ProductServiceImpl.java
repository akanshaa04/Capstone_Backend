package com.ecommerce.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecommerce.Exception.ProductException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.Seller;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.response.CreateProductRequest;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	@Autowired
	private final ProductRepository productRepository;

	@Autowired
	private final CategoryRepository categoryRepository;
	@Autowired
	private CategoryService categoryService;

	@Override
	public Product createProduct(CreateProductRequest req, Seller seller) {
		Category category1 = categoryRepository.findByCategoryId(req.getCategory());
		if (category1 == null) {
			Category category = new Category();
			category.setCategoryId(req.getCategory());
			category.setLevel(1);
			category1 = categoryRepository.save(category);
		}

		Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
		if (category2 == null) {
			Category category = new Category();
			category.setCategoryId(req.getCategory2());
			category.setLevel(2);
			category.setParentCategory(category1);
			category2 = categoryRepository.save(category);
		}

		Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
		if (category3 == null) {
			Category category = new Category();
			category.setCategoryId(req.getCategory3());
			category.setLevel(3);
			category.setParentCategory(category2);
			category3 = categoryRepository.save(category);
		}

		int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());
		Product product = new Product();
		product.setSeller(seller);
		product.setCategory(category3);
		product.setDescription(req.getDescription());
		product.setCreatedAt(LocalDateTime.now());
		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setSellingPrice(req.getSellingPrice());
		product.setImages(req.getImages());
		product.setMrpPrice(req.getMrpPrice());
//		product.setSizes(req.getSizes());	
		product.setSizes("S,M,L,XL");
		product.setQuantity(req.getQuantity());
		product.setDiscountPercent(discountPercentage);
		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(Long productId) {
		Product product = productRepository.findProductById(productId);
		productRepository.delete(product);
	}

	@Override
	public Product updateProduct(Long productId, Product product) throws ProductException {
		// Find the existing product by ID
		Product existingProduct = findProductById(productId);

		// Update only the fields that are provided in the request body

		if (product.getTitle() != null) {
			existingProduct.setTitle(product.getTitle());
		}

		if (product.getDescription() != null) {
			existingProduct.setDescription(product.getDescription());
		}

		if (product.getMrpPrice() != 0) { // Assuming 0 means no update for price
			existingProduct.setMrpPrice(product.getMrpPrice());
		}

		if (product.getSellingPrice() != 0) { // Assuming 0 means no update for price
			existingProduct.setSellingPrice(product.getSellingPrice());
		}

		if (product.getDiscountPercent() != 0) { // Assuming 0 means no update for discount
			existingProduct.setDiscountPercent(product.getDiscountPercent());
		}

		if (product.getQuantity() != 0) { // Assuming 0 means no update for quantity
			existingProduct.setQuantity(product.getQuantity());
		}

		if (product.getColor() != null) {
			existingProduct.setColor(product.getColor());
		}

		if (product.getImages() != null && !product.getImages().isEmpty()) {
			existingProduct.setImages(product.getImages());
		}

		if (product.getNumRatings() != 0) { // Assuming 0 means no update for ratings
			existingProduct.setNumRatings(product.getNumRatings());
		}

		if (product.getCategory() != null) {
			existingProduct.setCategory(product.getCategory());
		}

		if (product.getSeller() != null) {
			existingProduct.setSeller(product.getSeller());
		}

		if (product.getCreatedAt() != null) {
			existingProduct.setCreatedAt(product.getCreatedAt());
		}

		if (product.getSizes() != null) {
			existingProduct.setSizes(product.getSizes());
		}

		// Save the updated product
		return productRepository.save(existingProduct);
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("product not found with id " + productId));
	}

	@Override
	public List<Product> searchProducts(String query) {

		return productRepository.searchProducts(query);
	}

	@Override
	public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice,
			Integer minDiscount, Integer maxPrice, String sort, String stock, Integer pageNumber) {
		// using specification for data JPA filtering
		Specification<Product> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (category != null && !category.isEmpty()) {
				// Fetch all descendant category IDs from the service
				List<String> descendantCategoryIds = categoryService.findAllDescendantCategoryIds(category);

				// Join Product with Category
				Join<Product, Category> categoryJoin = root.join("category");

				// Apply predicate for direct category filtering
				Predicate categoryPredicate = categoryJoin.get("categoryId").in(descendantCategoryIds);
				predicates.add(criteriaBuilder.and(categoryPredicate));
			}

			// Apply brand filter if provided
			if (brand != null && !brand.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
			}

			// Apply colors filter if provided
			if (colors != null && !colors.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("color"), colors));
			}

			// Apply sizes filter if provided
			if (sizes != null && !sizes.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
			}

			// Apply price range filter if provided
			if (minPrice != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
			}
			if (maxPrice != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
			}

			// Apply discount filter if provided
			if (minDiscount != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
			}

			// Apply stock filter if provided
			if (stock != null && stock.equalsIgnoreCase("inStock")) {
				predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
			}

			// Combine all the predicates
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};

		Pageable pageable;
		if (sort != null && !sort.isEmpty()) {
			switch (sort) {
			case "price_low":
				pageable = PageRequest.of(pageNumber, 10, Sort.by("sellingPrice").ascending());
				break;
			case "price_high":
				pageable = PageRequest.of(pageNumber, 10, Sort.by("sellingPrice").descending());
				break;
			default:
				pageable = PageRequest.of(pageNumber, 10, Sort.unsorted());
			}
		} else {
			pageable = PageRequest.of(pageNumber, 10, Sort.unsorted());
		}
		return productRepository.findAll(spec, pageable);
	}

	@Override
	public Page<Product> getProductBySellerId(Long sellerId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size); // Create a Pageable object with page number and page size
		return productRepository.findBySellerId(sellerId, pageable);
	}

	private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
		if (mrpPrice <= 0) {
			throw new IllegalArgumentException("Actual Price must be > 0");
		}

		// Using float or double to prevent integer division
		float discount = ((float) (mrpPrice - sellingPrice) / mrpPrice) * 100;
		return (int) discount; // Convert the result back to an integer (percentage)
	}

}
