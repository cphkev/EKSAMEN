package dat.dto;

import java.util.List;

public class PackingItemResponse {
    private List<PackingItemDTO> items;

    public List<PackingItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PackingItemDTO> items) {
        this.items = items;
    }
}