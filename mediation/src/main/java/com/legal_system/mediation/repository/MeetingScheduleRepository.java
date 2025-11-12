package com.legal_system.mediation.repository;

import com.legal_system.mediation.model.MeetingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule, Integer> {

    // Find meetings for a specific mediator
    List<MeetingSchedule> findByMediatorId(Integer mediatorId);

    // Find meetings by mediator and status
    List<MeetingSchedule> findByMediatorIdAndStatus(Integer mediatorId, String status);

    // Count meetings by mediator and status
    Long countByMediatorIdAndStatus(Integer mediatorId, String status);

    // Find upcoming meetings (date >= today)
    List<MeetingSchedule> findByMediatorIdAndMeetingDateGreaterThanEqualOrderByMeetingDateAsc(
            Integer mediatorId, LocalDate date);
}