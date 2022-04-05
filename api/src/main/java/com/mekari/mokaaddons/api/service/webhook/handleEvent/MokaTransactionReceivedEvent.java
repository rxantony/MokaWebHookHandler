package com.mekari.mokaaddons.api.service.webhook.handleevent;

import java.time.OffsetDateTime;
import java.util.Currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.EventBody;
import com.mekari.mokaaddons.common.webhook.moka.EventData;

import lombok.Getter;
import lombok.Setter;

/*
{
    "header": {
        "timestamp": "2021-01-01T01:01:01Z",
        "event_name": "moka.transaction.created",
        "event_id": "69786666-8e68-451d-bc53-bff083b98e85",
        "outlet_id": "G123456",
        "version": 1
    },
    "body": {
        "transaction": {
            "id": "752cf7ff-6625-42cb-b317-c6ee9fb5b5d5",
            "date": "2021-01-01T01:01:01Z",
            "subtotal": 18000,
            "total_collected": 18000,
            "total_discounts": 0,
            "total_gratuities": 0,
            "total_taxes": 0,
            "total_gross_sales": 18000,
            "total_net_sales": 18000,
            "payment_no": "1GXSJEU",
            "payment_type": "cash",
            "items": [
                {
                    "id": 10582325,
                    "variant_id": 36629111,
                    "variant_name": "Vanilla",
                    "name": "Ice Cream",
                    "price": 18000,
                    "quantity": 1,
                    "category_id": 1839094,
                    "category_name": "Dessert",
                    "gross_sales": 18000,
                    "net_sales": 18000,
                    "total_price": 18000,
                    "modifiers": [
                        {
                            "id": 46842,
                            "name": "Topping",
                            "option_id": 1918514,
                            "option_name": "Meses",
                            "price": 0 //,
                        }
                    ]
                }
            ],
            "payment_taxes": [
                {
                    "id": 66724,
                    "name": "Pajak Jadian",
                    "amount": 1,
                    "total": -755.0125,
                    "taxable_amount": -75501.25
                }
            ],
            "payment_gratuities": [
                {
                    "id": 102878,
                    "name": "Zakat",
                    "amount": 2.5,
                    "total": -1806.25
                }
            ],
            "payment_discounts": [
                {
                    "id": 3223,
                    "payment_id": 86949,
                    "discount_id": 2712,
                    "discount_amount": -100000,
                    "discount_name": "Promo Natal",
                    "discount_type": "cash",
                    "discount_cash": 100000
                }
            ]
        }
    }
}
*/
@Getter
@Setter
public class MokaTransactionReceivedEvent extends AbstractEvent {

    private Body body;

    @Getter
    @Setter
    public static class Transaction implements EventData {
        private String id;
        private OffsetDateTime date;
        private Currency subtotal;

        @JsonProperty("total_collected")
        private Currency totalCollected;

        @JsonProperty("total_discount")
        private int totalDiscount;

        @JsonProperty("total_gratuities")
        private int totalGratuities;

        @JsonProperty("net_sales")
        private Currency netSales;

        @JsonProperty("payment_no")
        private String paymentNo;

        @JsonProperty("payment_type")
        private String paymentType;

        private Item[] items;

        @JsonProperty("payment_taxes")
        private PaymentTax[] paymentTaxes;

        @JsonProperty("payment_gratuities")
        private PaymentGratuity[] paymentGratuities;

        @JsonProperty("payment_discounts")
        private PaymentDiscount[] paymentDiscounts;

        @Getter
        @Setter
        public static class Item{
            private int id;

            @JsonProperty("variant_id")
            private int variantId; 

            private String variantName;

            private String name;

            private Currency price;

            private int quantity;

            @JsonProperty("category_id")
            private int categoryId;

            @JsonProperty("category_name")
            private String categoryName;

            @JsonProperty("gross_sales")
            private Currency grossSales;

            @JsonProperty("net_sales")
            private Currency netSales;

            @JsonProperty("total_price")
            private Currency totalPrice;
            
            private Modifier[] modifiers;

            @Getter
            @Setter
            public static class Modifier{
                private int id;
                private  String name;
                
                @JsonProperty("option_id")
                private  int optionId;
                
                @JsonProperty("option_name")
                private  String option_name;
                
                private Currency price;
            }
        }
    
        @Getter
        @Setter
        public class PaymentTax{
            private int id;
            private String name;

            private Currency amount;
            private Currency total;

            @JsonProperty("taxable_amount")
            private Currency taxableAmount;
        }

        @Getter
        @Setter
        public static class PaymentGratuity{
            private int id;
            private String name;
            private Currency amount;
            private Currency total;
        }
    
        @Getter
        @Setter
        public static class PaymentDiscount{
            private int id;

            @JsonProperty("payment_id")
            private int paymentId;

            @JsonProperty("discount_id")
            private int discountId;

            @JsonProperty("discount_amount")
            private Currency discountAmount;
            
            @JsonProperty("discount_name")
            private String discountName;
            
            @JsonProperty("discount_type")
            private String discountType;
            
            @JsonProperty("discount_cash")
            private Currency discountCash;
        }
    }

    @Getter
    @Setter
    public static class Body implements EventBody<Transaction> {
        @JsonProperty("transaction")
        private Transaction data;
    }
}

