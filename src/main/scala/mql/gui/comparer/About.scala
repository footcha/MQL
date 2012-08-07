package mql.gui.comparer

import swing._
import mql.Version
import io.Source
import java.io.{FilterInputStream, BufferedInputStream}
import java.awt.{Color, Insets}
import javax.swing.ImageIcon
import swing.GridBagPanel.Fill
import javax.swing.border.EmptyBorder
import java.text.SimpleDateFormat

object About extends Dialog with CommonDialogBehavior {
  override def owner = ComparerApplication.top

  title = "About"
  maximumSize.height = 200
  preferredSize.height = 300
  contents_=(new GridBagPanel {
    background = Color.WHITE
    val texts = Map(
      "Author" -> "Petr Kozelek <petr.kozelek@gmail.com>",
      "Version" -> Version.version,
      "Release Date" -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Version.releasedAt)
    )

    val label = new Label()
    val lic = Source.fromInputStream(Utilities.resource("LICENSE").getContent.asInstanceOf[FilterInputStream]).mkString
    val c = new Constraints()
    c.fill = Fill.Both
    c.anchor = GridBagPanel.Anchor.West
    c.gridheight = 4
    c.insets = new Insets(15, 15, 5, 5)
    add(new Label("") {
      icon = new ImageIcon(toolkit.getImage(Utilities.resource("icons/logo128.png")))
    }, c)
    c.insets.top = 25
    c.insets.left = 5
    c.gridheight = 1
    c.gridwidth = 2
    c.gridy += 1
    c.gridx = 2
    add(new Label("About MQL Comparer") {
      font = font.deriveFont(font.getStyle, font.getSize * 2)
      horizontalAlignment = Alignment.Left
    }, c)
    c.insets.top = 0
    c.insets.bottom = 6
    c.gridwidth = 1
    for (val item <- texts) {
      c.gridy += 1
      c.gridx = 1
      add(new Label(item._1 + ":") {
        horizontalAlignment = Alignment.Left
        verticalAlignment = Alignment.Center
      }, c)
      c.gridx = 2
      add(new TextField(item._2) {
        horizontalAlignment = Alignment.Left
        editable = false
        opaque = false
        border = new EmptyBorder(0, 0, 0, 0)
      }, c)
    }
    c.gridy += 1
    c.gridwidth = 3
    c.gridx = 0
    c.insets.right = -10
    add(new ScrollPane(new TextArea(lic) {
      editable = false
      rows = 15
      font = label.font
      font = new Font("sans-serif", 0, label.font.getSize)
    }), c)
  })

  pack()
}
