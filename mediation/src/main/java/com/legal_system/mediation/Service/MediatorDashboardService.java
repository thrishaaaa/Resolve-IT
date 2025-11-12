package com.legal_system.mediation.Service;

import com.legal_system.mediation.model.Cases;
import com.legal_system.mediation.model.MeetingSchedule;
import com.legal_system.mediation.model.Mediators;
import com.legal_system.mediation.repository.CasesRepository;
import com.legal_system.mediation.repository.MeetingScheduleRepository;
import com.legal_system.mediation.repository.MediatorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class MediatorDashboardService {

    @Autowired
    private MediatorsRepository mediatorsRepository;

    @Autowired
    private CasesRepository casesRepository;

    @Autowired
    private MeetingScheduleRepository meetingScheduleRepository;

    // Get mediator by ID
    public Mediators getMediatorById(Integer id) {
        return mediatorsRepository.findById(id).orElse(null);
    }

    // Get all cases for a mediator
    public List<Cases> getMediatorCases(Integer mediatorId) {
        return casesRepository.findByMediatorId(mediatorId);
    }

    // Get cases by status
    public List<Cases> getMediatorCasesByStatus(Integer mediatorId, String status) {
        return casesRepository.findByMediatorIdAndStatus(mediatorId, status);
    }

    // Update case status
    public void updateCaseStatus(Integer caseId, String newStatus) {
        Optional<Cases> caseOpt = casesRepository.findById(caseId);
        if (caseOpt.isPresent()) {
            Cases caseEntity = caseOpt.get();
            caseEntity.setStatus(newStatus);
            casesRepository.save(caseEntity);
        }
    }

    // Get all meetings for a mediator
    public List<MeetingSchedule> getMediatorMeetings(Integer mediatorId) {
        return meetingScheduleRepository.findByMediatorId(mediatorId);
    }

    // Get upcoming meetings
    public List<MeetingSchedule> getUpcomingMeetings(Integer mediatorId) {
        return meetingScheduleRepository
                .findByMediatorIdAndMeetingDateGreaterThanEqualOrderByMeetingDateAsc(
                        mediatorId, LocalDate.now());
    }

    // Create new meeting
    public void createMeeting(Integer mediatorId, LocalDate date, LocalTime time, String description) {
        Mediators mediator = mediatorsRepository.findById(mediatorId).orElse(null);
        if (mediator != null) {
            MeetingSchedule meeting = new MeetingSchedule();
            meeting.setMediator(mediator);
            meeting.setMeetingDate(date);
            meeting.setMeetingTime(time);
            meeting.setDescription(description);
            meeting.setStatus("Scheduled");
            meetingScheduleRepository.save(meeting);
        }
    }

    // Cancel meeting
    public void cancelMeeting(Integer meetingId) {
        Optional<MeetingSchedule> meetingOpt = meetingScheduleRepository.findById(meetingId);
        if (meetingOpt.isPresent()) {
            MeetingSchedule meeting = meetingOpt.get();
            meeting.setStatus("Cancelled");
            meetingScheduleRepository.save(meeting);
        }
    }

    // Get statistics
    public Long getTotalCases(Integer mediatorId) {
        return casesRepository.countByMediatorId(mediatorId);
    }

    public Long getActiveCases(Integer mediatorId) {
        return casesRepository.countByMediatorIdAndStatus(mediatorId, "Open");
    }

    public Long getResolvedCases(Integer mediatorId) {
        return casesRepository.countByMediatorIdAndStatus(mediatorId, "Resolved");
    }

    public Long getUpcomingMeetingsCount(Integer mediatorId) {
        return meetingScheduleRepository.countByMediatorIdAndStatus(mediatorId, "Scheduled");
    }
}