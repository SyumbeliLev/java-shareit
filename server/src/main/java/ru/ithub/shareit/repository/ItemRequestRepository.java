package ru.ithub.shareit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ithub.shareit.entity.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterId(Long requesterId);

    @Query("select ir from ItemRequest ir where ir.requester.id != :userId")
    Page<ItemRequest> findAllByOtherUsers(Long userId, Pageable page);
}
