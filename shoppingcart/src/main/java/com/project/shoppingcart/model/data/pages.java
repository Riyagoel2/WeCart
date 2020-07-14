package com.project.shoppingcart.model.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="pages")
@Data
public class pages {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Size(min=2 , message="title must be atleast 2 characters long")
    private String title;
    @Size(min=5 , message="content must be atleast 5 characters long")
    private String content;
    private String slug;
    private int sorting;


    
}