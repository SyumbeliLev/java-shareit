package ru.ithub.shareit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ithub.shareit.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query("select i from Item as i " +
            "where i.available = true " +
            "and (lower(i.name) like lower(concat('%', ?1, '%') ) " +
            "or lower(i.description) like lower(concat('%', ?1, '%') ))")
    Page<Item> search(String text, Pageable page);


}
