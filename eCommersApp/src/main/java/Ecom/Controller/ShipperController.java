package Ecom.Controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecom.ModelDTO.ShipperDTO;
import Ecom.Service.ShipperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/order-shippers")
public class ShipperController {

	private final ShipperService shipperService;

	@GetMapping("/{id}")
	public ResponseEntity<ShipperDTO> getShipperById(@PathVariable Integer id) {
		ShipperDTO shipper = shipperService.getShipperById(id);
		return ResponseEntity.ok(shipper);
	}

	@GetMapping
	public ResponseEntity<List<ShipperDTO>> getAllShippers() {
		List<ShipperDTO> shippers = shipperService.getAllShippers();
		return ResponseEntity.ok(shippers);
	}

	@PostMapping("/add")
	public ResponseEntity<ShipperDTO> saveShipper(@Valid @RequestBody ShipperDTO shipper) {
		ShipperDTO savedShipper = shipperService.saveShipper(shipper);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedShipper);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteShipperById(@PathVariable Integer id) {
		shipperService.deleteShipperById(id);
		return ResponseEntity.ok("Shipper with ID " + id + " successfully deleted.");
	}
}
