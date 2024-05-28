package ir.mahditavakoli.samangar.utils.ui

import com.example.common.ResultWrapper
import ir.mahditavakoli.samangar.utils.ApiLogic
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.swing.*
import java.awt.*

class SamangarLoginDialog(val onLoginClicked: (String?) -> Unit) : JDialog() {
    private val textField1 = JTextField("mahdi.tavakoli")
    private val textField2 = JTextField("0890518335Mahdi@")
    private val callFunctionButton = JButton("Call Function")

    init {
        title = "Login"
        layout = GridLayout(3, 2)

        val label1 = JLabel("Username")
        val label2 = JLabel("Password")

        add(label1)
        add(textField1)
        add(label2)
        add(textField2)
        add(JLabel())  // Empty cell
        add(callFunctionButton)

        callFunctionButton.addActionListener {
            callFunction()
        }

        defaultCloseOperation = DISPOSE_ON_CLOSE
        setSize(300, 150)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun callFunction(): String? {
        val text1 = textField1.text
        val text2 = textField2.text
        var token: String? = null

        GlobalScope
            .launch(Dispatchers.IO) {
                when (val response =
                    ApiLogic().login(
                        username = text1,
                        password = text2
                    )
                ) {
                    is ResultWrapper.Success -> {
                        response.resultData.let {
                            JOptionPane.showMessageDialog(null, "لاگین موفق")
                            onLoginClicked(it.data?.token)
                            token = it.data?.token

                        }
                    }

                    is ResultWrapper.Failure -> {
                        JOptionPane.showMessageDialog(null, "لاگین موفق نبود.")

                    }
                }
            }
        return token
    }
}
