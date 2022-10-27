package com.example.minhasfinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinhasfinancasApplication {
	
	public void testandoOLiveReload() {
		System.out.println("Live reload funcionando");
	}

	public static void main(String[] args) {
		SpringApplication.run(MinhasfinancasApplication.class, args);
	}

}
