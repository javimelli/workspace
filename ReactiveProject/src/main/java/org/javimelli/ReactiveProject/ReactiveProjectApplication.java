package org.javimelli.ReactiveProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
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
			IMPORTANTE. Todos los Observables o sujetos son inmutables.
			Cuando se prodesa su información se crean instancias por de bajo, pero no modifican el flujo original.
		 */

        // Observable. Es un flujo inmutable y por tanto los operadores que se utilicen, no los modifica.
        Flux<String> nameList = Flux.just("Andres", "Natalia", "Darel", "Alex", "JaviPadre", "Meme");

        // Observable de tipo Ususario. Se genera al procesar el onservable nameList, pero como son inmutables
        // nameList no se modifica. Se generan instancias por de bajo y el flujo original queda como esta.
        Flux<Usuario> userList = nameList.doOnNext(name -> {
                    if (name.isEmpty()) {
                        throw new RuntimeException("Nombre vacio");
                    }
                    System.out.println(name);
                })
                // map cambia el tipo de instancia con la que se trabaja.
                .map(name -> {
                    return new Usuario(name.toUpperCase(), 28L);
                }).filter(user -> user.getNombre().length() > 5);

        // Cuando nos subscribimos estamos observando. Consumidor que consume cada elemento que envia el observable.
		/*
			Los parametros son funcion por cada elemento, funicon por cada error, funcion cuando se completa el observable. Si no termina
			no se ejecuta.
		 */
        // En este caso solo imprimimos el flux original ya que en el paso donde se genera el flux de usuarios no se modifica al ser inmmutable
        nameList.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha terminado el flujo del observable");
            }
        });

        userList.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha terminado el flujo del observable");
            }
        });

        // Se pueden generar observables, no solo con just, también a partir de todos los objetos itreables de java.
        List<Usuario> userArrayList = new ArrayList<>();
        userArrayList.add(new Usuario("Andres"));
        userArrayList.add(new Usuario("Natalia"));
        userArrayList.add(new Usuario("Darel"));
        userArrayList.add(new Usuario("Alex"));
        userArrayList.add(new Usuario("JaviPadre"));
        userArrayList.add(new Usuario("Meme"));
        userArrayList.add(new Usuario("Carmen"));

        Flux<Usuario> usuariosFlux = Flux.fromIterable(userArrayList);
        log.info("Observable generado con el metodo fromIterable:");
        usuariosFlux.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha terminado el flujo del observable");
            }
        });

        // Se puede convertir un Flux en un Mono(La lista de objetos)
        Mono<List<Usuario>> usuariosMono = usuariosFlux.collectList();
        log.info("Se lista los componentes de la lista a aprtir de Mono de tipo lista");
        usuariosMono.subscribe(l -> {
            log.info(l.toString());
            l.forEach(user -> log.info(user.toString()));
        });

        // operador zipWith que mezcla dos flujos.
        log.info("Flujo mergeado con zipWith");
        usuariosFlux.zipWith(userList, (userWhitoutAge, userWhitAge) -> new Usuario(userWhitoutAge.getNombre(), userWhitAge.getEdad()))
                .subscribe(userZip -> log.info(userZip.toString()));
    }
}
