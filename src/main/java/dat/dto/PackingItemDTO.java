package dat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class PackingItemDTO {
    private String name;
    private int weightInGrams;
    private int quantity;
    private String description;
    private String category;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<BuyingOptionDTO> buyingOptions;

    @Getter
    @Setter
    public static class BuyingOptionDTO {
        private String shopName;
        private String shopUrl;
        private int price;
    }
}