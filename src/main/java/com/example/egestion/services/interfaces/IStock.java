package com.example.egestion.services.interfaces;

import com.example.egestion.models.Stock;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IStock {
    Stock setBasedStock(double basedStock, UUID productId);
    Stock incrementSales(double nbrSale,UUID productId);
    List<Stock> incrementManySales(Map<Double,UUID> selsProduct);
    Stock getProductStodck(UUID productId);
}
