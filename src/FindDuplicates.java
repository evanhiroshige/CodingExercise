import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.codec.language.Metaphone;

public class FindDuplicates {

  private List<String[]> users;
  private int[][] distances;
  private HashSet<String[]> nonDuplicates;
  private HashMap<String[], List<String[]>> duplicates;

  public FindDuplicates() {
    duplicates = new HashMap<>();
    nonDuplicates = new HashSet<>();
    users = new ArrayList<>();

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
      nonDuplicates.add(values);
    }

    distances = new int[count][count];
    scanner.close();
  }

  // compare fields 1 by 1 with Levenshtein and Metaphone
  private int getDistance(String[] a, String[] b) {
    int sum = 0;
    Metaphone mp = new Metaphone();

    for (int i = 1; i < a.length; i++) {
      String a1 = a[i];
      String b1 = b[i];
      if (i == 4) {
        a1 = a1.substring(0, a1.indexOf('@'));
        b1 = b1.substring(0, b1.indexOf('@'));
      }

      if (mp.isMetaphoneEqual(a1, b1)) {
        continue;
      }
      sum += Levenshtein.distance(a1, b1);
    }
    return sum;
  }

  public String findDups() {
    for (int i = 0; i < distances.length - 1; i++) {
      for (int k = i + 1; k < distances.length - 1; k++) {
        distances[i][k] = getDistance(users.get(i), users.get(k));
        if (distances[i][k] < 40) {
          if (!duplicates.containsKey(users.get(i))) {
            duplicates.put(users.get(i), new ArrayList<>());
            nonDuplicates.remove(users.get(i));
          }
          duplicates.get(users.get(i)).add(users.get(k));
          nonDuplicates.remove(users.get(k));
        }
      }
    }
    return dupsToString();
  }


  private String dupsToString() {
    StringBuilder output = new StringBuilder();
    int count = 1;
    for (String[] s : this.duplicates.keySet()) {
      output.append("Duplicate set ");
      output.append(count++);
      output.append('\n');
      output.append(Arrays.toString(s));
      output.append('\n');

      for (String[] dup : duplicates.get(s)) {
        output.append(Arrays.toString(dup));
        output.append('\n');

      }
      output.append('\n');
    }

    output.append("Non-duplicates\n");
    for (String[] s : nonDuplicates) {
      output.append(Arrays.toString(s));
      output.append('\n');

    }
    return output.toString();
  }


  public static void main(String[] args) {
    FindDuplicates fd = new FindDuplicates();
    System.out.println(fd.findDups());
  }

}
