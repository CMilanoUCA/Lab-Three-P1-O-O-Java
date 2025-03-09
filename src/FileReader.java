import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
  public static Object[][] readNobelWinnerData(String fileName) throws IOException {
      String contents = Files.readString(Path.of("nobel_prizes.csv"), StandardCharsets.UTF_8);

      List<String> lines = List.of(contents.split("\n"));
      ArrayList<Object[]> nobelWinnerDataStr = new ArrayList<>();
      nobelWinnerDataStr = lines.stream()
              .skip(1)
              .map(line -> line.split(","))
              .map(Parser::parseNobelWinner)
              .map(NobelPrizeAward::toObjectArray)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

      return nobelWinnerDataStr.toArray(new Object[0][0]);
  }
}
