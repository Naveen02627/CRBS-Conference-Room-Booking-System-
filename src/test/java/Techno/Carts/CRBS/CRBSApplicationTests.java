package Techno.Carts.CRBS;

import Techno.Carts.CRBS.Entity.Hall;
import Techno.Carts.CRBS.Entity.Location;
import Techno.Carts.CRBS.Repository.HallRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class CRBSApplicationTests {

	@Autowired
	HallRepository hallRepository;

	@Test
	public void CreateHall(){
		Hall hall = Hall.builder()
				.hallName("JC Bose Hall")
				.capacity(120)
				.price(450)
				.location(Location.builder()
						.state("Rajasthan")
						.district("Jaipur")
						.city("Sitapura")
						.pincode("302022")
						.build())
				.bookedSlots(new HashMap<>())
				.build();
		Hall savedHall = hallRepository.insert(hall);

		System.out.println(savedHall);
	}

}
