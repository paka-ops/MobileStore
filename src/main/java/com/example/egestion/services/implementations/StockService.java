package com.example.egestion.services.implementations;

import com.example.egestion.models.Product;
import com.example.egestion.models.Stock;
import com.example.egestion.repositories.StockRepository;
import com.example.egestion.security.SecurityValidator;
import com.example.egestion.services.interfaces.IStock;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class StockService implements IStock {
    private final StockRepository stockRepository;
    private final SecurityValidator securityValidator;

    public StockService(StockRepository stockRepository, SecurityValidator securityValidator) {
        this.stockRepository = stockRepository;
        this.securityValidator = securityValidator;
    }

    @Override
    @Transactional
    public Stock setBasedStock(double basedStock, UUID productId) {
        Stock stock = stockRepository.findByProductId(productId);
        stock.setBaseStock(stock.getBaseStock() + basedStock);
        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public Stock incrementSales(double nbrSale,UUID productId) {
        Stock stock = stockRepository.findByProductId(productId);
        stock.setTotalSell(stock.getTotalSell() + nbrSale);
        return stockRepository.save(stock);
    }

    @Override
    @Transactional
    public List<Stock> incrementManySales(Map<Double, UUID> selsProduct) {
        List<UUID> productIds = selsProduct.values().stream().toList();
        List<Stock> stocks = stockRepository.findAllByProductIdIn(productIds);
        Map<UUID,Stock> productsStocks = stocks.stream().collect(Collectors.toMap(Stock::getProductId, stock ->stock));
        List<Stock> updatedStocks = new ArrayList<>(productsStocks.size());
        selsProduct.forEach((qt,productId)->{
            Stock stock = productsStocks.get(productId);
            stock.setTotalSell(stock.getTotalSell() + qt);
            updatedStocks.add(stock);
        });
        return stockRepository.saveAll(updatedStocks);
    }

    @Override
    @Transactional
    public Stock getProductStodck(UUID productId) {
        securityValidator.validateProductAccess(productId);
        return stockRepository.findByProductId(productId);
    }

    public Stock createProductStock(Product product){
        Stock stock = new Stock();
        stock.setProductId(product.getId());
        stock.setBaseStock(product.getQuantity());
        stock.setBuyingPrice(product.getBuyingPrice());
        stock.setSellingPrice(product.getSalingPrice());
        stock.setDate(LocalDate.now());
        stock.setTotalSell(0);
        return stockRepository.save(stock);
    }


}
