package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.*;
import com.example.egestion.repositories.CategoryRepository;
import com.example.egestion.repositories.ProductRepository;

import com.example.egestion.repositories.StockRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecurityValidator;
import com.example.egestion.services.interfaces.IProduct;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
@Service

public class ProductService implements IProduct {
    private final ProductRepository productRepository;
    private final SecurityValidator securityValidator;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;


    public ProductService(ProductRepository productRepository, SecurityValidator securityValidator, CategoryRepository categoryRepository, StoreRepository storeRepository, StockRepository stockRepository, StockService stockService) {
        this.productRepository = productRepository;
        this.securityValidator = securityValidator;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
    }


    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Product add(Product product,UUID categoryId) throws NotAuthenticatedException, NotAuthorizedException, ElementNotFoundException, AccessDeniedException {
        this.securityValidator.hasRole("EMPLOYER");
        Authentication auth = securityValidator.getAuthentication();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ElementNotFoundException("Categorie not found "));
        securityValidator.validateCategoryAccess(categoryId);
        product.setCategory(category);
        Product savingProduct =  productRepository.save(product);
        Stock stock = new Stock();
        stock.setProduct(savingProduct);
        stock.setBaseStock(savingProduct.getQuantity());
        stock.setTotalSell(0);
        stockRepository.save(stock);
         return savingProduct;
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Product update(Product product, UUID id) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException,ElementNotFoundException {

        securityValidator.validateProductAccess(id);
        Product pro = productRepository.getReferenceById(id);
        if(product.getName() != null){
            pro.setName(product.getName());
        }if(product.getCategory() != null){
            pro.setCategory(product.getCategory());
        }if(product.getQuantity() != pro.getQuantity()){
            pro.setQuantity(product.getQuantity());
        }
        Product saved = productRepository.save(pro);
        Stock stock = stockRepository.findByProductId(id);
        stock.setBaseStock(product.getQuantity());
        stockRepository.save(stock);
        return saved;
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public void delete(UUID productId) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        securityValidator.validateProductAccess(productId);
        productRepository.deleteById(productId);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')||hasRole('EMPLOYEE')")
    public List<Product> getAllByStore(UUID storeId) throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementNotFoundException {
        securityValidator.validateStoreAccess(storeId);
        Store store = storeRepository.getReferenceById(storeId);
        List<Product> products = new ArrayList<>();
        for(Category cat : store.getCategories()){
            products.addAll(cat.getProducts());
        }
        return products;

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER') || hasRole('EMPLOYER')")
    public Product getOneByStore(UUID productId, UUID storeId) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException {
        securityValidator.validateProductAccess(productId);
        return productRepository.findById(productId).get();

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER') || hasRole('EMPLOYEE')")
    public List<Product> getAllByCategory(UUID categoryId) throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementNotFoundException {
        securityValidator.validateCategoryAccess(categoryId);
        Category category = categoryRepository.getReferenceById(categoryId);
        return category.getProducts();
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER') || hasRole('EMPLOYEE')")
    public Product getOneByCategory(UUID productId, UUID categoryId) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException {
        securityValidator.validateCategoryAccess(categoryId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ElementNotFoundException("Product not found "));
        return product;
    }

    @Override
    public Product getProductByNameAndByStore(String productName, UUID storeId) {
        return null;
    }

    @Override
    public Product getProductByProductNameAndByCategory(String productName, UUID categoryId) {
        return null;
    }

    @Override
    public Product incrementQty(double qty, Product product) {
        double newQty = product.getQuantity() + qty;
        product.setQuantity(newQty);
        return update(product,product.getId());
    }

    @Override
    public Product decrementQty(double qty, Product product) {
        double newQty = product.getQuantity() - qty;
        if(newQty <0) throw new OperationFailedException("Product out of stock");
        product.setQuantity(newQty);
        return update(product,product.getId());

    }

    @Override
    public List<Product> incrementAllQtys(Map<Product, Double> productsQtys) {
       return List.of();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<Product> decrementAllQtys(Map<Product, Double> productsQtys) {
        List<Product> products = new ArrayList<>(productsQtys.size());
        Map<Double,UUID> productsMap = new HashMap<>(productsQtys.size());
         productsQtys.forEach((product,qty)-> {
            double newQty = product.getQuantity() - qty;
            if(newQty<0) throw new OperationFailedException("Decremention opertation failed");
            product.setQuantity(newQty);
            products.add(product);
            productsMap.put(qty,product.getId());
        });
      List<Stock> stocks =    stockService.incrementManySales(productsMap);
        return  updateAll(products);
    }

    @Override
    /**
     *  Update a list of bulk
     *  <p>PreCondition: All securityCheck on productStore access right</p>
     */
    public List<Product> updateAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Product restockProduct(double qty, UUID productId) throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementNotFoundException {
        securityValidator.validateProductAccess(productId);
        Product product = productRepository.getReferenceById(productId);
        product.setQuantity(product.getQuantity() + qty );
        return productRepository.save(product);
    }


}
