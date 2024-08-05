package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.MyNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRepositoryInMemory implements ItemRepository {

    private final Map<Long, Item> items;
    private long id = 0L;

    @Override
    public Item create(Item item) {
        id += 1L;
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAllByOwner(long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == ownerId)
                .toList();
    }

    @Override
    public Item get(long id) {
        if (items.containsKey(id)) {
            return items.get(id);
        }
        log.warn("Вещи с id={} не существует в памяти приложения для получения.", id);
        throw new MyNotFoundException("Вещь с id " + id + " не найдена");
    }

    @Override
    public List<Item> getSelection(String searchText) {
        String text = searchText.toLowerCase();
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .toList();
    }
}