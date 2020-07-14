package com.project.shoppingcart.model;

import java.util.List;

import com.project.shoppingcart.model.data.Category;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{
Category findByName(String name);
List<Category> findAllByOrderBySortingAsc();
Category findBySlug(String slug);
    
}