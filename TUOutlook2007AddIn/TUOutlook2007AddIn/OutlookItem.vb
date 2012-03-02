Option Strict On

Imports System
Imports System.Reflection
Imports System.Runtime.InteropServices
Imports System.Diagnostics
Imports Outlook = Microsoft.Office.Interop.Outlook

#Region "Readme and Usage"

' Helper class to access common properties of Outlook Items.  This class uses
' reflection to call the common members of all Outlook items.  Because of the use 
' of reflection, all calls through this class will be significantly slower than
' executing the same call against a strongly typed object.

' Uses the IDispatch interface of the Outlook object model 
' to access the common properties of
' Outlook items regardless of the actual item type.  
' This prevents having to test for and cast to 
' a specific object in order to access common properties.
'
' Usage example:
' 
'    Dim myItem As OutlookItem = new OutlookItem(olApp.ActiveExplorer.Selection.Item(1))
'    Debug.WriteLine(myItem.EntryID)

#End Region

Friend Class OutlookItem

    Private m_Item As Object 'the wrapped Outlook item
    Private m_Type As Type 'type for the Outlook item
    Private m_Args As Object() 'dummy argument array
    Private m_TypeOlObjectClass As System.Type


#Region "OutlookItem Constants"
    Private Const OlActions As String = "Actions"
    Private Const OlApplication As String = "Application"
    Private Const OlAttachments As String = "Attachments"
    Private Const OlBillingInformation As String = "BillingInformation"
    Private Const OlBody As String = "Body"
    Private Const OlCategories As String = "Categories"
    Private Const OlClass As String = "Class"
    Private Const OlClose As String = "Close"
    Private Const OlCompanies As String = "Companies"
    Private Const OlConversationIndex As String = "ConversationIndex"
    Private Const OlConversationTopic As String = "ConversationTopic"
    Private Const OlCopy As String = "Copy"
    Private Const OlCreationTime As String = "CreationTime"
    Private Const OlDelete As String = "Delete"
    Private Const olDisplay As String = "Display"
    Private Const OlDownloadState As String = "DownloadState"
    Private Const OlEntryID As String = "EntryID"
    Private Const OlFormDescription As String = "FormDescription"
    Private Const OlGetInspector As String = "GetInspector"
    Private Const OlImportance As String = "Importance"
    Private Const OlIsConflict As String = "IsConflict"
    Private Const OlItemProperties As String = "ItemProperties"
    Private Const OlLastModificationTime As String = "LastModificationTime"
    Private Const OlLinks As String = "Links"
    Private Const OlMarkForDownload As String = "MarkForDownload"
    Private Const OlMessageClass As String = "MessageClass"
    Private Const OlMileage As String = "Mileage"
    Private Const OlMove As String = "Move"
    Private Const OlNoAging As String = "NoAging"
    Private Const OlOutlookInternalVersion As String = "OutlookInternalVersion"
    Private Const OlOutlookVersion As String = "OutlookVersion"
    Private Const OlParent As String = "Parent"
    Private Const OlPrintOut As String = "PrintOut"
    Private Const OlPropertyAccessor As String = "PropertyAccessor"
    Private Const OlSave As String = "Save"
    Private Const OlSaveAs As String = "SaveAs"
    Private Const OlSaved As String = "Saved"
    Private Const OlSensitivity As String = "Sensitivity"
    Private Const OlSession As String = "Session"
    Private Const OlShowCategoriesDialog As String = "ShowCategoriesDialog"
    Private Const OlSize As String = "Size"
    Private Const OlSubject As String = "Subject"
    Private Const OlUnRead As String = "UnRead"
    Private Const OlUserProperties As String = "UserProperties"
#End Region

#Region "Constructor"
    Public Sub New(ByVal Item As Object)
        m_Item = Item
        m_Type = m_Item.GetType()
        m_Args = New Object() {}
    End Sub
#End Region

#Region "Public Methods and Properties"
    Public ReadOnly Property Actions() As Outlook.Actions
        Get
            Return CType(GetPropertyValue(OlActions), Outlook.Actions)
        End Get
    End Property

    Public ReadOnly Property Application() As Outlook.Application
        Get
            Return CType(GetPropertyValue(OlApplication), Outlook.Application)
        End Get
    End Property

    Public ReadOnly Property Attachments() As Outlook.Attachments
        Get
            Return CType(GetPropertyValue(OlAttachments), Outlook.Attachments)
        End Get
    End Property

    Public Property BillingInformation() As String
        Get
            Return CType(GetPropertyValue(OlBillingInformation), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlBillingInformation, value)
        End Set
    End Property

    Public Property Body() As String
        Get
            Return CType(GetPropertyValue(OlBody), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlBody, value)
        End Set
    End Property

    Public Property Categories() As String
        Get
            Return CType(GetPropertyValue(OlCategories), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlCategories, value)
        End Set
    End Property

    Sub Close(ByVal SaveMode As Outlook.OlInspectorClose)
        Dim myArgs() As Object = {SaveMode}
        Me.CallMethod(OlClose, myArgs)
    End Sub

    Public Property Companies() As String
        Get
            Return CType(GetPropertyValue(OlCompanies), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlCompanies, value)
        End Set
    End Property

    Public ReadOnly Property ConversationIndex() As String
        Get
            Return CType(GetPropertyValue(OlConversationIndex), String)
        End Get
    End Property

    Public ReadOnly Property ConversationTopic() As String
        Get
            Return CType(GetPropertyValue(OlConversationTopic), String)
        End Get
    End Property

    Function Copy() As Object
        Copy = Me.CallMethod(OlCopy)
    End Function

    Public ReadOnly Property CreationTime() As System.DateTime
        Get
            Return CType(GetPropertyValue(OlCreationTime), System.DateTime)
        End Get
    End Property

    Sub Display()
        Me.CallMethod(olDisplay)
    End Sub

    Public ReadOnly Property DownloadState() As Outlook.OlDownloadState
        Get
            Return CType(GetPropertyValue(OlDownloadState), Outlook.OlDownloadState)
        End Get
    End Property

    Public ReadOnly Property EntryID() As String
        Get
            Return CType(GetPropertyValue(OlEntryID), String)
        End Get
    End Property

    Public ReadOnly Property FormDescription() As Outlook.FormDescription
        Get
            Return CType(GetPropertyValue(OlFormDescription), Outlook.FormDescription)
        End Get
    End Property

    Public ReadOnly Property GetInspector() As Outlook.Inspector
        Get
            Return CType(GetPropertyValue(OlGetInspector), Outlook.Inspector)
        End Get
    End Property

    Public Property Importance() As Outlook.OlImportance
        Get
            Return CType(GetPropertyValue(OlImportance), Outlook.OlImportance)
        End Get
        Set(ByVal value As Outlook.OlImportance)
            SetPropertyValue(OlImportance, value)
        End Set
    End Property

    Public ReadOnly Property InnerObject() As Object
        Get
            Return m_Item
        End Get
    End Property

    Public ReadOnly Property IsConflict() As Boolean
        Get
            Return CType(GetPropertyValue(OlIsConflict), Boolean)
        End Get
    End Property

    Public ReadOnly Property ItemProperties() As Outlook.ItemProperties
        Get
            Return CType(GetPropertyValue(OlItemProperties), Outlook.ItemProperties)
        End Get
    End Property

    Public ReadOnly Property LastModificationTime() As System.DateTime
        Get
            Return CType(GetPropertyValue(OlLastModificationTime), System.DateTime)
        End Get
    End Property

    Public ReadOnly Property Links() As Outlook.Links
        Get
            Return CType(GetPropertyValue(OlLinks), Outlook.Links)
        End Get
    End Property

    Public Property MarkForDownload() As Outlook.OlRemoteStatus
        Get
            Return CType(GetPropertyValue(OlMarkForDownload), Outlook.OlRemoteStatus)
        End Get
        Set(ByVal value As Outlook.OlRemoteStatus)
            SetPropertyValue(OlMarkForDownload, value)
        End Set
    End Property

    Public Property MessageClass() As String
        Get
            Return CType(GetPropertyValue(OlMessageClass), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlMessageClass, value)
        End Set
    End Property

    Public Property Mileage() As String
        Get
            Return CType(GetPropertyValue(OlMileage), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlMileage, value)
        End Set
    End Property

    Function Move(ByVal DestinationFolder As Outlook.Folder) As Object
        Dim myArgs() As Object = {DestinationFolder}
        Move = Me.CallMethod(OlMove, myArgs)
    End Function

    Public Property NoAging() As Boolean
        Get
            Return CType(GetPropertyValue(OlNoAging), Boolean)
        End Get
        Set(ByVal value As Boolean)
            SetPropertyValue(OlNoAging, value)
        End Set
    End Property

    Public ReadOnly Property [Class]() As Outlook.OlObjectClass
        Get
            Return CType(GetPropertyValue(OlClass), Outlook.OlObjectClass)
        End Get
    End Property

    Public ReadOnly Property OutlookInternalVersion() As Long
        Get
            Return CType(GetPropertyValue(OlOutlookInternalVersion), Long)
        End Get
    End Property

    Public ReadOnly Property OutlookVersion() As String
        Get
            Return CType(GetPropertyValue(OlOutlookVersion), String)
        End Get
    End Property

    Public ReadOnly Property Parent() As Outlook.Folder
        Get
            Return CType(GetPropertyValue(OlParent), Outlook.Folder)
        End Get
    End Property

    Sub PrintOut()
        Me.CallMethod(OlPrintOut)
    End Sub

    Public ReadOnly Property PropertyAccessor() As Outlook.PropertyAccessor
        Get
            Return CType(GetPropertyValue(OlPropertyAccessor), Outlook.PropertyAccessor)
        End Get
    End Property

    Sub Save()
        Me.CallMethod(OlSave)
    End Sub

    Sub SaveAs(ByVal Path As String, ByVal Type As Outlook.OlSaveAsType)
        If Path.Length = 0 Then
            Exit Sub
        Else
            Dim myArgs() As Object = {Path, Type}
            Me.CallMethod(OlSaveAs, myArgs)
        End If
    End Sub

    Public ReadOnly Property Saved() As Boolean
        Get
            Return CType(GetPropertyValue(OlSaved), Boolean)
        End Get
    End Property

    Public Property Sensitivity() As Outlook.OlSensitivity
        Get
            Return CType(GetPropertyValue(OlSensitivity), Outlook.OlSensitivity)
        End Get
        Set(ByVal value As Outlook.OlSensitivity)
            SetPropertyValue(OlSensitivity, value)
        End Set
    End Property

    Public ReadOnly Property Session() As Outlook.NameSpace
        Get
            Return CType(GetPropertyValue(OlSession), Outlook.NameSpace)
        End Get
    End Property

    Sub ShowCategoriesDialog()
        Me.CallMethod(OlShowCategoriesDialog)
    End Sub

    Public ReadOnly Property Size() As Long
        Get
            Return CType(GetPropertyValue(OlSize), Long)
        End Get
    End Property

    Public Property Subject() As String
        Get
            Return CType(GetPropertyValue(OlSubject), String)
        End Get
        Set(ByVal value As String)
            SetPropertyValue(OlSubject, value)
        End Set
    End Property

    Public Property UnRead() As Boolean
        Get
            Return CType(GetPropertyValue(OlUnRead), Boolean)
        End Get
        Set(ByVal value As Boolean)
            SetPropertyValue(OlUnRead, value)
        End Set
    End Property

    Public ReadOnly Property UserProperties() As Outlook.UserProperties
        Get
            Return CType(GetPropertyValue(OlUserProperties), Outlook.UserProperties)
        End Get
    End Property
#End Region

#Region "Private Helper Functions"

    Private Sub SetPropertyValue(ByVal PropertyName As String, ByVal Value As Object)
        Try
            m_Type.InvokeMember(PropertyName, _
             BindingFlags.Public Or BindingFlags.SetField Or BindingFlags.SetProperty, _
             Nothing, _
             m_Item, _
             New Object() {Value})
        Catch ex As Exception
            Debug.Write(String.Format("OutlookItem: SetPropertyValue for {0} Exception: {1}", _
             PropertyName, ex.Message))
        End Try
    End Sub

    Private Function GetPropertyValue(ByVal PropertyName As String) As Object
        Try
            Return m_Type.InvokeMember(PropertyName, _
             BindingFlags.Public Or BindingFlags.GetField Or BindingFlags.GetProperty, _
             Nothing, _
             m_Item, _
             m_Args)
        Catch ex As SystemException
            Debug.Write(String.Format("OutlookItem: GetPropertyValue for {0} Exception: {1} ", _
             PropertyName, ex.Message))
            Return Nothing
        End Try
    End Function

    Private Overloads Function CallMethod(ByVal MethodName As String) As Object
        Try
            Return m_Type.InvokeMember(MethodName, _
            BindingFlags.Public Or BindingFlags.InvokeMethod, _
            Nothing, _
            m_Item, _
            m_Args)
        Catch ex As SystemException
            Debug.Write(String.Format("OutlookItem: CallMethod for {0} Exception: {1} ", _
             MethodName, ex.Message))
            Return Nothing
        End Try
    End Function

    Private Overloads Function CallMethod(ByVal MethodName As String, ByVal Args() As Object) As Object
        Try
            Return m_Type.InvokeMember(MethodName, _
            BindingFlags.Public Or BindingFlags.InvokeMethod, _
            Nothing, _
            m_Item, _
            Args)
        Catch ex As SystemException
            Debug.Write(String.Format("OutlookItem: CallMethod for {0} Exception: {1} ", _
             MethodName, ex.Message))
            Return Nothing
        End Try
    End Function
#End Region
End Class

