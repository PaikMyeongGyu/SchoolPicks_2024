package org.schoolPicks.domain.repository.shop;

import org.schoolPicks.domain.entity.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
