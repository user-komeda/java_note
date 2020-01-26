package java_memo_note;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
  Path filePath;

  @Override
  public void start(final Stage stage) throws Exception {
    final TextArea textArea = new TextArea();
    final MenuBar menuBar = new MenuBar();
    createFileMenu(menuBar, textArea, stage);
    createEditMenu(menuBar);
    final BorderPane pane = new BorderPane();
    pane.setCenter(textArea);
    pane.setTop(menuBar);
    final Scene scene = new Scene(pane, 500, 500);
    stage.setScene(scene);
    stage.setTitle("無題");
    stage.show();
  }

  /* 編集メニューを追加 */
  private void createEditMenu(final MenuBar menuBar) {
    final Menu editMenu = new Menu("編集");
    final MenuItem restoreItemMenu = new MenuItem("元に戻す");
    final MenuItem cutItemMenu = new MenuItem("切り取り");
    final MenuItem copyItemMenu = new MenuItem("コピー");
    final MenuItem pasteItemMenu = new MenuItem("貼り付け");
    final MenuItem deleteItemMenu = new MenuItem("削除");
    editMenu.getItems().addAll(restoreItemMenu, cutItemMenu, copyItemMenu, pasteItemMenu,
        deleteItemMenu);
    menuBar.getMenus().add(editMenu);
  }

  /* ファイルメニューを追加 */
  private void createFileMenu(final MenuBar menuBar, final TextArea textArea, Stage stage) {
    final Menu fileMenu = new Menu("ファイル");
    final MenuItem newItemMenu = new MenuItem("新規");
    final MenuItem newWindowItemMenu = new MenuItem("新規ウインドウ");
    final MenuItem openItemMenu = new MenuItem("開く");
    final MenuItem saveAgainItemMenu = new MenuItem("上書き保存");
    final MenuItem saveItemMenu = new MenuItem("名前を付けて保存");
    final MenuItem finishItemMenu = new MenuItem("終了");
    fileMenu.getItems().addAll(newItemMenu, newWindowItemMenu, openItemMenu, saveAgainItemMenu,
        saveItemMenu, finishItemMenu);
    menuBar.getMenus().add(fileMenu);
    newItemMenu.setOnAction(event -> createNewFile(textArea));
    saveItemMenu.setOnAction(event -> save(textArea, stage));
    saveAgainItemMenu.setOnAction(event -> overrideSave(textArea));
  }

  private void createNewFile(final TextArea textArea) {
    textArea.clear();
  }

  private void save(final TextArea textArea, Stage stage) {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("名前を付けて保存");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("すべてのテキストファイル", "*.txt"));
    final File file = fileChooser.showSaveDialog(null);
    if (file != null) {
      try {
        filePath = file.toPath();
        final BufferedWriter bufferedWriter =
            Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
        stage.setTitle(file.getName());
        bufferedWriter.append(textArea.getText());
        bufferedWriter.close();
      } catch (final IOException e) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.error("error", e);
      }
    }
  }

  private void overrideSave(TextArea textArea) {
    if (filePath != null) {
      try {
        final BufferedWriter bufferedWriter =
            Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
        bufferedWriter.append(textArea.getText());
        bufferedWriter.close();
      } catch (final IOException e) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.error("error", e);
      }
    } else {
      System.out.println("error");
    }
  }
}
