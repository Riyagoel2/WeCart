package com.project.shoppingcart.model;

import java.util.List;

import com.project.shoppingcart.model.data.Product;

 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{
Product findByName(String name);

Product findByNameAndIdNot(String name , int id);
Page<Product> findAll( Pageable pageable);

void findBySlug(String slug);

List<Product> findAllByCategoryId(String categoryId, Pageable pageable);

long countByCategoryId(String categoryId);

Product findBySlugAndIdNot(String slug, int id);

 
    
}