package com.medical.schoolMedical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//để lập lịch chạy hàm có annotation schedule
@EnableScheduling
public class SchoolMedicalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolMedicalApplication.class, args);
	}

}
