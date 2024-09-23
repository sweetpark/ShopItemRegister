package Shop.Item.repository;

import Shop.Item.dto.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findALl(){
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item paramItem){
        Item item = store.get(itemId);
        item.setItemName(paramItem.getItemName());
        item.setPrice(paramItem.getPrice());
        item.setQuantity(paramItem.getQuantity());
    }

    public void clear(){
        store.clear();
    }
}
