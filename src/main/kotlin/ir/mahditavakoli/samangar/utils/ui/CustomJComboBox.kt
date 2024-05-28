package ir.mahditavakoli.samangar.utils.ui

import com.jetbrains.rd.swing.awtKeyEvent
import com.jetbrains.rd.util.first
import org.apache.commons.lang3.tuple.MutablePair
import org.jdesktop.swingx.HorizontalLayout
import org.jdesktop.swingx.VerticalLayout
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JButton

class CustomJComboBox(
    title: String,
    items: Map<String, String>,
    onItemSelected: (String, String) -> Unit
) : JPanel() {

    private val itemMap: Map<String, String>
    private val comboBox: JComboBox<String>
    private val label: JLabel
    private val btn_submit: JButton
    private var selectedItem = MutablePair<String, String>()

    init {

        selectedItem.setRight(items.first().key)
        selectedItem.setLeft(items.first().value)

        layout = HorizontalLayout(10)

        btn_submit = JButton("ثبت")
        // Initialize the map and combo box
        itemMap = items
        comboBox = JComboBox<String>()
        for (key in itemMap.keys) {
            comboBox.addItem(key)
        }

        // Add action listener to the combo box
        comboBox.addActionListener {
            val selectedKey = comboBox.selectedItem as? String ?: return@addActionListener
            val value = itemMap[selectedKey] ?: return@addActionListener

            selectedItem.setRight(selectedKey)
            selectedItem.setLeft(value)

        }

        btn_submit.addActionListener {
            onItemSelected(selectedItem.right, selectedItem.left)
        }


        // Create label for the title
        label = JLabel(title)
        add(btn_submit)

        // Add combo box to the panel
        add(comboBox)
        add(label)
    }
}