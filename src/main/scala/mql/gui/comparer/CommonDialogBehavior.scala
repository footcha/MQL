package mql.gui.comparer

import swing.{RichWindow, Dialog}
import swing.event.WindowActivated

trait CommonDialogBehavior extends Dialog {
  modal = true
  resizable = false
  reactions += {
    case WindowActivated(win) => setLocationRelativeTo(owner)
  }
}