package ir.mahditavakoli.samangar.utils.ui

import javax.swing.JPanel

fun JPanel.reDrawCustomJComboBox(item: CustomJComboBox) {
    this.remove(item)
    this.add(item)
}