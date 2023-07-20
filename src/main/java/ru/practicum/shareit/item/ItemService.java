package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ServiceMain;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exeption.ExeptionBadRequest;
import ru.practicum.shareit.exeption.NotFoundData;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService implements ServiceMain<ItemDto, Item> {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;


    @Override
    public Optional<ItemDto> save(Item item) {
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public Optional<ItemDto> update(Item item) {
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public Optional<ItemDto> find(long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundData("Not found item");
        }
        return ItemMapper.toDto(itemRepository.findById(id).get());
    }

    public Optional<ItemDto> findWihtUserId(long id, long userId, LocalDateTime dateTime) {
        List<CommentsDto> commentsDtoList;
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundData("Not found item");
        }
        if (item.get().getOwner() == userId) {
            Booking lastBooking = bookingRepository.findLastBooking(item.get().getId());
            Booking nextBooking = bookingRepository.findNextBooking(item.get().getId());
            List<BookingDto> lastAndNextBooking = new ArrayList<>();
            if (lastBooking != null) {
                lastAndNextBooking.add(BookingMapper.toDto(lastBooking).get());
            }
            if (nextBooking != null) {
                lastAndNextBooking.add(BookingMapper.toDto(nextBooking).get());
            }
            return ItemMapper.toDtoWithBooking(item.get(), lastAndNextBooking, findComments(item.get()));
        }
        return ItemMapper.toDtoWithBooking(item.get(), new ArrayList<>(), findComments(item.get()));
    }


    @Override
    public Optional<Item> find(String str) {
        return Optional.empty();
    }

    @Override
    public Collection<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(item -> ItemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> findAllWithUser(long userId) {
        return itemRepository.findAllByOwner(userId).stream()
                .map(item -> {
                    List<CommentsDto> commentsDtoList = null;

                    Booking lastBooking = bookingRepository.findLastBooking(item.getId());
                    Booking nextBooking = bookingRepository.findNextBooking(item.getId());
                    List<BookingDto> lastAndNextBooking = new ArrayList<>();
                    if (lastBooking != null) {
                        lastAndNextBooking.add(BookingMapper.toDto(lastBooking).get());
                    }
                    if (nextBooking != null) {
                        lastAndNextBooking.add(BookingMapper.toDto(nextBooking).get());
                    }
                    return ItemMapper.toDtoWithBooking(item, lastAndNextBooking, findComments(item)).get();
                }).collect(Collectors.toList());
    }

    public Collection<ItemDto> findAll(String text) {
        return itemRepository.findByNameContainingIgnoreCase(text).stream()
                .map(item -> ItemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }


    @Override
    public void delete(long id) {
        itemRepository.deleteById(id);
    }

    public CommentsDto saveComment(Comments comments) {
        //проверям, есть ли item
        Optional<ItemDto> findItem = find(comments.getItemId());
        //ищем user
        Optional<User> findUser = userRepository.findById(comments.getAuthor());
        //проверяем, есть ли бронь
        Optional<Booking> findBooking = bookingRepository.findAllByUserIdAndItemId(comments.getAuthor(), comments.getItemId()).stream()
                .filter(i -> i.getState().equals(State.valueOf("APPROVED")) && i.getStart().isBefore(LocalDateTime.now()))
                .findFirst();
        if (findBooking.isEmpty()) {
            throw new ExeptionBadRequest("Бронь не была зарегистрирована, либо она в будущем");
        }
        //сохраняем
        return ItemMapper.toDto(commentsRepository.save(comments), findUser.get());

    }

    private List<CommentsDto> findComments(Item item) {
        List<CommentsDto> commentsDtoList = null;
        List<Comments> comments = commentsRepository.findAllByItemId(item.getId());
        if (!comments.isEmpty()) {
            commentsDtoList = comments.stream()
                    .map(i -> {
                        Optional<User> user = userRepository.findById(i.getAuthor());
                        return ItemMapper.toDto(i, user.get());
                    })
                    .collect(Collectors.toList());
        }
        return commentsDtoList;
    }
}
