import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}

/**
  * 画面起動クラス.
  * Created by T_Kikuyama on 2016/10/19.
  */
object ValidUserInfo extends JFXApp {

    val resource = getClass.getResource("view/ValidUserInfo.fxml")
    val root = FXMLView(resource, NoDependencyResolver)

    stage = new JFXApp.PrimaryStage() {
        title = "不備データチェック"
        scene = new Scene(root)
    }
}
