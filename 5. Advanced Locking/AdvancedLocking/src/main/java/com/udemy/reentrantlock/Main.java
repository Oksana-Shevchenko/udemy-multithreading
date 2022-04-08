package com.udemy.reentrantlock;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //similar to the run method in the thread class
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cryptocurrency prices");
        GridPane grid = createGridPane();
        Map<String, Label> cryptoLabels = createCryptoPriceLabels();
        addLabelsToGrid(cryptoLabels, grid);
        double width = 300;
        double height = 250;
        StackPane root = new StackPane();
        Rectangle background = createBackGroundRectangleWithAnimation(width, height);
        root.getChildren().add(background);
        root.getChildren().add(grid);

        primaryStage.setScene(new Scene(root, width, height));
        PricesContainer pricesContainer = new PricesContainer();
        PricesUpdater pricesUpdater = new PricesUpdater(pricesContainer);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (pricesContainer.getLockObject().tryLock()) {
                    try {
                        Label bitcoinLabel = cryptoLabels.get("BTC");
                        bitcoinLabel.setText(String.valueOf(pricesContainer.getBitcoinPrice()));

                        Label etherLabel = cryptoLabels.get("ETN");
                        etherLabel.setText(String.valueOf(pricesContainer.getEtherPrice()));

                        Label litecoinLabel = cryptoLabels.get("LTC");
                        litecoinLabel.setText(String.valueOf(pricesContainer.getLitecoinPrice()));

                        Label bitcoinCashLabel = cryptoLabels.get("BCN");
                        bitcoinCashLabel.setText(String.valueOf(pricesContainer.getBitcoinCashPrice()));

                        Label rippleLabel = cryptoLabels.get("XRP");
                        rippleLabel.setText(String.valueOf(pricesContainer.getRipplePrice()));
                    } finally {
                        pricesContainer.getLockObject().unlock();
                    }
                }
            }
        };
        animationTimer.start();
        pricesUpdater.start();
        primaryStage.show();
    }

    private Rectangle createBackGroundRectangleWithAnimation(double width, double height) {
        Rectangle background = new Rectangle(width, height);
        FillTransition fillTransition = new FillTransition(Duration.millis(1000), background, Color.LIGHTGREEN, Color.LIGHTBLUE);
        fillTransition.setCycleCount(Timeline.INDEFINITE);
        fillTransition.setAutoReverse(true);
        fillTransition.play();
        return background;
    }

    private void addLabelsToGrid(Map<String, Label> cryptoLabels, GridPane grid) {
        int row = 0;
        for (Map.Entry<String, Label> entry : cryptoLabels.entrySet()) {
            String cryptoName = entry.getKey();
            Label nameLabel = new Label(cryptoName);
            nameLabel.setTextFill(Color.BLUE);
            nameLabel.setOnMousePressed(event -> nameLabel.setTextFill(Color.RED));
            nameLabel.setOnMouseReleased(event -> nameLabel.setTextFill(Color.BLUE));

            grid.add(nameLabel, 0, row);
            grid.add(entry.getValue(), 1, row);
            row++;
        }
    }

    private Map<String, Label> createCryptoPriceLabels() {
        Label bitcoinPrice = new Label("0");
        bitcoinPrice.setId("BTC");

        Label etherPrice = new Label("0");
        etherPrice.setId("ETN");

        Label liteCoinPrice = new Label("0");
        liteCoinPrice.setId("LTC");

        Label bitcoinCashPrice = new Label("0");
        bitcoinCashPrice.setId("BCN");

        Label ripplePrice = new Label("0");
        ripplePrice.setId("XRP");

        Map<String, Label> cryptoLabelMap = new HashMap<String, Label>();
        cryptoLabelMap.put("BTC", bitcoinPrice);
        cryptoLabelMap.put("ETN", etherPrice);
        cryptoLabelMap.put("LTC", liteCoinPrice);
        cryptoLabelMap.put("BCN", bitcoinCashPrice);
        cryptoLabelMap.put("XRP", ripplePrice);
        return cryptoLabelMap;
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    public static class PricesContainer {
        private Lock lockObject = new ReentrantLock();

        private double bitcoinPrice;
        private double etherPrice;
        private double litecoinPrice;
        private double bitcoinCashPrice;
        private double ripplePrice;

        public Lock getLockObject() {
            return lockObject;
        }

        public double getBitcoinPrice() {
            return bitcoinPrice;
        }

        public void setBitcoinPrice(double bitcoinPrice) {
            this.bitcoinPrice = bitcoinPrice;
        }

        public double getEtherPrice() {
            return etherPrice;
        }

        public void setEtherPrice(double etherPrice) {
            this.etherPrice = etherPrice;
        }

        public double getLitecoinPrice() {
            return litecoinPrice;
        }

        public void setLitecoinPrice(double litecoinPrice) {
            this.litecoinPrice = litecoinPrice;
        }

        public double getBitcoinCashPrice() {
            return bitcoinCashPrice;
        }

        public void setBitcoinCashPrice(double bitcoinCashPrice) {
            this.bitcoinCashPrice = bitcoinCashPrice;
        }

        public double getRipplePrice() {
            return ripplePrice;
        }

        public void setRipplePrice(double ripplePrice) {
            this.ripplePrice = ripplePrice;
        }
    }

    public static class PricesUpdater extends Thread {
        private final PricesContainer pricesContainer;
        private final Random random = new Random();

        public PricesUpdater(PricesContainer pricesContainer) {
            this.pricesContainer = pricesContainer;
        }

        @Override
        public void run() {
            while (true) {
                pricesContainer.getLockObject().lock();
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pricesContainer.setBitcoinPrice(random.nextInt(20000));
                    pricesContainer.setBitcoinCashPrice(random.nextInt(20000));
                    pricesContainer.setEtherPrice(random.nextInt(20000));
                    pricesContainer.setLitecoinPrice(random.nextInt(20000));
                    pricesContainer.setRipplePrice(random.nextInt(20000));
                } finally {
                    pricesContainer.getLockObject().unlock();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
