/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.programmingtechie.inventoryService.repository;

import com.programmingtechie.inventoryService.model.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lenovo
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    List<Inventory> findBySkuCodeIn(List<String> skuCode);
    
}
