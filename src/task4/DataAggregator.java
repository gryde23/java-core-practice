package task4;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class DataAggregator {

    public static ProductInfo aggregateProductInfo(String productName) {
        CompletableFuture<Double> price = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchPrice(productName);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            System.out.println(ex.getMessage());
            return 0.0;
        });

        CompletableFuture<String> description = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchDescription(productName);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            System.out.println(ex.getMessage());
            return "Нет данных";
        });

        CompletableFuture<Double> rating = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchRating(productName);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            System.out.println(ex.getMessage());
            return 0.0;
        });

        ProductInfo productInfo = CompletableFuture.allOf(price, description, rating)
                .thenApply(p -> new ProductInfo(productName, price.join(), description.join(), rating.join()))
                .join();

        return productInfo;
    }

    private static double fetchPrice(String productName) throws InterruptedException {
        Thread.sleep(1000);
        dropService("Цены товаров");
        return new Random().nextInt(1, 1000) + 0.99;
    }

    private static String fetchDescription(String productName) throws InterruptedException {
        Thread.sleep(2000);
        dropService("Описания товаров");
        return "Очень подробное описание товара";
    }

    private static double fetchRating(String productName) throws InterruptedException {
        Thread.sleep(3000);
        dropService("Рейтинг товаров");
        return new Random().nextInt(1, 4) + 0.75;
    }

    private static void dropService(String serviceName) {
        if (Math.random() <= 0.2) throw new RuntimeException("Сбой сервиса " + serviceName);
    }

    public static void main(String[] args) {
        ProductInfo productInfo = aggregateProductInfo("Ноутбук");

        System.out.println(productInfo);
    }
}
