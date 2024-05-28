package ir.mahditavakoli.samangar.utils.ui

import java.awt.Color
import java.awt.Graphics
import javax.swing.JTextField

class PlaceholderTextField(private val placeholder: String) : JTextField(1) {

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (text.isEmpty() && !isFocusOwner) {
            val g2 = g.create() as Graphics
            g2.color = Color.GRAY
            g2.drawString(placeholder, insets.right, g.fontMetrics.maxAscent + insets.top)
            g2.dispose()
        }
    }



}
