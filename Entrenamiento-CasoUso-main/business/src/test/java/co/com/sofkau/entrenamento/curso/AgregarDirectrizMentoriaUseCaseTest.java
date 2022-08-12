package co.com.sofkau.entrenamento.curso;


import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.CursoId;
import co.com.sofkau.entrenamiento.curso.values.Descripcion;
import co.com.sofkau.entrenamiento.curso.values.Directiz;
import co.com.sofkau.entrenamiento.curso.values.Fecha;
import co.com.sofkau.entrenamiento.curso.values.MentoriaId;
import co.com.sofkau.entrenamiento.curso.values.Nombre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AgregarDirectrizMentoriaUseCaseTest {

    @InjectMocks
    private AgregarDirectrizMentoriaUseCase useCase;
    @Mock
    private DomainEventRepository repository;

    /**
     * Test para agregar una directriz a una mentoria
     */
    @Test
    void agregarDirectrizAMentoria() {

        /**
         * Arrange
         * Se crea el curso con una directriz y una mentoria asociada a el curso
         */
        var mentoriaId = MentoriaId.of("DDD1");
        var cursoId = CursoId.of("DDD2");
        var nombre = new Nombre("Juan");
        var directiz = new Directiz("Nueva Directriz");
        var descripcion = new Descripcion("Nueva Descripcion");
        var fecha = new Fecha(LocalDateTime.now(), LocalDate.now());
        var command = new AgregarDirectrizMentoria(cursoId, mentoriaId, directiz);

        when(repository.getEventsBy("DDD2")).thenReturn(history());
        useCase.addRepository(repository);

        /**
         * Act
         * Se agrega la directriz a la mentoria
         */
        var events = UseCaseHandler.getInstance()
              .setIdentifyExecutor(command.getMentoriaId().value())
              .syncExecutor(useCase, new RequestCommand<>(command))
              .orElseThrow()
              .getDomainEvents();

        /**
         * Assert
         * Se verifica que se haya agregado la directriz a la mentoria
         */
        var event = (DirectrizAgregadaAMentoria)events.get(0);
        Assertions.assertEquals("Nueva Directriz", event.getDirectiz().value());
    }

    /**
     * Test para agregar una directriz a una mentoria
     * Aqui cuadro los datos para compararlos con los datos que se van a agregar
     */
    private List<DomainEvent> history() {
        var mentoriId = MentoriaId.of("DDD1");
        var cursoId = CursoId.of("DDD2");
        var nombre =new Nombre("Juan");
        var directiz = new Directiz("Nueva Directriz");
        var descripcion = new Descripcion("Nueva Directriz");
        var fecha = new Fecha(LocalDateTime.now(),LocalDate.now());
        var event2 = new MentoriaCreada(mentoriId,nombre,fecha);
        var event = new CursoCreado(nombre,descripcion);
        event.setAggregateRootId(cursoId.value());
        return List.of(event,event2);
    }
}


