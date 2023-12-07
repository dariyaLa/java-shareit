package ru.practicum.shareIt.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.exception.NotFoundData;
import ru.practicum.shareIt.items.ItemDto;
import ru.practicum.shareIt.items.ItemMapper;
import ru.practicum.shareIt.items.ItemRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ru.practicum.shareIt.ServiceMain<ItemRequestDto, ItemRequest> {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Optional<ItemRequestDto> save(ItemRequest itemRequest) {
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Optional<ItemRequestDto> update(ItemRequest itemRequest) {
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Optional<ItemRequestDto> find(long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundData("Not found itemRequest with id = " + id));
        List<ItemDto> itemList = itemRepository.findByRequestId(itemRequest.getRequestor()).stream()
                .map(item -> ItemMapper.toDto(item).get())
                .collect(Collectors.toList());

        return ItemRequestMapper.toDtoWithItems(itemRequest, itemList);
    }

    @Override
    public Collection<ItemRequestDto> findAll() {
        return itemRequestRepository.findAll().stream()
                .map(i -> ItemRequestMapper.toDto(i).get())
                .collect(Collectors.toList());
    }

    public Collection<ItemRequestDto> findAll(long userId, Pageable pageable) {
        return itemRequestRepository.findByRequestor(userId, pageable).stream()
                .map(i -> {
                    List<ItemDto> itemList = itemRepository.findByRequestId(userId).stream()
                            .map(item -> ItemMapper.toDto(item).get())
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toDtoWithItems(i, itemList).get();
                })
                .collect(Collectors.toList());
    }

    public Collection<ItemRequestDto> findAllWithoutOwner(long userId, Pageable pageable) {
        return itemRequestRepository.findByRequestorNot(userId, pageable).stream()
                .map(i -> {
                    List<ItemDto> itemList = itemRepository.findByRequestId(i.getRequestor()).stream()
                            .map(item -> ItemMapper.toDto(item).get())
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toDtoWithItems(i, itemList).get();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        itemRequestRepository.deleteById(id);
    }

}
