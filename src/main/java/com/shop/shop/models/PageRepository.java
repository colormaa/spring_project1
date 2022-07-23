package com.shop.shop.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.shop.models.data.Page;

// public interface PageRepository extends CrudRepository<Page, Integer> {

//     @Override
//     List<Page> findAll();
// }
public interface PageRepository extends JpaRepository<Page, Integer> {
    Page findBySlug(String slug);

    // @Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
    // Page findBySlug(int id, String slug);

    // List<Page> findAll();
    Page findBySlugAndIdNot(String slug, int id);

    List<Page> findAllByOrderBySortingAsc();
}