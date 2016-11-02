package controller

import model.ReadFile

import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.stage.FileChooser
import scalafxml.core.macros.sfxml

/**
  * ECValidTool.fxmlのControllerクラス.
  * Created by T_Kikuyama on 2016/10/19.
  */
@sfxml
class ValidUserInfoController(private val readFileButton: Button) {
    val chooser = new FileChooser()
    chooser.setTitle("ファイル選択")

    readFileButton.onMouseClicked = (e: MouseEvent) => {
        readFile()
    }

    def readFile() = {
        val importFile = chooser.showOpenDialog(null)
        if (importFile != null) {
            val readFile = new ReadFile()
            readFile.operateFile(importFile.getPath)
        }
    }
}
