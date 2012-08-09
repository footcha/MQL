/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.gui.comparer

import swing._
import java.io.File
import collection.mutable.ListBuffer
import swing.Dialog.Result
import swing.GridBagPanel.Fill

class TwoFilesChooser(override val owner: Frame) extends Dialog(owner) with CommonDialogBehavior {
  thisFrame =>
  title = "Compare Files"
  preferredSize = new Dimension(380, 120)
  peer.setIconImage(owner.iconImage)

  private var result = Result.Cancel
  private val okButton = Button("OK") {
    commit()
  }

  defaultButton = okButton

  private val cancelButton = Button("Cancel") {
    thisFrame.visible = false
  }

  private def commit() {
    try {
      validate("First", files._1)
      validate("Second", files._2)
      result = Result.Ok
      thisFrame.visible = false
    } catch {
      case ex: Exception => Dialog.showMessage(
        message = ex.getMessage,
        title = "Error",
        messageType = Dialog.Message.Error
      )
    }
  }

  def validate(errorLabel: String, filePath: String) {
    val file = new File(filePath)
    if (!file.exists()) throw new Exception("File '%s' does not exist." format filePath)
  }

  val components = (fileComponent("First:", 0), fileComponent("Second:", 1))
  contents = new BorderPanel {
    add(new GridBagPanel {
      def addFileComponent(comp: (Label, TextField, Button), row: Int) {
        val c = new this.Constraints()
        c.fill = Fill.Horizontal
        c.gridy = row
        layout(comp._1) = c
        c.gridx = 2
        c.weightx = 1
        layout(comp._2) = c
        c.gridx = 3
        c.weightx = 0
        layout(comp._3) = c
      }

      addFileComponent(components._1, 1)
      addFileComponent(components._2, 2)
    }, BorderPanel.Position.Center)

    add(new Panel {
      _contents += okButton += cancelButton
    }, BorderPanel.Position.South)
  }

  private var currentDir = new ListBuffer() += "." += "."

  def files = (components._1._2.text, components._2._2.text)

  def show(): Result.Value = {
    visible = true
    result
  }

  def fileComponent[T](labelText: String, index: Int) = {
    val filePath = new TextField()
    val chooser = new FileChooser(new File(".")) {
      multiSelectionEnabled = false
      fileSelectionMode = FileChooser.SelectionMode.FilesOnly
    }
    val label = new Label(labelText)
    val button = Button("File...") {
      chooser.selectedFile = new File(currentDir(index))
      chooser.showOpenDialog(thisFrame.contents.head) match {
        case FileChooser.Result.Approve => {
          currentDir(index) = chooser.selectedFile.getAbsolutePath
          filePath.text = chooser.selectedFile.getAbsolutePath
        }
        case _ =>
      }
    }
    (label, filePath, button)
  }
}