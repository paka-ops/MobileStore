package com.example.egestion.services.implementations;

import com.example.egestion.dto.OrderContentDto;
import com.example.egestion.exceptions.AccessDeniedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.models.*;
import com.example.egestion.repositories.*;
import com.example.egestion.security.SecurityValidator;
import com.example.egestion.services.interfaces.IOrderContent;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderContentService implements IOrderContent {
    private final OrderService orderService;
    private final SecurityValidator securityValidator;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderContentRepository orderContentRepository;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final StockService stockService;

    public OrderContentService( OrderService orderService, SecurityValidator securityValidator, OrderRepository orderRepository, ProductRepository productRepository, OrderContentRepository orderContentRepository, ProductService productService, CategoryRepository categoryRepository, StockService stockService) {
        this.orderService = orderService;
        this.securityValidator = securityValidator;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderContentRepository = orderContentRepository;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Order add(List<OrderContentDto> orderContentDtos, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ElementNotFoundException("Order not found"));
        Store store = order.getStore();
        securityValidator.validateStoreAccess(store.getId());
        List<OrderContent> orderContents = new ArrayList<>(orderContentDtos.size());
        List<UUID> productUUIDs = new ArrayList<>(orderContentDtos.size());
        orderContentDtos.forEach(orderContentDto -> {
            productUUIDs.add(orderContentDto.getProductId());
        });
        List<Product> products =  productRepository.findAllById(productUUIDs);
        List<UUID> productsCategoriesIds = new ArrayList<>(orderContentDtos.size());
        products.forEach(product -> {
            productsCategoriesIds.add(product.getCategory().getId());
        });

        boolean isAllProductFromStore = categoryRepository.existsAllByIdInAndStoreId(productsCategoriesIds,store.getId());
        if(!isAllProductFromStore) throw new AccessDeniedException("There is product wich is not from the store");
        Map<UUID,Product> productMap = products.stream().collect(Collectors.toMap(Product::getId,p->p));
        Map<Product,OrderContent> orderProducts = new HashMap<>(orderContentDtos.size());
        Map<Product,Double> productQtyMap = new HashMap<>();
        orderContentDtos.forEach(orderContentDto -> {
            Product product = productMap.get(orderContentDto.getProductId());
            if(product != null){
                OrderContent orderContent = new OrderContent();
                orderContent.setOrder(order);
                orderContent.setProduct(product);
                orderContent.setQuantity(orderContentDto.getQuantity());
                orderContents.add(orderContent);
                orderProducts.put(product,orderContent);
                productQtyMap.put(product, orderContent.getQuantity());
            }
        });
        orderContentRepository.saveAll(orderContents);
        productService.decrementAllQtys(productQtyMap);
        order.setProducts(orderProducts);
        return orderService.update(order,orderId);
    }

    @Override
    public OrderContent update(OrderContent orderContent, UUID orderId) {
        return null;
    }

    
    @PreAuthorize("hasRole('EMPLOYER') || hasRole('EMPLOYEE')")
    @Transactional
    @Override
    public boolean delete(UUID orderContentId) {
        OrderContent orderContent = orderContentRepository.findById(orderContentId).orElseThrow(()->new ElementNotFoundException("OrderContent not found"));
        securityValidator.validateProductAccess(orderContent.getProduct().getId());
        stockService.setBasedStock(orderContent.getQuantity(), orderContent.getProduct().getId());
        orderContentRepository.deleteById(orderContent.getId());
        return true;

    }

    @PreAuthorize("hasRole('EMPLOYER') || hasRole('EMPLOYEE')")
    @Transactional
    @Override
    public boolean deleteMany(@NotEmpty List<UUID> orderContentsIds) {
        List<OrderContent> orderContents = orderContentRepository.findAllById(orderContentsIds);
        Map<UUID,Double> productQtyMap = new HashMap<>(orderContentsIds.size());
        orderContents.forEach(element ->{
            securityValidator.validateProductAccess(element.getProduct().getId());
            productQtyMap.put(element.getProduct().getId(), element.getQuantity());
        });
        productQtyMap.forEach((productId,qty)->{
            stockService.setBasedStock(qty,productId);
        });
        orderContentRepository.deleteAllById(orderContentsIds);
        return true;
    }

    @Override
    public List<OrderContent> getAllByOrderId(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new ElementNotFoundException("order not Found"));
        securityValidator.validateStoreAccess(order.getStore().getId());
        return orderContentRepository.findAllByOrderId(orderId);
    }

    @Override
    public List<OrderContent> getAllByOrderIds(List<UUID> ordersIds) {
        List<OrderContent> orderContents = new ArrayList<>(ordersIds.size());
        ordersIds.forEach(id->{
            List<OrderContent> orderContent = orderContentRepository.findAllByOrderId(id);
            if(!orderContent.isEmpty()) orderContents.addAll(orderContent);
        });
        return orderContents;
    }
}
