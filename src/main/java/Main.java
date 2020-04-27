
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ui.Console;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting the application...");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "config","domain","repository","service","ui"
                );
        context.getBean(Console.class).runConsole();

    }
}
