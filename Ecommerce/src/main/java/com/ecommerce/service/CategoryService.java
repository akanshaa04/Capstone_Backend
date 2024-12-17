package com.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;


public interface CategoryService {
	public List<String> findAllDescendantCategoryIds(String categoryId);
}
