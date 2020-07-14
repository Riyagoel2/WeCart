package com.project.shoppingcart.model.data;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Size(min = 2, message = "Name must be atleast 2 characters long")
  private String name;
  
  private String slug;

  @Size(min = 5, message = "Description  must be atleast 5 characters long")
  private String description;

  private String image;

  @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?"  , message="Expected format : 5,5.89,15.99")
  private String price;
  
  
  @Column(name = "category_id")
  @Pattern(regexp = "^[1-9]", message = "Please choose a category")
  public String categoryId;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

}