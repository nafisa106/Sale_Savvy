package com.example.Sale_Savvy.adminservices;


import com.example.Sale_Savvy.Entities.Category;
import com.example.Sale_Savvy.Entities.Order;
import com.example.Sale_Savvy.Entities.Product;
import com.example.Sale_Savvy.Entities.ProductImage;
import com.example.Sale_Savvy.Repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminProductService(ProductRepository productRepository,
                               ProductImageRepository productImageRepository,
                               CategoryRepository categoryRepository,
                               UserRepository userRepository,
                               OrderRepository orderRepository) {

        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl) {
        // Validate the category
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Invalid category ID");
        }

        // Create and save the product
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);
        product.setCategory(category.get());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        // Create and save the product image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(savedProduct);
            productImage.setImageUrl(imageUrl);
            productImageRepository.save(productImage);
        } else {
            throw new IllegalArgumentException("Product image URL cannot be empty");
        }

        return savedProduct;
    }

    public void deleteProduct(Integer productId) {
        // Check if the product exists
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found");
        }

        // Delete associated product images
        productImageRepository.deleteByProductId(productId);

        // Delete the product
        productRepository.deleteById(productId);
    }


    public Product updateProduct(
            Integer productId,
            String name,
            String description,
            Double price,
            Integer stock,
            Integer categoryId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        product.setCategory(category);

        return productRepository.save(product);
    }


    public long getTotalProducts()
    {
        return productRepository.count();
    }
    public long getTotalUsers()
    {
        return userRepository.count();
    }


    public long getTotalOrders()
    {
        return orderRepository.countSuccessfulOrders();
    }

    public BigDecimal getTotalRevenue()
    {
        return orderRepository.calculateOverallBusiness();
    }

    public BigDecimal getDailyRevenue()
    {
        List<Order> orders =
                orderRepository.findSuccessfulOrdersByDate(LocalDate.now());

        BigDecimal total = BigDecimal.ZERO;

        for (Order order : orders)
        {
            total = total.add(order.getTotalAmount());
        }

        return total;
    }


    public BigDecimal getMonthlyRevenue()
    {
        List<Order> orders =
                orderRepository.findSuccessfulOrdersByMonthAndYear(
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                );

        BigDecimal total = BigDecimal.ZERO;

        for (Order order : orders)
        {
            total = total.add(order.getTotalAmount());
        }

        return total;
    }

    public BigDecimal getYearlyRevenue()
    {
        List<Order> orders =
                orderRepository.findSuccessfulOrdersByYear(
                        LocalDate.now().getYear()
                );

        BigDecimal total = BigDecimal.ZERO;

        for (Order order : orders)
        {
            total = total.add(order.getTotalAmount());
        }

        return total;
    }



    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}
