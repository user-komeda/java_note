package java_memo_note;

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
import javafx.stage.Stage;

public class Main extends Application {


  @Override
  public void start(Stage stage) throws Exception {
    final TextArea textArea = new TextArea();
    final MenuBar menuBar = new MenuBar();
    createFileMenu(menuBar, textArea);
    createEditMenu(menuBar);
    BorderPane pane = new BorderPane();
    pane.setCenter(textArea);
    pane.setTop(menuBar);
    Scene scene = new Scene(pane, 500, 500);
    stage.setScene(scene);
    stage.show();
  }

  /*編集メニューを追加*/
  private void createEditMenu(final MenuBar menuBar) {
    final Menu editMenu = new Menu("編集");
    final MenuItem restoreItemMenu = new MenuItem("元に戻す");
    final MenuItem cutItemMenu = new MenuItem("切り取り");
    final MenuItem copyItemMenu = new MenuItem("コピー");
    final MenuItem pasteItemMenu = new MenuItem("貼り付け");
    final MenuItem deleteItemMenu = new MenuItem("削除");
    editMenu
        .getItems()
        .addAll(restoreItemMenu, cutItemMenu, copyItemMenu, pasteItemMenu, deleteItemMenu);
    menuBar.getMenus().add(editMenu);
  }

  /*ファイルメニューを追加*/
  private void createFileMenu(final MenuBar menuBar, final TextArea textArea) {
    final Menu fileMenu = new Menu("ファイル");
    final MenuItem newItemMenu = new MenuItem("新規");
    final MenuItem newWindowItemMenu = new MenuItem("新規ウインドウ");
    final MenuItem openItemMenu = new MenuItem("開く");
    final MenuItem saveAgainItemMenu = new MenuItem("上書き保存");
    final MenuItem saveItemMenu = new MenuItem("名前を付けて保存");
    final MenuItem finishItemMenu = new MenuItem("終了");
    fileMenu
        .getItems()
        .addAll(
            newItemMenu,
            newWindowItemMenu,
            openItemMenu,
            saveAgainItemMenu,
            saveItemMenu,
            finishItemMenu);
    menuBar.getMenus().add(fileMenu);
    newItemMenu.setOnAction(event -> createNewFile(textArea));
  }

  private void createNewFile(final TextArea textArea) {
    textArea.clear();
  }
}
