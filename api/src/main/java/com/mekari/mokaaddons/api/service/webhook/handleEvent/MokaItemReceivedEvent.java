package com.mekari.mokaaddons.api.service.webhook.handleEvent;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEventBody;
import com.mekari.mokaaddons.common.webhook.moka.EventData;

import lombok.Getter;
import lombok.Setter;

/*
{
    "header": {
        "event_name": "moka.item.created",
        "event_id": "c1be7fa1-645e-3d57-9ca3-f2cb54212345",
        "outlet_id": "G123456",
        "version": 1,
        "timestamp": "2022-01-01T01:01:01Z"
    },
    "body": {
        "item": {
            "id": 10582325,
            "name": "Vanilla Ice Cream",
            "description": "Fresh and delicious vanilla ice cream",
            "category": {
                "id": 1839094,
                "name": "Dessert"
            },
            "image": {
                "url": "https://images.com/vanilla-ice-cream.png"
            },
            "variants": [
                {
                    "id": 17748136,
                    "name": "Vanilla Ice Cream - Online",
                    "sku": "vanilla-ice-cream-online",
                    "sales_type_items": [
                        {
                            "id": "144365",
                            "name": "Store Delivery",
                            "price": 10000,
                            "is_default": true
                        },
                        {
                            "id": "144366",
                            "name": "GoFood",
                            "price": 20000
                        }
                    ]
                }
            ],
            "active_modifiers": [
                {
                    "id": 362774,
                    "name": "Topping",
                    "options": [
                        {
                            "id": "2482802",
                            "name": "Strawberry Sauce"
                        }
                    ]
                }
            ],
            "is_sales_type_price": true,
            "is_deleted": false
        }
    }
}
*/
@Getter
@Setter
public class MokaItemReceivedEvent extends AbstractEvent {

    private Body body;

    @Getter
    @Setter
    public static class Item implements EventData {
        private String id;
        private String name;
        private OffsetDateTime date;
        private String description;
    }

    @Getter
    @Setter
    public static class Body extends AbstractEventBody<Item> {
        @JsonProperty("item")
        private Item data;
    }
}
