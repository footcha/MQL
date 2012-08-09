/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
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