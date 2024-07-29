package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item create(Item item);

    Item update(Item item);

    List<Item> getAllByOwner(long ownerId);

    Item get(long id);

    List<Item> getSelection(String searchText);

}