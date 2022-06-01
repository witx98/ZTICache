package witkowski.mateusz.bookseat.initialization.web;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import witkowski.mateusz.bookseat.initialization.application.port.InitializationUseCase;

@RestController
@AllArgsConstructor
@RequestMapping("/init")
public class InitializationController {

    private final InitializationUseCase initializationService;

    @PostMapping
    public void initialize() {
        initializationService.initialize();
    }
}
