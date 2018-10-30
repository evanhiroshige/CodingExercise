import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class FindDuplicates {

  private List<String[]> users;
  private int[][] distances;

  public FindDuplicates() {

    Scanner scanner;
    try {
      scanner = new Scanner(new File("normal.csv"));
    } catch (
        FileNotFoundException e) {
      throw new IllegalArgumentException("could not find file.");
    }

    scanner.useDelimiter("\n");
    scanner.next();
    int count = 0;
    while (scanner.hasNext()) {
      count++;
      String[] values = scanner.next().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      users.add(values);
    }

    distances = new int[count][count];
    scanner.close();
  }
}
