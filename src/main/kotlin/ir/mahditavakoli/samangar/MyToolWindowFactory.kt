package ir.mahditavakoli.samangar

import com.example.common.ResultWrapper
import com.example.tasks.model.remote.ReportRequest
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ui.JBUI
import ir.mahditavakoli.samangar.task.model.*
import ir.mahditavakoli.samangar.utils.ApiLogic
import ir.mahditavakoli.samangar.utils.getGitBranchName
import ir.mahditavakoli.samangar.utils.getHeadBranchName
import ir.mahditavakoli.samangar.utils.getPersianCurrentDateYMD
import ir.mahditavakoli.samangar.utils.ui.CustomJComboBox
import ir.mahditavakoli.samangar.utils.ui.PlaceholderTextField
import ir.mahditavakoli.samangar.utils.ui.SamangarLoginDialog
import kotlinx.coroutines.*
import org.jdesktop.swingx.VerticalLayout
import java.awt.Dimension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.*

class Samangar : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow()
        val contentFactory = ApplicationManager.getApplication().getService(ContentFactory::class.java)
        val content: Content = contentFactory.createContent(myToolWindow.content, "mahdi", true)
        toolWindow.contentManager.addContent(content)
    }
}

@OptIn(DelicateCoroutinesApi::class)
class MyToolWindow {

    val content: JPanel
    private val tvTaskName: PlaceholderTextField
    private val button1: JButton
    private val button2: JButton
    private val btnRegisterTask: JButton
    private val btnLogin: JButton

    private val startTime: JLabel // Label to display the current time
    private val endTime: JLabel // Label to display the current time

    private var startTimeStr = ""
    private var endTimeStr = ""

    private var token: String? = null

    val globalProjects = mutableListOf<ReportProjectEntity>()
    val globalTagsResponse = mutableListOf<ReportTagEntity>()

    private var selectedProject = ""
    private var selectedTag = ""

    init {

        val logoPanel = JLabel("SAMANGAR").apply {
            alignmentX = JPanel.CENTER_ALIGNMENT
            alignmentY = JPanel.CENTER_ALIGNMENT
        }

        tvTaskName = PlaceholderTextField("نام تسک را وارد کنید").apply {
            preferredSize = Dimension(100, 30) // Set the preferred size (width, height)
            size = Dimension(100, 30)
            alignmentX = JPanel.CENTER_ALIGNMENT
            alignmentY = JPanel.CENTER_ALIGNMENT
        }

        GlobalScope.launch(Dispatchers.IO){
            println("IO OUTPUT ->")
            val result = getHeadBranchName()
            println("result is : $result")
            tvTaskName.text = result
            println("<- IO OUTPUT")
        }

        // Create a sub-panel with X_AXIS layout
        val taskNamePanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(tvTaskName)

            preferredSize = Dimension(500, 50) // Set the preferred size (width, height)
            minimumSize = Dimension(400, 50) // Set the minimum size
            maximumSize = Dimension(700, 50) // Set the maximum size
        }


        content = JPanel().apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(java.awt.Color.BLACK), // Outer border
                JBUI.Borders.empty(10) // Inner padding
            )

            alignmentX = JPanel.CENTER_ALIGNMENT
        }

        button1 = JButton("شروع")
        button2 = JButton("پایان")
        btnRegisterTask = JButton("ثبت تسک").apply {
            alignmentX = JPanel.CENTER_ALIGNMENT
            alignmentY = JPanel.CENTER_ALIGNMENT
        }

        btnLogin = JButton("ورود به سامانگر").apply {
            alignmentX = JPanel.CENTER_ALIGNMENT
            alignmentY = JPanel.CENTER_ALIGNMENT
        }

        startTime = JLabel("شروع : ").apply {
            alignmentX = JPanel.CENTER_ALIGNMENT
            alignmentY = JPanel.CENTER_ALIGNMENT
        }
        endTime = JLabel("پایان : ").apply {
            alignmentX = JPanel.CENTER_ALIGNMENT
            alignmentY = JPanel.CENTER_ALIGNMENT
        }

        content.layout = VerticalLayout(10).apply {

        }

        val buttonsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(button1)
            add(button2)
        }



        content.add(logoPanel)
        content.add(btnLogin)
        content.add(taskNamePanel)
        content.add(buttonsPanel)
        content.add(startTime)
        content.add(endTime)

        button1.addActionListener { callApi1() }
        button2.addActionListener { callApi2() }
        btnLogin.addActionListener {
            val dialog = SamangarLoginDialog { token ->
                this.token = token
                getAllProjectsOfUserViaTags()

            }
            dialog.pack()
            dialog.isVisible = true
        }

        btnRegisterTask.addActionListener {
            registerTask()
        }
    }

    private fun callApi1() {

        // Capture the current time
        val currentTime = LocalDateTime.now()
        val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        startTimeStr = formattedTime
        // Set the current time to the label
        startTime.text = "${tvTaskName.text} شروع شد در  :  $formattedTime"

        // Implementation for API call 1
        JOptionPane.showMessageDialog(null, "${tvTaskName.text} started on :  $formattedTime ")
    }

    private fun callApi2() {
        // Capture the current time
        val currentTime = LocalDateTime.now()
        val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        endTimeStr = formattedTime
        // Set the current time to the label
        endTime.text = "${tvTaskName.text} زمان پایان در  :  $formattedTime"

        // Implementation for API call 2
        JOptionPane.showMessageDialog(null, tvTaskName.text)
    }

    private fun registerTask() {
        val task = ReportRequest(
            date = getPersianCurrentDateYMD(),
            description = tvTaskName.text,
            project = selectedProject,
            tag = selectedTag,
            start = startTimeStr,
            end = endTimeStr,
            issue = null
        )

        token?.let {
            GlobalScope
                .launch(Dispatchers.IO) {
                    when (val response =
                        ApiLogic().createTask(
                            token = it,
                            reportRequest = task
                        )
                    ) {
                        is ResultWrapper.Success -> {
                            response.resultData.let {
                                JOptionPane.showMessageDialog(null, "ثبت تسک موفق بود.")
                            }
                        }

                        is ResultWrapper.Failure -> {

                            JOptionPane.showMessageDialog(null, "ثبت تسک موفق نبود.")
                            JOptionPane.showMessageDialog(null, "${response.code}:${response.message}")

                        }
                    }
                }
        }
    }

    private fun getAllProjectsOfUserViaTags() {
        token?.let {
            GlobalScope
                .launch(Dispatchers.IO) {
                    when (val response =
                        ApiLogic().getAllProjectsOfUserViaTags(
                            token = it
                        )
                    ) {
                        is ResultWrapper.Success -> {

                            response.resultData.data.let { reportDataResponses ->
                                reportDataResponses.map { reportDataResponse ->
                                    reportDataResponse.toReportProjectEntity()?.let { reportProjectEntity ->
                                        globalProjects.add(reportProjectEntity)
                                    }
                                    val projectId = reportDataResponse.id
                                    val tagsResponse =
                                        reportDataResponse.tags.mapNotNull { reportTagsResponse ->
                                            reportTagsResponse.toReportTagEntity(
                                                projectId = projectId ?: ""
                                            )
                                        }
                                    globalTagsResponse.addAll(tagsResponse)
                                }
                                ResultWrapper.Success(resultData = Unit)
                            }


                            val projectPanel = JPanel().also { panel ->
                                val projectItems = globalProjects.associate {
                                    (it.project to it.id)
                                }


                                // Create a CustomJComboBox with the items and an action listener
                                val projectBox = CustomJComboBox("پروژه : ", projectItems) { key, value ->
                                    println("Selected item: Key = $key, Value = $value")
                                    selectedProject = value
                                    println("Selected project $selectedProject")

                                    // Create a map of items for the JComboBox
                                    val tagItems = globalTagsResponse.filter {
                                        it.projectId == selectedProject
                                    }.associate {
                                        (it.name to it.id)
                                    }

                                    // Create a CustomJComboBox with the items and an action listener
                                    val tagBox = CustomJComboBox("تگ : ", tagItems) { key, value ->
                                        println("Selected item: Key = $key, Value = $value")
                                        selectedTag = value
                                        println("Selected tag $selectedTag")
                                    }
                                    panel.add(tagBox)
                                }
                                panel.layout = VerticalLayout(10)
                                // Add the CustomJComboBox to the JPanel
                                panel.add(projectBox)
                            }

                            content.add(projectPanel)
                            content.add(btnRegisterTask)
                        }

                        is ResultWrapper.Failure -> {

                            JOptionPane.showMessageDialog(null, "ثبت تسک موفق نبود.")
                            JOptionPane.showMessageDialog(null, response.message)

                        }
                    }
                }
        }
    }
}
