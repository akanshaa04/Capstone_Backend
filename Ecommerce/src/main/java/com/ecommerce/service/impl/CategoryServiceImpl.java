package com.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<String> findAllDescendantCategoryIds(String categoryId) {
        List<String> descendantIds = new ArrayList<>(); // Store all descendants
        Queue<Category> queue = new LinkedList<>(); // BFS queue

        // Fetch the root category by categoryId
        Category rootCategory = categoryRepository.findByCategoryId(categoryId);
        if (rootCategory == null) {
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
        queue.add(rootCategory); // Add the root category to the queue

        // Perform BFS to find all descendants
        while (!queue.isEmpty()) {
            Category current = queue.poll();
            descendantIds.add(current.getCategoryId()); // Add current category ID to the list

            // Fetch all child categories of the current category
            List<Category> children = categoryRepository.findByParentCategory(current);
            queue.addAll(children); // Add all child categories to the queue
        }

        return descendantIds; // Return the list of descendant category IDs
    }
    
   

}
