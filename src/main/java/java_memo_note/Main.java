package java_memo_note;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.WindowEvent;
import org.checkerframework.checker.units.qual.s;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

  Path filePath;
  Boolean flag;
  Optional<ButtonType> result;
  Boolean closeFlag;
  String noteTitle;

  @Override
  public void start(final Stage stage) throws Exception {
    noteTitle = "無題";
    final TextArea textArea = new TextArea();
    final MenuBar menuBar = new MenuBar();
    createFileMenu(menuBar, textArea, stage);
    createEditMenu(menuBar);
    final BorderPane pane = new BorderPane();
    pane.setCenter(textArea);
    pane.setTop(menuBar);
    final Scene scene = new Scene(pane, 500, 500);
    stage.setScene(scene);
    stage.setTitle(noteTitle);
    stage.show();
    flag = checkChange(textArea);
    stage.setOnCloseRequest((WindowEvent event) -> {
      closeNote(textArea, stage);
      if (closeFlag == false) {
        event.consume();
      }
    });
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
  private void createFileMenu(final MenuBar menuBar, final TextArea textArea, final Stage stage) {
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
    newItemMenu.setOnAction(event -> createNewFile(textArea, stage));
    saveItemMenu.setOnAction(event -> save(textArea, stage));
    saveAgainItemMenu.setOnAction(event -> overrideSave(textArea));
    finishItemMenu.setOnAction(event -> closeNote(textArea, stage));
  }

  private void createNewFile(final TextArea textArea, Stage stage) {
    final Optional<ButtonType> result = checkSave(textArea);
    if (result != null && result.isPresent()) {
      if (result.get().getButtonData() == ButtonData.YES) {
        save(textArea, stage);
        textArea.clear();
        noteTitle = "無題";
        stage.setTitle(noteTitle);
      } else if (result.get().getButtonData() == ButtonData.NO) {
        textArea.clear();
        noteTitle = "無題";
        stage.setTitle(noteTitle);
      }
    }
  }

  private void save(final TextArea textArea, final Stage stage) {
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
        noteTitle = file.getName();
        stage.setTitle(noteTitle);
        bufferedWriter.append(textArea.getText());
        bufferedWriter.close();
      } catch (final IOException e) {
        System.out.println(e.toString());
        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.error("error", e);
      }
    } else {
      closeFlag = false;
    }
  }

  private void overrideSave(final TextArea textArea) {
    if (filePath != null) {
      try {
        final BufferedWriter bufferedWriter =
            Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
        bufferedWriter.append(textArea.getText());
        bufferedWriter.close();
      } catch (final IOException e) {
        System.out.println(e.toString());
        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.error("error", e);
      }
    } else {
      System.out.println("error");
    }
  }

  private void closeNote(final TextArea textArea, Stage stage) {
    final Optional<ButtonType> result = checkSave(textArea);
    if (result != null && result.isPresent()) {
      if (result.get().getButtonData() == ButtonData.YES) {
        closeFlag = true;
        save(textArea, stage);
        if (closeFlag == true) {
          stage.close();
        }
      } else if (result.get().getButtonData() == ButtonData.NO) {
        closeFlag = true;
        stage.close();
      } else {
        closeFlag = false;
      }
    } else {
      closeFlag = true;
      stage.close();
    }
  }

  private Optional<ButtonType> checkSave(final TextArea textArea) {
    // final Boolean flag = checkChange(textArea);
    if (flag == true) {
      final Alert alert = new Alert(AlertType.NONE);
      final ButtonType buttonTypeSave = new ButtonType("保存する", ButtonData.YES);
      final ButtonType buttonTypeNoSave = new ButtonType("保存しない", ButtonData.NO);
      final ButtonType buttonTypeCancel = new ButtonType("キャンセル", ButtonData.CANCEL_CLOSE);
      alert.setContentText("ファイルを保存しますか");
      alert.setTitle("メモ帳");
      alert.getButtonTypes().addAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel);
      result = alert.showAndWait();
    }
    return result;
  }

  private Boolean checkChange(final TextArea textArea) {
    flag = false;
    textArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(final ObservableValue<? extends String> observableValue, final String s,
          final String t1) {
        textArea.textProperty().removeListener(this);
        flag = true;
      }
    });
    return flag;
  }
}
