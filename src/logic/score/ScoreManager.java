package logic.score;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class ScoreManager {

  private String fileName = "scores.txt";
  private String fileFolderPath = "./";
  private File fileFolder;
  private File file;
  private String name;
  private String time;

  public ScoreManager() {
    this.fileFolder = new File(fileFolderPath);
    this.file = new File(fileFolder, fileName);
  }

  private Score scoreFromString(String string) {
    // ler do arquivo

    var split = string.split(":::::");
    if (split.length != 2) {
      return null;
    }

    String name = split[0];
    String scoreString = split[1];

    try {
      var scoreNumber = Integer.parseInt(scoreString);
      return new Score(name, scoreNumber);
    } catch (NumberFormatException ex) {
    }
    return null;
  }

  private String scoreToString(Score score) {
    // Salvar no arquivo

    return score.getName() + ":::::" + score.getTime();
  }

  private List<Score> readAllScores() {
    List<Score> scores = new ArrayList<>();
    if (!this.file.exists()) {
      return scores;
    }
    try (FileReader fr = new FileReader(this.file); BufferedReader br = new BufferedReader(fr)) {

      String line;
      while ((line = br.readLine()) != null) {
        Score score = scoreFromString(line);
        if (score != null) {
          scores.add(score);
        }

      }
    } catch (IOException ex) {
      return scores;
    }

    return scores;
  }

  public List<Score> topScore(int tamanho) {

    var scores = this.readAllScores();

    scores.sort((s1, s2) -> s1.getTime() - s2.getTime());
    // top 15 em ordem crescente

    if (scores.size() < tamanho) {
      return scores;
    }
    return scores.subList(0, tamanho);
  }

  public void savePlayer(Score score) {
    try {

      if (!this.file.exists()) {

        this.fileFolder.mkdirs();
        this.file.createNewFile();
      }
      try (FileWriter fw = new FileWriter(this.file,true); BufferedWriter bw = new BufferedWriter(fw);) {
      bw.write(scoreToString(score) + '\n');
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}