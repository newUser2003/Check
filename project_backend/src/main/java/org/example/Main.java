package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args){
        Factory factory = new Factory();
        Action action = factory.getSelectedAction(args);
        action.getAction();
    }
}
class Factory {
    public Action getSelectedAction(String[] input) {
        Action action = null;
        if (input.length < 1) {
            action = new EnterProductsAndCards();
        } else if (input[0].contains("discount cards.txt")) {
            action = new WatchDiscountCards(input[0]);
        } else if (input[0].contains("products.txt")) {
            action = new WatchProducts(input[0]);
        } else  {
            action = new PrintCheck(input);
        }
        return action;
    }
}
interface Action {
    void getAction();
}
class EnterProductsAndCards implements Action {
    public void getAction () {
        ProductsAndCards information = new Product();
        information.findOutDate();
        information = new Card();
        information.findOutDate();
    }
}
class WatchDiscountCards implements Action {
    private String fileName;
    public WatchDiscountCards(String fileName){
        this.fileName = fileName;
    }
    public void getAction () {
        Card discountCard = new Card();
        discountCard.showArray(fileName);
    }
}
class WatchProducts implements Action {
    private String fileName;
    public WatchProducts(String fileName){
        this.fileName = fileName;
    }
    public void getAction () {
        Product product = new Product();
        product.showArray(fileName);
    }
}
class PrintCheck implements Action {
    private String[] args;
    public PrintCheck(String[] input){
        args = input;
    }
    public void getAction () {
        Build build = new Build(args);
        build.buildCheck();
    }
}
 class ToCheckDate {
    public static int checkInt(int min){
        Scanner scan = new Scanner(System.in);
        int number;
        while(true) {
            if (scan.hasNextInt()) {
                number = scan.nextInt();
                if (number >= min) break;
            }
            System.out.println("Incorrect input! Try again.");
            scan.nextLine();
        }
        return number;
    }
    public static int checkInt(int min, int max){
        Scanner scan = new Scanner(System.in);
        int number;
        while(true) {
            if (scan.hasNextInt()) {
                number = scan.nextInt();
                if (number >= min && number <= max) break;
            }
            System.out.println("Incorrect input! Try again.");
            scan.nextLine();
        }
        return number;
    }
    public static Float checkFloat(float min){
        Scanner scan = new Scanner(System.in);
        float number;
        while(true) {
            if (scan.hasNextFloat()) {
                number = scan.nextFloat();
                if (number >= min) break;
            }
            System.out.println("Incorrect input! Try again.");
            scan.nextLine();
        }
        return number;
    }
    public static Float checkFloat(float min, float max){
        Scanner scan = new Scanner(System.in);
        float number;
        while(true) {
            if (scan.hasNextFloat()) {
                number = scan.nextFloat();
                if (number >= min) break;
            }
            System.out.println("Incorrect input! Try again.");
            scan.nextLine();
        }
        return number;
    }
}
abstract class ProductsAndCards{
    protected String fileStr = "";
    abstract void findOutDate();
    abstract protected void writeFile();
    abstract protected void readFile(String fileName);
    abstract void showArray(String fileName);
}
class Product extends ProductsAndCards {
    private ArrayList<Product> productsArray = new ArrayList<>();;
    private String productName;
    private String productPromotion;
    private int productId = 0;
    private float productPrice;
    public Product(){}
    public Product(int productId, String productName, float productPrice, String productPromotion){
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPromotion = productPromotion;
    }
    public void findOutDate(){
        Scanner scan = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("product name: ");
            productName = scan.nextLine();
            System.out.print("price: ");
            productPrice = ToCheckDate.checkFloat(0.01f);
            System.out.print("promotion: yes - 1   no - 2");
            choice = ToCheckDate.checkInt(1, 2);
            if (choice == 1) {
                productPromotion = "promotional";
            } else {
                productPromotion = "non-promotional";
            }
            productId++;
            fileStr += productId + "\n" + productName + "\n" + productPrice + "\n" + productPromotion + "\n";
            System.out.println("Add a product: yes - 1   no - 2");
            choice = ToCheckDate.checkInt(1, 2);
            if (choice == 2) {
                writeFile();
                break;
            }
        }
    }
    protected void writeFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("products.txt")))
        {
            bw.write(fileStr);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    protected void readFile(String fileName){
        productsArray.clear();
        Product product;
        try(BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String s;
            while((s = br.readLine()) != null){
                productId = Integer.parseInt(s);
                productName = br.readLine();
                productPrice = Float.parseFloat(br.readLine());
                productPromotion = br.readLine();
                product = new Product(productId, productName, productPrice, productPromotion);
                productsArray.add(product);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        catch (NumberFormatException ex){
            System.out.println(ex.getMessage());
        }
    }
    public int getProductId(){return productId;}
    public String getProductName(){return productName;}
    public float getProductPrice(){return productPrice;}
    public String getProductPromotion(){return productPromotion;}
    private void showThisProduct(){
        String printedLine = "";
        printedLine += productId;
        while(printedLine.length() < 6){printedLine += " ";}
        printedLine += productName;
        while(printedLine.length() < 20){printedLine += " ";}
        printedLine += "$" + productPrice;
        while(printedLine.length() < 26){printedLine += " ";}
        printedLine += productPromotion;
        System.out.println(printedLine);
    }
    public void showArray(String fileName){
        readFile(fileName);
        System.out.println("ID    DESCRIPTION   PRICE PROMOTION");
        for(Product product : productsArray){
            product.showThisProduct();
        }
    }
    public ArrayList<Product> getArray(){
        readFile("products.txt");
        return productsArray; }
}
class Card extends ProductsAndCards{
    private int cardNumber;
    private float discount;
    private HashMap<Integer, Float> cardsArray= new HashMap<Integer, Float>();
    public void findOutDate(){
        int choice;
        while (true){
            System.out.print("discount card number: ");
            cardNumber = ToCheckDate.checkInt(1000);
            System.out.print("card discount: ");
            discount = ToCheckDate.checkFloat(0.1f, 90f);
            fileStr += cardNumber + "\n" + discount + "\n";
            System.out.println("Add a discount card: yes - 1    no - 2");
            choice = ToCheckDate.checkInt(1, 2);
            if (choice == 2) {
                writeFile();
                break;
            }
        }
    }
    protected void writeFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("discount cards.txt")))
        {
            bw.write(fileStr);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    protected void readFile(String fileName){
        cardsArray.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String s;
            while((s = br.readLine()) != null){
                cardNumber = Integer.parseInt(s);
                discount = Float.parseFloat(br.readLine());
                cardsArray.put(cardNumber, discount);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        catch(NumberFormatException ex){
            System.out.println(ex.getMessage());
        }
    }
    public void showArray(String fileName){
        readFile(fileName);
        System.out.println(cardsArray.entrySet());
    }
    public HashMap<Integer, Float> getArray(){ readFile("discount cards.txt"); return cardsArray; }
}
class Check {
    private ArrayList<Product> productsArray;
    private HashMap<Integer, Float> cardsArray;
    private ArrayList<ProductsInCheck> productsList = new ArrayList<>();
    private float discount = 0;
    private String check = "";
    public void setArrays(){
        Product reedProductArray = new Product();
        productsArray = reedProductArray.getArray();
        Card reedCardArray = new Card();
        cardsArray = reedCardArray.getArray();
    }
    public int setProductList(String[] args) {
        int strId, productId, cardNumber = 1, productQuantity, deleteFromProductList = -1;
        String productIdStr;
        boolean missingElements = false, thereIsDeletableItem = false;
        for(String product : args){
            try {
                strId = product.indexOf('-');
                if(strId < 0) return -1;
                productIdStr = product.substring(0, strId);
                productQuantity = Integer.parseInt(product.substring(strId + 1));
                if (productIdStr.equals("card")){
                    cardNumber = productQuantity;
                    break;
                }
                productId = Integer.parseInt(productIdStr);
            } catch (NumberFormatException e){
                return -1;
            }
            for(ProductsInCheck productInCheck: productsList){
                if(productInCheck.id() == productId){
                    productQuantity += productInCheck.quantity();
                    deleteFromProductList = productsList.indexOf(productInCheck);
                    thereIsDeletableItem = true;
                    break;
                }
            }
            if(thereIsDeletableItem) {
                productsList.remove(deleteFromProductList);
            }
            for(Product existingProduct : productsArray){
                missingElements = true;
                if(existingProduct.getProductId() == productId){
                    missingElements = false;
                    boolean promotion;
                    String name = existingProduct.getProductName();
                    float prise = existingProduct.getProductPrice();
                    String hasPromotion = existingProduct.getProductPromotion();
                    if (hasPromotion.equals("promotional") && productQuantity > 5){
                        promotion = true;
                    }
                    else{
                        promotion = false;
                    }
                    productsList.add(new ProductsInCheck(productId, name, productQuantity, prise, promotion));
                    break;
                }
            }
            if (missingElements){
                System.out.println("Missing element!");
            }
        }
        return cardNumber;
    }
    public void findDiscountCard(int cardNumber){
        if(cardsArray.containsKey(cardNumber)) {
            discount = cardsArray.get(cardNumber);
        }
        else{
            System.out.println("Card not found!");
        }
    }
    public void printCheck(){
        TextForCheck checkRedactor = new Title();
        check += checkRedactor.getText();
        checkRedactor = new ProductString(productsList, discount);
        check += checkRedactor.getText();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("check.txt")))
        {
            bw.write(check);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println(check);
    }
}
interface TextForCheck {
    String getText();
}
class Title implements TextForCheck {
    String title;
    public String getText() {
        title = "              CASH RECEIPT\n" +
                "            SUPERMARKET 123\n" +
                "       12, MILKYWAY Galaxy/Earth\n" +
                "            Tel :123-456-7890\n\n" +
                "CASHIER: №1250           DATE: ";
        getDate();
        title += "                         TIME: ";
        getTime();
        title += "_______________________________________________\n\n" +
                "QTY DESCRIPTION             PRICE       TOTAL\n\n";
        return title;
    }
    private void getDate(){
        LocalDate dateToday = LocalDate.now();
        title += dateToday.getDayOfMonth() + "/" + dateToday.getMonthValue() + "/" + dateToday.getYear() + "\n";
    }
    private void getTime(){
        LocalTime time = LocalTime.now();
        title += time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "\n";
    }
}
class ProductString implements TextForCheck{
    ArrayList<ProductsInCheck> productsList = new ArrayList<>();
    private int quantity;
    private String description;
    private float prise;
    private float resultPrise;
    private boolean promotion;
    private float totalPrise;
    private float discount;
    String positionsStr;
    public ProductString(ArrayList<ProductsInCheck> productsList, float discount){
        this.productsList = productsList;
        totalPrise = 0;
        positionsStr = "";
        this.discount = discount;
    }
    public String getText(){
        final float PAYMENT_SHARE = 0.9f;
        int strLength;
        for(ProductsInCheck position : productsList){
            strLength = positionsStr.length();
            quantity = position.quantity();
            description = position.name();
            prise = position.price();
            promotion = position.promotion();
            resultPrise = prise * quantity;
            positionsStr += quantity;
            while(positionsStr.length() < (strLength + 4)){ positionsStr += " ";}
            positionsStr += description;
            while(positionsStr.length() < (strLength + 28)){ positionsStr += " ";}
            positionsStr += "$" + prise;
            if(promotion){
                resultPrise *= PAYMENT_SHARE;
                positionsStr += "  -10% ";
            }
            totalPrise += resultPrise;
            while(positionsStr.length() < (strLength + 40)){ positionsStr += " ";}
            positionsStr += "$" + resultPrise + "\n";
        }
        Total();
        return positionsStr;
    }
    private void Total(){
        final int PERCENT = 100;
        float saved = totalPrise * discount / PERCENT;
        positionsStr += "\n____________________________________________\n\n" +
                "AMOUNT                                  $" + totalPrise + "\n" +
                "DISCOUNT " + discount + "%                           $" + saved + "\n" +
                "TOTAL                                   $" + (totalPrise - saved) + "\n";
    }
}
record ProductsInCheck(int id, String name, int quantity, float price, boolean promotion){}
abstract class Builder {
    protected org.example.Check car;
    public abstract org.example.Check buildCheck(String[] input);
}
class BuilderImpl extends Builder {
    public BuilderImpl(){
        car = new org.example.Check();
    }
    public org.example.Check buildCheck(String[] args) {
        car.setArrays();
        int discountCardNumber = car.setProductList(args);
        if(discountCardNumber > -1) {
            car.findDiscountCard(discountCardNumber);
            car.printCheck();
        }
        else{
            System.out.println("Error!");
        }
        return car;
    }
}
class Build {
    private Builder builder;
    private String[] args;
    public Build(String[] args){
        this.args = args;
        builder = new BuilderImpl();
    }
    public org.example.Check buildCheck(){
        return builder.buildCheck(args);
    }
}