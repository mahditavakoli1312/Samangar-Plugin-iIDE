package ir.mahditavakoli.samangar.utils

import javax.swing.*
import java.awt.*

// Custom JPanel class to draw an image
class ImagePanel(imagePath: String) : JPanel() {
    private val image: Image

    init {
        // Load the image
        val imageIcon = ImageIcon(imagePath)
        image = imageIcon.image
    }

    // Override paintComponent to draw the image
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        // Draw the image at (0, 0)
        g.drawImage(image, 0, 0, this)
    }

    // Optionally, you can override getPreferredSize for the panel size
    override fun getPreferredSize(): Dimension {
        return if (image == null) {
            Dimension(100, 100) // Default size if no image
        } else {
            Dimension(image.getWidth(this), image.getHeight(this))
        }
    }
}