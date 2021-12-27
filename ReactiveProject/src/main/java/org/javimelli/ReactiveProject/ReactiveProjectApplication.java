package org.javimelli.ReactiveProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Locale;

// Para que la aplicacion sea de consola o de linea de comandos implementamos CommandLineRunner
@SpringBootApplication
public class ReactiveProjectApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ReactiveProjectApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReactiveProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		/*
			IMPORTANTE. Todos los operadores map, filter... no modifican. Son instancias inmutables.
			Crean instancias por de bajo.
		 */

		// Observable
		Flux<Usuario> nameList = Flux.just("Andres","Natalia","Darel","Alex","JaviPadre","Meme")
				.doOnNext(name -> {
					if (name.isEmpty()) {
						throw new RuntimeException("Nombre vacio");
					}
					System.out.println(name);
				})
				// map cambia el tipo de instancia con la que se trabaja.
				.map(name -> {
						return new Usuario(name.toUpperCase(), 28L);
				})
				.filter(user -> user.getNombre().length() > 5);

		// Cuando nos subscribimos estamos observando. Consumidor que consume cada elemento que envia el observable.
		/*
			Los parametros son funcion por cada elemento, funicon por cada error, funcion cuando se completa el observable. Si no termina
			no se ejecuta.
		 */
		nameList.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha terminado el flujo del observable");
					}
				});
	}
}
