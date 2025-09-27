package overlook_hotel.overlook_hotel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import overlook_hotel.overlook_hotel.service.ManagerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OverlookHotelApplication {
	// Insert a demo manager on startup (docker compose up)
	// @Bean
	// public CommandLineRunner demoManagerInsert(ManagerService managerService) {
	// 	return args -> {
	// 		managerService.createManager(
	// 			"Doe",
	// 			"John",
	// 			"j@d.com",
	// 			"password",
	// 			"demosalt"
	// 		);
	// 	};
	// }

	public static void main(String[] args) {
		SpringApplication.run(OverlookHotelApplication.class, args);
	}

}
