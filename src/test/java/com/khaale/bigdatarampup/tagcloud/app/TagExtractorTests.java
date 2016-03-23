package com.khaale.bigdatarampup.tagcloud.app;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Aleksander on 22.03.2016.
 */
public class TagExtractorTests {

    @Test
    public void getTags_shouldReturnEmtyList_WhenTextIsEmpty() {
        //arrange
        String text = "";

        //act
        TagExtractor sut = new TagExtractor();
        Collection<String> actual = sut.getTags(text);

        //assert
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void getTags_shouldReturnTop10Words() {
        //arrange
        String text = "12V/24V Digital LED Auto Car/Truck Voltmeter Gauge   2016 –  $5.99     " +
                "English Français Español Deutsch Italiano Português 日本語 Русский Nederlands العربية Norsk Dansk Svenska 한국어 Suomi עברית Türkçe Polski Čeština Ελληνικά Hrvatski Română Magyar " +
                "Homepage Flash Sale Login Customer Service Ship to: $USD × MiniInTheBox.com ships worldwide to nearly every country across the globe. Choose Currency: $ USD Save Cancel SHOP ALL CATEGORIES Apple Accessories (42388) Samsung Accessories (28663) Cell Phone Accessori... (15583) Jewelry & Watches (59444) LED & Lighting (9444) Electronic Accessori... (20857) PC & Tablet Accessor... (9763) Sports & Lifestyle (26150) Household & Pets (10704) Fashion & Clothing LightInTheBox.com Flash Sales 00Cart Your shopping cart is empty. more items in the cart... Cart Total:  Proceed to Checkout View My Cart » Electronics Accessories Car Accessories Tools & Equipment Measuring Tools Shown Color: " +
                "12V/24V Digital LED Auto Car/Truck Voltmeter Gauge #00284486 (32) " +
                "USD USD CNY EUR GBP CAD AUD CHF HKD JPY RUB BRL CLP NOK DKK SAR SEK KRW ILS MXN $7.37 $5.99 (19% OFF) 12V/24V Digital LED Auto Car/Truck Voltmeter Gauge $ 5.99 Earn Additional 1% Cash Rewards On All Orders Ship to Belarus Processing Time: Will be calculated when item is selected Shipping Time: Expedited 3-4 business days QTY: " +
                "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100 (71) " +
                "You May Also Like USD $8.99 $11.60 $11.60 USD $8.99 USD $4.99 $6.44 $6.44 USD $4.99 USD $12.99 USD $12.99 USD $20.99 $27.08 $27.08 USD $20.99 USD $1.99 USD $1.99 USD $15.99 $18.07 $18.07 USD $15.99 USD $5.99 $7.97 $7.97 USD $5.99 USD $10.99 USD $10.99 USD $40.99 $52.88 $52.88 USD $40.99 USD $22.99 USD $22.99 USD $5.99 $7.25 $7.25 USD $5.99 USD $12.99 $14.94 $14.94 USD $12.99 USD $4.99 USD $4.99 USD $6.99 USD $6.99 USD $9.99 $12.99 $12.99 USD $9.99 USD $2.99 $3.62 $3.62 USD $2.99 USD $37.99 USD $37.99 USD $27.99 USD $27.99 USD $13.99 USD $13.99 USD $4.99 USD $4.99 USD $15.99 $21.27 $21.27 USD $15.99 USD $4.99 USD $4.99 USD $10.99 $18.99 $18.99 USD $10.99 USD $7.99 USD $7.99 Product Details Specifications Type Gadgets & Auto Parts, See All > Other Products From This Supplier USD $45.99 $56.57 USD $9.99 USD $43.99 $61.59 USD $3.99 USD $34.99 $48.99 USD $6.99 $8.46" +
                " See all 1 questions and answers > Q & A All Faqs Product description Shipping or Payment Customer service issue Hide all answers Q: Q:The display has lost the centre digit. Can we have it replaced? The display has lost the centre digit. Can we have it replaced? ByRick Jul 28, 2015 Reply Please input your reply. Post A:In your case, please contact customer service for professional help. Please log in to your account on our site and go to My Order; then, click the related Order Number that you need help with to submit a ticket for customer service. They will be glad to provide professional help. Thanks a lot for shopping with us. ByAlison Jul 28, 2015 Was this answer helpful? (0) (0) View All Q: Q:The display has lost the centre digit. Can we have it replaced? The display has lost the centre digit. Can we have it replaced? ByRick Jul 28, 2015 Reply Please input your reply. Post A:In your case, please contact customer service for professional help. Please log in to your account on our site and go to My Order; then, click the related Order Number that you need help with to submit a ticket for customer service. They will be glad to provide professional help. Thanks a lot for shopping with us. ByAlison Jul 28, 2015 Was this answer helpful? (0) (0) View All Ask a new question " +
                "Your Recent History   MiniInTheBox.com – All Small GadgetsTM A New York Stock Exchange Listed Company (NYSE: LITB) Variety for A Better Life More than a million different Items, 1000+ new items everyday Discover new trendy products for you with big data technology Special made-to-order items only at LightInThebox Low Price via Factory-Direct Work with factory directly to cut the middle man Forster innovations in manufacturing to improve efficiency Safe & Trustworthy Work only with the most secure payment provider Never store your credit card information Quality insurance system for superb product quality Fast Delivery Local warehouse in Europe & USA Global express shipping in 3-5 days Customer Friendly Services Native speaker to serve you in 27 Languages 30-day satisfaction guaranteed free return policy * Local return services for Europe & USA * Not include made-to-order items. Join Our Family Sign-Up Download our mobile apps for extra cash rewards Register our news letter for exclusive benefits Subscribe Visit our other site: LightInTheBox.com Join our community Popular Pages: A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0-9 " +
                "Hot Categories Wii Controller,Xbox 360 Accessories,PS3 Accessories,Nintendo DS Accessories,iPad Accessories,Tablet Cables,Games Worldwide Shipping,Best Cool Gadgets,Adopter for Charger New Products & Reviews New Products |  Product Reviews Company Info About MiniInTheBox.com Jobs Site Map MiniInTheBox Publicity NewsInTheBox Customer Service Contact Us Manage My Orders Help Page and Knowledge Base Payment & Shipping Payment Methods Shipping Guide Locations We Ship To Estimated Delivery Time Company Policies Return Policy Privacy Policy Terms of Use Intellectual Property Infringement Policy Other Business Affiliate Program Become a Seller 寻求供应商   Ship to: Belarus / $USD visa, master card, maestro, carte bluepaypalpaypal verifieddhl ups ems Copyright © 2006-2016 Light In The Box Ltd. All Rights Reserved. \u007F BTest";

        //act
        TagExtractor sut = new TagExtractor();
        Collection<String> actual = sut.getTags(text);

        //assert
        assertNotNull(actual);
        assertEquals(10, actual.size());
    }
}
