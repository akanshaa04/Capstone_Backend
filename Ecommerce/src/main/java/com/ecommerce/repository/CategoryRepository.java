package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Category findByCategoryId(String categoryId);
    List<Category> findByParentCategory(Category parentCategory);

}
