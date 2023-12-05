package ru.practicum.shareIt.items;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.booking.*;
import ru.practicum.shareIt.exception.ExeptionBadRequest;
import ru.practicum.shareIt.exception.NotFoundData;
import ru.practicum.shareIt.users.User;
import ru.practicum.shareIt.users.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService implements ru.practicum.shareIt.ServiceMain<ItemDto, Item> {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private UserRepository userRepository;


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

    public Optional<ItemDto> findWihtUserId(long id, long userId) {
        Optional<Item> item = itemRepository.findById(id);
        LocalDateTime now = LocalDateTime.now();
        if (item.isEmpty()) {
            throw new NotFoundData("Not found item");
        }
        //если user является владельцем item, выводим с бронями, иначе без броней
        if (item.get().getOwner() == userId) {
            log.info("Выводим номер item " + item.get());
            log.info("Выводим user " + item.get().getOwner());
            log.info("Выводим userId " + userId);
            log.info("Выводим время " + now);
            Booking lastBooking = bookingRepository.findLastBooking(item.get().getId(), now);
            log.info("Выводим lastBooking " + String.valueOf(lastBooking));
            List<BookingDto> lastAndNextBooking = new ArrayList<>();
            if (lastBooking != null) {
                lastAndNextBooking.add(BookingMapper.toDto(lastBooking).get());
                //log.info("Выводим nextBooking из листа " + lastAndNextBooking.get(0));
            } else {
                lastAndNextBooking.add(null);
            }
            Booking nextBooking = bookingRepository.findNextBooking(item.get().getId(), now);
            log.info("Выводим nextBooking " + String.valueOf(nextBooking));
            if (nextBooking != null) {
                BookingDto bookingDto = BookingMapper.toDto(nextBooking).get();
                log.info("NextBooking преобразовали в dto " + bookingDto);
                lastAndNextBooking.add(BookingMapper.toDto(nextBooking).get());
                log.info("Выводим размер массива брони " + lastAndNextBooking.size());
            } else {

                lastAndNextBooking.add(null);

            }
            //log.info("Выводим 0 индекс листа " + lastAndNextBooking.get(0));
            return ItemMapper.toDtoWithBooking(item.get(), lastAndNextBooking, findComments(item.get()));
        }
        return ItemMapper.toDtoWithBooking(item.get(), new ArrayList<>(), findComments(item.get()));
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
                    LocalDateTime now = LocalDateTime.now();
                    Booking lastBooking = bookingRepository.findLastBooking(item.getId(), now);
                    List<BookingDto> lastAndNextBooking = new ArrayList<>();

                    if (lastBooking != null) {
                        lastAndNextBooking.add(BookingMapper.toDto(lastBooking).get());
                    } else {
                        lastAndNextBooking.add(null);
                    }
                    Booking nextBooking = bookingRepository.findNextBooking(item.getId(), now);
                    log.debug("Выводим nextBooking " + String.valueOf(nextBooking));
                    if (nextBooking != null) {
                        lastAndNextBooking.add(BookingMapper.toDto(nextBooking).get());
                    } else {
                        lastAndNextBooking.add(null);
                    }
                    return ItemMapper.toDtoWithBooking(item, lastAndNextBooking, findComments(item)).get();
                })
                .sorted(Comparator.comparing(ItemDto::getId)) //здесь добавила
                .collect(Collectors.toList());
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
