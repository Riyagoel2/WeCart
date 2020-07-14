package com.project.shoppingcart.model;

import java.util.List;

import com.project.shoppingcart.model.data.pages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PageRepository extends JpaRepository<pages, Integer> {
pages findBySlug(String slug);
pages findBySlugAndIdNot(String slug,int id);
List<pages> findAllByOrderBySortingAsc();
}