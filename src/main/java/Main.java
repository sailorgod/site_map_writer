import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {

        System.out.println("Введите ссылку главную страницу сайта");
        String link = new Scanner(System.in).nextLine();
        System.out.println("Составляю карту данного сайта.");
        String result = new ForkJoinPool().invoke(new TaskParsingLinks(link));
        System.out.println(result + "\n\n");
        try {
            PrintWriter writer = new PrintWriter("src/data/site_map.txt");
            writer.write(result);
            writer.flush();
            writer.close();
            System.out.println("Результат записан в файл site_map.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
