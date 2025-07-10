package com.medical.schoolMedical.service;

import com.medical.schoolMedical.entities.HealthCheckSchedule;
import com.medical.schoolMedical.repositories.HealthCheckScheduleRepository;
import com.medical.schoolMedical.repositories.VaccinationScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StatisticsService {
    @Autowired
    private HealthCheckScheduleRepository healthCheckScheduleRepository;
    @Autowired
    private VaccinationScheduleRepository vaccinationScheduleRepository;

    public List<Integer> getMonthlyVaccinationCounts(int year) {
        List<Object[]> rawStats = vaccinationScheduleRepository.getMonthlyVaccinationStats(year);
        return rawStats.stream()
                .map(row -> ((Number) row[1]).intValue())
                .toList();
    }

    public List<Integer> getMonthlyHealthCheckCounts(int year) {
        List<Object[]> rawStats = healthCheckScheduleRepository.getMonthlyHealthCheckStats(year);
        return rawStats.stream()
                .map(row -> ((Number) row[1]).intValue())
                .toList();
    }

}
