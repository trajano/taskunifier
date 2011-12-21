package com.leclercb.taskunifier.gui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.models.beans.ComBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComNoteBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComTaskBean;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class MailUtils {
	
	private MailUtils() {
		
	}
	
	public static boolean mail(
			Address[] to,
			Address[] cc,
			Address[] bcc,
			Note[] notes) {
		try {
			ComBean bean = new ComBean();
			
			List<ComNoteBean> noteBeans = new ArrayList<ComNoteBean>();
			for (Note note : notes) {
				noteBeans.add(new ComNoteBean(note.toBean()));
			}
			
			bean.setApplicationName(Constants.TITLE);
			bean.setNotes(noteBeans.toArray(new ComNoteBean[0]));
			
			File attachment = File.createTempFile("taskunifier_mail_", ".xml");
			FileOutputStream attachmentOs = new FileOutputStream(attachment);
			bean.encodeToXML(attachmentOs);
			attachmentOs.close();
			
			return mail(
					to,
					cc,
					bcc,
					getSubject(notes),
					getBody(notes, false),
					getBody(notes, true),
					attachment);
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.cannot_open_mail_client"),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
			return false;
		}
	}
	
	public static boolean mail(
			Address[] to,
			Address[] cc,
			Address[] bcc,
			Task[] tasks) {
		try {
			ComBean bean = new ComBean();
			
			List<ComTaskBean> taskBeans = new ArrayList<ComTaskBean>();
			for (Task task : tasks) {
				taskBeans.add(new ComTaskBean(task.toBean()));
			}
			
			bean.setApplicationName(Constants.TITLE);
			bean.setTasks(taskBeans.toArray(new ComTaskBean[0]));
			
			File attachment = File.createTempFile("taskunifier_mail_", ".xml");
			FileOutputStream attachmentOs = new FileOutputStream(attachment);
			bean.encodeToXML(attachmentOs);
			attachmentOs.close();
			
			return mail(
					to,
					cc,
					bcc,
					getSubject(tasks),
					getBody(tasks, false),
					getBody(tasks, true),
					attachment);
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.cannot_open_mail_client"),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
			return false;
		}
	}
	
	public static boolean mail(
			Address[] to,
			Address[] cc,
			Address[] bcc,
			String subject,
			String textBody,
			String htmlBody,
			File attachment) throws Exception {
		Properties p = System.getProperties();
		Session session = Session.getInstance(p);
		MimeMessage message = new MimeMessage(session);
		
		message.setSubject(subject);
		
		if (to != null)
			message.setRecipients(javax.mail.Message.RecipientType.TO, to);
		
		if (cc != null)
			message.setRecipients(javax.mail.Message.RecipientType.CC, cc);
		
		if (bcc != null)
			message.setRecipients(javax.mail.Message.RecipientType.BCC, bcc);
		
		Multipart multipart = new MimeMultipart();
		
		BodyPart bodyPart;
		
		bodyPart = new MimeBodyPart();
		multipart.addBodyPart(bodyPart);
		bodyPart.setText(textBody);
		bodyPart.setContent(htmlBody, "text/html");
		
		bodyPart = new MimeBodyPart();
		multipart.addBodyPart(bodyPart);
		DataSource source = new FileDataSource(attachment);
		bodyPart.setDataHandler(new DataHandler(source));
		bodyPart.setFileName("tasks.tue");
		
		message.setContent(multipart);
		
		File emlFile = File.createTempFile("taskunifier_mail_", ".eml");
		FileOutputStream emlOs = new FileOutputStream(emlFile);
		message.writeTo(emlOs);
		emlOs.close();
		
		DesktopUtils.open(emlFile);
		
		return true;
	}
	
	private static String getSubject(Note[] notes) {
		if (notes == null || notes.length == 0)
			return "TaskUnifier";
		
		if (notes.length == 1)
			return "TaskUnifier: " + notes[0].getTitle();
		
		return "TaskUnifier: "
				+ notes.length
				+ " "
				+ Translations.getString("general.tasks");
	}
	
	private static String getSubject(Task[] tasks) {
		if (tasks == null || tasks.length == 0)
			return "TaskUnifier";
		
		if (tasks.length == 1)
			return "TaskUnifier: " + tasks[0].getTitle();
		
		return "TaskUnifier: "
				+ tasks.length
				+ " "
				+ Translations.getString("general.tasks");
	}
	
	private static String getBody(Note[] notes, boolean html) {
		List<NoteColumn> columns = new ArrayList<NoteColumn>(
				Arrays.asList(NoteColumn.values()));
		columns.remove(NoteColumn.MODEL);
		columns.remove(NoteColumn.MODEL_CREATION_DATE);
		columns.remove(NoteColumn.MODEL_UPDATE_DATE);
		NoteColumn[] c = columns.toArray(new NoteColumn[0]);
		
		return NoteUtils.toText(notes, c, html);
	}
	
	private static String getBody(Task[] tasks, boolean html) {
		List<TaskColumn> columns = new ArrayList<TaskColumn>(
				Arrays.asList(TaskColumn.values()));
		columns.remove(TaskColumn.MODEL);
		columns.remove(TaskColumn.MODEL_CREATION_DATE);
		columns.remove(TaskColumn.MODEL_UPDATE_DATE);
		columns.remove(TaskColumn.MODEL_EDIT);
		columns.remove(TaskColumn.SHOW_CHILDREN);
		columns.remove(TaskColumn.ORDER);
		TaskColumn[] c = columns.toArray(new TaskColumn[0]);
		
		return TaskUtils.toText(tasks, c, html);
	}
	
}
