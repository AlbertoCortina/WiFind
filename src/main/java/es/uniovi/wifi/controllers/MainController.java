package es.uniovi.wifi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class MainController {

	@GetMapping(path = "/prueba")
	public String prueba() {
		return "hola";
	}
}