package com.swe2040group4.doctorsplaza.repository;

import com.swe2040group4.doctorsplaza.entity.Doctor;
import com.swe2040group4.doctorsplaza.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByDoctor(Doctor doctor);
}
