Public Class ThisAddIn

    Dim btNewTask As Office.CommandBarButton
    Dim btNewNote As Office.CommandBarButton
    Dim btSettings As Office.CommandBarButton

    Private Sub ThisAddIn_Startup() Handles Me.Startup

    End Sub

    Private Sub ThisAddIn_Shutdown() Handles Me.Shutdown

    End Sub

    Private Sub Application_ItemContextMenuDisplay(ByVal CommandBar As Microsoft.Office.Core.CommandBar, ByVal Selection As Microsoft.Office.Interop.Outlook.Selection) Handles Application.ItemContextMenuDisplay
        Dim cmdTaskUnifierPopup As Office.CommandBarPopup

        Try
            Dim i As Integer
            Dim item As OutlookItem
            For i = 1 To Selection.Count
                item = New OutlookItem(Selection(i))
                If item.Class <> Outlook.OlObjectClass.olMail Then Exit Sub
            Next

            cmdTaskUnifierPopup = CommandBar.FindControl(Tag:="TaskUnifierVB.TaskUnifierPopup")
            If cmdTaskUnifierPopup Is Nothing Then
                cmdTaskUnifierPopup = CommandBar.Controls.Add(Office.MsoControlType.msoControlPopup, , "TaskUnifierVB.TaskUnifierPopup")
                cmdTaskUnifierPopup.Caption = "TaskUnifier"
                cmdTaskUnifierPopup.Tag = "TaskUnifierVB.TaskUnifierPopup"
                cmdTaskUnifierPopup.BeginGroup = True

                btNewTask = cmdTaskUnifierPopup.CommandBar.Controls.Add(Office.MsoControlType.msoControlButton)
                btNewTask.Caption = "Create new task(s)"
                AddHandler btNewTask.Click, AddressOf btNewTask_Click

                btNewNote = cmdTaskUnifierPopup.CommandBar.Controls.Add(Office.MsoControlType.msoControlButton)
                btNewNote.Caption = "Create new note(s)"
                AddHandler btNewNote.Click, AddressOf btNewNote_Click

                btSettings = cmdTaskUnifierPopup.CommandBar.Controls.Add(Office.MsoControlType.msoControlButton)
                btSettings.Caption = "Settings"
                AddHandler btSettings.Click, AddressOf btSettings_Click
            End If
        Catch ex As Exception

        End Try
    End Sub

    Private Sub btSettings_Click(ByVal ctrl As Microsoft.Office.Core.CommandBarButton, ByRef CancelDefault As Boolean)
        Dim wfSettings As Settings = New Settings
        wfSettings.Show()
    End Sub

    Private Sub btNewTask_Click(ByVal ctrl As Microsoft.Office.Core.CommandBarButton, ByRef CancelDefault As Boolean)
        Dim mails As List(Of Outlook.MailItem) = New List(Of Outlook.MailItem)
        Dim i As Integer
        For i = 1 To Application.ActiveExplorer.Selection.Count
            mails.Add(Application.ActiveExplorer.Selection(i))
        Next

        Dim tcpThread As New TcpThread(mailsToTasksXml(mails.ToArray))
        Dim myThread = New System.Threading.Thread(AddressOf tcpThread.run)
        myThread.Start()
    End Sub

    Private Sub btNewNote_Click(ByVal ctrl As Microsoft.Office.Core.CommandBarButton, ByRef CancelDefault As Boolean)
        Dim mails As List(Of Outlook.MailItem) = New List(Of Outlook.MailItem)
        Dim i As Integer
        For i = 1 To Application.ActiveExplorer.Selection.Count
            mails.Add(Application.ActiveExplorer.Selection(i))
        Next

        Dim tcpThread As New TcpThread(mailsToNotesXml(mails.ToArray))
        Dim myThread = New System.Threading.Thread(AddressOf tcpThread.run)
        myThread.Start()
    End Sub

    Private Class TcpThread

        Private _message As String

        Public Sub New(ByVal message As String)
            _message = message
        End Sub

        Public Sub run()
            If Not sendTcpMessage() Then
                Windows.Forms.MessageBox.Show("Cannot communicate with TaskUnifier." & vbNewLine & "Please check that TaskUnifier is started.", "Error")
            End If
        End Sub

        Private Function sendTcpMessage() As Boolean
            Dim tcpClient As Net.Sockets.TcpClient = New Net.Sockets.TcpClient

            Try
                tcpClient.Connect(My.Settings.SocketAddress, My.Settings.SocketPort)
            Catch ex As Exception
                Return False
            End Try

            Try
                Dim bytes As [Byte]() = Encoding.UTF8.GetBytes(_message & vbNewLine)
                tcpClient.GetStream.Write(bytes, 0, bytes.Length)
            Catch ex As Exception
                tcpClient.Close()
                Return False
            End Try

            tcpClient.Close()
            Return True
        End Function

    End Class

    Private Function mailsToTasksXml(ByVal mails() As Outlook.MailItem) As String
        Dim mailXml As XDocument = New XDocument(New XDeclaration("1.0", "UTF-8", True))
        Dim comXml As XElement = New XElement("com")
        comXml.Add(New XElement("applicationname", "Outlook"))
        mailXml.Add(comXml)
        Dim tasksXml As XElement = New XElement("tasks")
        comXml.Add(tasksXml)

        For Each mail As Outlook.MailItem In mails
            Dim taskXml As XElement = New XElement("task")
            tasksXml.Add(taskXml)

            If My.Settings.TaskTitle Then
                taskXml.Add(New XElement("title", mail.Subject))
            End If

            If My.Settings.TaskCategories = 0 Then
                Dim categories As String = mail.Categories
                If Not categories Is Nothing Then
                    If categories.Contains(";") Then
                        categories = categories.Substring(0, categories.IndexOf(";"))
                    End If

                    taskXml.Add(New XElement("contexttitle", categories))
                End If
            End If

            If My.Settings.TaskCategories = 1 Then
                Dim categories As String = mail.Categories
                If Not categories Is Nothing Then
                    If categories.Contains(";") Then
                        categories = categories.Substring(0, categories.IndexOf(";"))
                    End If

                    taskXml.Add(New XElement("foldertitle", categories))
                End If
            End If

            If My.Settings.TaskCategories = 2 Then
                Dim categories As String = mail.Categories
                If Not categories Is Nothing Then
                    If categories.Contains(";") Then
                        categories = categories.Substring(0, categories.IndexOf(";"))
                    End If

                    taskXml.Add(New XElement("tags", categories))
                End If
            End If

            If My.Settings.TaskCategories = 3 Then
                Dim contexts() As String
                Dim categories As String = mail.Categories
                If Not categories Is Nothing Then
                    contexts = categories.Split(";")

                    For Each context As String In contexts
                        taskXml.Add(New XElement("contexttitle", context.Trim()))
                    Next
                End If
            End If

            If My.Settings.TaskCategories = 4 Then
                Dim categories As String = mail.Categories
                If Not categories Is Nothing Then
                    categories = categories.Replace(",", " ").Replace(";", ",")

                    taskXml.Add(New XElement("tags", categories))
                End If
            End If

            If My.Settings.TaskStartDate Then
                taskXml.Add(New XElement("startdate", dateToTimestamp(mail.TaskStartDate.ToUniversalTime) * 1000))
            End If

            If My.Settings.TaskDueDate Then
                taskXml.Add(New XElement("duedate", dateToTimestamp(mail.TaskDueDate.ToUniversalTime) * 1000))
            End If

            If My.Settings.TaskNote Then
                taskXml.Add(New XElement("note", mail.Body))
            End If
        Next

        Return mailXml.ToString
    End Function

    Private Function mailsToNotesXml(ByVal mails() As Outlook.MailItem) As String
        Dim mailXml As XDocument = New XDocument(New XDeclaration("1.0", "UTF-8", True))
        Dim comXml As XElement = New XElement("com")
        comXml.Add(New XElement("applicationname", "Outlook"))
        mailXml.Add(comXml)
        Dim notesXml As XElement = New XElement("notes")
        comXml.Add(notesXml)

        For Each mail As Outlook.MailItem In mails
            Dim noteXml As XElement = New XElement("note")
            notesXml.Add(noteXml)

            If My.Settings.NoteTitle Then
                noteXml.Add(New XElement("title", mail.Subject))
            End If

            If My.Settings.NoteFolder Then
                Dim folder As String = mail.Categories
                If Not folder Is Nothing Then
                    If folder.Contains(";") Then
                        folder = folder.Substring(0, folder.IndexOf(";"))
                    End If
                End If

                noteXml.Add(New XElement("foldertitle", folder))
            End If

            If My.Settings.NoteNote Then
                noteXml.Add(New XElement("note", mail.Body))
            End If
        Next

        Return mailXml.ToString
    End Function

    Public Function dateToTimestamp(ByVal dt As Date) As Long
        Dim origin As New Date(1970, 1, 1)
        Dim span As TimeSpan = dt - origin
        Dim seconds As Double = span.TotalSeconds
        Return CType(seconds, Long)
    End Function

End Class
