package obssolution.task1.repository;

import obssolution.task1.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT COALESCE(SUM(CASE WHEN i.type = obssolution.task1.enums.InventoryType.T THEN i.quantity ELSE 0 END), 0) - " +
            "      COALESCE(SUM(CASE WHEN i.type = obssolution.task1.enums.InventoryType.W THEN i.quantity ELSE 0 END), 0) " +
            "FROM Inventory i " +
            "WHERE i.item.id = :itemId ")
    int findTotalStockByItemId(@Param("itemId") Long itemId);

}
