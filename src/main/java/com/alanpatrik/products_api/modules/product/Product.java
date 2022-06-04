package com.alanpatrik.products_api.modules.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "product")
public class Product {

    @Id
    private String id;
    private String name;
    private double price;
    private int qty;
}
