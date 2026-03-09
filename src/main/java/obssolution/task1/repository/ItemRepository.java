package obssolution.task1.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import obssolution.task1.entity.Item;
import obssolution.task1.records.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query("""
        SELECT new obssolution.task1.records.ItemDto(
            i.id,
            i.name,
            i.price,
            CAST((SUM(CASE WHEN inv.type = 'T' THEN inv.quantity ELSE 0 END) -
                  SUM(CASE WHEN inv.type = 'W' THEN inv.quantity ELSE 0 END)) AS integer)
        )
        FROM Item i
        LEFT JOIN Inventory inv ON i.id = inv.item.id
        GROUP BY i.id, i.name, i.price
        """)
    Page<ItemDto> findAllWithStock(Pageable pageable);

    @Query("""
            SELECT new obssolution.task1.records.ItemDto(
                i.id,
                i.name,
                i.price,
                CAST((SUM(CASE WHEN inv.type = 'T' THEN inv.quantity ELSE 0 END) -
                      SUM(CASE WHEN inv.type = 'W' THEN inv.quantity ELSE 0 END)) AS integer)
            )
            FROM Item i
            LEFT JOIN Inventory inv ON i.id = inv.item.id
            WHERE i.id = :id
            GROUP BY i.id, i.name, i.price
            """)
    Optional<ItemDto> findByIdIncludeStock(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")}) // Tunggu max 1 detik
    Optional<Item> findByIdWithLock(Long id);


}
