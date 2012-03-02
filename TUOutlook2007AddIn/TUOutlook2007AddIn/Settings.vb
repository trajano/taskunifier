Public Class Settings

    Public Sub New()
        InitializeComponent()
        cbCategories.SelectedIndex = My.Settings.TaskCategories
    End Sub

    Private Sub btOk_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btOk.Click
        My.Settings.TaskCategories = cbCategories.SelectedIndex
        My.Settings.Save()
        Me.Close()
    End Sub

    Private Sub btCancel_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btCancel.Click
        My.Settings.Reset()
        Me.Close()
    End Sub

End Class